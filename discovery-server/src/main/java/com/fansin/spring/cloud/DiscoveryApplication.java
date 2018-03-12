package com.fansin.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * 基于AP(CAP理论)的peer-to-peer服务发现Eurake(有趣小知识:古希腊语:阿基米德发现浮力定律时,我发现了)
 * 特点:
 * 	AP: peer-to-peer,客户端缓存,自我保护
 * 	restful
 *
 * vs consul vs zookeeper
 *
 * consul: raft,一种用于保证命令
 *
 *
 *
 *
 * @author fansin
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryApplication.class, args);
	}
}
