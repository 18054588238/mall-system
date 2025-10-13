package com.personal.mall.order.utils;

import com.alibaba.fastjson.JSON;
import com.personal.common.constant.OrderConstant;
import com.personal.common.constant.SeckillConstant;
import com.personal.mall.order.service.OrderService;
import com.personal.mall.order.vo.OrderResponseVO;
import com.personal.mall.order.vo.OrderSubmitVO;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName SeckillOrderMsgConsumer
 * @Author liupanpan
 * @Date 2025/10/13
 * @Description
 */
@Component
@RocketMQMessageListener(topic = OrderConstant.ROCKETMQ_SECKILL_ORDER_TOPIC,consumerGroup = "seckill-order")
public class SeckillOrderMsgConsumer implements RocketMQListener<String> {

    @Autowired
    private OrderService orderService;

    @Override
    public void onMessage(String message) {
        System.out.println("收到的消息s------->"+message+"消费的时间:"+ LocalDateTime.now());
        // 执行创建订单操作
        OrderSubmitVO submitVO = JSON.parseObject(message, OrderSubmitVO.class);
        OrderResponseVO responseVO = orderService.orderSubmit(submitVO);
        System.out.println("订单创建成功-------》"+responseVO);

    }
}
