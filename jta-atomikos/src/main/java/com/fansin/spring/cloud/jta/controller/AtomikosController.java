package com.fansin.spring.cloud.jta.controller;

import com.fansin.spring.cloud.jta.mybatis.consumer.entity.Consumer;
import com.fansin.spring.cloud.jta.mybatis.consumer.mapper.base.ConsumerBaseMapper;
import com.fansin.spring.cloud.jta.mybatis.publisher.entity.Publisher;
import com.fansin.spring.cloud.jta.mybatis.publisher.mapper.base.PublisherBaseMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.transaction.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18 -1-24 下午4:54
 */
@RestController
@RequestMapping("atomikos/")
public class AtomikosController {

    @Autowired
    private JtaTransactionManager jtaTransactionManager;

    @Autowired
    private ConsumerBaseMapper consumerMapper;

    @Autowired
    private PublisherBaseMapper publisherMapper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    private String QUEUE_NAME = "sample.queue";

    /**
     * Atomikos string.
     *
     * 多个数据源同时成功或同时失败
     *
     * @return the string
     */
    @RequestMapping
    public String atomikos(){

        try {
            TransactionManager transactionManager = jtaTransactionManager.getTransactionManager();
            transactionManager.begin();
            //第一个 第一db数据源
            System.out.println("分布式事务开始....");
            Publisher publisher = new Publisher();
            publisher.setName("first send");
            publisher.setSend_time(LocalDateTime.now());
            publisher.setResult(0);
            publisherMapper.insertPublisher(publisher);
            System.out.println("插入publisher成功！");

            //第二个 mq数据源
            System.out.println("发送jms消息");
            jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(QUEUE_NAME),"publisher写入成功！");

            //第三个 第二db数据源
            Consumer consumer = new Consumer();
            //这里设置了主键，所以只能插入一次，第二次插入将会报错
            consumer.setId(BigDecimal.valueOf(100));
            consumer.setName("first received");
            consumer.setRecv_time(LocalDateTime.now());
            consumer.setResult(1);
            consumerMapper.insertConsumer(consumer);
            System.out.println("插入consumer成功！");


            transactionManager.commit();
            System.out.println("分布式事务提交....");
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (HeuristicMixedException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {
            e.printStackTrace();
        } catch (RollbackException e) {
            e.printStackTrace();
        }
        return "successful";
    }


    /**
     * 监听队列 队列名称看
     * @see AtomikosController#QUEUE_NAME
     * @param payload
     */
    @JmsListener(destination = "sample.queue")
    public void receiveQueue(String payload){
        System.out.println("payload = " + payload);
    }

}
