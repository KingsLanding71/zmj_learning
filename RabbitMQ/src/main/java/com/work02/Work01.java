package com.work02;

import com.utils.Sleep;
import com.utils.Utils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/16 17:14
 */
public class Work01 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = Utils.RabbitChannel();
        /*
        1、概念一：设置不公平分发channel.basicQos(1);
         能者多劳，相比之前的轮询，两个队列交替处理消息会导致处理较慢的队列拖慢整体进程
         设置prefetchCount为1 ，代表如果某一个队列处理较慢，立马又空闲队列进行消息处理
         2、概念二：预取值：channel.basicQos(2);也就是预先取两个消息进入信道排队，哪怕我很快
         但是只取两个在我的信道里排队
         */
//        channel.basicQos(1);
        channel.basicQos(2);

        //等待接收消息
        //4.推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {

                //中断测试
                Sleep.sleep(1);
                byte[] body = message.getBody();
                String massage = new String(body);
                System.out.println("接收到消息" + massage);
                //手动应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        //5.取消消费的一个回调接口，如在消费时队列被删除
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("消费中断");
            }
        };
        //autoAck:设置非自动应答，改为手动应答
        //此时程序中断，消息不会丢失，而是将消息重新放回队列重新消费
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
        System.out.println("c1消息接收");
    }
}
