package com.personal.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.personal.common.utils.R;
import com.personal.mall.product.config.ThreadPoolConfig;
import com.personal.mall.product.entity.*;
import com.personal.mall.product.entity.vo.ItemVO;
import com.personal.mall.product.entity.vo.SeckillSkuVO;
import com.personal.mall.product.entity.vo.SkuSaleAttrValueVO;
import com.personal.mall.product.entity.vo.SpuItemGroupAttrVO;
import com.personal.mall.product.feign.SeckillFeignService;
import com.personal.mall.product.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.product.dao.SkuInfoDao;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuImagesService imagesService;
    @Autowired
    private SkuInfoDao skuInfoDao;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private ThreadPoolExecutor executor;
    @Autowired
    private SeckillFeignService seckillFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            wrapper.and(w -> {
                w.eq("sku_id", key)
                        .or().like("sku_name", key);
            });
        }
        String min = (String) params.get("min");
        if (StringUtils.isNotEmpty(min)) {
            wrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        // max=0时 ，不需要加这个条件
        if (StringUtils.isNotEmpty(max) && !"0".equalsIgnoreCase(max)) {
            wrapper.le("price", max);
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public ItemVO item(Long skuId) throws ExecutionException, InterruptedException {
        ItemVO itemVO = new ItemVO();
        //    sku基本信息
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = this.getById(skuId);
            itemVO.setSkuInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);
//        Long spuId = skuInfoEntity.getSpuId();
//        Long catalogId = skuInfoEntity.getCatalogId();
        // spu中的销售属性组合,根据spu找到所有sku下对应的属性信息
        CompletableFuture<Void> saleFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            List<SkuSaleAttrValueVO> saleAttrs = skuInfoDao.getSaleAttrs(res.getSpuId());
            itemVO.setSaleAttrs(saleAttrs);
        }, executor);

        // spu描述
        CompletableFuture<Void> descFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            SpuInfoDescEntity desc = spuInfoDescService.getById(res.getSpuId());
            itemVO.setDesc(desc);
        }, executor);

        // spu规格参数
        CompletableFuture<Void> baseAttrFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            List<SpuItemGroupAttrVO> groupAttr = attrGroupService.getGroupAttr(res.getSpuId(), res.getCatalogId());
            itemVO.setBaseAttrs(groupAttr);
        }, executor);

        // sku图片信息
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> imagesEntities = imagesService.list(new QueryWrapper<SkuImagesEntity>()
                    .eq("sku_id", skuId));
            itemVO.setImages(imagesEntities);
        }, executor);

        // 查询秒杀商品
        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            R r = seckillFeignService.getSeckillSkuBySkuId(skuId);
            if (r.getCode() == 0) {
                SeckillSkuVO seckillSkuVO = JSON.parseObject(r.get("data").toString(), SeckillSkuVO.class);
                itemVO.setSeckillSkuVO(seckillSkuVO);
            }
        }, executor);

        CompletableFuture.allOf(skuInfoFuture,saleFuture,descFuture,baseAttrFuture,imageFuture,seckillFuture).get();// 阻塞

        return itemVO;
    }

}