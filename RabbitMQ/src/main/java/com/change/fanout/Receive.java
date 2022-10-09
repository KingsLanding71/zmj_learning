package com.change.fanout;

import com.rabbitmq.client.*;
import com.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/18 15:07
 */
public class Receive {
    public static final String Exchanges_name = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();

        //生成交换机
        /*
        Fanout 这种类型非常简单。正如从名称中猜到的那样，它是将接收到的所有消息广播到它知道的所有队列中。
         */
        channel.exchangeDeclare(Exchanges_name, "fanout");
        /*
        生成一个临时队列：用完即删
        消费者断开该队列连接时，队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        //将该临时队列与交换机进行绑定
        /*
        1.队列名称
        2.交换机名称
        3.routingKey路由key，指定哪个队列，广播模式下不用管
         */
        channel.queueBind(queueName, Exchanges_name, "");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody());
                System.out.println("C1接收:" + message);
            }
        };

        CancelCallback confirmCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("为确认的消息");
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, confirmCallback);
    }
}
