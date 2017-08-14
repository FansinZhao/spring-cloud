package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class WarrenHAProxyDemo {

    private static CountDownLatch latch = new CountDownLatch(1);

    private static final String QUEUE = "queue.haproxy";

    private static String USER = "";
    private static String PASSWD = "";

    private static int MSG_NUM = 1000;



    public static void main(String[] args) throws InterruptedException {


        System.out.println("HA backup");
        /**
         * 本地 5672 5673 5674 三个实例可以向任意一个发送消息,其他监听都可以收到,证明集群成功
         */
        USER = "admin";
        PASSWD = "admin";
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new ClusterReceiver(5680));
        latch.await();
        service.execute(new ClusterSender(5680));
        service.shutdown();

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
                for (int i = 0; i < MSG_NUM; i++) {
                    String msg = "cluster msg..."+i;
                    System.out.println(msg);
                    channel.basicPublish("",QUEUE,null,msg.getBytes());
                }
                channel.close();
                connection.close();
                System.out.println(factory.getHost()+ " " + factory.getPort()+" 发送完毕....");
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
            //重要 不能少了这个重连设置
            factory.setAutomaticRecoveryEnabled(true);

            try {
                System.out.println("连接客户端.....");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE, false, false, false, null);
                channel.basicConsume(QUEUE, true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println(factory.getHost() + " " + factory.getPort() + " 接收到消息:" + new String(body));
//                            channel.basicAck(envelope.getDeliveryTag(),false);
                        try {
                            Thread.sleep(1000l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void handleCancel(String consumerTag) throws IOException {
                        System.out.println(consumerTag + " handleCancel 取消....");
                    }
                });

                System.out.println(factory.getHost() + " " + factory.getPort() + " 客户端启动成功....");
                latch.countDown();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
