package com.personal.mall.order.vo;

import com.personal.mall.order.entity.OrderEntity;
import lombok.Data;

@Data
public class OrderResponseVO {
    private OrderEntity orderEntity;
    private Integer code; // 0 表示成功 否则失败
}
