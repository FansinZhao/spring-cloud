package com.fansin.spring.cloud.controller;

import com.fansin.spring.cloud.mybatis.dao.MybatisDao;
import com.fansin.spring.cloud.mybatis.entity.Mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18 -2-1 上午8:54
 */
@RestController
@RequestMapping("/mybatis")
public class MybatisController {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * Query string.
     *
     * @return the string
     */
    @RequestMapping
    public String query(){
        List<Mybatis> mybatisList =mybatisDao.queryAll(0,1000);

        StringBuilder stringBuilder = new StringBuilder();

        mybatisList.stream().forEach(x-> stringBuilder.append(x.toString()).append("\n"));

        return stringBuilder.toString();
    }

    /**
     * Insert string.
     *
     * @return the string
     */
    @RequestMapping("insert")
    public String insert(){
        Mybatis mybatis = new Mybatis();
        mybatis.setName("web插入");
        mybatis.setNumber(123);
        mybatis.setCreateTime(LocalDateTime.now());
        int result = mybatisDao.insert(mybatis);
        return "插入结果："+result;
    }

}
