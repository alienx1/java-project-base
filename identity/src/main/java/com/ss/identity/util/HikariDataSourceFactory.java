package com.ss.identity.util;
import org.keycloak.component.ComponentModel;

import com.ss.identity.storage.PgUserStorageProviderFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class HikariDataSourceFactory {

        private static HikariDataSource dataSource;

        private HikariDataSourceFactory() {
                log.debug(" HikariDataSourceFactory()");
        }

        public static HikariDataSource getDataSource(ComponentModel model) {
                log.debug(" getDataSource()");
                if (dataSource == null || dataSource.isClosed()) {
                        log.debug("-- new HikariDataSource()");
                        HikariConfig hConfig = getDataSourceConfig(model);
                        dataSource = new HikariDataSource(hConfig);
                }
                return dataSource;
        }

        public static HikariConfig getDataSourceConfig(ComponentModel model) {
// @formatter:off
                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setDataSourceClassName(model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_DATASOURCE_CLASS_NAME));
                hikariConfig.setUsername(model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_DB_USERNAME));
                hikariConfig.setPassword(model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_DB_PASSWORD));
                hikariConfig.setSchema(model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_DB_SCHEMA));
                hikariConfig.addDataSourceProperty("databaseName",model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_DB_NAME));
                hikariConfig.addDataSourceProperty("serverName",model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_SERVER_NAME));
                hikariConfig.addDataSourceProperty("portNumber",model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_SERVER_PORT));
                hikariConfig.addDataSourceProperty("ApplicationName", "kc-user-provider");
                hikariConfig.setPoolName("kc-user-provider");
                hikariConfig.setMaximumPoolSize(10);
// @formatter:on                
                return hikariConfig;
        }
}