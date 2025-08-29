package com.personal.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.personal.common.utils.R;
import com.personal.mall.ware.entity.PurchaseDetailEntity;
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

}