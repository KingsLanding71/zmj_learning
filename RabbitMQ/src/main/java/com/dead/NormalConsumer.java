package com.dead;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/20 15:16
 */
public class NormalConsumer {

    //交换机：普通交换机、死信交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //队列：普通队列、死信队列
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.RabbitChannel();

        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, "direct");
        //声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, "direct");

        //声明普通队列
        /*
        arguments：map类型，用于在队列中绑定死信队列的信息
         */
        HashMap<String, Object> arguments = new HashMap<>();
        //设置过期时间    注：key一般都是指定的key，相当于绑定配置名，但是一般都在生产者端预先设置过期时间
//        arguments.put("x-message-ttl",10000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信的RoutingKey
        arguments.put("x-dead-letter-routing-key", "com/dead");
        //设置队列最大长度
//        arguments.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        //死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //交换机与队列的绑定
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "normal");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "com/dead");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                //设置消息拒收
                String msg = new String(message.getBody());
                if (msg.equals("消息5")) {
                    System.out.println(msg + "：此消息被C1拒收");
                    //拒收    参数二：是否返回队列
                    channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                } else {
                    String strMessage = new String(message.getBody());
                    System.out.println("C1消息接收：" + strMessage);
                    //手动应答，不开启，自动应答会导致拒收的消息被删除
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                }
            }
        };
        //开启手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });
    }
}
