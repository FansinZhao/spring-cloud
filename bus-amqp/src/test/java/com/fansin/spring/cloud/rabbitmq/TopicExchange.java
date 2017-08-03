package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * http://www.rabbitmq.com/tutorials/tutorial-five-java.html
 */
public class TopicExchange {

    private static CountDownLatch latch = new CountDownLatch(3);
    private static final String TOPIC = "topic";

    private static ConnectionFactory factory = new ConnectionFactory();

    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(new TopicConsumer("开源机构","*.springframework.amqp","*.apache.*","com.github"));
        service.execute(new TopicConsumer("开源中间件","*.apache.activemq","#.rabbitmq"));
        service.execute(new TopicConsumer("springframework","org.framework.#"));
        latch.await();
        service.execute(new TopicPublisher("全字key1","org.springframework.amqp","com.rabbitmq","com.github"));
        service.execute(new TopicPublisher("全字key1","org.apache.activemq","org.framework.cloud"));
        service.shutdown();
    }


    static class TopicPublisher implements Runnable{
        String name;
        String[] routingKeys;

        public TopicPublisher(String name, String ... routingKeys) {
            this.name = name;
            this.routingKeys = routingKeys;
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
                channel.exchangeDeclare(TOPIC, ExchangeTypes.TOPIC);
                for (String routingKey:routingKeys) {
                    channel.basicPublish(TOPIC,routingKey,null,routingKey.getBytes());
                }
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }


    static class TopicConsumer implements Runnable{

        String name;
        String[] routingKeys;

        public TopicConsumer(String name, String ... routingKeys) {
            this.name = name;
            this.routingKeys = routingKeys;
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
                channel.exchangeDeclare(TOPIC,ExchangeTypes.TOPIC);
                String queueName = channel.queueDeclare().getQueue();
                for (int i = 0; i < routingKeys.length; i++) {
                    String routingKey = routingKeys[i];
                    channel.queueBind(queueName, TOPIC, routingKey);
                }
                channel.basicConsume(queueName,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println(name+ " : " +new String(body) );
                    }
                });
                System.out.println(name+" 等待中.....");
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }


        }
    }

}
