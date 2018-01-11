package com.fansin.spring.cloud.kafka.message;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;


/**
 * The type Message receiver.
 *
 * @author zhaofeng
 * @date 17 -6-20.
 */
@Component
public class MessageReceiver {

    /**
     * Receive msg.
     * 异步 只能监听 @input
     * @param msg the msg
     */
    @StreamListener(Processor.INPUT)
    public void receiveMsg(String msg) {
        System.out.println("接收消息 msg = " + msg);
    }


//    //同步
//    @StreamListener(Processor.INPUT)
//    @SendTo(Processor.OUTPUT)
//    public SinkMessage receiveAndSend(SinkMessage msg) {
//
//        System.out.println("接收并发送消息 msg = " + msg+"-> 响应成功!");
//
//        return msg;
//    }



}
