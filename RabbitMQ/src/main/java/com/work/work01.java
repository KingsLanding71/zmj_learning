package com.work;

import com.utils.Utils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/16 15:35
 */
public class work01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = Utils.RabbitChannel();

        //等待接收消息
        //4.推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {

                byte[] body = message.getBody();
                String massage = new String(body);
                System.out.println(massage);
            }
        };
        //5.取消消费的一个回调接口，如在消费时队列被删除
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("消费中断");
            }
        };

        /**
         * 6.消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true自动
         * 3.成功
         * 3.消费失败的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        System.out.println("c2消息接收");
    }
}
