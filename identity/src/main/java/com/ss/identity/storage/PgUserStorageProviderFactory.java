package com.ss.identity.storage;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import com.ss.identity.exception.UserException;
import com.ss.identity.util.HikariDataSourceFactory;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class PgUserStorageProviderFactory implements UserStorageProviderFactory<PgUserStorageProvider> {
    private static final String PROVIDER_NAME = "pg-user-storage-provider";
    protected final List<ProviderConfigProperty> configMetadata;
    public static final String CONFIG_KEY_DATASOURCE_CLASS_NAME = "dataSourceClassName";
    public static final String CONFIG_KEY_SERVER_NAME = "dataSource.serverName";
    public static final String CONFIG_KEY_SERVER_PORT = "dataSource.portNumber";
    public static final String CONFIG_KEY_DB_USERNAME = "dataSource.user";
    public static final String CONFIG_KEY_DB_PASSWORD = "dataSource.password";
    public static final String CONFIG_KEY_DB_SCHEMA = "dataSource.schema";
    public static final String CONFIG_KEY_DB_NAME = "dataSource.databaseName";

    public static final String CONFIG_KEY_VALIDATION_QUERY = "connectionTestQuery";

    public static final String CONFIG_VALUE_VARIANT_SU_USER_QUERY = "QUERY";
    public static final String CONFIG_KEY_VARIANT_SU_USER = "VARIANT_SU_USER";
    public static final String CONFIG_NPRU_SSO_FEATURE_ENABLED = "feature.sso.enabled";
    public static final String CONFIG_NPRU_SSO_NAME = "feature.sso.url";
    public static final String CONFIG_NPRU_SSO_URL = "https://example.com";
    public static final String CONFIG_NPRU_LDAP_SEARCH_URL = "https://example.com/ldapsso/search";
    public static final String CONFIG_NPRU_LDAP_SEARCH_URL_NAME = "LDAP_SEARCH_URL";
    public static final String CONFIG_NPRU_LDAP_SEARCH_APIKEY = "THIS-IS-SECRET";
    public static final String CONFIG_NPRU_LDAP_SEARCH_APIKEY_NAME = "LDAP_SEARCH_APIKEY";

    public PgUserStorageProviderFactory() {
        log.debug(" PgUserStorageProviderFactory created");

        configMetadata = ProviderConfigurationBuilder.create().property()
                .name(CONFIG_KEY_DATASOURCE_CLASS_NAME).type(ProviderConfigProperty.STRING_TYPE)
                .label("Datasource class name (Hikari)")
                .defaultValue("org.postgresql.ds.PGSimpleDataSource")
                .helpText(
                        "see list from https://github.com/brettwooldridge/HikariCP#popular-datasource-class-names")
                .add()

                .property().name(CONFIG_KEY_SERVER_NAME).type(ProviderConfigProperty.STRING_TYPE)
                .label("Server name").defaultValue("localhost").add()

                .property().name(CONFIG_KEY_SERVER_PORT).type(ProviderConfigProperty.STRING_TYPE)
                .label("Server port").defaultValue("5432").add()

                .property().name(CONFIG_KEY_DB_NAME).type(ProviderConfigProperty.STRING_TYPE)
                .label("Database name").defaultValue("").add()

                .property().name(CONFIG_KEY_DB_SCHEMA).type(ProviderConfigProperty.STRING_TYPE)
                .label("Database Schema").defaultValue("").add()

                .property().name(CONFIG_KEY_DB_USERNAME).type(ProviderConfigProperty.STRING_TYPE)
                .label("user").defaultValue("").add()

                .property().name(CONFIG_KEY_DB_PASSWORD).type(ProviderConfigProperty.PASSWORD)
                .label("password").defaultValue("").add()

                .property().name(CONFIG_KEY_VALIDATION_QUERY)
                .type(ProviderConfigProperty.STRING_TYPE).label("Validation Query")
                .defaultValue("SELECT 1").helpText("SQL to test connection").add()

                .property().name(CONFIG_KEY_VARIANT_SU_USER)
                .type(ProviderConfigProperty.STRING_TYPE).label("Variant of SU_USER")
                .defaultValue(CONFIG_VALUE_VARIANT_SU_USER_QUERY)
                .helpText("valid value are NPRU, QUERY, SMARTU, CRBANK").add()

                .property().name(CONFIG_NPRU_SSO_FEATURE_ENABLED)
                .type(ProviderConfigProperty.BOOLEAN_TYPE).label("Remote SSO Feature enabled")
                .defaultValue("false").helpText("Is remote SSO service enabled?").add()

                .property().name(CONFIG_NPRU_SSO_NAME).type(ProviderConfigProperty.STRING_TYPE)
                .label("Remote SSO URL").defaultValue(CONFIG_NPRU_SSO_URL)
                .helpText("URL of remote SSO service").add()

                .property().name(CONFIG_NPRU_LDAP_SEARCH_URL_NAME)
                .type(ProviderConfigProperty.STRING_TYPE).label("LDAP Search URL")
                .defaultValue(CONFIG_NPRU_LDAP_SEARCH_URL)
                .helpText(
                        "https://$KC_HOSTNAME_URL/realms/<my-realm-id>/ldap/search OR https://ldap.example.com/search")
                .add()

                .property().name(CONFIG_NPRU_LDAP_SEARCH_APIKEY_NAME)
                .type(ProviderConfigProperty.STRING_TYPE).label("LDAP Search API Key")
                .defaultValue(CONFIG_NPRU_LDAP_SEARCH_APIKEY)
                .helpText("an API Key or request will rejected").add().build();
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm,
            ComponentModel componentModel) throws ComponentValidationException {
        log.debug("validateConfiguration() ");
        if ("".equalsIgnoreCase(componentModel.getConfig().getFirst(CONFIG_KEY_VARIANT_SU_USER))) {
            throw new ComponentValidationException("SU_USER Variant can not blank");
        }
        if ("".equalsIgnoreCase(componentModel.getConfig().getFirst(CONFIG_KEY_SERVER_NAME))) {
            throw new ComponentValidationException("DataSource connection can not blank");
        }
        try (HikariDataSource ds = HikariDataSourceFactory.getDataSource(componentModel)) {
            QueryRunner run = new QueryRunner();
            int result = run.execute(ds.getConnection(),
                    componentModel.getConfig().getFirst(CONFIG_KEY_VALIDATION_QUERY));
            log.debugv("result; -1 is OK! {0}", result);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new UserException("Unable to validate the connection", e);
        }
    }

    @Override
    public PgUserStorageProvider create(KeycloakSession keycloakSession,
            ComponentModel componentModel) {
        log.debugv("create() {0}", PROVIDER_NAME);
        PgUserStorageProvider provider;
       
        String providerVariant = componentModel.getConfig().getFirst(CONFIG_KEY_VARIANT_SU_USER);
        // Provider selection
        log.debugv("VARIANT {0}", providerVariant);
        if("".equalsIgnoreCase(providerVariant)){
            throw new ComponentValidationException("SU_USER Variant can not blank");
        }
        
        provider = new QueryUserStorageProvider(keycloakSession, componentModel);
  

        log.debugv("provider: {0}",
                componentModel.getConfig().getFirst(CONFIG_KEY_VARIANT_SU_USER));
        return provider;
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }

    @Override
    public String getHelpText() {
        return "SU_USER User Storage Provider";
    }

}