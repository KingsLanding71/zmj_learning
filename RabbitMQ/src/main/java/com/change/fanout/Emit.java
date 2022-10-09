package com.change.fanout;

import com.rabbitmq.client.Channel;
import com.utils.Utils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/18 15:35
 * 广播模式：fanout
 */
public class Emit {
    public static final String Exchanges_name = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();
        //交换机：名称、类型（扇出or广播）
        channel.exchangeDeclare(Exchanges_name, "fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(Exchanges_name, "", null, message.getBytes());
            System.out.println("消息发送成功");
        }
    }
}
