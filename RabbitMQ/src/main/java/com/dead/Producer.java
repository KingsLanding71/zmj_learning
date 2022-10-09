package com.dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/20 15:16
 */
public class Producer {
    //交换机：普通交换机、死信交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //队列：普通队列、死信队列
    public static final String NORMAL_QUEUE = "normal_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, "direct");

        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "normal");

        //设置消息过期时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String str = "消息" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "normal", properties, str.getBytes());
            System.out.println("消息已经发送");
        }
    }
}
