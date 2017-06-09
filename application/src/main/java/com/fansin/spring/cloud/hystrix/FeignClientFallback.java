package com.fansin.spring.cloud.hystrix;

import com.fansin.spring.cloud.ribbon.FeignClientI;
import org.springframework.stereotype.Component;

/**
 * Created by zhaofeng on 17-6-9.
 */
@Component
public class FeignClientFallback implements FeignClientI {
    @Override
    public String feignService() {
        return "降级方法:>>>>>feignService";
    }

    @Override
    public String feignServiceFail() {
        return "降级方法:>>>>>feignServiceFail";
    }
}
