package com.yuman.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("spring.datasource.url")
    private String url;

    @Value("spring.datasource.username")
    private String username;

    @Value("spring.datasource.password")
    private String password;

    @Value("spring.datasource.driver-class-name")
    private String driverClassName;


    //配置DataSource数据源
    @Bean(initMethod = "init",destroyMethod = "close")
    public DruidDataSource defaultDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setValidationQuery("select 1");
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }


    @Autowired
    @Bean
    public JdbcTemplate jdbcTemplate(DynamicDataSource dynamicDataSource){
        return new JdbcTemplate(dynamicDataSource);
    }


    @Bean
    @Autowired
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        return sessionFactory.getObject();
    }



}
