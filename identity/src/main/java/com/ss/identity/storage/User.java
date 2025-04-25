package com.ss.identity.storage;

import java.util.List;
import java.util.Map;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.jbosslog.JBossLog;

/**
 * This will delegate every single UserModel method except get/set of username
 * to user federated storage.
 * Override the methods you need to override to delegate to your external
 * storage representations
 */
@JBossLog
public class User extends AbstractUserAdapterFederatedStorage {

    protected String keycloakId;
    @Getter
    @Setter
    private String password;
    private String username;

    // attributes
    // language
    // userType; employee, student ?
    // profileMenu
    // parameter?
    // emailtemplate

    private User(KeycloakSession session, RealmModel realm, ComponentModel componentModel, String userId,
            Builder builder) {
        super(session, realm, componentModel);

        this.keycloakId = StorageId.keycloakId(componentModel, userId);

        try {

            setUsername(builder.username);
            setPassword(builder.password);
            setEnabled(builder.isActive);
            setFirstName(builder.firstName);
            setLastName(builder.lastName);
            setCreatedTimestamp(builder.createdTimestamp);
            setSingleAttribute(EMAIL, builder.email);
            setSingleAttribute(LOCALE, builder.defaultLanguage);
            String userIDAttribute = "user_id";
            setSingleAttribute(userIDAttribute, userId);
            String userTypeAttribute = "user_type";
            setSingleAttribute(userTypeAttribute, builder.userType);

            ObjectMapper programMap = new ObjectMapper();
            String programListJson = programMap.writeValueAsString(builder.programList);
            String programListAttributes = "program_list";
            setSingleAttribute(programListAttributes, programListJson);

            // Loop thru attributeList then set single attribute like this
            // setSingleAttribute(key,value)
            if (builder.attributeList != null) {
                for (Map<String, String> attribute : builder.attributeList) {
                    for (Map.Entry<String, String> entry : attribute.entrySet()) {
                        setSingleAttribute(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        }

    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String s) {
        this.username = s;
    }

    @Override
    public String getId() {
        return keycloakId;
    }

    @JBossLog
    static class Builder {
        private final KeycloakSession session;
        private final RealmModel realm;
        private final ComponentModel componentModel;
        private final String id;
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private Boolean isActive;
        private String companyCode;
        private String employeeCode;
        private Long createdTimestamp;
        private String defaultLanguage;
        private String userType;
        private List<String> programList;
        private List<Map<String, String>> attributeList;
        Builder(KeycloakSession session, RealmModel realm, ComponentModel componentModel, String userId) {
            this.session = session;
            this.realm = realm;
            this.componentModel = componentModel;
            this.id = userId;
            log.debugv("Builder()");
        }

        User.Builder username(String username) {
            this.username = username;
            return this;
        }

        User.Builder password(String password) {
            this.password = password;
            return this;
        }

        User.Builder email(String email) {
            this.email = email;
            return this;
        }

        User.Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        User.Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        User.Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        User.Builder createdTimestamp(Long createdTimestamp) {
            this.createdTimestamp = createdTimestamp;
            return this;
        }

        User.Builder companyCode(String companyCode) {
            this.companyCode = companyCode;
            return this;
        }

        User.Builder employeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
            return this;
        }

        User.Builder userType(String userType) {
            this.userType = userType;
            return this;
        }

        User.Builder language(String defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
            return this;
        }

        User.Builder programList(List<String> list) {
            this.programList = list;
            return this;
        }


        User.Builder attributeList(List<Map<String, String>> attributeList) {
            this.attributeList = attributeList;
            return this;
        }

        User build() {
            log.debugv("build()");
            return new User(session, realm, componentModel, id, this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return (this.getId() == ((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}