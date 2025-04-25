package com.ss.identity.storage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ModelException;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.policy.PasswordPolicyManagerProvider;
import org.keycloak.policy.PolicyError;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.ImportedUserValidation;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import com.ss.identity.util.HikariDataSourceFactory;
import com.zaxxer.hikari.HikariConfig;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public abstract class PgUserStorageProvider implements UserLookupProvider, UserStorageProvider, UserQueryProvider,
        CredentialInputUpdater, CredentialInputValidator, ImportedUserValidation {
    protected HikariConfig hikariConfig;

    protected abstract List<String> getProgramList(Long userId, String systemCode);

    protected static final String SECRET_KEY_ALGO_HMAC_SHA1 = "PBKDF2WithHmacSHA1";
    protected static final String SECRET_KEY_ALGO_HMAC_SHA256 = "PBKDF2WithHmacSHA256";
    protected static final String SECRET_KEY_ALGO_HMAC_SHA512 = "PBKDF2WithHmacSHA512";
    public static final String PASSWORD_CACHE_KEY = User.class.getName() + ".password";

    // Key preference of dotnet specification V3
    private static final int ITERATION_COUNT = 100000;
    private static final int SALT_SIZE = 128 / 8;
    private static final int OUTPUT_SIZE = 256 / 8;

    protected final KeycloakSession session;
    protected final ComponentModel model;

    protected String remoteSsoUrl = "";
    protected boolean kcProviderFeatureRemoteAuthEnabled = false;

    protected PgUserStorageProvider(KeycloakSession session, ComponentModel model) {
        this.session = session;
        this.model = model;
        setup();
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        log.debug("getUsersCount()");
        Long result = 0L;
        try {
            QueryRunner queryRunner = new QueryRunner(getDataSource());
            Object[] count = queryRunner.query("SELECT COUNT(user_id) FROM su_user", resultSetHandler);
            result = (Long) count[0];
        } catch (SQLException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        }
        return (result).intValue();
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        log.debugv("supportsCredentialType() {0}", credentialType);
        return PasswordCredentialModel.TYPE.endsWith(credentialType);
    }

    @Override
    public void disableCredentialType(RealmModel realmModel, UserModel userModel,
            String credentialType) {
        log.debug("❌disableCredentialType()");
    }

    @Override
    public Stream<String> getDisableableCredentialTypesStream(RealmModel realmModel,
            UserModel userModel) {
        log.debug("❌getDisableableCredentialTypesStream()");
        return Stream.empty();
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel,
            String credentialType) {
        return supportsCredentialType(credentialType) && getPassword(userModel) != null;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String email) {
        log.warn("getUserByEmail() - Not implemented");
        return null;
    }

    @Override
    public void close() {
        // default implementation ignored
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group,
            Integer firstResult, Integer maxResults) {
        log.debug("getGroupMembersStream() not implemented");
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName,
            String attrValue) {
        log.debug("searchForUserByUserAttributeStream() not implemented");
        return Stream.empty();
    }

    @Override
    public UserModel validate(RealmModel realmModel, UserModel userModel) {
        log.debug("validate()");
        StorageId storageId = new StorageId(userModel.getId());
        String externalId = storageId.getExternalId();
        return getUserById(realmModel, externalId);
    }

    private String getPassword(UserModel userModel) {
        String password = null;
        if (userModel instanceof CachedUserModel) {
            password = (String) ((CachedUserModel) userModel).getCachedWith().get(PASSWORD_CACHE_KEY);
        } else if (userModel instanceof User) {
            password = ((User) userModel).getPassword();
        } else {
            log.warn("Cannot get password from userModel ");
        }
        return password;
    }

    protected UserModel createAdapter(RealmModel realm, Object[] obj) {
        log.debugv("createAdapter() {0}", Arrays.toString(obj));

        String userId = ((Long) obj[0]).toString();
        log.debugv("userId: {0}", userId);

        // @formatter:off
        List<String> programList = getProgramList((Long) obj[0] , "WT");
        if(obj[13]==null){
            log.warn("user created date was null; using currentTimeMillis instead!");
        }
        long createdDate = obj[13]==null?System.currentTimeMillis():((Timestamp)obj[13]).getTime();
        return new User.Builder(session, realm, model, userId)
                .username((String) obj[1]).password((String) obj[2])
                .isActive((Boolean) obj[3]).userType((String) obj[4])
                .firstName( obj[8]!=null?(String)obj[8]:"").lastName(obj[9]!=null?(String) obj[9]:"")
                .email((String) obj[5])
                .companyCode(obj[6]!=null?(String)obj[6]:"")
                .employeeCode(obj[7]!=null?(String)obj[7]:"")
                .createdTimestamp(createdDate)
                .language((String) obj[10])
                .programList(programList)
                .build();
        // @formatter:on
    }

    /*
     * ResultSetHandler [Reference
     * usage](https://commons.apache.org/proper/commons-dbutils/examples.html)
     *
     */
    ResultSetHandler<Object[]> resultSetHandler = new ResultSetHandler<>() {
        public Object[] handle(ResultSet rs) throws SQLException {
            if (!rs.next()) {
                return new Object[0];
            }
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            Object[] result = new Object[cols];

            for (int i = 0; i < cols; i++) {
                result[i] = rs.getObject(i + 1);
            }
            return result;
        }
    };

    /**
     * Creates a program list based on the given list of objects.
     *
     * @param obj the list of objects to create the program list from
     * @return the program list created
     */
    protected List<String> createProgramList(List<Object[]> obj) {
        log.debug("createProgramList()");
        List<String> programList = new ArrayList<>();
        for (Object[] value : obj) {
            log.debugv("prog {0}", value[0]);
            programList.add((String) value[0]);
        }
        return programList;
    }

    protected void setup() {
        /**
         * Respect specs from dotnet
         * HMACSHA1 0
         * The HMAC algorithm (RFC 2104) using the SHA-1 hash function (FIPS 180-4).
         * HMACSHA256 1
         * The HMAC algorithm (RFC 2104) using the SHA-256 hash function (FIPS 180-4).
         * HMACSHA512 2
         * The HMAC algorithm (RFC 2104) using the SHA-512 hash function (FIPS 180-4).
         * more:
         * `https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.cryptography.keyderivation.keyderivationprf?view=aspnetcore-3.1`
         **/
        // switch
        // (model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_KEY_VARIANT_SU_USER))
        // {
        // case PgUserStorageProviderFactory.CONFIG_VALUE_VARIANT_SU_USER_SMARTU:
        // setSecretKeyAlgo(SECRET_KEY_ALGO_HMAC_SHA256);
        // break;
        // case PgUserStorageProviderFactory.CONFIG_VALUE_VARIANT_SU_USER_SSCLEAN:
        // case PgUserStorageProviderFactory.CONFIG_VALUE_VARIANT_SU_USER_NPRU:
        // case PgUserStorageProviderFactory.CONFIG_VALUE_VARIANT_SU_USER_CREDITBANK:
        // default:
        // setSecretKeyAlgo(SECRET_KEY_ALGO_HMAC_SHA512);
        // }
        setSSOURL(model);
        setFeaturesToggle(model);
    }

    /**
     * Will update only if an user was external user; not in SSO
     */
    @Override
    public boolean updateCredential(RealmModel realmModel, UserModel userModel,
            CredentialInput credentialInput) {
        log.debug("updateCredential()");
        if (!(credentialInput instanceof UserCredentialModel)) {
            return false;
        }
        if (!credentialInput.getType().equals(PasswordCredentialModel.TYPE)) {
            return false;
        }

        try {
            String password = credentialInput.getChallengeResponse();
            /* validate aginst password policy */
            PolicyError error = session.getProvider(PasswordPolicyManagerProvider.class)
                    .validate(realmModel, userModel, password);
            if (error != null) {
                throw new ModelException(error.getMessage(), error.getParameters());
            }
            String hashedPassword = this.hashPassword(password);
            if ("".equalsIgnoreCase(hashedPassword)) {
                throw new IllegalStateException("Cannot hash the given password!");
            }
            StorageId storageId = new StorageId(userModel.getId());
            String userId = storageId.getExternalId();
            QueryRunner queryRunner = new QueryRunner(getDataSource());
            int result = queryRunner.update(
                    "update su_user set password_hash = ?, force_change_password = false where user_id = ?",
                    hashedPassword, Long.valueOf(userId));
            log.debugv("credential saved? {0} ", result);
            return (result > 0);
        } catch (SQLException e) {
            log.error(e, e.fillInStackTrace());
        }
        return false;
    }

    /**
     * Verify hashed password which produced from Dotnet Identity Core
     * <p>
     * // IdentityV3 includes configuration in the header, IdentityV2 does not. //
     * dotnet core 3.1
     * use key algo PBKDF2 with HMAC-SHA256 // dotnet 8 use PBKDF2 with HMAC-SHA512
     * </p>
     */
    protected boolean verifyPassword(String password, String hashedPassword) {
        log.infov("verifyPassword() ");

        try {
            // Decode the stored hashed password to bytes
            byte[] decodedHash = Base64.getDecoder().decode(hashedPassword);

            // Extract header information for re-create salt,hash
            int keyDerivationPref = readNetworkByteOrder(decodedHash, 1);
            int iterCount = readNetworkByteOrder(decodedHash, 5);
            int saltLength = readNetworkByteOrder(decodedHash, 9);
            log.debugv("keyDerivationPref: {0}", keyDerivationPref);
            // keyDerivationPref from ASP net core Enum
            // HMACSHA1 0
            // The HMAC algorithm (RFC 2104) using the SHA-1 hash function (FIPS 180-4).
            // HMACSHA256 1
            // The HMAC algorithm (RFC 2104) using the SHA-256 hash function (FIPS 180-4).
            // HMACSHA512 2
            // The HMAC algorithm (RFC 2104) using the SHA-512 hash function (FIPS 180-4).
            // more:
            // `https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.cryptography.keyderivation.keyderivationprf?view=aspnetcore-3.1`

            // Extract the salt and hash from the decoded bytes
            byte[] salt = new byte[saltLength];
            System.arraycopy(decodedHash, 13, salt, 0, salt.length);
            // subKey
            int subKeyLength = decodedHash.length - 13 - salt.length;
            byte[] expectedSubkey = new byte[subKeyLength];
            System.arraycopy(decodedHash, 13 + salt.length, expectedSubkey, 0,
                    expectedSubkey.length);
            // Create a PBEKeySpec with the provided password and salt
            // Using same key length but in bit not bytes; 8bit = 1 byte
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterCount, (subKeyLength * 8));
            // Generate the hash using the same key derivation preferences
            String secretKeyAlgo;
            switch (keyDerivationPref) {
                case 0:
                    secretKeyAlgo = SECRET_KEY_ALGO_HMAC_SHA1;
                    break;
                case 1:
                    secretKeyAlgo = SECRET_KEY_ALGO_HMAC_SHA256;
                    break;
                case 2:
                default:
                    secretKeyAlgo = SECRET_KEY_ALGO_HMAC_SHA512;
            }
            log.debugv("SecretKey algo: {0}", secretKeyAlgo);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyAlgo);
            byte[] computedHash = factory.generateSecret(spec).getEncoded();
            // Compare the computed hash with the stored hash
            return MessageDigest.isEqual(expectedSubkey, computedHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return false;
        }
    }

    /**
     * Validates the given credential for the specified user.
     *
     * @param realmModel      the realm model
     * @param userModel       the user model
     * @param credentialInput the credential input
     * @return true if the credential is valid, false otherwise
     */
    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel,
            CredentialInput credentialInput) {
        log.debugv("isValid() - Validating credential for user: {00}", userModel.getId());

        if (!this.supportsCredentialType(credentialInput.getType())
                || !(credentialInput instanceof UserCredentialModel)) {
            log.warn(" Credential type not supported 🚨");
            return false;
        }

        final String getPasswordSQL = "SELECT coalesce(su.password_hash,'') FROM su_user su WHERE su.user_id = ?";

        try {
            StorageId storageId = new StorageId(userModel.getId());
            String userId = storageId.getExternalId();

            QueryRunner queryRunner = new QueryRunner(getDataSource());
            Object[] result = queryRunner.query(getPasswordSQL, resultSetHandler, Long.valueOf(userId));

            if (result.length > 0) {
                String hashedPassword = (String) result[0];
                log.debugv("password hashed: {0}", hashedPassword);

                UserCredentialModel cred = (UserCredentialModel) credentialInput;
                log.debugv("Verify against DB");
                return !"".equalsIgnoreCase(hashedPassword)
                        && verifyPassword(cred.getValue(), hashedPassword);
            } else {
                log.warn("user not found 🚨");
                return false;
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        }
        return false;
    }

    protected String hashPassword(String password) {
        SecureRandom rng = new SecureRandom();
        byte[] result = hashPasswordV3(password, rng, ITERATION_COUNT, SALT_SIZE, OUTPUT_SIZE);
        return result.length > 0 ? Base64.getEncoder().encodeToString(result) : "";
    }

    /*
     * ======================= HASHED PASSWORD FORMATS =======================
     *
     * Version 2: PBKDF2 with HMAC-SHA1, 128-bit salt, 256-bit subkey, 1000
     * iterations. (See also:
     * SDL crypto guidelines v5.1, Part III) Format: { 0x00, salt, subkey }
     *
     * Version 3: PBKDF2 with HMAC-SHA256, 128-bit salt, 256-bit subkey, 10000
     * iterations. Since
     * dotnet 7.0+: PBKDF2 with HMAC-SHA512, 128-bit salt, 256-bit subkey, 100000
     * iterations.
     * Format: { 0x01, prf (UInt32), iter count (UInt32), salt length (UInt32),
     * salt, subkey } (All
     * UInt32s are stored big-endian.)
     *
     * https://github.com/dotnet/aspnetcore/blob/v3.1.32/src/Identity/Extensions.
     * Core/src/PasswordHasher.cs#L132
     */
    private byte[] hashPasswordV3(String password, SecureRandom rng, int iterCount, int saltSize,
            int outputSize) {
        log.debugv("hashPasswordV3() ");
        // Produce a version 3 (see comment above) text hash.
        byte[] salt = new byte[saltSize];
        rng.nextBytes(salt);
        // Create a PBEKeySpec with the provided password and salt
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterCount, outputSize);
        try {
            // key algorithm; using Version 3 same as Dotnet 8 LTS
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGO_HMAC_SHA512);
            byte[] subKey = factory.generateSecret(keySpec).getEncoded();

            byte[] outputBytes = new byte[13 + salt.length + subKey.length];
            outputBytes[0] = 0x01; // format marker

            // dotnet Microsoft.AspNetCore.Cryptography.KeyDerivation.KeyDerivationPrf
            // enum: HMACSHA1 0, HMACSHA256 1; HMACSHA512 2;
            int prf = 2;
            log.debugv("KeyDerivationPrf: {0} ", prf);
            log.debugv("iteration: {0} ", iterCount);
            log.debugv("salt size: {0} ", saltSize);

            writeNetworkByteOrder(outputBytes, 1, prf);
            writeNetworkByteOrder(outputBytes, 5, iterCount);
            writeNetworkByteOrder(outputBytes, 9, saltSize);
            System.arraycopy(salt, 0, outputBytes, 13, salt.length);
            System.arraycopy(subKey, 0, outputBytes, 13 + saltSize, subKey.length);
            return outputBytes;
        } catch (Exception e) {
            log.error(e);
            return new byte[0];
        }

    }

    private int readNetworkByteOrder(byte[] buffer, int offset) {
        return ((buffer[offset + 0] & 0xFF) << 24) | ((buffer[offset + 1] & 0xFF) << 16)
                | ((buffer[offset + 2] & 0xFF) << 8) | (buffer[offset + 3] & 0xFF);
    }

    private static void writeNetworkByteOrder(byte[] buffer, int offset, int value) {
        buffer[offset] = (byte) ((value >> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }

    protected DataSource getDataSource() {
        return HikariDataSourceFactory.getDataSource(this.model);
    }

    private void setSSOURL(ComponentModel model) {
        this.remoteSsoUrl = model.getConfig().getFirst(PgUserStorageProviderFactory.CONFIG_NPRU_SSO_NAME);
    }

    private void setFeaturesToggle(ComponentModel model) {
        kcProviderFeatureRemoteAuthEnabled = "true".equalsIgnoreCase(model.getConfig()
                .getFirst(PgUserStorageProviderFactory.CONFIG_NPRU_SSO_FEATURE_ENABLED));
        // read environment variable
        if (null != System.getenv("KC_PROVIDER_FEATURE_REMOTE_AUTH_ENABLED")) {
            kcProviderFeatureRemoteAuthEnabled = System
                    .getenv("KC_PROVIDER_FEATURE_REMOTE_AUTH_ENABLED").equalsIgnoreCase("true");
            log.debugv("featuresToggle(): Override setting with ENV");
        }
        log.debugv("featuresToggle() - KC_PROVIDER_FEATURE_REMOTE_AUTH_ENABLED? {0}",
                kcProviderFeatureRemoteAuthEnabled);
    }

}
