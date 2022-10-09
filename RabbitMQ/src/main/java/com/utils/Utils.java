package com.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/16 15:25
 */
public class Utils {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //获取信道
    public static Channel RabbitChannel() throws IOException, TimeoutException {
        //连接池信息
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.222.128");
        factory.setUsername("admin");
        factory.setPassword("123123");
        //创建链接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
