package com.personal.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName SkuESModel
 * @Author liupanpan
 * @Date 2025/8/28
 * @Description 把商品信息存储到ES中，为检索提供源文件
 */
@Data
public class SkuESModel {
    private Long skuId;
    private Long spuId;
    private String subTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

}

