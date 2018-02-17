package com.fansin.spring.cloud.jta.mybatis.publisher.mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.fansin.spring.cloud.jta.mybatis.publisher.entity.Publisher;

/**
 * The interface Publisher base mapper.
 *
 * @author author
 */
@Mapper
public interface PublisherBaseMapper {

    /**
     * Insert publisher int.
     *
     * @param object the object
     * @return the int
     */
    int insertPublisher(Publisher object);

    /**
     * Update publisher int.
     *
     * @param object the object
     * @return the int
     */
    int updatePublisher(Publisher object);

    /**
     * Query publisher list.
     *
     * @param object the object
     * @return the list
     */
    List<Publisher> queryPublisher(Publisher object);

    /**
     * Query publisher limit 1 publisher.
     *
     * @param object the object
     * @return the publisher
     */
    Publisher queryPublisherLimit1(Publisher object);

}