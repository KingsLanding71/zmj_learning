package com.change.direct;

import com.rabbitmq.client.Channel;
import com.utils.Utils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/18 16:36
 * 路由模式direct
 */
public class EmitDirect {
    public static final String Exchanges_name = "logs_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();
        //声明交换机名称及其类型
        channel.exchangeDeclare(Exchanges_name, "direct");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(Exchanges_name, "receive01", null, message.getBytes());
            System.out.println("消息已经发送");
        }
    }
}
