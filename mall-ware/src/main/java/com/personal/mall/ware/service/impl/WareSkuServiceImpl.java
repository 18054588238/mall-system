package com.personal.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.personal.common.exception.WareNoStockException;
import com.personal.common.utils.R;
import com.personal.mall.ware.entity.PurchaseDetailEntity;
import com.personal.mall.ware.entity.vo.OrderWareLockVO;
import com.personal.mall.ware.feign.ProductFeignService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.ware.dao.WareSkuDao;
import com.personal.mall.ware.entity.WareSkuEntity;
import com.personal.mall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private WareSkuDao wareSkuDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");

        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq("sku_id", skuId);
        }
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(PurchaseDetailEntity detailEntity) {
        // 库存中有该商品就更新，没有就创建
        WareSkuEntity wareSku = this.getOne(new QueryWrapper<WareSkuEntity>()
                .eq("sku_id", detailEntity.getSkuId())
                .eq("ware_id", detailEntity.getWareId()));
        if (wareSku == null) {
            // 创建
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(detailEntity.getSkuId());
            try {
                R info = productFeignService.info(detailEntity.getSkuId());
                Map<String,Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode() == 0){
                    skuEntity.setSkuName((String) skuInfo.get("skuName"));//openfeign调用product服务
                }
            } catch (FeignException e) {
                log.error("openfeign调用失败：{}",e.getMessage());
            }

            skuEntity.setWareId(detailEntity.getWareId());
            skuEntity.setStock(detailEntity.getSkuNum());
            skuEntity.setStockLocked(0);
            this.save(skuEntity);
        } else {
            // 更新
            this.update(wareSku,new UpdateWrapper<WareSkuEntity>()
                    .set("stock", wareSku.getStock() + detailEntity.getSkuNum()));
        }
    }

    @Override
    public List<Long> haveStock(List<Long> skuIds) {
        ArrayList<Long> list = new ArrayList<>();
        skuIds.forEach(s -> {
            Integer stock = wareSkuDao.getStock(s);
            // 说明有库存
            if (stock != null && stock > 0) {
                list.add(s);
            }
        });

        return list;
    }

    @Transactional
    @Override
    public void lockWareStock(List<OrderWareLockVO> vos) {

        for (OrderWareLockVO vo : vos) {
            Integer lockCount = vo.getLockCount();
            // 遍历每一个skuid，根据skuid找到其对应的所有ware，计算剩余库存
            List<WareSkuEntity> skuEntities = this.list(new QueryWrapper<WareSkuEntity>().eq("sku_id", vo.getSkuId()));
            // skuid所在仓库，总的剩余库存
            Integer stockCount = 0;
            if (skuEntities!=null && !skuEntities.isEmpty()) {
                for (WareSkuEntity skuEntity : skuEntities) {
                    stockCount += (skuEntity.getStock() - skuEntity.getStockLocked());
                }
            }

            if (lockCount > stockCount) {
                // 库存不足，抛异常
                throw new WareNoStockException(vo.getSkuId());
            }
            // 库存足够，锁定库存
            for (WareSkuEntity skuEntity : skuEntities) {
                Integer tempCount = skuEntity.getStock() - skuEntity.getStockLocked(); // 剩余库存
                if (lockCount <= tempCount) {
                    // 锁定
                    boolean update = this.update(new UpdateWrapper<WareSkuEntity>()
                            .eq("id", skuEntity.getId())
                            .set("stock_locked", skuEntity.getStockLocked()+lockCount));
                    // 退出库存循环
                    break; // break语句总是跳出最近的一层循环；
                } else {
                    boolean update = this.update(new UpdateWrapper<WareSkuEntity>()
                            .eq("id", skuEntity.getId())
                            .set("stock_locked", skuEntity.getStockLocked()+tempCount));
                    lockCount-=tempCount;
                }
            }
            if (lockCount > 0) {
                // 未锁定完成
                throw new WareNoStockException(vo.getSkuId());
            }
        }
    }

}