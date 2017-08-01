package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhaofeng on 17-7-28.
 */
public class TransactionVsPublishConfirm {


    private static final int MSG_NUM = 100000;
    private static ConnectionFactory factory = new ConnectionFactory();

    public static void main(String[] args) {

    }


    static class Transaction {

        private static final String TANSACTION = "transaction";
        private static CountDownLatch latch = new CountDownLatch(2);

        public static void main(String[] args) throws IOException, InterruptedException {

            ExecutorService service = Executors.newFixedThreadPool(2);
            service.execute(new TranConsumer());
            latch.await();
            service.execute(new TranPublisher());
            service.shutdown();
        }

        static class TranPublisher implements Runnable {

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
                    try (Connection connection = factory.newConnection()) {
                        Channel channel = connection.createChannel();
                        channel.queueDeclare(TANSACTION, false, false, false, null);
                        long start = System.currentTimeMillis();
                        try {
                            for (int i = 0; i < MSG_NUM; i++) {
                                //开启事务
                                channel.txSelect();
                                String msg = "rabbitmq msg!";
                                channel.basicPublish("", TANSACTION, null, msg.getBytes());
                                //提交事务
                                channel.txCommit();
                            }
                        } catch (Exception e) {
                            //回滚事务
                            channel.txRollback();
                            e.printStackTrace();
                        } finally {
                            channel.close();
                        }
                        long end = System.currentTimeMillis();
                        System.out.println("[tx发送方]发送方耗时:" + (end - start));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }

        static class TranConsumer implements Runnable {

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
                    channel.queueDeclare(TANSACTION, false, false, false, null);
                    //每次1条
                    channel.basicQos(1);
                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String msg = new String(body);
                            //发送ack
                            channel.basicAck(envelope.getDeliveryTag(), false);
//                     System.out.println("确认"+msg);
                        }
                    };
                    //手动ack
                    channel.basicConsume(TANSACTION, false, consumer);
                    System.out.println("[tx接收方]客户端等待中......");
                    latch.countDown();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static class Confirm{

        private static final String TANSACTION = "confirm";
        private static CountDownLatch latch = new CountDownLatch(2);


        public static void main(String[] args) throws IOException {

            ExecutorService service = Executors.newFixedThreadPool(2);
            service.execute(new ConfirmConsumer());
            service.execute(new ConfirmPublisher());
            service.shutdown();
        }

        static class ConfirmPublisher implements Runnable {

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
                    try (Connection connection = factory.newConnection()) {
                        Channel channel = connection.createChannel();
                        channel.queueDeclare(TANSACTION, false, false, false, null);
                        long start = System.currentTimeMillis();
                        try {
                            for (int i = 0; i < MSG_NUM; i++) {
                                //开启confirm
                                channel.confirmSelect();
                                String msg = "rabbitmq msg!";
                                channel.basicPublish("", TANSACTION, null, msg.getBytes());
                                //confirm
//                              waitForConfirmsOrDie 相对于 waitForConfirms 来说,只要有nack就好抛出异常,同时也是一种阻塞式
                                channel.waitForConfirmsOrDie();
//                                channel.addConfirmListener(new ConfirmListener() {
//                                    @Override
//                                    public void handleAck(long deliveryTag, boolean multiple) throws IOException {
////                                        System.out.println("ack deliveryTag = " + deliveryTag);
//                                    }
//
//                                    @Override
//                                    public void handleNack(long deliveryTag, boolean multiple) throws IOException {
////                                        System.out.println("nack deliveryTag = " + deliveryTag);
//                                    }
//                                });
                            }
                        } catch (Exception e) {
                            //回滚事务
                            channel.txRollback();
                            e.printStackTrace();
                        } finally {
                            channel.close();
                        }
                        long end = System.currentTimeMillis();
                        System.out.println("[confirm发送方]发送方耗时:" + (end - start));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }

        static class ConfirmConsumer implements Runnable {

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
                    channel.queueDeclare(TANSACTION, false, false, false, null);
                    //每次1条
                    channel.basicQos(1);
                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String msg = new String(body);
                            //发送ack
                            channel.basicAck(envelope.getDeliveryTag(), false);
//                     System.out.println("确认"+msg);
                        }
                    };
                    //手动ack
                    channel.basicConsume(TANSACTION, false, consumer);
                    System.out.println("[confirm接收方]客户端等待中......");
                    latch.countDown();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
