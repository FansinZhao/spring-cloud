package com.fansin.spring.cloud.mybatis.dao;

import com.fansin.spring.cloud.mybatis.mapper.SeckilllMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhaofeng on 17-4-9.
 */
@Service
public class SeckillDao {
    @Autowired
    SeckilllMapper seckilllMapper;

    public void test(){
        seckilllMapper.queryById(1000l);
    }
}
