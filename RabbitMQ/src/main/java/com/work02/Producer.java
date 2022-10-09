package com.work02;

import com.rabbitmq.client.MessageProperties;
import com.utils.Utils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/16 16:53
 */
public class Producer {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();
        /*
        发布确认
        单个确认模式：发一个确认一个
         */
        channel.confirmSelect();
        //生成队列
        /*
        durable:队列是否持久化
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String massage = scanner.next();
            //props:消息持久化   MessageProperties.PERSISTENT_TEXT_PLAIN持久化属性
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, massage.getBytes());
            System.out.println("发送成功");
        }
    }
}
