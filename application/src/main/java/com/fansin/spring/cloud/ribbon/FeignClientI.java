package com.fansin.spring.cloud.ribbon;

import com.fansin.spring.cloud.hystrix.FeignClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author  zhaofeng
 * @date  17-6-9.
 */
@FeignClient(value = "${ServiceId.FeignController}",fallback = FeignClientFallback.class)
public interface FeignClientI {

    /**
     * Feign service string.
     *
     * @return the string
     */
    @RequestMapping("/feignService")
    String feignService();

    /**
     * Feign service fail string.
     *
     * @return the string
     */
    @RequestMapping("/feignServiceFail")
    String feignServiceFail();
}
