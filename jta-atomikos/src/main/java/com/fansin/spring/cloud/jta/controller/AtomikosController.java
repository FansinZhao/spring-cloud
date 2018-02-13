package com.fansin.spring.cloud.jta.controller;

import com.fansin.spring.cloud.jta.mybatis.consumer.entity.Consumer;
import com.fansin.spring.cloud.jta.mybatis.consumer.mapper.base.ConsumerBaseMapper;
import com.fansin.spring.cloud.jta.mybatis.publisher.entity.Publisher;
import com.fansin.spring.cloud.jta.mybatis.publisher.mapper.base.PublisherBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Atomikos string.
     *
     * @return the string
     */
    @RequestMapping
    public String atomikos(){

        try {
            TransactionManager transactionManager = jtaTransactionManager.getTransactionManager();
            transactionManager.begin();
            System.out.println("分布式事务开始....");
            Publisher publisher = new Publisher();
            publisher.setName("first send");
            publisher.setSend_time(LocalDateTime.now());
            publisher.setResult(0);
            publisherMapper.insertPublisher(publisher);
            System.out.println("插入publisher成功！");


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

}
