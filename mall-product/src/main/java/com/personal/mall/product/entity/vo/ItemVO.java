package com.personal.mall.product.entity.vo;

import com.personal.mall.product.entity.SkuImagesEntity;
import com.personal.mall.product.entity.SkuInfoEntity;
import com.personal.mall.product.entity.SkuSaleAttrValueEntity;
import com.personal.mall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/*商品的详情信息*/
@Data
public class ItemVO {
    // sku基本信息
    private SkuInfoEntity skuInfo;
    // 是否有库存
    private boolean hasStock = true;
    // sku图片信息
    private List<SkuImagesEntity> images;
    // spu中的销售属性组合
    private List<SkuSaleAttrValueVO> saleAttrs;
    // spu描述
    private SpuInfoDescEntity desc;
    // spu规格参数
    private List<SpuItemGroupAttrVO> baseAttrs;
}
