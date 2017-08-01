package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * http://www.rabbitmq.com/tutorials/tutorial-six-java.html
 */
public class RPCDemo {


    private static ConnectionFactory factory = new ConnectionFactory();

    private static final String REQUEST_QUEUE = "rpc_queue";

    private static CountDownLatch latch = new CountDownLatch(1);


    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new RPCServer());
        latch.await();
        service.execute(new RPCClient("4"));
        service.shutdown();

    }


    static class RPCServer implements Runnable{


        private int fibonacci(int value){
            if(value == 0 || value == 1){
                return value;
            }else {
                return fibonacci(value-1)+fibonacci(value-2);
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
            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.basicQos(1);
                channel.queueDeclare(REQUEST_QUEUE,false,false,true,null);
                System.out.println("RPC 服务器等待....");
                channel.basicConsume(REQUEST_QUEUE,false,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String replyQueue = properties.getReplyTo();
                        AMQP.BasicProperties replyProp = new AMQP.BasicProperties().builder().correlationId(properties.getCorrelationId()).build();
                        String message = new String(body);
                        int n = Integer.parseInt(message);
                        String responseBody =String.valueOf(fibonacci(n));
                        channel.basicPublish("",properties.getReplyTo(),replyProp,responseBody.getBytes());
                        channel.basicAck(envelope.getDeliveryTag(),false);
                        System.out.println("计算 Fibonacci ["+message+"] = "+responseBody);
                    }
                });
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }


    static class RPCClient implements Runnable{


        String message;

        public RPCClient(String message) {
            this.message = message;
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
                String queueName = channel.queueDeclare().getQueue();
                String correlationId = UUID.randomUUID().toString();
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().correlationId(correlationId).replyTo(queueName).build();
                channel.basicPublish("",REQUEST_QUEUE,properties,message.getBytes());

                BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);
                channel.basicConsume(queueName,true,new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        if (correlationId.equalsIgnoreCase(properties.getCorrelationId())){
                            response.offer(new String(body));
                        }
                    }
                });

                System.out.println("接收到消息:"+response.take());


            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
