package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * http://www.rabbitmq.com/tutorials/tutorial-four-java.html
 *
 */

public class RoutingOrDirectExchange {

    private static CountDownLatch latch = new CountDownLatch(3);

    private static ConnectionFactory factory = new ConnectionFactory();

    private static final int MSG_NUM = 3;

    private static final String ROUNTING = "rounting";


    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(new RoutingConsumer("info"));
        service.execute(new RoutingConsumer("warning"));
        service.execute(new RoutingConsumer("error"));
        latch.await();
        service.execute(new RoutingPublisher("发送1"));
        service.execute(new RoutingPublisher("发送2"));
        service.shutdown();


    }


    static class RoutingPublisher implements Runnable{

        String name;

        public RoutingPublisher(String name) {
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
            try (Connection connection = factory.newConnection()){
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(ROUNTING, ExchangeTypes.DIRECT);
                if (name.contains("1")){
                    channel.basicPublish(ROUNTING,"error",null,(name+">>error").getBytes());
                    channel.basicPublish(ROUNTING,"warning",null,(name+">>warning").getBytes());
                }else {
                    channel.basicPublish(ROUNTING,"info",null,(name+">>infoinfo").getBytes());
                }
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    static class RoutingConsumer implements Runnable{
        String routingKey;

        public RoutingConsumer(String routingKey) {
            this.routingKey = routingKey;
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
                channel.exchangeDeclare(ROUNTING, ExchangeTypes.DIRECT);
                String queueNm = channel.queueDeclare().getQueue();
                channel.queueBind(queueNm,ROUNTING,routingKey);
                if(routingKey.contains("info")){
                    channel.queueBind(queueNm,ROUNTING,"error");
                }
                DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String msg = new String(body);
                        System.out.println(routingKey+" : " + msg);
                    }
                };
                channel.basicConsume(queueNm,true,defaultConsumer);
                System.out.println(routingKey+"客户端等待中....");
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }


        }
    }

}
