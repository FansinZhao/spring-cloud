package com.fansin.spring.cloud.distributedlock.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * 原理:
 * 使用redis中的hash数据结构,提供了包括jdk常用JUC锁和多锁(redlock锁)等分布式锁
 * <p>
 * redlock
 * 官方推荐的一种分布式锁,使用多个(5+)redis保存锁,当获取多数实例锁后才能拿到锁.
 * 提供了超时,延迟
 *
 * @author fansin
 * @version 1.0
 * @date f18-3-9 上午8:04
 */
@Slf4j
@RestController
@RequestMapping("/redisson")
public class RedissonLockController {

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping
    public String redLock() {
        //使用时更换为多节点版
        RLock lock1 = redissonClient.getLock("lock1");
        RLock lock2 = redissonClient.getLock("lock2");
        RLock lock3 = redissonClient.getLock("lock3");
        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);
        redLock.lock();
        log.info("加锁中...自己业务处理");
        redLock.unlock();
        return "redlock";
    }

}
