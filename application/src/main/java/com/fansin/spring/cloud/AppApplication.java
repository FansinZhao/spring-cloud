package com.fansin.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author fansin
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableFeignClients
public class AppApplication {

	@Bean
	RestTemplate restTemplate() {
        return new RestTemplate();
    }

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
		//多进程
		/*ProcessBuilder processBuilder = new ProcessBuilder("java -version");
		processBuilder.redirectErrorStream(true);
		processBuilder.command(new File(System.getProperty("java.home"), "bin/java").toString(), "ProcessTest");
		processBuilder.redirectErrorStream(true);
		processBuilder.directory(new File(AppApplication.class.getResource("/").getPath()));
		try {
			Process process = processBuilder.start();
			InputStream in = process.getInputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
			byte[] bytes = new byte[10240];
			bufferedInputStream.read(bytes,0,10240);
			System.out.println(new String(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
