package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.ForgivingExceptionHandler;

import java.io.IOException;
import java.util.concurrent.*;

public class ShovelClusterDemo {

    private static CountDownLatch latch = new CountDownLatch(1);

    private static final String QUEUE = "shovel.queue";
    private static final String EXCHANGE = "shovel.exchange";

    private static String USER = "";
    private static String PASSWD = "";

    private static final int MSG_NUM = 100;

    public static void main(String[] args) throws InterruptedException {



        String type="exchange";

        /**
         * 172.17.0.3 src 172.17.0.4 des 两个实例可以向任意一个发送消息,其他监听都可以收到,证明集群成功
         */
        switch (type){
            case "queue":
                System.out.println("shovel 集群 queue (未先创建队列,首次启动有时无法接收消息,第二次启动后正常?!)");
                USER = "admin";
                PASSWD = "admin";
                ExecutorService service1 = Executors.newFixedThreadPool(2);
                service1.execute(new ShovelClusterDemo.ClusterReceiver("172.17.0.4:5672"));
                latch.await();
                service1.execute(new ShovelClusterDemo.ClusterSender("172.17.0.3:5672"));
                service1.shutdown();
                break;
            case "exchange":
                System.out.println("shovel 集群 exchange (未先创建exchange,首次启动有时无法接收消息,第二次启动后正常?!)");
                USER = "admin";
                PASSWD = "admin";
                ExecutorService service2 = Executors.newFixedThreadPool(2);
                service2.execute(new ShovelClusterDemo.ClusterReceiver("172.17.0.4:5672",EXCHANGE));
                latch.await();
                service2.execute(new ShovelClusterDemo.ClusterSender("172.17.0.3:5672",EXCHANGE));
                service2.shutdown();
                break;
        }

    }

    static class ClusterSender implements Runnable{

        String exchange;

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

        public ClusterSender(String host, String exchange) {
            this(host);
            this.exchange = exchange;
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

                if (null == this.exchange){
                    channel.queueDeclare(QUEUE,false,false,false,null);
                    for (int i = 1; i <= MSG_NUM; i++) {
                        String msg = "shovel 消息"+i;
                        channel.basicPublish("",QUEUE,null,msg.getBytes());
                    }
                }else{
                    channel.exchangeDeclare(this.exchange,"fanout");
                    for (int i = 1; i <= MSG_NUM; i++) {
                        String msg = "shovel 消息"+i;
                        channel.basicPublish(this.exchange,"",null,msg.getBytes());
                    }
                }

                String host  = connection.getAddress().getHostAddress();
                System.out.println(host+ " " + factory.getPort()+" 发送消息完毕 "+ MSG_NUM);
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

        String exchange;

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

        public ClusterReceiver(String host, String exchange) {
            this(host);
            this.exchange = exchange;
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
                if (null == this.exchange){
                    channel.queueDeclare(QUEUE,false,false,false,null);
                    channel.basicConsume(QUEUE,true,new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            System.out.println(connection.getAddress().getHostAddress()+ " " + factory.getPort()+" 接收到消息:"+new String(body));
                        }
                    });
                }else {
                    channel.exchangeDeclare(this.exchange,"fanout");
                    String queue = channel.queueDeclare().getQueue();
                    channel.queueBind(queue,this.exchange,"");
                    channel.basicConsume(queue,true,new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            System.out.println(connection.getAddress().getHostAddress() + " " + factory.getPort()+" 接收到消息:"+new String(body));
                        }
                    });
                }
                System.out.println(connection.getAddress().getHostAddress() +" shovel 客户端启动,等待消息...");
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }
    }
}
