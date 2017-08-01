package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * http://www.rabbitmq.com/tutorials/tutorial-one-java.html
 *
 * Created by zhaofeng on 17-7-27.
 */
public class HelloWorld {


    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("tree",".");
        Process process = builder.start();
        try (BufferedInputStream inputStream = new BufferedInputStream(process.getInputStream())) {
            byte[] msg = new byte[2048];
            inputStream.read(msg);
            System.out.println(new String(msg));
        }
    }

    @Test
    public void send() throws IOException, TimeoutException {
        /*基本步骤,工厂-配置-连接-通道*/
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()
        ) {
            Channel channel = connection.createChannel();
            /*创建queue,发送消息*/
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String msg = "hello rabbitmq";
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            System.out.println("发送消息 msg = " + msg);
            channel.close();
        }
    }

    static class Recv{
        private static final String QUEUE_NAME = "hello";
        public static void main(String[] args) throws IOException, TimeoutException {
         /*模板样式*/
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            System.out.println("客户准备中....");
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body,"UTF-8");
                    System.out.println("接收到消息 msg = " + msg);
                }
            };
            channel.basicConsume(QUEUE_NAME,true,consumer);
        }
    }

}

