package com.ss.web.http;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class KeycloakConfig {

    @Autowired
    private Environment env;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(env.getProperty("spring.security.oauth2.provider.auth-uri"))
                .realm(env.getProperty("spring.security.oauth2.provider.admin-realm"))
                .clientId(env.getProperty("spring.security.oauth2.provider.admin-client-id"))
                .clientSecret(env.getProperty("spring.security.oauth2.provider.admin-client-secret"))
                .grantType("client_credentials")
                .build();
    }
}
