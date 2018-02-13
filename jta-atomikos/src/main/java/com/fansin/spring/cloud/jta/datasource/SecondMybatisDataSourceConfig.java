package com.fansin.spring.cloud.jta.datasource;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18 -2-12 下午11:18
 */
@EnableAutoConfiguration
@Configuration
@MapperScan(basePackages = "com.fansin.spring.cloud.jta.mybatis.consumer.mapper",sqlSessionFactoryRef = "secondSqlSessionFactory",sqlSessionTemplateRef = "secondSqlSessionTemplate")
public class SecondMybatisDataSourceConfig {

    private String RESOURCE_PATH = "classpath*:mapper/consumer/**/*.xml";

    /**
     * Second data source properties data source properties.
     *
     * @return the data source properties
     */
    @Bean
    @ConfigurationProperties("spring.datasource.second")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Second data source data source.
     *
     * 这里需要将datasource更换为AtomikosDataSourceBean，其他的配置
     * @see org.springframework.boot.autoconfigure.transaction.jta.AtomikosJtaConfiguration
     *
     * @return the data source
     */
    @Bean
    @ConfigurationProperties("spring.datasource.second")
    public DataSource secondDataSource() {
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource((MysqlXADataSource)secondDataSourceProperties().initializeDataSourceBuilder().build());
        xaDataSource.setUniqueResourceName("secondDataSource");
        return xaDataSource;
    }

    /**
     * Second sql session factory sql session factory.
     *
     * @param dataSource the data source
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Bean
    public SqlSessionFactory secondSqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(RESOURCE_PATH));
        return factoryBean.getObject();

    }

    /**
     * Second sql session template sql session template.
     *
     * @param dataSource the data source
     * @return the sql session template
     * @throws Exception the exception
     */
    @Bean
    public SqlSessionTemplate secondSqlSessionTemplate(@Qualifier("secondDataSource") DataSource dataSource) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(secondSqlSessionFactory(dataSource));
        return template;
    }

}
