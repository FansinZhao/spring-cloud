package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class AutoRecoveryDemo {

    private static CountDownLatch latch = new CountDownLatch(1);

    private static String USER = "admin";
    private static String PASSWD = "admin";

    private static final String QUEUE = "queue.recovery";

    public static void main(String[] args) throws InterruptedException {


        /**
         * 172.17.0.3 172.17.0.4 两个实例自动切换重连
         */
        System.out.println("自动恢复连接automaticRecovery 拓扑恢复topologyRecovery");


        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new AutoRecoveryDemo.ClusterReceiver("172.17.0.3:5672","172.17.0.4:5672"));
        latch.await();
        service.shutdown();

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
