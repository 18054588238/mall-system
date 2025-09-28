package com.personal.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.order.entity.OrderEntity;
import com.personal.mall.order.vo.OrderConfirmVO;
import com.personal.mall.order.vo.OrderResponseVO;
import com.personal.mall.order.vo.OrderSubmitVO;

import java.util.Map;

/**
 * 订单
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:07:15
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVO confirmOrder();

    OrderResponseVO orderSubmit(OrderSubmitVO vo);
}

