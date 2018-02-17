package com.fansin.spring.cloud.bytetcc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;

/**
 * TODO
 *
 * @ImportResource 这里使用了很多xml配置文件.有时间可以将其转化为annotation
 */
@ImportResource({ "classpath:bytetcc-supports-springcloud.xml" })
@EnableDiscoveryClient
@SpringBootApplication
public class ProviderApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}
