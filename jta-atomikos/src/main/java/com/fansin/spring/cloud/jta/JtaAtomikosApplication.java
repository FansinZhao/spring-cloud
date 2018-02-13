package com.fansin.spring.cloud.jta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 配置方式
 * <p>
 * https://www.atomikos.com/bin/view/Documentation/SpringIntegration#Configuring_Atomikos_as_the_Spring_JTA_Transaction_Manager
 * <p>
 * spring boot 默认配置文件
 *
 * @author fansin
 * @date 2018 -01-29
 * @see org.springframework.boot.autoconfigure.transaction.jta.AtomikosJtaConfiguration
 */
@SpringBootApplication
public class JtaAtomikosApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		SpringApplication.run(JtaAtomikosApplication.class, args);
	}
}
