package com.fansin.spring.cloud.jta.mybatis.consumer.mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.fansin.spring.cloud.jta.mybatis.consumer.entity.Consumer;

/**
 * The interface Consumer base mapper.
 *
 * @author author
 */
@Mapper
public interface ConsumerBaseMapper {

    /**
     * Insert consumer int.
     *
     * @param object the object
     * @return the int
     */
    int insertConsumer(Consumer object);

    /**
     * Update consumer int.
     *
     * @param object the object
     * @return the int
     */
    int updateConsumer(Consumer object);

    /**
     * Query consumer list.
     *
     * @param object the object
     * @return the list
     */
    List<Consumer> queryConsumer(Consumer object);

    /**
     * Query consumer limit 1 consumer.
     *
     * @param object the object
     * @return the consumer
     */
    Consumer queryConsumerLimit1(Consumer object);

}