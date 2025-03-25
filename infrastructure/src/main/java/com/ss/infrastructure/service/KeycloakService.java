package com.ss.infrastructure.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KeycloakService {

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private Environment env;

    public UserRepresentation getUserInfo(String userId) {
        return keycloak.realm(env.getProperty("spring.security.oauth2.provider.realm"))
                .users().get(userId).toRepresentation();
    }

    public void forcePasswordUpdate(String userId) {
        UserResource userResource = keycloak.realm(env.getProperty("spring.security.oauth2.provider.realm"))
                .users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        user.setEnabled(true);
        user.setEmailVerified(false);
        userResource.update(user);
    }

    public void updateUser(String userId, String newEmail) {
        UserResource userResource = keycloak.realm(env.getProperty("spring.security.oauth2.provider.realm"))
                .users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        user.setEmail(newEmail);
        userResource.update(user);
    }

    public void updatePasswordPolicy(String policy) {
        RealmResource realmResource = keycloak.realm(env.getProperty("spring.security.oauth2.provider.realm"));
        RealmRepresentation realmRepresentation = realmResource.toRepresentation();

        realmRepresentation.setPasswordPolicy(policy);

        realmResource.update(realmRepresentation);

        log.info("Password policy updated for realm {}", env.getProperty("spring.security.oauth2.provider.realm"));
    }
}
