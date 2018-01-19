package com.fansin.spring.cloud.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created with IntelliJ IDEA.
 *
 * spring session 借助SessionRepositoryFilter将Tomcat中的HttpSession替换为SpringSession(例如RedisSession)
 * 然后将session信息存储起来,从而实现session的共享.
 *
 * 场景: session共享
 * 特点:
 * 1 第三方存储session
 * 2 restful api,不依赖cookie,使用header传递jessionID,便于用户管理
 * 3 不依赖web,集群环境下的session共享
 * 4 单浏览器多session
 *
 * @author fansin
 * @version 1.0
 * @date 18 -1-13 上午12:26
 */
@EnableRedisHttpSession
@RestController
@RequestMapping("/share")
public class ShareSessionController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Index string.
     *
     * @return the string
     */
    @RequestMapping
    public String index(){
        return "1先启动应用,然后访问注册用户,访问用户可见,可以在redis看到key值\n" +
                "2更换端口,重启应用,然后访问用户,依然可见,redis值保持不变\n" +
                "3轻松实现分布式环境下,单浏览器负载访问后端session一致性问题";
    }


    /**
     * Register string.
     *
     * session信息以hash形式存放在redis中,更详细描述{@link RedisOperationsSessionRepository}
     * 同时会缓存在本地内存中,使用map存储
     *
     * @param session the session
     * @param userId  the user id
     * @return the string
     */
    @RequestMapping("register/{userId}")
    public String register(HttpSession session,@PathVariable String userId){
        session.setAttribute(userId,userId+ ThreadLocalRandom.current().nextInt());
        return "首次访问,持久化到redis";
    }

    /**
     * Get string.
     * 从缓存Map中获取,新应用启动会加载redis保存的信息
     * @return the string
     */
    @RequestMapping("visit/{userId}")
    public String visit(HttpSession session,@PathVariable String userId){
        Optional<Object> value = Optional.of(session.getAttribute(userId));
        return value.toString();
    }
}
