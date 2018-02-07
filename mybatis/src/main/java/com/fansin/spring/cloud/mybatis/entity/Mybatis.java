package com.fansin.spring.cloud.mybatis.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * The type Mybatis.
 *
 * @author zhaofeng on 17-4-9.
 */
@Data
public class Mybatis {

    private long mybatisId;
    private String name;
    private int number;
    private LocalDateTime createTime;

}
