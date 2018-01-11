package com.fansin.spring.cloud.controller;

import com.fansin.spring.cloud.hystrix.CircuitBreakerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Ribbon controller.
 * RestController see Controller ResponseBody
 * @author zhaofeng on 17-6-5.
 */
@RestController
public class RibbonController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${ServiceId.RibbonController}")
    private String serviceId;

    @Autowired
    private CircuitBreakerService circuitBreakerService;


    /**
     * Ops string.
     *
     * @return the string
     */
    @RequestMapping("/")
    public String ops(){
        return "Startup successful";
    }

    /**
     * Do service string.
     *
     * @param service the service
     * @return the string
     */
    @RequestMapping("/doService/{service}")
    public String doService(@PathVariable String service){
        return circuitBreakerService.invokeRemoteService(serviceId,service);
    }

    /**
     * Service string.
     *
     * @return the string
     */
    @RequestMapping("/service")
    public String service(){

        return ">>>>>>>>RibbonController.service<<<<<<<<";
    }



}
