package com.fansin.spring.cloud.sleuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;
import zipkin.server.EnableZipkinServer;
/**
 * @author fansin
 */
@SpringBootApplication
@EnableZipkinServer
@EnableZipkinStreamServer
@EnableDiscoveryClient
public class SleuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleuthApplication.class, args);
	}
}
