package com.rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/16 1:22
 * <p>
 * 生产者
 */
public class Producer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        //1.创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置连接基本信息
        factory.setHost("192.168.222.128");
        factory.setUsername("admin");
        factory.setPassword("123123");
        //3.获取一个链接，创建信道（实现自动close接口）
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            /**
             * 4.生成一个队列
             * 1.队列名称
             * 2.队列里的消息是否持久化，默认消息存储在内存中
             * 3.如果声明的是独占队列（仅限于此连接），则为true,共享为false
             * 4.是否自动删除，最后一个消费者断开连接后该队列是否自动删除
             * 5.其他参数
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String massage = "hello,RabbitMQ";
            /**
             * 5.发送一个消息
             * 1.发送到哪个交换机
             * 2.路由的key是哪个，也就是哪个消息队列
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, massage.getBytes());
            System.out.println("消息发送成功！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
