package com.personal.mall.order.utils;

import com.personal.common.constant.OrderConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
// 消息监听
@RocketMQMessageListener(topic = OrderConstant.ROCKETMQ_ORDER_TOPIC,consumerGroup = "${rocketmq.consumer.group}")
public class OrderMsgConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("收到的消息s------->"+s+"消费的时间"+ LocalDateTime.now());
        // 订单关单的逻辑实现
    }
}
