package com.change.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/18 17:58
 */
public class Receive02 {
    public static final String Exchanges_name = "logs_topics";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();
        //声明交换机
        channel.exchangeDeclare(Exchanges_name, "topic");
        //声明队列
        channel.queueDeclare("R2", false, false, false, null);
        //保定交换机，设置topics规则
        channel.queueBind("R2", Exchanges_name, "*.orange.*");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String massage = new String(message.getBody());
                System.out.println("R2消息接收：" + massage);
            }
        };
        channel.basicConsume("R2", true, deliverCallback, consumerTag -> {
        });
    }
}
