package com.fansin.spring.cloud.hystrix;

import com.fansin.spring.cloud.ribbon.RibbonClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhaofeng on 17-6-8.
 *
 * 访问 http://127.0.0.1:8011/hystrix 打开面板 可在日志中发现 "Proxy opening connection to:"字样
 * 输入 http://127.0.0.1:8011/hystrix.stream 查看单机版的熔断信息
 *
 */
@Component
public class CircuitBreakerService {

    @Autowired
    private RibbonClient ribbonClient;

    @HystrixCommand(fallbackMethod = "fallback",commandProperties = {@HystrixProperty(name = "execution.isolation.strategy",value = "SEMAPHORE")})
    public String invokeRemoteService(String serviceId,String service){
        //调用服务
        //成功: curl -v http://127.0.0.1:8011/doService/service
        //失败: curl -v http://127.0.0.1:8011/doService/service1
        String result = ribbonClient.invokeForString(serviceId,service);
        return "正常方法successful>>"+result;
    }

    /**
     * 降级方法 参数必须跟熔断方法一致
     * @param serviceId
     * @param service
     * @return
     */
    private String fallback(String serviceId,String service){
        return "降级方法:ok "+serviceId+" "+service;
    }
}
