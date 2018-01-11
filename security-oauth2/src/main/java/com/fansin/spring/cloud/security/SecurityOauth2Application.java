package com.fansin.spring.cloud.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
/**
 * @author fansin
 *
 * 开启Spring-cloud-security
 * @EnableGlobalMethodSecurity
 */
@SpringBootApplication
@EnableAuthorizationServer
@EnableOAuth2Client
public class SecurityOauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(SecurityOauth2Application.class, args);
	}
}
