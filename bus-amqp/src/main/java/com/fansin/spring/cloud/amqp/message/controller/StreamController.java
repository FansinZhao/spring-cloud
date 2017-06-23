package com.fansin.spring.cloud.amqp.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaofeng on 17-6-20.
 */
@RestController
public class StreamController {


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
