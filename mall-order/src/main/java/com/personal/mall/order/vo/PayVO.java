package com.personal.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
/*封装支付需要的相关信息*/
@Data
public class PayVO {
    // 订单号
    private String out_trade_no;
    // 支付金额
    private String total_amount;
    // 订单标题
    private String subject;
    // 描述
    private String body;
}
