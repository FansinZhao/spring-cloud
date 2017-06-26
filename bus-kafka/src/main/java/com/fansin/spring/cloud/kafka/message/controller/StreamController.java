package com.fansin.spring.cloud.kafka.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaofeng on 17-6-20.
 */
//@RefreshScope --> /bus/refresh
@RefreshScope
@RestController
public class StreamController {

    @Value("${bus.key}")
    private String busValue;

    @RequestMapping("/busEnv")
    public String showEnv(){
        return busValue;
    }

    @Autowired
    private Processor processor;

    @RequestMapping("/")
    public String index(){
        return "StreamApplication Startup successful!";
    }

    @RequestMapping("/send/{msg}")
    public String sendMsg(@PathVariable String msg){
        boolean result = processor.output().send(MessageBuilder.withPayload(msg).build());
        return result+" 发送消息:"+msg + " "+ System.currentTimeMillis();
    }
}
