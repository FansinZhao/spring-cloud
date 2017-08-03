package com.fansin.spring.cloud.rabbitmq;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.rabbitmq.client.*;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.concurrent.*;

public class ProtoBuff {


    private static ConnectionFactory factory = new ConnectionFactory();

    private static final String PROTOBUFF = "protobuff";
    private static final String NO_PROTOBUFF = "no_protobuff";

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(1);

    private static final int NUM = 100000;


    private  static  byte[] testBody(){
        StringBuilder sb = new StringBuilder(1024);
        for (int i = 0; i < 256; i++) {
            sb.append("test");
        }
        return sb.toString().getBytes();
    }


    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

        //TODO
        /*
        * 测试发现,protostuff 压缩 1024 的字符串后,反而大小变为了4倍大小?!!!
        * */

        String test = "2";
        switch (test){
            case "1":

                ExecutorService service = Executors.newFixedThreadPool(2);
                service.execute(new NoConsumer());
                cyclicBarrier.await();
                service.execute(new NoPulbisher());
                service.shutdown();


                break;
            case "2":

                ExecutorService bufferService = Executors.newFixedThreadPool(2);
                bufferService.execute(new BufferConsumer());
                cyclicBarrier.await();
                bufferService.execute(new BufferPublisher());
                bufferService.shutdown();


                break;
        }





    }



    static class NoPulbisher implements Runnable{
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
                    channel.exchangeDeclare(NO_PROTOBUFF, ExchangeTypes.DIRECT,false,true,null);

                    byte[] bytes = testBody();
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < NUM; i++) {
                        channel.basicPublish(NO_PROTOBUFF,"",null,bytes);
                    }
                    channel.basicPublish(NO_PROTOBUFF,"",null,"over".getBytes());
                    long end = System.currentTimeMillis();
                    System.out.println("服务端发送完毕."+(end - start));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    static class NoConsumer implements Runnable{

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
                channel.exchangeDeclare(NO_PROTOBUFF,ExchangeTypes.DIRECT,false,true,null);
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName,NO_PROTOBUFF,"");
                long start = System.currentTimeMillis();
                channel.basicConsume(queueName,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String msg = new String(body);
                        if(msg.startsWith("over")){
                            System.out.println("接收完毕>>"+(System.currentTimeMillis()-start));
                        }
                    }
                });
                System.out.println("客户端启动.");
                cyclicBarrier.await();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }


    private static RuntimeSchema<String> runtimeSchema = RuntimeSchema.createFrom(String.class);

    static class BufferPublisher implements Runnable{
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
                    channel.exchangeDeclare(PROTOBUFF, ExchangeTypes.DIRECT,false,true,null);

                    byte[] bytes = testBody();
                    System.out.println(bytes.length);
                    bytes = ProtobufIOUtil.toByteArray(new String(bytes), runtimeSchema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < NUM; i++) {
                        channel.basicPublish(PROTOBUFF,"",null,bytes);
                    }

                    byte[] over = ProtobufIOUtil.toByteArray("over", runtimeSchema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                    channel.basicPublish(PROTOBUFF,"",null,over);
                    long end = System.currentTimeMillis();
                    System.out.println("服务端发送完毕."+(end - start) +" size: "+ bytes.length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    static class BufferConsumer implements Runnable{

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
                channel.exchangeDeclare(PROTOBUFF,ExchangeTypes.DIRECT,false,true,null);
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName,PROTOBUFF,"");
                long start = System.currentTimeMillis();
                channel.basicConsume(queueName,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String msg = runtimeSchema.newMessage();
                        ProtobufIOUtil.mergeFrom(body,msg,runtimeSchema);
                        if(msg.startsWith("over")){
                            System.out.println("接收完毕>>"+(System.currentTimeMillis()-start));
                        }
                    }
                });
                System.out.println("客户端启动.");
                cyclicBarrier.await();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }




}
