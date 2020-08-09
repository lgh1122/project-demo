package com.laiyuezs.storage.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 */
//@Configuration
public class DataSourceConfig {



    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSourceBuilder.create().build();
        return firstDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
//                .driverClassName(properties.determineDriverClassName())
//                .url(properties.determineUrl())
//                .username(properties.determineUsername())
//                .password(properties.determinePassword())
                .build();
    }

//
//    @Bean
//    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
//        return new DataSourceProxy(dataSource);
//    }
//    @Bean
//    @Primary
//    public DruidDataSource druidDataSource(){
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(dataSourceProperties.getUrl());
//        druidDataSource.setUsername(dataSourceProperties.getUsername());
//        druidDataSource.setPassword(dataSourceProperties.getPassword());
//        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
//        druidDataSource.setInitialSize(0);
//        druidDataSource.setMaxActive(180);
//        druidDataSource.setMaxWait(60000);
//        druidDataSource.setMinIdle(0);
//        druidDataSource.setValidationQuery("Select 1 from DUAL");
//        druidDataSource.setTestOnBorrow(false);
//        druidDataSource.setTestOnReturn(false);
//        druidDataSource.setTestWhileIdle(true);
//        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
//        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
//        druidDataSource.setRemoveAbandoned(true);
//        druidDataSource.setRemoveAbandonedTimeout(1800);
//        druidDataSource.setLogAbandoned(true);
//        return druidDataSource;
//    }
}
