package com.personal.mall.order.dto;

import com.personal.mall.order.entity.OrderEntity;
import com.personal.mall.order.entity.OrderItemEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName OrderCreateDTO
 * @Author liupanpan
 * @Date 2025/9/29
 * @Description
 */
@Data
public class OrderCreateDTO {
    // 订单信息
    private OrderEntity orderEntity;
    // 订单项信息
    private List<OrderItemEntity> orderItemEntities;
}
