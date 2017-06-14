package com.fansin.spring.cloud.controller;

/**
 * Created by zhaofeng on 17-6-12.
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LegacyController {

    @RequestMapping("/")
    public String legacy(){
        return "对不起,无法响应您此类请求!";
    }


}
