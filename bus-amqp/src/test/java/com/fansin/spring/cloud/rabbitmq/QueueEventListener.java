package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 开启插件:
 *
 * rabbitmq-plugins enable rabbitmq_event_exchange
 *
 * 通过开启rabbitmq_event_exchange插件,创建一个"amq.rabbitmq.event" exchange, 所有的队列操作,会发往此exchange.
 */
public class QueueEventListener {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");


        Connection connection = factory.newConnection(new Address[]{new Address("172.17.0.3", 5672)});
        Channel channel = connection.createChannel();
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue,"amq.rabbitmq.event","#.#");

        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String event = envelope.getRoutingKey();
                Map<String, Object> headers = properties.getHeaders();
                Object name = headers.get("name");
                Object vhost = headers.get("vhost");
                Object durable = headers.get("durable");
                System.out.println("事件: " +" "+ event + " "+name + " " + durable + " " +vhost);
            }
        });
        System.out.println("监听rabbitmq事件....");
    }


}
