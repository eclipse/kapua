/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.apache.shiro.codec.Base64;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialAttributes;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.exception.DuplicatedPasswordCredentialException;
import org.eclipse.kapua.service.authentication.exception.PasswordLengthException;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * {@link CredentialService} implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class CredentialServiceImpl extends AbstractKapuaConfigurableService implements CredentialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private static final String PASSWORD_MIN_LENGTH = "password.minLength";

    /**
     * The minimum password length specified for the whole system. If not defined, assume 12; if defined and less than 12, assume 12.
     */
    private final int systemMinimumPasswordLength;

    private static final int SYSTEM_MAXIMUM_PASSWORD_LENGTH = 255;

    public CredentialServiceImpl() {
        super(CredentialService.class.getName(), AuthenticationDomains.CREDENTIAL_DOMAIN, AuthenticationEntityManagerFactory.getInstance());
        int minPasswordLengthConfigValue;
        try {
            minPasswordLengthConfigValue = KapuaAuthenticationSetting.getInstance().getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_PASSWORD_MINLENGTH);
        } catch (NoSuchElementException ex) {
            LOGGER.warn("Minimum password length not set, 12 characters minimum will be enforced");
            minPasswordLengthConfigValue = 12;
        }
        if (minPasswordLengthConfigValue < 12) {
            LOGGER.warn("Minimum password length too short, 12 characters minimum will be enforced");
            minPasswordLengthConfigValue = 12;
        }
        systemMinimumPasswordLength = minPasswordLengthConfigValue;
    }

    @Override
    public Credential create(CredentialCreator credentialCreator)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(credentialCreator, "credentialCreator");
        ArgumentValidator.notNull(credentialCreator.getScopeId(), "credentialCreator.scopeId");
        ArgumentValidator.notNull(credentialCreator.getUserId(), "credentialCreator.userId");
        ArgumentValidator.notNull(credentialCreator.getCredentialType(), "credentialCreator.credentialType");
        ArgumentValidator.notNull(credentialCreator.getCredentialStatus(), "credentialCreator.credentialStatus");
        if (credentialCreator.getCredentialType() != CredentialType.API_KEY) {
            ArgumentValidator.notEmptyOrNull(credentialCreator.getCredentialPlainKey(), "credentialCreator.credentialKey");
        }

        if (credentialCreator.getCredentialType() == CredentialType.PASSWORD) {
            //
            // Check if a PASSWORD credential already exists for the user
            CredentialListResult existingCredentials = findByUserId(credentialCreator.getScopeId(), credentialCreator.getUserId());
            for (Credential credential : existingCredentials.getItems()) {
                if (credential.getCredentialType().equals(CredentialType.PASSWORD)) {
                    throw new DuplicatedPasswordCredentialException();
                }
            }

            // Validate Password length
            int minPasswordLength = getMinimumPasswordLength(credentialCreator.getScopeId());
            if (credentialCreator.getCredentialPlainKey().length() < minPasswordLength ||
                    credentialCreator.getCredentialPlainKey().length() > SYSTEM_MAXIMUM_PASSWORD_LENGTH) {
                throw new PasswordLengthException(minPasswordLength, SYSTEM_MAXIMUM_PASSWORD_LENGTH);
            }

            //
            // Validate Password regex
            ArgumentValidator.match(credentialCreator.getCredentialPlainKey(), CommonsValidationRegex.PASSWORD_REGEXP, "credentialCreator.credentialKey");
        }

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, credentialCreator.getScopeId()));

        //
        // Do create
        Credential credential;
        EntityManager em = AuthenticationEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            //
            // Do pre persist magic on key values
            String fullKey = null;
            switch (credentialCreator.getCredentialType()) {
                case API_KEY: // Generate new api key
                    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

                    KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
                    int preLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);
                    int keyLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_KEY_LENGTH);

                    byte[] bPre = new byte[preLength];
                    random.nextBytes(bPre);
                    String pre = Base64.encodeToString(bPre).substring(0, preLength);

                    byte[] bKey = new byte[keyLength];
                    random.nextBytes(bKey);
                    String key = Base64.encodeToString(bKey);

                    fullKey = pre + key;

                    credentialCreator = new CredentialCreatorImpl(credentialCreator.getScopeId(),
                            credentialCreator.getUserId(),
                            credentialCreator.getCredentialType(),
                            fullKey,
                            credentialCreator.getCredentialStatus(),
                            credentialCreator.getExpirationDate());

                    break;
                case PASSWORD:
                default:
                    // Don't do nothing special
                    break;

            }

            credential = CredentialDAO.create(em, credentialCreator);
            credential = CredentialDAO.find(em, credential.getScopeId(), credential.getId());

            em.commit();

            //
            // Do post persist magic on key values
            switch (credentialCreator.getCredentialType()) {
                case API_KEY:
                    credential.setCredentialKey(fullKey);
                    break;
                case PASSWORD:
                default:
                    credential.setCredentialKey(fullKey);
            }
        } catch (Exception pe) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(pe);
        } finally {
            em.close();
        }

        return credential;
    }

    @Override
    public Credential update(Credential credential)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(credential, "credential");
        ArgumentValidator.notNull(credential.getId(), "credential.id");
        ArgumentValidator.notNull(credential.getScopeId(), "credential.scopeId");
        ArgumentValidator.notNull(credential.getUserId(), "credential.userId");
        ArgumentValidator.notNull(credential.getCredentialType(), "credential.credentialType");
        ArgumentValidator.notEmptyOrNull(credential.getCredentialKey(), "credential.credentialKey");

        // FIXME These check are not correct, since they're applied to an
        //  already encrypted password. Checks are moved temporary to
        //  GwtCredentialServiceImpl#update
