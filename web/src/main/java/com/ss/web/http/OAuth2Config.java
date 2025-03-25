package com.ss.web.http;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class OAuth2Config {

    @Autowired
    private Environment env;

    @Bean
    public JwtDecoder jwtDecoder() throws RuntimeException {
        String issuerUri = env.getProperty("spring.security.oauth2.provider.issuer-uri");
        String expectedAudience = env.getProperty("spring.security.oauth2.provider.audience");

        String jwkSetUri = env.getProperty("spring.security.oauth2.provider.jwk-set-uri");

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        return jwt -> {
            Jwt decodedJwt = jwtDecoder.decode(jwt);

            validateJwtClaims(decodedJwt, issuerUri, expectedAudience);

            return decodedJwt;
        };
    }

    private void validateJwtClaims(Jwt decodedJwt, String expectedIssuer, String expectedAudience) {
        // Validate the issuer claim
        String issuer = decodedJwt.getIssuer().toString();
        if (issuer == null || !issuer.equals(expectedIssuer)) {
            throw new IllegalArgumentException("Invalid issuer: " + issuer);
        }

        // Validate the audience claim
        List<String> audience = decodedJwt.getAudience();
        if (audience == null || !audience.contains(expectedAudience)) {
            throw new IllegalArgumentException("Invalid audience: " + audience);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/public/**") // Disable CSRF protection for public (Test) endpoints
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/public/**").permitAll() // Allow public access to "/public/**"
                        .anyRequest().authenticated() // Require authentication for all other routes
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())) // Configure JWT-based OAuth2 authentication
                );

        return http.build();
    }

}
