package com.fansin.spring.cloud.springbootadmin;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * @author fansin
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class SpringbootAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAdminApplication.class, args);
	}
}
