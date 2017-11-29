package com.yjl.distributed.mq.consumer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import org.apache.commons.lang3.StringUtils;
import com.yjl.distributed.mq.constant.BaseConstant;
import com.yjl.distributed.mq.factory.ActivemqPoolFactory;
import com.yjl.distributed.mq.factory.ActivemqPools;

/**
 * 消费者
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年11月16日 上午10:33:18
 */
public class Consumer {

    /**
     * 增加或恢复节点需要动态加入监听，宕机需要动态删除 TODO
     */
    private static List<MessageConsumer> consumers;

    /**
     * 消息确认方式
     */
    private static int acknowledgeMode =
            Integer.parseInt(BaseConstant.properties.getProperty("consumer.acknowledgeMode"));


    /**
     * 接收队列消息
     * 
     * @param queue 队列名称
     * @param listener 消息监听器
     */
    public static void receiveQueueMessage(String queue, MessageListener listener) {
        receiveMessage(queue, true, null, listener);
    }



    /**
     * 订阅消息
     * 
     * @param topic 主题名称
     * @param clientID 不为空则为持久订阅，且分布式订阅需要使用虚拟主题
     * @param listener 消息监听器
     */
    public static void receiveTopicMessage(String topic, String clientID,
            MessageListener listener) {
        receiveMessage(topic, false, clientID, listener);
    }

    /**
     * 接收队列或者订阅消息
     * 
     * @param mqName 队列名称或者主题名称
     * @param isQueue 是否队列
     * @param clientID 不为空则为持久订阅，且分布式订阅需要使用虚拟主题
     * @param listener 消息监听器
     */
    private static void receiveMessage(String mqName, boolean isQueue, String clientID,
            MessageListener listener) {
        try {
            consumers = new CopyOnWriteArrayList<MessageConsumer>();
            List<ActivemqPoolFactory> pools = ActivemqPools.getPools();
            for (ActivemqPoolFactory pool : pools) {
                // 消费者，消息接收者
                MessageConsumer consumer;

                // 构造从工厂得到连接对象
                Connection connection = pool.getPooledConnectionFactory().createConnection();
                // 持久订阅需要设置客户端id
                connection.setClientID(clientID);
                // 启动
                connection.start();

                // 获取操作连接,客户端不支持事务，只能是false
                Session session = connection.createSession(Boolean.FALSE, acknowledgeMode);

                if (isQueue) {
                    // 获取队列消息目的地
                    Queue queue = session.createQueue(mqName);
                    consumer = session.createConsumer(queue);
                } else {
                    // 获取主题订阅消息目的地
                    Topic topic = session.createTopic(mqName);
                    if (StringUtils.isEmpty(clientID)) {
                        // 一般订阅
                        consumer = session.createConsumer(topic);
                    } else {
                        // 持久化订阅
                        consumer = session.createDurableSubscriber(topic, clientID);
                    }

                }

                // 保存，便于管理
                consumers.add(consumer);

                // 消息监听
                consumer.setMessageListener(listener);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static List<MessageConsumer> getConsumers() {
        return consumers;
    }

    public static void setConsumers(List<MessageConsumer> consumers) {
        Consumer.consumers = consumers;
    }
}
