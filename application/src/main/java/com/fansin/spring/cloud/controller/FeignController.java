package com.fansin.spring.cloud.controller;

import com.fansin.spring.cloud.ribbon.FeignClientI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaofeng on 17-6-9.
 */
@RestController
public class FeignController {

    @Autowired
    private FeignClientI feignClient;

    @RequestMapping("/invokeFeignClient")
    public String invokerRemoteService(){
        return feignClient.feignService();
    }
    @RequestMapping("/feignService")
    private String feignService(){
        return ">>>>FeignClient<<<<";
    }

}
