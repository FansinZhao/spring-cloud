package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 *http://www.rabbitmq.com/tutorials/tutorial-three-java.html
 *
 *
 * */
public class FanoutExchangeOrPubSub {

    public static void main(String[] args) throws InterruptedException {




        ExecutorService service = Executors.newFixedThreadPool(5);
        service.execute(new ReceiveLog("接收器1"));
        service.execute(new ReceiveLog("接收器2"));
        service.execute(new ReceiveLog("接收器3"));
        //等待客户端启动成功
        latch.await();
        service.execute(new LogEmitter("发送方1"));
        service.execute(new LogEmitter("发送方2"));
        service.shutdown();


    }

    private static final String EXCHANGE_LOG = "logs";

    private static ConnectionFactory factory = new ConnectionFactory();

    private static final int MSG_NUM = 5;

    private static CountDownLatch latch = new CountDownLatch(3);


    static class LogEmitter implements Runnable{


        String name = "";

        public LogEmitter(String name) {
            this.name = name;
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

            try (Connection connection = factory.newConnection()){
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_LOG, ExchangeTypes.FANOUT);

                for (int i = 0; i < MSG_NUM; i++) {

                    String logMsg =name +">>>> 日志.... "+i;

                    channel.basicPublish(EXCHANGE_LOG,"",null,logMsg.getBytes());
                }


                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }
    }

    static class ReceiveLog implements Runnable{

        String name = "";

        public ReceiveLog(String name) {
            this.name = name;
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
            try{
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_LOG, ExchangeTypes.FANOUT);
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName,EXCHANGE_LOG,"");
                Consumer consumer = new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String log = new String(body);
                        System.out.println(name+"<<<<  " + log);
                    }
                };
                channel.basicConsume(queueName,true,consumer);
                System.out.println(name +" 客户端等待中....." );
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

}
