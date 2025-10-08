package com.personal.mall.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
class MallOrderApplicationTests {

	@Test
	void contextLoads() throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
		/*在基本样例中我们提供如下的功能场景：
		1. 使用RocketMQ发送三种类型的消息：同步消息、异步消息和单向消息。其中前两种消息是可靠的，因为会有发送是否成功的应答。
		2. 使用RocketMQ来消费接收到的消息。*/
		// 发送同步消息
		// 实例化消息生产者Producer
		DefaultMQProducer producer = new DefaultMQProducer("order_group");
		// 设置NameServer的地址
		producer.setNamesrvAddr("192.168.100.96:9876");
		// 启动Producer实例
		producer.start();
		for (int i = 0; i < 100; i++) {
			// 创建消息，并指定Topic，Tag和消息体
			Message msg = new Message("TopicTest" /* Topic */,
					"TagA" /* Tag */,
					("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
			);
			// 发送消息到一个Broker
			SendResult sendResult = producer.send(msg);
			// 通过sendResult返回消息是否成功送达
			System.out.printf("%s%n", sendResult);
		}
		// 如果不再发送消息，关闭Producer实例。
		producer.shutdown();

	}

	@Test
	void contextLoads1() throws UnsupportedEncodingException, MQClientException, RemotingException, InterruptedException {
		// 发送一异步消息
		// 实例化消息生产者Producer
		DefaultMQProducer producer = new DefaultMQProducer("order_group");
		// 设置NameServer的地址
		producer.setNamesrvAddr("192.168.100.96:9876");
		// 启动Producer实例
		producer.start();
		producer.setRetryTimesWhenSendAsyncFailed(0);

		int messageCount = 100;
		// 根据消息数量实例化倒计时计算器
		final CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);

		for (int i = 0; i < messageCount; i++) {
			final int index = i;
			// 创建消息，并指定Topic，Tag和消息体
			Message msg = new Message("TopicTest",
					"TagA",
					"OrderID188",
					"Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
			// SendCallback接收异步返回结果的回调
			producer.send(msg, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					countDownLatch.countDown();
					System.out.printf("%-10d OK %s %n", index,
							sendResult.getMsgId());
				}
				@Override
				public void onException(Throwable e) {
					countDownLatch.countDown();
					System.out.printf("%-10d Exception %s %n", index, e);
					e.printStackTrace();
				}
			});
		}
		// 等待5s
		countDownLatch.await(5, TimeUnit.SECONDS);
		// 如果不再发送消息，关闭Producer实例。
		producer.shutdown();
	}

	@Test
	void consumer() throws MQClientException, InterruptedException {
		// 实例化消费者
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_consumer_group");

		// 设置NameServer的地址
		consumer.setNamesrvAddr("192.168.100.96:9876");

		// 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
		consumer.subscribe("TopicTest", "*");
		// 注册回调实现类来处理从broker拉取回来的消息
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
				System.out.println("context------>"+context);
				// 标记该消息已经被成功消费
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		// 启动消费者实例
		consumer.start();
		System.out.printf("Consumer Started.%n");
		Thread.sleep(Integer.MAX_VALUE);
	}

	// 延时消息
	@Test
	void ScheduledMessageConsumer() throws MQClientException, InterruptedException {
		// 实例化消费者
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TestDelayConsumer");
		// 设置NameServer的地址
		consumer.setNamesrvAddr("192.168.100.96:9876");
		// 订阅Topics
		consumer.subscribe("TestDelayTopic", "*");
		// 注册消息监听者
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
				for (MessageExt message : messages) {
					// Print approximate delay time period
					System.out.println("Receive message[msgId=" + message.getMsgId() + "] " + (System.currentTimeMillis() - message.getBornTimestamp()) + "ms later");
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		// 启动消费者
		consumer.start();
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Test
	void ScheduledMessageProducer() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
		// 实例化一个生产者来产生延时消息
		DefaultMQProducer producer = new DefaultMQProducer("TestDelayProducer");
		// 设置NameServer的地址
		producer.setNamesrvAddr("192.168.100.96:9876");
		// 启动生产者
		producer.start();
		int totalMessagesToSend = 100;
		for (int i = 0; i < totalMessagesToSend; i++) {
			Message message = new Message("TestDelayTopic", ("Hello scheduled message " + i).getBytes());
			// 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
			message.setDelayTimeLevel(4); // 4 表示30s后发送
			// 发送消息
			producer.send(message);
		}
		// 关闭生产者
		producer.shutdown();
	}
}
