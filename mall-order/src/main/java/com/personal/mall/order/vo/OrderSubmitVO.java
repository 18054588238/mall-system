package com.personal.mall.order.vo;

import lombok.Data;

/**
 * @ClassName OrderSumbitVO
 * @Author liupanpan
 * @Date 2025/9/28
 * @Description 提交订单
 */
@Data
public class OrderSubmitVO {
    private Long addressId;

    private String orderToken;
}
