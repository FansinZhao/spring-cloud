package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class ShovelClusterDemo {

    private static CountDownLatch latch = new CountDownLatch(1);

    private static final String QUEUE = "queue.shovel";

    private static String USER = "";
    private static String PASSWD = "";

    private static final int MSG_NUM = 100;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("shovel 消息复制 集群");
        /**
         * 172.17.0.3 上游 172.17.0.4 下游 三个实例可以向任意一个发送消息,其他监听都可以收到,证明集群成功
         */
        USER = "admin";
        PASSWD = "admin";
        ExecutorService service1 = Executors.newFixedThreadPool(2);
        service1.execute(new ClusterReceiver("172.17.0.4"));
        latch.await();
        service1.execute(new ClusterSender("172.17.0.3"));
        service1.shutdown();

    }

    static class ClusterSender implements Runnable{

        int port = 5672;
        String host = "localhost";

        public ClusterSender(String host) {
            this.host = host;
        }

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
            factory.setHost(this.host);
            factory.setUsername(USER);
            factory.setPassword(PASSWD);
            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                for (int i = 1; i <= MSG_NUM; i++) {
                    String msg = "shovel 消息"+i;
                    channel.basicPublish("",QUEUE,null,msg.getBytes());
                }
                System.out.println(factory.getHost()+ " " + factory.getPort()+" 发送消息完毕 "+ MSG_NUM);
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

        int port = 5672;
        String host = "localhost";

        public ClusterReceiver(String host) {
            this.host = host;
        }

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
            factory.setHost(this.host);
            factory.setUsername(USER);
            factory.setPassword(PASSWD);
            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE,false,false,false,null);
                channel.basicConsume(QUEUE,true,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println(factory.getHost()+ " " + factory.getPort()+" 接收到消息:"+new String(body));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println(factory.getHost() +"shovel downstream 客户端启动,等待消息...");
            latch.countDown();
        }
    }
}
