package com.personal.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personal.common.dto.SpuBoundsDTO;
import com.personal.common.dto.SkuReductionDTO;
import com.personal.common.utils.R;
import com.personal.mall.product.entity.*;
import com.personal.mall.product.entity.vo.SpuInfoVO;
import com.personal.mall.product.entity.vo.spu.BaseAttrs;
import com.personal.mall.product.entity.vo.spu.Bounds;
import com.personal.mall.product.entity.vo.spu.MemberPrice;
import com.personal.mall.product.entity.vo.spu.Skus;
import com.personal.mall.product.feign.CouponFeignService;
import com.personal.mall.product.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private SkuSaleAttrValueService saleAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfoVO vo) {
        // 1.保存spu基本信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);

        Long spuId = spuInfoEntity.getId();

        // 2.保存spu详情信息
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuId);
        descEntity.setDecript(String.join(",", vo.getDecript()));
        spuInfoDescService.save(descEntity);
        // 3.保存图集信息
        List<SpuImagesEntity> imagesEntities = new ArrayList<>();
        vo.getImages().forEach(item -> {
            SpuImagesEntity imagesEntity = new SpuImagesEntity();
            imagesEntity.setSpuId(spuId);
            imagesEntity.setImgUrl(item);
            imagesEntities.add(imagesEntity);
        });
        spuImagesService.saveBatch(imagesEntities);
        // 4.保存规格参数信息pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> attrValueEntities = new ArrayList<>();
        baseAttrs.forEach(item -> {
            ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
            attrValueEntity.setSpuId(spuId);
            attrValueEntity.setAttrId(item.getAttrId());
            String attrName = attrService.getById(item.getAttrId()).getAttrName();
            attrValueEntity.setAttrName(attrName);
            attrValueEntity.setAttrValue(item.getAttrValues());
            attrValueEntity.setQuickShow(item.getShowDesc());
            attrValueEntities.add(attrValueEntity);
        });
        attrValueService.saveBatch(attrValueEntities);
        // 5.保存sku信息
        List<Skus> skus = vo.getSkus();
        if (skus != null && !skus.isEmpty()) {
            skus.forEach(sku -> {
                // 5.1 保存sku销售属性
                List<SkuSaleAttrValueEntity> saleAttrValueEntityList = sku.getAttr().stream().map(a -> {
                    SkuSaleAttrValueEntity saleAttrValue = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, saleAttrValue);
                    saleAttrValue.setSkuId(spuId);
                    return saleAttrValue;
                }).collect(Collectors.toList());
                saleAttrValueService.saveBatch(saleAttrValueEntityList);
                // 5.2 保存sku基本信息
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setSpuId(spuId);
                skuInfoEntity.setSkuDesc(sku.getDescar().toString());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0l);
                AtomicReference<String> defaultImage = new AtomicReference<>("");
                sku.getImages().forEach(item -> {
                    if (item.getDefaultImg() == 1) {
                        defaultImage.set(item.getImgUrl());
                    }
                });
                skuInfoEntity.setSkuDefaultImg(defaultImage.get());
                skuInfoService.save(skuInfoEntity);
                // 5.3保存sku图片信息
                List<SkuImagesEntity> skuImagesEntities = new ArrayList<>();
                sku.getImages().stream().filter(i -> i.getDefaultImg() == 1).forEach(item -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    BeanUtils.copyProperties(item, skuImagesEntity);
                    skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    skuImagesEntities.add(skuImagesEntity);
                });
                skuImagesService.saveBatch(skuImagesEntities);
                // 5.4 保存满减/折扣/会员价等信息
                SkuReductionDTO dto = new SkuReductionDTO();
                BeanUtils.copyProperties(sku, dto);
                dto.setSkuId(skuInfoEntity.getSkuId());
                List<MemberPrice> memberPrice = sku.getMemberPrice();
                if (memberPrice != null && !memberPrice.isEmpty()) {
                    List<com.personal.common.dto.MemberPrice> list = memberPrice.stream().map(m -> {
                        com.personal.common.dto.MemberPrice memberPrice1 = new com.personal.common.dto.MemberPrice();
                        BeanUtils.copyProperties(m, memberPrice1);
                        return memberPrice1;
                    }).collect(Collectors.toList());
                    dto.setMemberPrice(list);
                }
                couponFeignService.saveFullReductionInfo(dto);
            });
        }
        // 6. 保存积分信息
        Bounds bounds = vo.getBounds();
        SpuBoundsDTO skuBoundsDTO = new SpuBoundsDTO();
        BeanUtils.copyProperties(bounds, skuBoundsDTO);
        skuBoundsDTO.setSpuId(spuId);
        R r = couponFeignService.saveSpuBounds(skuBoundsDTO);
        if (r.getCode() != 0) {
            log.error("调用Coupon服务存储积分信息失败");
        }
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            wrapper.and(w -> {
                w.eq("id", key)
                        .or().like("spu_name", key)
                        .or().like("spu_description", key);
            });
        }
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) {
            wrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        List<SpuInfoVO> vos = page.getRecords().stream().map(p -> {
            SpuInfoVO vo = new SpuInfoVO();
            BeanUtils.copyProperties(p, vo);
            vo.setBrandName(brandService.getById(p.getBrandId()).getName());
            vo.setCatalogName(categoryService.getById(p.getCatalogId()).getName());
            return vo;
        }).collect(Collectors.toList());

        IPage<SpuInfoVO> voiPage = new Page<>();
        BeanUtils.copyProperties(page, voiPage);
        voiPage.setRecords(vos);

        return new PageUtils(voiPage);
    }

}