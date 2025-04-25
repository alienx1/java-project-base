package com.ss.identity.storage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class QueryUserStorageProvider extends PgUserStorageProvider {
    public QueryUserStorageProvider(KeycloakSession session, ComponentModel model) {
        super(session, model);
        log.debug("QueryUserStorageProvider()");
    }

    private static final String SQL_SELECT_SUUSER_COLUMNS = "SELECT \n" + //
            "    u.user_id,\n" + //
            "    u.username,\n" + //
            "    u.password,\n" + //
            "    u.active,\n as \"enabled\",\n" + //
            "   'u' AS user_type,\n" + //
            "    e.email,\n" + //
            "    e.company_code,\n" + //
            "    e.employee_code,\n" + //
            "    el.employee_first_name AS \"firstName\",\n" + //
            "    el.employee_last_name AS \"lastName\",\n" + //
            "    u.default_language_code\n" + //
            "FROM su_user u\n" + //
            "INNER JOIN db_employee e \n" + //
            "    ON u.employee_id = e.employee_id\n" + //
            "INNER JOIN db_employee_lang el \n" + //
            "    ON e.employee_id = el.employee_id \n" + //
            "    AND u.default_language_code = el.language_code\n";
    private static final String SQL_ORDER_SUUSER_USERNAME = "ORDER BY su.user_name ";

    @Override
    public UserModel getUserById(RealmModel realmModel, String userId) {

        // @formatter:off
        StringBuilder sql = new StringBuilder();
            sql.append(SQL_SELECT_SUUSER_COLUMNS)
               .append(" WHERE su.user_id = ?");
        // @formatter:on
        String userByIDSQL = sql.toString();

        log.debugv("getUserById() {0} (storageId)", userId);
        UserModel result = null;
        try {
            StorageId storageId = new StorageId(userId);
            String externalId = storageId.getExternalId();
            QueryRunner queryRunner = new QueryRunner(getDataSource());
            log.debugv("external Id: ", externalId);
            Object[] object = queryRunner.query(userByIDSQL, resultSetHandler, Long.valueOf(externalId));
            result = createAdapter(realmModel, object);
        } catch (SQLException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        }
        return result;
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String username) {
        log.debugv("getUserByUsername() {0}", username);

// @formatter:off
        StringBuilder sql = new StringBuilder();
            sql.append(SQL_SELECT_SUUSER_COLUMNS)
            .append(" WHERE LOWER(su.user_name) LIKE LOWER(?) ")
            .append(SQL_ORDER_SUUSER_USERNAME);
// @formatter:on
        String searchForUserSQL = sql.toString();

        UserModel result = null;
        try {

            QueryRunner queryRunner = new QueryRunner(getDataSource());
            List<Object[]> resultList = queryRunner.query(searchForUserSQL, new ArrayListHandler(), username);
            if (!resultList.isEmpty()) {
                result = createAdapter(realmModel, resultList.get(0));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        }
        return result;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String email) {
        log.debugv("getUserByEmail() {0}", email);

// @formatter:off
        StringBuilder sql = new StringBuilder();
            sql.append(SQL_SELECT_SUUSER_COLUMNS)
            .append(" WHERE LOWER(su.email) LIKE LOWER(?) ")
            .append(SQL_ORDER_SUUSER_USERNAME);
// @formatter:on

        String searchForUserSQL = sql.toString();
        UserModel result = null;
        try {

            QueryRunner queryRunner = new QueryRunner(getDataSource());
            List<Object[]> resultList = queryRunner.query(searchForUserSQL, new ArrayListHandler(), email);
            if (!resultList.isEmpty()) {
                result = createAdapter(realmModel, resultList.get(0));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        }
        return result;
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, Map<String, String> params,
            Integer firstResult, Integer maxResults) {
// @formatter:off
        StringBuilder sql = new StringBuilder();
        sql.append(SQL_SELECT_SUUSER_COLUMNS)
           .append("WHERE LOWER(su.user_name) LIKE ? ")
           .append(SQL_ORDER_SUUSER_USERNAME)
           .append("LIMIT ? ")
           .append("OFFSET ?");
// @formatter:on

        String searchForUserSQL = sql.toString();
        String search = "%";
        String usernameSearch = params.containsKey(UserModel.USERNAME) ? params.get(UserModel.USERNAME)
                : params.get(UserModel.SEARCH);

        if (null != usernameSearch) {
            search = search.concat(usernameSearch).concat("%").toLowerCase();
        }

        log.debugv("searchForUserStream() {0}", search);
        log.debugv("maxResults {0}", maxResults);
        log.debugv("firstResult {0}", firstResult);

        ArrayList<UserModel> results = new ArrayList<>();
        try {
            QueryRunner queryRunner = new QueryRunner(getDataSource());
            List<Object[]> resultList = queryRunner.query(searchForUserSQL, new ArrayListHandler(),
                    search, maxResults, firstResult);
            for (Object[] objects : resultList) {
                results.add(createAdapter(realmModel, objects));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        }
        return results.stream();
    }

    @Override
    protected List<String> getProgramList(Long userId, String systemCode) {
        log.debug("getProgramList()");

// @formatter:off
        final String PROGRAM_LIST_SQL = "SELECT\n" + //
                        "    m.program_code\n" + //
                        "FROM\n" + //
                        "    db_menu m\n" + //
                        "WHERE\n" + //
                        "    exists (\n" + //
                        "        SELECT\n" + //
                        "            1\n" + //
                        "        FROM\n" + //
                        "            su_user_profile up\n" + //
                        "        JOIN su_profile p ON up.profile_id = p.profile_id\n" + //
                        "        JOIN su_profile_authority pa ON up.profile_id = pa.profile_id\n" + //
                        "        JOIN db_program pg ON m.program_code = pg.program_code \n" + //
                        "        JOIN db_module md ON pg.module_code = md.module_code AND md.system_code = ?\n" + //
                        "        where m.menu_code = pa.menu_code AND up.user_id = ?\n" + //
                        "    )\n" + //
                        "    AND m.program_code is not null";
// @formatter:on
        try {
            QueryRunner queryRunner = new QueryRunner(getDataSource());
            List<Object[]> resultSet = queryRunner.query(PROGRAM_LIST_SQL, new ArrayListHandler(), userId, systemCode);
            // log.debugv("result size:{0}", resultSet.size());
            return createProgramList(resultSet);
        } catch (SQLException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return Collections.emptyList();
        }
    }

}