//        if (CredentialType.PASSWORD == credential.getCredentialType()) {
//            //
//            // Validate Password length
//            int minPasswordLength = getMinimumPasswordLength(credential.getScopeId());
//            if (credential.getCredentialKey().length() < minPasswordLength ||
//                    credential.getCredentialKey().length() > SYSTEM_MAXIMUM_PASSWORD_LENGTH) {
//                throw new PasswordLengthException(minPasswordLength, SYSTEM_MAXIMUM_PASSWORD_LENGTH);
//            }
//
//            //
//            // Validate Password regex
//            ArgumentValidator.match(credential.getCredentialKey(), CommonsValidationRegex.PASSWORD_REGEXP, "credential.credentialKey");
//        }

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, credential.getScopeId()));

        return entityManagerSession.doTransactedAction(em -> {
            Credential currentCredential = CredentialDAO.find(em, credential.getScopeId(), credential.getId());

            if (currentCredential == null) {
                throw new KapuaEntityNotFoundException(Credential.TYPE, credential.getId());
            }

            if (currentCredential.getCredentialType() != credential.getCredentialType()) {
                throw new KapuaIllegalArgumentException("credentialType", credential.getCredentialType().toString());
            }

            // Passing attributes??
            return CredentialDAO.update(em, credential);
        });
    }

    @Override
    public Credential find(KapuaId scopeId, KapuaId credentialId)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(credentialId, "credentialId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.doAction(em -> CredentialDAO.find(em, scopeId, credentialId));
    }

    @Override
    public CredentialListResult query(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> CredentialDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> CredentialDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId credentialId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(credentialId, "credential.id");
        ArgumentValidator.notNull(scopeId, "credential.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.doTransactedAction(em -> {
            if (CredentialDAO.find(em, scopeId, credentialId) == null) {
                throw new KapuaEntityNotFoundException(Credential.TYPE, credentialId);
            }
            return CredentialDAO.delete(em, scopeId, credentialId);
        });
    }

    @Override
    public CredentialListResult findByUserId(KapuaId scopeId, KapuaId userId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(userId, "userId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, scopeId));

        //
        // Build query
        CredentialQuery query = new CredentialQueryImpl(scopeId);
        QueryPredicate predicate = query.attributePredicate(CredentialAttributes.USER_ID, userId);
        query.setPredicate(predicate);

        //
        // Query and return result
        return query(query);
    }

    @Override
    public Credential findByApiKey(String apiKey) throws KapuaException {

        KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        int preLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);

        //
        // Argument Validation
        ArgumentValidator.notEmptyOrNull(apiKey, "apiKey");
        ArgumentValidator.lengthRange(apiKey, preLength, null, "apiKey");

        //
        // Do the find
        Credential credential;
        EntityManager em = AuthenticationEntityManagerFactory.getEntityManager();
        try {

            //
            // Build search query
            String preSeparator = setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_SEPARATOR);
            String apiKeyPreValue = apiKey.substring(0, preLength).concat(preSeparator);

            //
            // Build query
            KapuaQuery query = new CredentialQueryImpl();
            AttributePredicate<CredentialType> typePredicate = query.attributePredicate(CredentialAttributes.CREDENTIAL_TYPE, CredentialType.API_KEY);
            AttributePredicate<String> keyPredicate = query.attributePredicate(CredentialAttributes.CREDENTIAL_KEY, apiKeyPreValue, Operator.STARTS_WITH);

            AndPredicate andPredicate = query.andPredicate(
                    typePredicate,
                    keyPredicate
            );

            query.setPredicate(andPredicate);

            //
            // Query
            CredentialListResult credentialListResult = CredentialDAO.query(em, query);

            //
            // Parse the result
            credential = credentialListResult.getFirstItem();

        } catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        } finally {
            em.close();
        }

        //
        // Check Access
        if (credential != null) {
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, credential.getId()));
        }

        return credential;
    }

    @Override
    public void unlock(KapuaId scopeId, KapuaId credentialId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(credentialId, "credentialId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, scopeId));

        Credential credential = find(scopeId, credentialId);
        credential.setLoginFailures(0);
        credential.setFirstLoginFailure(null);
        credential.setLoginFailuresReset(null);
        credential.setLockoutReset(null);
        update(credential);
    }

    @Override
    public int getMinimumPasswordLength(KapuaId scopeId) throws KapuaException {
        Object minPasswordLengthConfigValue = getConfigValues(scopeId).get(PASSWORD_MIN_LENGTH);
        int minPasswordLength = systemMinimumPasswordLength;
        if (minPasswordLengthConfigValue != null) {
            minPasswordLength = Integer.parseInt(minPasswordLengthConfigValue.toString());
        }
        return minPasswordLength;
    }

    @Override
    protected boolean validateNewConfigValuesCoherence(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId) throws KapuaException {
        boolean valid = super.validateNewConfigValuesCoherence(ocd, updatedProps, scopeId, parentId);
        if (updatedProps.get(PASSWORD_MIN_LENGTH) != null) {
            // If we're going to set a new limit, check that it's not less than system limit
            int newPasswordLimit = Integer.parseInt(updatedProps.get(PASSWORD_MIN_LENGTH).toString());
            if (newPasswordLimit < systemMinimumPasswordLength || newPasswordLimit > SYSTEM_MAXIMUM_PASSWORD_LENGTH) {
                throw new KapuaIllegalArgumentException(PASSWORD_MIN_LENGTH, String.valueOf(newPasswordLimit));
            }
        }
        return valid;
    }

    private long countExistingCredentials(CredentialType credentialType, KapuaId scopeId, KapuaId userId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
        KapuaQuery query = credentialFactory.newQuery(scopeId);

        QueryPredicate credentialTypePredicate = query.attributePredicate(CredentialAttributes.CREDENTIAL_TYPE, credentialType);
        QueryPredicate userIdPredicate = query.attributePredicate(CredentialAttributes.USER_ID, userId);

        QueryPredicate andPredicate = query.andPredicate(
                credentialTypePredicate,
                userIdPredicate
        );

        query.setPredicate(andPredicate);

        return count(query);
    }

    //@ListenServiceEvent(fromAddress="account")
    //@ListenServiceEvent(fromAddress="user")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("CredentialService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("user".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteCredentialByUserId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        } else if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteCredentialByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteCredentialByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        CredentialQuery query = credentialFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(CredentialAttributes.USER_ID, userId));

        CredentialListResult credentialsToDelete = query(query);

        for (Credential c : credentialsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

    private void deleteCredentialByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        CredentialQuery query = credentialFactory.newQuery(accountId);

        CredentialListResult credentialsToDelete = query(query);

        for (Credential c : credentialsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

}
