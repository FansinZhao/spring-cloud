package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * http://www.rabbitmq.com/tutorials/tutorial-two-java.html
 *
 * Created by zhaofeng on 17-7-27.
 */
public class WorkQueue {

    static class NewWork{

        private static final String WORK_QUEUE = "work";

        public static void main(String[] args) throws IOException, TimeoutException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection()) {
                Channel channel = connection.createChannel();
                channel.queueDeclare(WORK_QUEUE,false,false,false,null);

                for (int i = 0; i < 10; i++) {
                    String dots = "";
                    for (int j =0;j<i;j++){
                        dots = dots.concat(".");
                    }
                    String  msg = "work queue"+ dots + dots.length();
                    channel.basicPublish("",WORK_QUEUE,MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
                    System.out.println("发送消息:"+msg);
                }
                channel.close();
            }
        }
        static class ConfirmPublisher{


            public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                try (Connection connection = factory.newConnection()) {
                    Channel channel = connection.createChannel();
                    channel.queueDeclare(WORK_QUEUE,false,false,false,null);
                    //confirm
                    channel.confirmSelect();
                    for (int i = 0; i < 10; i++) {
                        String dots = "";
                        for (int j =0;j<i;j++){
                            dots = dots.concat(".");
                        }
                        String  msg = "work queue"+ dots + dots.length();
                        channel.basicPublish("",WORK_QUEUE,MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
                        System.out.println("发送消息:"+msg);
                    }
                    //confirm
                    channel.waitForConfirmsOrDie();
                    channel.queueDelete(WORK_QUEUE);
                    System.out.println("结束");
                    channel.close();
                }
            }
        }
    }

    static class Worker{

        private static final String WORK_QUEUE = "work";

        public static void main(String[] args) throws IOException, TimeoutException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(WORK_QUEUE,false,false,false,null);

            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body,"UTF-8");
                    for (char c : msg.toCharArray()) {
                        if(c == '.'){
//                        try {
//                            Thread.sleep(1000l);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        }
                    }
                    System.out.println("接收到消息"+msg);
                }
            };
            System.out.println("等待消息....");
            channel.basicConsume(WORK_QUEUE,true,consumer);
        }
    }

    static class AckWorker{

        private static final String WORK_QUEUE = "work";

        public static void main(String[] args) throws IOException, TimeoutException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(WORK_QUEUE,false,false,false,null);
            channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body,"UTF-8");
                    for (char c : msg.toCharArray()) {
                        if(c == '.'){
                            try {
                                Thread.sleep(1000l);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println("接收到消息"+msg);
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            };
            System.out.println("等待消息....");
            channel.basicConsume(WORK_QUEUE,false,consumer);
        }
    }

}






