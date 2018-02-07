package com.fansin.spring.cloud.mybatis.dao;

import com.fansin.spring.cloud.mybatis.entity.Mybatis;
import com.fansin.spring.cloud.mybatis.mapper.MybatisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Mybatis dao.
 *
 * @author zhaofeng on 17-4-9.
 */
@Service
public class MybatisDao {
    /**
     * The Mybatis mapper.
     */
    @Autowired
    MybatisMapper mybatisMapper;

    /**
     * Query mybatis.
     *
     * @param mybatisId the mybatis id
     * @return the mybatis
     */
    public Mybatis query(Long mybatisId){
        return mybatisMapper.queryById(mybatisId);
    }

    /**
     * Insert int.
     *
     * @param mybatis the mybatis
     * @return the int
     */
    public int insert(Mybatis mybatis){
        return mybatisMapper.insert(mybatis);
    }

    /**
     * Query all list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    public List<Mybatis> queryAll(int offset, int limit){
        return mybatisMapper.queryAll(offset,limit);
    }
}
