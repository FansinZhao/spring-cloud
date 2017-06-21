package com.fansin.spring.cloud.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by zhaofeng on 17-6-20.
 */
public interface HelloBinding {

    String APP_INPUT = "appInput";
    String APP_INPUT_ONE = "appInput1";
    String APP_OUTPUT = "appOutput";

    //input 对应 app内的 StreamListener

    //(MQ->)binder->app接收消息
    @Input //默认方法名
    SubscribableChannel appInput();

    //(MQ->)binder->app接收消息
    @Input //默认方法名
    SubscribableChannel appInput1();

    //app->binder(->MQ)发送消息
    @Output //默认方法名
    MessageChannel appOutput();
}
