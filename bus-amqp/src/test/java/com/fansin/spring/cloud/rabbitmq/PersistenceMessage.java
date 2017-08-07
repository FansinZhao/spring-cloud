package com.fansin.spring.cloud.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PersistenceMessage {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //1 exchange 持久化
        channel.exchangeDeclare("persistence", "direct", true);
        //2 queue 持久化
        channel.queueDeclare("queue.persistence",true,false,false,null);
        channel.queueBind("queue.persistence","persistence","");
        //3 发送的消息持久化 deliveryMode = 2
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).build();
        channel.basicPublish("persistence","",basicProperties,"hello".getBytes());
    }

}
