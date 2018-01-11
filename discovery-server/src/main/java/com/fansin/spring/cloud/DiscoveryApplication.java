package com.fansin.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author fansin
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication {

	public static void main(String[] args) {

		SpringApplication.run(DiscoveryApplication.class, args);
	}
}
