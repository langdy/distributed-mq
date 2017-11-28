package com.yjl.distributed.mq.producer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.pool.PooledConnectionFactory;

import com.yjl.distributed.mq.constant.BaseConstant;
import com.yjl.distributed.mq.factory.ActivemqPools;
import com.yjl.fastjson.JSON;

/**
 * 生产者
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年11月15日 下午3:58:07
 */
public class Producer {

	/**
	 * 是否支持事务
	 */
	private static boolean transacted =
			Boolean.parseBoolean(BaseConstant.properties.getProperty("producer.transacted"));

	/**
	 * 消息确认方式，transacted==true时，此项忽略
	 */
	private static int acknowledgeMode =
			Integer.parseInt(BaseConstant.properties.getProperty("producer.acknowledgeMode"));

	/**
	 * 消息投递模式,持久化和非持久化 {@link javax.jms.DeliveryMode}
	 */
	private static int deliveryMode = Integer.parseInt(BaseConstant.properties.getProperty("producer.deliveryMode"));

	/**
	 * 路由算法{@link com.yjl.distributed.mq.constant.RouteModeEnum}
	 */
	private static String routeMode = BaseConstant.properties.getProperty("producer.routeMode");


	/**
	 * 发送队列消息
	 * 
	 * @param queue 队列名称
	 * @param msg 消息体 {@linkplain com.yjl.distributed.mq.producer.Message}
	 */
	public static void sendQueueMessage(String queue, Message msg) {
		sendMessage(queue, true, msg);
	}

	/**
	 * 发送订阅消息
	 * 
	 * @param topic 主题名称
	 * @param msg 消息体 {@linkplain com.yjl.distributed.mq.producer.Message}
	 */
	public static void sendTopicMessage(String topic, Message msg) {
		sendMessage(topic, false, msg);
	}

	/**
	 * 发送队列或者订阅消息
	 * 
	 * @param mqName 队列名称或者主题名称
	 * @param isQueue 是否队列
	 * @param msg 消息体 {@linkplain com.yjl.distributed.mq.producer.Message}
	 */
	private static void sendMessage(String mqName, boolean isQueue, Message msg) {

		// JMS 客户端连接到JMS
		Connection connection = null;
		// 一个发送或接收消息的线程
		Session session = null;
		// 消息的目的地;消息发送给谁
		Destination destination;
		// 消息发送者
		MessageProducer producer;

		try {
			// 构造从工厂得到连接对象
			PooledConnectionFactory pool = ActivemqPools.getPool(msg.getTag(), routeMode).getPooledConnectionFactory();
			// System.out.println(pool.getProperties().get("brokerURL"));
			connection = pool.createConnection();
			// 获取操作连接
			session = connection.createSession(transacted, acknowledgeMode);

			if (isQueue) {
				// 生成队列消息目的地
				destination = session.createQueue(mqName);
			} else {
				// 生成主题订阅消息目的地
				destination = session.createTopic(mqName);
			}

			// 得到消息生成者【发送者】
			producer = session.createProducer(destination);
			// 设置投递模式
			producer.setDeliveryMode(deliveryMode);
			// 构造消息
			TextMessage message = session.createTextMessage(JSON.toJSONString(msg.getMsg()));
			// 发送消息到目的地方
			System.out.println(msg.getMsg());
			producer.send(message);
			if (session.getTransacted()) {
				// 事务提交，主要耗时的地方
				session.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Throwable ignore) {
			}
		}
	}


}
