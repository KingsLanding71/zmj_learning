package com.change.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/18 16:36
 * <p>
 * 路由模式
 */
public class Receive01 {
    public static final String Exchanges_name = "logs_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();

        //声明交换机名称及其类型
        channel.exchangeDeclare(Exchanges_name, "direct");

        //生成一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        //将该临时队列与交换机进行绑定
        //设置routingKey指定路由key
        channel.queueBind(queueName, Exchanges_name, "receive01");
        //绑定多个key
        channel.queueBind(queueName, Exchanges_name, "receive0101");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String strMessage = new String(message.getBody());
                System.out.println("R1消息接收：" + strMessage);
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
