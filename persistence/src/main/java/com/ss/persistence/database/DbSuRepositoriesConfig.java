package com.ss.persistence.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableR2dbcRepositories(basePackages = { "com.ss.persistence.repository.db", "com.ss.persistence.repository.su" })
public class DbSuRepositoriesConfig {

    @Autowired
    private Environment env;

    @Bean(name = "DbSuManagerConnectionFactory")
    public ConnectionFactory dbSuManagerConnectionFactory() {
        return ConnectionFactoryBuilder.withUrl(env.getProperty("spring.datasource.db-su-manager.r2dbc-url"))
                .username(env.getProperty("spring.datasource.db-su-manager.username"))
                .password(env.getProperty("spring.datasource.db-su-manager.password")).build();
    }

    @Bean(name = "DbSuManagerClient")
    public DatabaseClient dbSuManagerClient(
            @Qualifier("DbSuManagerConnectionFactory") ConnectionFactory dbSuManagerConnectionFactory) {
        return DatabaseClient.create(dbSuManagerConnectionFactory);
    }
}
