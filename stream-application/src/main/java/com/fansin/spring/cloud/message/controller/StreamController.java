package com.fansin.spring.cloud.message.controller;

import com.fansin.spring.cloud.stream.HelloBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaofeng on 17-6-20.
 */
@RestController
public class StreamController {


    @Autowired
    HelloBinding helloBinding;


    @RequestMapping("/")
    public String index(){
        return "StreamApplication Startup successful!";
    }

    @RequestMapping("/send/{msg}")
    public String sendMsg(@PathVariable String msg){
        helloBinding.appOutput().send(MessageBuilder.withPayload(msg.getBytes()).build());
        return "发送消息:"+msg + " "+ System.currentTimeMillis();
    }
}
