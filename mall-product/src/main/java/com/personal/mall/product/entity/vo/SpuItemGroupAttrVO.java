package com.personal.mall.product.entity.vo;

import com.personal.mall.product.entity.SkuSaleAttrValueEntity;
import com.personal.mall.product.entity.vo.spu.Attr;
import lombok.Data;

import java.util.List;

@Data
public class SpuItemGroupAttrVO {
    private String groupName;
    private List<Attr> baseAttrs;
}
