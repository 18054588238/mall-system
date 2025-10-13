package com.personal.mall.seckill.vo;

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
    // 防重token
    private String orderToken;

    // 以下没有用到，后面可以自己补充
    // 支付方式
    private Integer payType;
    // 买家备注
    private String note;
}
