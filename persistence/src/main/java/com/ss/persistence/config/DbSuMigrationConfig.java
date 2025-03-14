package com.ss.persistence.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DbSuMigrationConfig {
    @Autowired
    private Environment env;

    @Bean
    public Flyway dbSuManagerFlyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(env.getProperty("spring.datasource.db-su-manager.jdbc-url"),
                        env.getProperty("spring.datasource.db-su-manager.username"),
                        env.getProperty("spring.datasource.db-su-manager.password"))
                .locations(env.getProperty("spring.datasource.db-su-manager.locations"))
                .driver(env.getProperty("spring.datasource.db-su-manager.driver-class-name"))
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }

}
