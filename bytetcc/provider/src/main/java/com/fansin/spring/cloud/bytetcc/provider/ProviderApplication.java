package com.fansin.spring.cloud.bytetcc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;

/**
 * Created with IntelliJ IDEA.
 *
 * TODO
 *
 * @ImportResource 这里使用了很多xml配置文件.有时间可以将其转化为annotation
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-21 下午23:51
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
