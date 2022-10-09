package com.dead;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/20 15:15
 */
public class DeadConsumer {

    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();

        channel.exchangeDeclare(DEAD_EXCHANGE, "direct");
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "com/dead");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String strMessage = new String(message.getBody());
                System.out.println("C2消息接收" + strMessage);
            }
        };

        channel.basicConsume(DEAD_QUEUE, deliverCallback, consumerTag -> {
        });
    }
}
