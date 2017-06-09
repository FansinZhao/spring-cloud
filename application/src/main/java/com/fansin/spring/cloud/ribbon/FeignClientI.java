package com.fansin.spring.cloud.ribbon;

import com.fansin.spring.cloud.hystrix.FeignClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zhaofeng on 17-6-9.
 */
@FeignClient(value = "${ServiceId.FeignController}",fallback = FeignClientFallback.class)
public interface FeignClientI {

    @RequestMapping("/feignService")
    String feignService();

    @RequestMapping("/feignServiceFail")
    String feignServiceFail();
}
