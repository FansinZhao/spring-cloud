package com.fansin.spring.cloud.controller;

import com.fansin.spring.cloud.ribbon.RibbonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaofeng on 17-6-5.
 */
//RestController see Controller ResponseBody
@RestController
public class RibbonController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RibbonClient ribbonClient;

    @Value("${ServiceId.RibbonController}")
    private String serviceId;


    @RequestMapping("/")
    public String ops(){
        return "Startup successful";
    }

    @RequestMapping("/doService/{service}")
    public String doService(@PathVariable String service){
        return ribbonClient.invokeForString(serviceId,service);
    }

    @RequestMapping("/service")
    public String service(){
        return ">>>>>>>>RibbonController.service<<<<<<<<";
    }



}
