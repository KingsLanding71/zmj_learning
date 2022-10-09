package com.publish;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.utils.Utils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by KingsLanding on 2022/9/17 15:44
 */
public class Producer {

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
//        PublishOne();
//        PublishMore();
        PublishAsync();
    }

    //单个确认
    public static void PublishOne() throws IOException, TimeoutException, InterruptedException {
        Channel channel = Utils.RabbitChannel();
        //开启发步确认功能
        channel.confirmSelect();
        //队列名称
        String queue = UUID.randomUUID().toString();
        //创建队列
        channel.queueDeclare(queue, false, false, false, null);

        //测试耗时
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("", queue, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean confirms = channel.waitForConfirms();
            if (confirms) {
                System.out.println("消息发布成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("单个发布确认耗时为：" + (end - start));//发布耗时为：415
    }

    //批量确认
    public static void PublishMore() throws IOException, TimeoutException, InterruptedException {
        Channel channel = Utils.RabbitChannel();
        //开启发步确认功能
        channel.confirmSelect();
        //队列名称
        String queue = UUID.randomUUID().toString();
        //创建队列
        channel.queueDeclare(queue, false, false, false, null);

        //测试耗时
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("", queue, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            //消息发送一百次后，批量确认
            if ((i + 1) % 100 == 0) {
                boolean confirms = channel.waitForConfirms();
                if (confirms) {
                    System.out.println("消息发布成功");
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("批量发布耗时为：" + (end - start));//批量发布耗时为：89
    }

    //异步确认
    public static void PublishAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = Utils.RabbitChannel();
        //开启发步确认功能
        channel.confirmSelect();
        //队列名称
        String queue = UUID.randomUUID().toString();
        //创建队列
        channel.queueDeclare(queue, false, false, false, null);

        //消息保存
        /*
        线程安全有序的哈希表，适用于高并发的情况
        1、将序号与消息关联
        2、批量删除条目，只要拿到序列号
        3、支持并发访问
         */
        ConcurrentSkipListMap<Long, String> skipListMap = new ConcurrentSkipListMap<>();

        //消息确认成功，回调函数
        /*
        deliveryTag：消息的标记
        multiple：是否是批量确认
         */
        ConfirmCallback ackCallback = new ConfirmCallback() {
            @Override
            public void handle(long deliveryTag, boolean multiple) throws IOException {
                //如果批量确认；获取批量确认清除
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> navigableMap = skipListMap.headMap(deliveryTag, true);
                    navigableMap.clear();
                } else {
                    //非批量确认，直接从集合中删除
                    skipListMap.remove(deliveryTag);
                }
                System.out.println("确认的消息" + deliveryTag + "---批量" + multiple);
            }
        };

        //消息确认失败，回调函数
        ConfirmCallback nackCallback = new ConfirmCallback() {
            @Override
            public void handle(long deliveryTag, boolean multiple) throws IOException {
                //删除了已经确认的消息，集合中就只剩下未被确认的消息
                String message = skipListMap.get(deliveryTag);
                System.out.println("未确认的消息:" + message + "序列号" + deliveryTag + "");
            }
        };
        //添加一个异步确认监听器
        /*
        ackCallback：监听哪些消息成功了
        nackCallback：监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback);
        //测试耗时
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            /*
                将发送的所有消息存到map中
             */
            skipListMap.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", queue, null, message.getBytes());

        }
        long end = System.currentTimeMillis();
        System.out.println("异步发布耗时为：" + (end - start));//异步发布耗时为：39
    }
}
