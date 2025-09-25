package com.personal.mall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName SkuInfoVO
 * @Author liupanpan
 * @Date 2025/9/25
 * @Description
 */
@Data
public class SkuInfoVO {

    private Long skuId;

    private Long spuId;

    private String skuName;

    private String skuDesc;

    private Long catalogId;

    private Long brandId;

    private String skuDefaultImg;

    private String skuTitle;

    private String skuSubtitle;

    private BigDecimal price;

    private Long saleCount;
}
