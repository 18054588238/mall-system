package com.personal.mall.product.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName OrderItemSpuInfoVO
 * @Author liupanpan
 * @Date 2025/9/29
 * @Description
 */
@Data
@Builder
public class OrderItemSpuInfoVO implements Serializable {

    private Long spuId;

    private String spuName;

    private String spuDesc;
    private String spuPic;

    private Long spuBrandId;
    private String spuBrandName;

    private Long categoryId;
    private String categoryName;

}
