package com.yjl.distributed.mq.test;

import org.junit.Test;
import com.yjl.distributed.mq.producer.Message;
import com.yjl.distributed.mq.producer.Producer;

public class TestProducer {

    @Test
    public void queue() {
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            User user = new User(i, "user" + i);
            Message msg = new Message("order1", user);
            Producer.sendQueueMessage("deal-details-queue-demo", msg);
        }
        long s2 = System.currentTimeMillis();
        System.out.println("s2-s1=" + (s2 - s1));

        System.exit(0);
    }



    @Test
    public void topic() {
        long s3 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Message msg = new Message("order1", "消息。。。。。" + i);
            Producer.sendTopicMessage("deal-details-topic-demo", msg);
        }
        long s4 = System.currentTimeMillis();
        System.out.println("s4-s3=" + (s4 - s3));

        System.exit(0);
    }
}
