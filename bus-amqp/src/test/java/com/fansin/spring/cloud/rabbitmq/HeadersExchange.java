package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

public class HeadersExchange {

    private static ConnectionFactory factory = new ConnectionFactory();

    private static final String HEADERS = "headers";
    private static final String HEADERS_QUEUE = "headers_queue";

    private static CountDownLatch latch = new CountDownLatch(3);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        service.execute(new HeadersConsumer("consumer all"));
        service.execute(new HeadersConsumer("consumer any1"));
        service.execute(new HeadersConsumer("consumer any2"));
        latch.await();
        service.execute(new HeadersPublisher("publisher"));
    }


    static class HeadersPublisher implements Runnable{


        private String name;

        public HeadersPublisher(String name) {
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
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(HEADERS, ExchangeTypes.HEADERS);
                Map<String,Object> headers = new HashMap<>();
                headers.put("name","张三");
                headers.put("phone","123456789");
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().headers(headers).build();
                channel.basicPublish(HEADERS,"",properties,"hello rabbitmq  ".getBytes());
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println("服务端启动.");

        }

    }

    static class HeadersConsumer implements Runnable{


        private String name;

        public HeadersConsumer(String name) {
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
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(HEADERS_QUEUE,ExchangeTypes.HEADERS);
                String queueName = channel.queueDeclare().getQueue();
//                channel.queueDeclare(HEADERS_QUEUE,false,false,true,null);
                Map<String,Object> headers = new HashMap<>();
                if(name.endsWith("all")){
                    headers.put("x-match","all");
                    headers.put("name","张三");
                    headers.put("phone","123456789");
                }else if (name.endsWith("any1")){
                    headers.put("x-match","any");
                    headers.put("name","张三");
                    headers.put("phone","0000");
                }else{
                    headers.put("x-match","any");
                    headers.put("name","张三1");
                    headers.put("phone","123456789");
                }
                channel.queueBind(queueName,HEADERS,"",headers);
                channel.basicConsume(queueName,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println("接收消息>>"+new String(body));
                    }
                });
                System.out.println("客户启动.");
                latch.countDown();
;            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }
    }

}
