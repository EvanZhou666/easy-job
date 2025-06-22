//package com.qianlima.easyjob.config;
//
//import org.springframework.context.annotation.Configuration;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//
// 不需要时候可以注释掉
//@Configuration
//public class MyDataSourceConfig {
//
//    @Autowired
//    private DataSourceProperties dataSourceProperties;

//    @Primary
//    @Bean
//    public DataSource dataSource() {
//        HikariConfig hikariConfig = new HikariConfig();
//
//        // 基本连接配置
//        hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
//        hikariConfig.setJdbcUrl(dataSourceProperties.getUrl());
//        hikariConfig.setUsername(dataSourceProperties.getUsername());
//        hikariConfig.setPassword(dataSourceProperties.getPassword());
//
//        // 连接池配置
//        hikariConfig.setMinimumIdle(1);
//        hikariConfig.setMaximumPoolSize(5);
//        hikariConfig.setConnectionTimeout(20000);
//        hikariConfig.setValidationTimeout(1000);
//        hikariConfig.setIdleTimeout(300000);
//        hikariConfig.setAutoCommit(true);
//        hikariConfig.setPoolName("HikariPool-Primary");
//
//        // 连接测试配置
//        hikariConfig.setConnectionTestQuery("SELECT 1");
//        hikariConfig.setInitializationFailTimeout(2000);
//
//        return new HikariDataSource(hikariConfig);
//    }
//}
