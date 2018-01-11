package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * http://www.rabbitmq.com/tutorials/tutorial-one-java.html
 *
 * @author zhaofeng on 17-7-27.
 */
public class HelloWorld {


    private static final String QUEUE_NAME = "hello";

    private static CountDownLatch latch = new CountDownLatch(1);


    public static void main(String[] args) throws IOException, InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new HelloConsumer("consumer"));
        latch.await();
        service.execute(new HelloPublisher("publisher"));
        service.shutdown();

    }


    static class HelloPublisher implements Runnable{


        private String name;

        public HelloPublisher(String name) {
            this.name = name;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {

             /*基本步骤,工厂-配置-连接-通道*/
            //1 工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            //2 连接
            try (Connection connection = factory.newConnection()
            ) {
                //3 渠道
                Channel channel = connection.createChannel();
                channel.exchangeDeclare("hello.world","direct");
                //4 创建队列
                channel.queueDeclare(QUEUE_NAME,false,false,false,null);
                channel.queueBind(QUEUE_NAME,"hello.world","key-hello");
                //5 发布消息
                String msg = "hello rabbitmq";
                channel.basicPublish("hello.world","key-hello",null,msg.getBytes());
                System.out.println(name + " 发送消息 msg = " + msg);
                channel.close();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    static class HelloConsumer implements Runnable{


        private String name;

        public HelloConsumer(String name) {
            this.name = name;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                //1 工厂
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                //2 连接
                Connection connection = factory.newConnection();
                //3 渠道
                Channel channel = connection.createChannel();
                channel.exchangeDeclare("hello.world","direct");
                //4 创建队列
                channel.queueDeclare(QUEUE_NAME,false,false,false,null);
                channel.queueBind(QUEUE_NAME,"hello.world","key-hello");
                //5 设置消费
                channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String msg = new String(body,"UTF-8");
                        System.out.println(name + " 接收到消息 msg = " + msg);
                    }
                });
                System.out.println("客户端启动.");
                latch.countDown();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

