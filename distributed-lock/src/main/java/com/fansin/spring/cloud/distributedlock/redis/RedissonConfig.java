package com.fansin.spring.cloud.distributedlock.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18-3-9 上午7:14
 */
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson(RedissonProperties properties) {
        return Redisson.create(properties.build());
    }
}
