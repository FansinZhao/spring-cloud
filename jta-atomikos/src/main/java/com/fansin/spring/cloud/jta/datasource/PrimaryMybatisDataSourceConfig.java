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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @EnableAutoConfiguration 会覆盖默认的datasource配置文件 ，如果不添加将不会生效
 * @date 18 -2-12 下午11:18
 */
@EnableAutoConfiguration
@Configuration
@MapperScan(basePackages = "com.fansin.spring.cloud.jta.mybatis.publisher",sqlSessionFactoryRef = "primarySqlSessionFactory",sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryMybatisDataSourceConfig {

    private String RESOURCE_PATH = "classpath*:mapper/publisher/**/*.xml";


    /**
     * Primary data source properties data source properties.
     *
     * 必须设置一个数据源为 @Primary
     *
     * @return the data source properties
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Primary data source data source.
     *
     * 这里需要将datasource更换为AtomikosDataSourceBean，其他的配置
     *
     * @see org.springframework.boot.autoconfigure.transaction.jta.AtomikosJtaConfiguration
     *
     * @return the data source
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.primary")
    public DataSource primaryDataSource() {
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource((MysqlXADataSource)primaryDataSourceProperties().initializeDataSourceBuilder().build());
        xaDataSource.setUniqueResourceName("primaryDataSource");
        return xaDataSource;
    }

    /**
     * Primary sql session template sql session template.
     *
     * @param dataSource the data source
     * @return the sql session template
     * @throws Exception the exception
     */
    @Primary
    @Bean
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(primarySqlSessionFactory(dataSource));
        return template;
    }

    /**
     * Primary sql session factory sql session factory.
     *
     * @param dataSource the data source
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Primary
    @Bean
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(RESOURCE_PATH));
        return factoryBean.getObject();
    }



}
