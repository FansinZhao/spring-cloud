package com.fansin.spring.cloud.message;

import com.fansin.spring.cloud.stream.HelloBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author zhaofeng on 17-6-20.
 */
@Component
public class MessageReceiver {


    @StreamListener(HelloBinding.APP_INPUT)
    public void receiveMsg(byte[] msg) {
        System.out.println("接收消息 msg = " + new String(msg));
    }



    //只能通过程序发送才能到达?
    @StreamListener(HelloBinding.APP_OUTPUT)
    public void receiveMsgOut(byte[] msg) {
        //TODO 只能收到第二次的模拟消息?
        System.out.println("模拟接收output消息 msg = " + new String(msg));
    }


    @StreamListener(HelloBinding.APP_INPUT_ONE)
    @SendTo(HelloBinding.APP_OUTPUT)
    public byte[] receiveAndSend(byte[] msg) {

        System.out.println("接收并发送消息 msg = " + new String(msg)+"-> 响应成功!");

        return (new String(msg)+"响应成功!").getBytes();
    }



}
