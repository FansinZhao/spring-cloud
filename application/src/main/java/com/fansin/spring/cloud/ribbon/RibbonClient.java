package com.fansin.spring.cloud.ribbon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaofeng on 17-6-5.
 */
@Component
public class RibbonClient implements InitializingBean,DisposableBean{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoadBalancerClient balancerClient;

    @Autowired
    private RestTemplate restTemplate;

    private ConcurrentHashMap<String,URI> uriCache = new ConcurrentHashMap<>();


    private RibbonClient(){

    }

    /**
     * 调用eureka客户端
     * @param service 应用对应的接口
     * @return
     */
    public String invokeForString(String serviceId,String service,Object... uriVariables){

        Assert.notNull(serviceId,"serviceId 不能为空!");
        //优先从缓冲获取!
        URI  uri = uriCache.getOrDefault(serviceId+service,null);
        //第一次获取URI
        if (uri==null){
            ServiceInstance instance = balancerClient.choose(serviceId);
            uri = instance.getUri();
            Assert.notNull(uri,"无法获取实例uri");
            //保存到cache
            uriCache.putIfAbsent(serviceId+service,uri);
        }
        logger.info("serviceId = [" + serviceId + "], service = [" + service + "]");
        String result = restTemplate.getForObject(uri+ File.separator+service,String.class,uriVariables);
        return result;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO
    }

    @Override
    public void destroy() throws Exception {
        uriCache.clear();
    }
}

