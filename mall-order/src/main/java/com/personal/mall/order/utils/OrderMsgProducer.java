package com.personal.mall.order.utils;

import com.personal.common.constant.OrderConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderMsgProducer {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public void sendOrderMessage(String orderNo) {
        rocketMQTemplate.syncSend(OrderConstant.ROCKETMQ_ORDER_TOPIC, MessageBuilder.withPayload(orderNo).build(),5000,4);// test用 30秒延迟
    }
}
