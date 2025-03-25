package com.ss.persistence.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JobDatasourceConfig {
    @Autowired
    private Environment env;

    @Bean(name = "jobsDataSource")
    @Primary
    public DataSource jobsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.db-su-manager.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.db-su-manager.jdbc-url"));
        dataSource.setUsername(env.getProperty("spring.datasource.db-su-manager.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.db-su-manager.password"));
        return dataSource;
    }

    @Bean(name = "dataSource")
    public DataSource defaultDataSource(@Autowired @Qualifier("jobsDataSource") DataSource jobsDataSource) {
        return jobsDataSource;
    }
}
