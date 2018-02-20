package com.fansin.spring.cloud.bytetcc.provider.config;

import org.bytesoft.bytejta.supports.jdbc.LocalXADataSource;
import org.bytesoft.bytetcc.supports.springcloud.config.SpringCloudConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * The type Provider config.
 */
@Import(SpringCloudConfiguration.class)
@Configuration
public class ProviderConfig {

	/**
	 * Gets data source.
	 *
	 * @return the data source
	 */
	@Bean(name = "dataSource")
	public DataSource getDataSource(DataSourceProperties properties) {
		LocalXADataSource dataSource = new LocalXADataSource();
		dataSource.setDataSource(properties.initializeDataSourceBuilder().build());
		return dataSource;
	}
}
