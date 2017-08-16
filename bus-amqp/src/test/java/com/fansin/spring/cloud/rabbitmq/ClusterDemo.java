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

    private static String USER = "";
    private static String PASSWD = "";

    public static void main(String[] args) throws InterruptedException {


        int i = 1;

        switch (i){
            case 0:

                System.out.println("本地集群");
                /**
                 * 本地 5672 5673 5674 三个实例可以向任意一个发送消息,其他监听都可以收到,证明集群成功
                 */
                USER = "guest";
                PASSWD = "guest";
                ExecutorService service = Executors.newFixedThreadPool(2);
                service.execute(new ClusterReceiver("localhost:5672"));
                latch.await();
                service.execute(new ClusterSender("localhost:5673"));
                service.shutdown();

                break;
            case 1:

                System.out.println("多机集群");
                /**
                 * 例如docker三个地址172.17.0.3 5672, 172.17.0.4 5672, 172.17.0.3 5672 三个实例可以向任意一个发送消息,其他监听都可以收到,证明集群成功
                 */
                USER = "admin";
                PASSWD = "admin";
                ExecutorService service1 = Executors.newFixedThreadPool(2);
                service1.execute(new ClusterReceiver("172.17.0.3:5672"));
                latch.await();
                service1.execute(new ClusterSender("172.17.0.4:5672"));
                service1.shutdown();

                break;
        }
    }

    static class ClusterSender implements Runnable{

        Address[] addresses = new Address[]{new Address("localhost",5672)};

        public ClusterSender(String... hosts) {
            addresses = new Address[hosts.length];
            for (int i = 0; i < hosts.length; i++) {
                String[] host = hosts[i].split(":");
                if(host.length != 2){
                    System.err.println("格式错误,确保格式 host:port! 错误格式" + host[i]);
                }
                addresses[i] = new Address(host[0],Integer.parseInt(host[1]));
            }
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
            factory.setUsername(USER);
            factory.setPassword(PASSWD);
            try {
                Connection connection = factory.newConnection(addresses);
                Channel channel = connection.createChannel();
                channel.basicPublish("",QUEUE,null,"cluster msg...".getBytes());
                System.out.println(connection.getAddress().getHostAddress()+ " " + factory.getPort()+" 发送消息");
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


        Address[] addresses = new Address[]{new Address("localhost",5672)};

        public ClusterReceiver(String... hosts) {
            addresses = new Address[hosts.length];
            for (int i = 0; i < hosts.length; i++) {
                String[] host = hosts[i].split(":");
                if(host.length != 2){
                    System.err.println("格式错误,确保格式 host:port! 错误格式" + host[i]);
                }
                addresses[i] = new Address(host[0],Integer.parseInt(host[1]));
            }
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
            factory.setUsername(USER);
            factory.setPassword(PASSWD);
            try {
                Connection connection = factory.newConnection(addresses);
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE,false,false,false,null);
                channel.basicConsume(QUEUE,true,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println(connection.getAddress().getHostAddress()+ " " + factory.getPort()+" 接收到消息:"+new String(body));
                    }
                });
                System.out.println(connection.getAddress().getHostAddress() +" 客户端启动成功....");
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }


        }
    }
}
