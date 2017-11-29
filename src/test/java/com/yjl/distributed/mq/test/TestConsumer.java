package com.yjl.distributed.mq.test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.junit.Test;
import com.yjl.distributed.mq.consumer.Consumer;

public class TestConsumer {

    @Test
    public void queue() throws InterruptedException {
        // consumer.exclusive=true代表独家消费者
        Consumer.receiveQueueMessage("deal-details-queue-demo?consumer.exclusive=true",
                new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        try {
                            System.out.println("收到队列消息=" + ((TextMessage) message).getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });

        // 禁止测试程序停止，导致监听不到后续数据
        Thread.sleep(1000000000);

    }

    @Test
    public void topic() throws InterruptedException {
        // consumer.exclusive=true代表独家消费者
        Consumer.receiveTopicMessage("deal-details-topic-demo?consumer.exclusive=true", "aadd",
                new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        try {
                            System.out.println("收到订阅消息=" + ((TextMessage) message).getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });

        // 禁止测试程序停止，导致监听不到后续数据
        Thread.sleep(1000000000);

    }
}
