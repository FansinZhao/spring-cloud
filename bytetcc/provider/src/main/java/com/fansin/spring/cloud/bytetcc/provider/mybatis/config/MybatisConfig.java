package com.fansin.spring.cloud.bytetcc.provider.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-17 下午6:33
 */
@Configuration
@MapperScan(basePackages = "com.**.mybatis",sqlSessionFactoryRef = "sqlSessionFactory",sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisConfig {

    private String resourcePath = "classpath*:mybatis/**/*.xml";

    /**
     * Sql session factory sql session factory.
     *
     * @param dataSource the data source
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(resourcePath));
        return factoryBean.getObject();
    }

    /**
     * Sql session template sql session template.
     *
     * @param dataSource the data source
     * @return the sql session template
     * @throws Exception the exception
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory(dataSource));
        return template;
    }

}
