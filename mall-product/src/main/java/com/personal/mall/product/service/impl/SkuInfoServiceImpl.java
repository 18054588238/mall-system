package com.personal.mall.product.service.impl;

import com.personal.mall.product.entity.*;
import com.personal.mall.product.entity.vo.ItemVO;
import com.personal.mall.product.entity.vo.SkuSaleAttrValueVO;
import com.personal.mall.product.entity.vo.SpuItemGroupAttrVO;
import com.personal.mall.product.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    public ItemVO item(Long skuId) {
        ItemVO itemVO = new ItemVO();
        //    sku基本信息
        SkuInfoEntity skuInfoEntity = this.getById(skuId);
        Long spuId = skuInfoEntity.getSpuId();
        Long catalogId = skuInfoEntity.getCatalogId();
        itemVO.setSkuInfo(skuInfoEntity);
        //    // sku图片信息
        List<SkuImagesEntity> imagesEntities = imagesService.list(new QueryWrapper<SkuImagesEntity>()
                .eq("sku_id", skuId));
        itemVO.setImages(imagesEntities);
        //    // spu中的销售属性组合,根据spu找到所有sku下对应的属性信息
        List<SkuSaleAttrValueVO> saleAttrs = skuInfoDao.getSaleAttrs(spuId);
        itemVO.setSaleAttrs(saleAttrs);
        //    // spu描述
        SpuInfoDescEntity desc = spuInfoDescService.getById(spuId);
        itemVO.setDesc(desc);
        //    // spu规格参数
        List<SpuItemGroupAttrVO> groupAttr = attrGroupService.getGroupAttr(spuId,catalogId);
        itemVO.setBaseAttrs(groupAttr);
        return itemVO;
    }

}