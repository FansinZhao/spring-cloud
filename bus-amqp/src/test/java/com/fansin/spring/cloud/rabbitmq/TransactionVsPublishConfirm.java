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
    private static final int BATCH = 1000;

    private static ConnectionFactory factory = new ConnectionFactory();

    private static final String NO_TRANSACTION = "no_transaction";
    private static CountDownLatch latch = new CountDownLatch(1);


    public static void main(String[] args) throws InterruptedException {
        //System.out.println("测试开始:"+System.currentTimeMillis());
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new NoConsumer());
        latch.await();
        service.execute(new NoPublisher());
        service.shutdown();
    }



    static class NoPublisher implements Runnable {

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
                    channel.queueDeclare(NO_TRANSACTION, false, false, false, null);
                    long start = System.currentTimeMillis();
                    try {
                        for (int i = 0; i < MSG_NUM; i++) {
                            String msg = "rabbitmq msg!";
                            channel.basicPublish("", NO_TRANSACTION, null, msg.getBytes());
                        }
                        channel.basicPublish("", NO_TRANSACTION, null, "end".getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        channel.close();
                    }
                    long end = System.currentTimeMillis();
                    System.out.println("[发送方]发送方耗时:" + (end - start));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    static class NoConsumer implements Runnable {

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
                channel.queueDeclare(NO_TRANSACTION, false, false, false, null);
                //每次1条
                channel.basicQos(1);
                long start = System.currentTimeMillis();
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String msg = new String(body);
                        channel.basicAck(envelope.getDeliveryTag(),false);
                        if (msg.equalsIgnoreCase("end")){
                            long end = System.currentTimeMillis();
                            System.out.println("[接收方]接收完毕"+(end-start));
                            try {
                                channel.close();
                                connection.close();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }
//                            System.out.println("测试结束:"+System.currentTimeMillis());
                        }
                    }
                };
                //手动ack
                channel.basicConsume(NO_TRANSACTION, false, consumer);
                System.out.println("[接收方]客户端等待中......");
                latch.countDown();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static class Transaction {

        private static final String TRANSACTION = "transaction";
        private static CountDownLatch latch = new CountDownLatch(1);

        public static void main(String[] args) throws IOException, InterruptedException {
            //System.out.println("测试开始:"+System.currentTimeMillis());
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
                        channel.queueDeclare(TRANSACTION, false, false, false, null);
                        long start = System.currentTimeMillis();
                        try {
                            //开启 channel 事务
                            channel.txSelect();
                            for (int i = 0; i < MSG_NUM;) {
                                if (i%BATCH ==0){
                                    for (int j = 0; j < BATCH; j++) {
                                        String msg = "rabbitmq msg!";
                                        if(i + j != MSG_NUM -1 ){
                                            channel.basicPublish("", TRANSACTION, null, msg.getBytes());
                                        }else{
                                            channel.basicPublish("", TRANSACTION, null, "end".getBytes());
                                        }
                                    }
                                    i += BATCH;
                                    //commit
                                    channel.txCommit();
                                }
                            }

                        } catch (Exception e) {
                            //回滚事务
                            channel.txRollback();
                            e.printStackTrace();
                        } finally {
                            channel.close();
                        }
                        long end = System.currentTimeMillis();
                        System.out.println("[tx发送方]发送方耗时:" + (end - start)+" 批量大小="+BATCH);
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
                    channel.queueDeclare(TRANSACTION, false, false, false, null);
                    //每次1条
                    channel.basicQos(1);
                    long start = System.currentTimeMillis();
                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String msg = new String(body);
                            //发送ack
                            channel.basicAck(envelope.getDeliveryTag(), false);
                            if (msg.equalsIgnoreCase("end")){
                                long end = System.currentTimeMillis();
                                System.out.println("[tx接收方]接收完毕"+(end-start));
                                try {
                                    channel.close();
                                    connection.close();
                                } catch (TimeoutException e) {
                                    e.printStackTrace();
                                }System.out.println("测试结束:"+System.currentTimeMillis());

                            }
                        }
                    };
                    //手动ack
                    channel.basicConsume(TRANSACTION, false, consumer);
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

        private static final String CONFIRM = "confirm";
        private static CountDownLatch latch = new CountDownLatch(1);


        public static void main(String[] args) throws IOException, InterruptedException {
            //System.out.println("测试开始:"+System.currentTimeMillis());
            ExecutorService service = Executors.newFixedThreadPool(2);
            service.execute(new ConfirmConsumer());
            latch.await();
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
                        channel.queueDeclare(CONFIRM, false, false, false, null);
                        long start = System.currentTimeMillis();
                        try {
                            //开启confirm3 以channel为单位
                            channel.confirmSelect();
                            for (int i = 0; i < MSG_NUM; i++) {
                                String msg = "rabbitmq msg!";
                                if(i  != MSG_NUM -1){
                                    channel.basicPublish("", CONFIRM, null, msg.getBytes());
                                }else{
                                    channel.basicPublish("", CONFIRM, null, "end".getBytes());
                                }
                                //confirm
//                                  waitForConfirmsOrDie 相对于 waitForConfirms 来说,只要有nack就好抛出异常,同时也是一种阻塞式
                                channel.waitForConfirmsOrDie();
//                                channel.addConfirmListener(new ConfirmListener() {
//                                @Override
//                                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
////                                        System.out.println("ack deliveryTag = " + deliveryTag);
//                                }
//
//                                @Override
//                                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
////                                        System.out.println("nack deliveryTag = " + deliveryTag);
//                                }
//                            });
                            }
                        } catch (Exception e) {
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

                    channel.queueDeclare(CONFIRM, false, false, false, null);
                    //每次1条
                    channel.basicQos(1);
                    long start = System.currentTimeMillis();
                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String msg = new String(body);
                            //发送ack
                            channel.basicAck(envelope.getDeliveryTag(), false);
//                     System.out.println("确认"+msg);
                            if (msg.equals("end")){
                                long end = System.currentTimeMillis();
                                System.out.println("[confirm接收方]接收完毕"+(end-start));
                                try {
                                    channel.close();
                                    connection.close();
                                } catch (TimeoutException e) {
                                    e.printStackTrace();
                                }
//                                System.out.println("测试结束:"+System.currentTimeMillis());
                            }
                        }
                    };
                    //手动ack
                    channel.basicConsume(CONFIRM, false, consumer);
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
