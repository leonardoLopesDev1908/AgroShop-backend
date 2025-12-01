package com.dailycodework.agroshop.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfiguration {
    
    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.driver-class-name}")
    String driver;

    @Bean
    @Profile("dev")
    public DataSource hikariDataSourceDev(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setPoolName("agroshop-db-pool");
        config.setMaxLifetime(600000);
        config.setConnectionTimeout(100000);
        config.setConnectionTestQuery("select 1");

        return new HikariDataSource(config);
    }

    @Bean
    @Profile("tests")
    public DataSource hikariDataSourceTests(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setPoolName("agroshop-db-pool");
        config.setMaxLifetime(600000);
        config.setConnectionTimeout(100000);
        config.setConnectionTestQuery("select 1");

        return new HikariDataSource(config);
    }

}
