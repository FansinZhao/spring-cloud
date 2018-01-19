package com.fansin.spring.cloud.web.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18 -1-16 下午5:47
 */
@EnableCaching
@RestController
@RequestMapping("/cache")
public class CacheController {

    /**
     * Index string.
     *
     * @return the string
     */
    @RequestMapping
    public String index(){
        return "<p>spring cache 会按照顺序查找下面的缓存库(强调一下,有顺序的)\n</p>" +
                "<p>Generic --适合包含org.springframework.cache.Cache\n</p>" +
                "<p>JCache(JSR-107) -- 定义了一套缓存规范,5中接口,CachingProvider->CacheManager->Cache->Entry,Expiry.可以使用其他兼容库(类似sf4j),下面所有的库都是基于这个标准的! \n</p>" +
                "<p/>" +
                "<p>EhCache -- 适合缓存访问实时性高 Hibernate默认的内存/磁盘级缓存\n</p>" +
                "<p>Hazelcast --大数据下效率高 基于内存的数据网格管理平台,高度可扩展的数据分发和集群平台\n</p>" +
                "<p>Infinispan --高并发下 基于内存来进行键值对存储的分布式存储工具\n</p>" +
                "<p>Couchbase --需要高性能,高可用性和高伸缩性场景 具有高性能、可扩展性和可用性强的数据库引擎,分布式数据库\n</p>" +
                "<p>Redis --业务数据类型多样 高性能的key-value数据库\n</p>" +
                "<p>Caffeine --高性能场景 java8下高性能库,是guava的重写版\n</p>" +
                "<p>Guava--已废弃,使用Caffeine\n</p>" +
                "<p>Simple --代价昂贵,不推荐 使用ConcurrentHashMap做简单缓存";
    }

    /**
     * User string.
     *
     * @param userId the user id
     * @return the string
     */
    @Cacheable(value = "user",key = "#userId",sync = true)
    @RequestMapping("user/{userId}")
    public String user(@PathVariable String userId){
        System.out.println("[第一次访问打印,之后直接从缓存获取,不会再到这里] userId = " + userId);
        return "首次访问保存到缓存 userId = " + userId;
    }

    /**
     * Update string.
     *
     * @param userId the user id
     * @return the string
     */
    @CachePut(value = "user",key = "#userId",condition = "#userId.contains('Fansin')")
    @RequestMapping("update/{userId}")
    public String update(@PathVariable String userId){
        return "更新缓存!全小写"+userId.toLowerCase();

    }

    /**
     * Delete string.
     *
     * @param userId the user id
     * @return the string
     */
    @CacheEvict(value = "user",key = "#userId")
    @RequestMapping("delete/{userId}")
    public String delete(@PathVariable String userId){
        return "删除缓存! userId = "+userId;
    }

}
