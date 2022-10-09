package com.rabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/16 1:42
 * <p>
 * 消费者
 */
public class Consumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置连接基本信息
        factory.setHost("192.168.222.128");
        factory.setUsername("admin");
        factory.setPassword("123123");
        //3.获取一个链接，创建信道（实现自动close接口）
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
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
        System.out.println("消息接收");
    }
}
