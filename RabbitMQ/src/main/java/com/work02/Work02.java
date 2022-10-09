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
 * Created by KingsLanding on 2022/9/16 17:15
 */
public class Work02 {

    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = Utils.RabbitChannel();
        /*
        1.概念一、channel.basicQos(1);信道只有一个排队状态
        设置不公平分发：我不行，找别人帮忙
        果这个任务我还没有处理完或者我还没有应答你，你先别分配给我，
        我目前只能处理一个任务，然后 rabbitmq 就会把该任务分配给没有那么忙的那个空闲消费者
        2.概念二、channel.basicQos(5);预取值，也就是预先取五个消息进入信道排队，尽管我很慢，但是你已经
        在我这里排上队了，就无法选择快速通道了
         */
//        channel.basicQos(1);
        channel.basicQos(5);
        //等待接收消息
        //4.推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {

                //中断测试
                Sleep.sleep(30);
                byte[] body = message.getBody();
                String massage = new String(body);
                System.out.println("接收到消息" + massage);
                //手动应答
                /*
                1、message.getEnvelope().getDeliveryTag():消息的标记 tag
                2、multiple:是否批量应答：批量应答可能使整个队列都应答，当程序中止时，未被消费的消息就会被删
                 */
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
        //autoAck:设置手动应答
        //此时程序中断消息不会丢失，而是将消息重新放回队列重新消费
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
        System.out.println("c2消息接收");
    }
}
