package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class ClusterDemo {

    private static CountDownLatch latch = new CountDownLatch(1);

    private static final String QUEUE = "queue.cluster";

    public static void main(String[] args) throws InterruptedException {

        /**
         * 本地 5672 5673 5674 三个实例可以向任意一个发送消息,其他监听都可以收到,证明集群成功
         */
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new ClusterReceiver(5674));
        latch.await();
        service.execute(new ClusterSender(5673));
        service.shutdown();

    }

    static class ClusterSender implements Runnable{

        int port;

        public ClusterSender(int port) {
            this.port = port;
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
            ConnectionFactory factory = new ConnectionFactory();
            factory.setPort(this.port);
            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.basicPublish("",QUEUE,null,"cluster msg...".getBytes());
                System.out.println(factory.getPort()+" 发送消息");
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    static class ClusterReceiver implements Runnable{

        int port;

        public ClusterReceiver(int port) {
            this.port = port;
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
            ConnectionFactory factory = new ConnectionFactory();
            factory.setPort(this.port);
            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE,false,false,false,null);
                channel.basicConsume(QUEUE,true,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println(factory.getPort()+" 接收到消息:"+new String(body));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }


            System.out.println("客户端启动成功....");
            latch.countDown();
        }
    }
}
