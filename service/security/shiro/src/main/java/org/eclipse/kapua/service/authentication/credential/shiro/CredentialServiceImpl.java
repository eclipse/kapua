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
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceLinker;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialAttributes;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.exception.DuplicatedPasswordCredentialException;
import org.eclipse.kapua.service.authentication.shiro.CredentialServiceConfigurationManager;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.storage.TxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link CredentialService} implementation.
 *
 * @since 1.0
 */
@Singleton
public class CredentialServiceImpl extends KapuaConfigurableServiceLinker implements CredentialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private SecureRandom random;
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final TxManager txManager;
    private final CredentialRepository credentialRepository;
    private final CredentialFactory credentialFactory;
    private final CredentialMapper credentialMapper;
    private final PasswordValidator passwordValidator;

    public CredentialServiceImpl(
            CredentialServiceConfigurationManager serviceConfigurationManager,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            TxManager txManager,
            CredentialRepository credentialRepository,
            CredentialFactory credentialFactory,
            CredentialMapper credentialMapper,
            PasswordValidator passwordValidator) {
        super(serviceConfigurationManager);
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.txManager = txManager;
        this.credentialRepository = credentialRepository;
        this.credentialFactory = credentialFactory;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw KapuaRuntimeException.internalError(e, "Cannot instantiate SecureRandom (SHA1PRNG)");
        }
        this.credentialMapper = credentialMapper;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public Credential create(CredentialCreator credentialCreatorer)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(credentialCreatorer, "credentialCreator");
        ArgumentValidator.notNull(credentialCreatorer.getScopeId(), "credentialCreator.scopeId");
        ArgumentValidator.notNull(credentialCreatorer.getUserId(), "credentialCreator.userId");
        ArgumentValidator.notNull(credentialCreatorer.getCredentialType(), "credentialCreator.credentialType");
        ArgumentValidator.notNull(credentialCreatorer.getCredentialStatus(), "credentialCreator.credentialStatus");
        if (credentialCreatorer.getCredentialType() != CredentialType.API_KEY) {
            ArgumentValidator.notEmptyOrNull(credentialCreatorer.getCredentialPlainKey(), "credentialCreator.credentialKey");
        }
        final AtomicReference<String> fullKey = new AtomicReference<>(null);
        final AtomicReference<CredentialCreator> credentialCreatorRef = new AtomicReference<>(credentialCreatorer);

        final Credential res = txManager.execute(tx -> {
            CredentialCreator credentialCreator = credentialCreatorRef.get();
            if (credentialCreator.getCredentialType() == CredentialType.PASSWORD) {
                // Check if a PASSWORD credential already exists for the user
                CredentialListResult existingCredentials = credentialRepository.findByUserId(tx, credentialCreator.getScopeId(), credentialCreator.getUserId());
                for (Credential credential : existingCredentials.getItems()) {
                    if (credential.getCredentialType().equals(CredentialType.PASSWORD)) {
                        throw new DuplicatedPasswordCredentialException();
                    }
                }

                try {
                    validatePassword(credentialCreator.getScopeId(), credentialCreator.getCredentialPlainKey());
                } catch (KapuaIllegalArgumentException ignored) {
                    throw new KapuaIllegalArgumentException("credentialCreator.credentialKey", credentialCreator.getCredentialPlainKey());
                }
            }
            // Check access
            authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, credentialCreator.getScopeId()));
            // Do create
            // Do pre persist magic on key values
            switch (credentialCreator.getCredentialType()) {
                case API_KEY: // Generate new api key
                    KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
                    int preLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);
                    int keyLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_KEY_LENGTH);

                    byte[] bPre = new byte[preLength];
                    random.nextBytes(bPre);
                    String pre = Base64.encodeToString(bPre).substring(0, preLength);

                    byte[] bKey = new byte[keyLength];
                    random.nextBytes(bKey);
                    String key = Base64.encodeToString(bKey);

                    fullKey.set(pre + key);

                    credentialCreator = new CredentialCreatorImpl(credentialCreator.getScopeId(),
                            credentialCreator.getUserId(),
                            credentialCreator.getCredentialType(),
                            fullKey.get(),
                            credentialCreator.getCredentialStatus(),
                            credentialCreator.getExpirationDate());
                    break;
                case PASSWORD:
                default:
                    // Don't do anything special
                    break;
            }
            // Create Credential
            Credential newCredential = credentialMapper.map(credentialCreator);
            // Do create
            return credentialRepository.create(tx, newCredential);
        });
        // Do post persist magic on key values
        res.setCredentialKey(fullKey.get());
        return res;
    }

    @Override
    public Credential update(Credential credential)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(credential, "credential");
        ArgumentValidator.notNull(credential.getId(), "credential.id");
        ArgumentValidator.notNull(credential.getScopeId(), "credential.scopeId");
        ArgumentValidator.notNull(credential.getUserId(), "credential.userId");
        ArgumentValidator.notNull(credential.getCredentialType(), "credential.credentialType");

        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, credential.getScopeId()));

        final Credential updatedCredential = txManager.execute(tx -> {
            Credential currentCredential = credentialRepository.find(tx, credential.getScopeId(), credential.getId());

            if (currentCredential == null) {
                throw new KapuaEntityNotFoundException(Credential.TYPE, credential.getId());
            }

            if (currentCredential.getCredentialType() != credential.getCredentialType()) {
                throw new KapuaIllegalArgumentException("credentialType", credential.getCredentialType().toString());
            }

            // Passing attributes??
            return credentialRepository.update(tx, credential);
        });
        updatedCredential.setCredentialKey(null);
        return updatedCredential;
    }

    @Override
    public Credential find(KapuaId scopeId, KapuaId credentialId)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(credentialId, "credentialId");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, scopeId));

        Credential credential = txManager.execute(tx -> credentialRepository.find(tx, scopeId, credentialId));
        credential.setCredentialKey(null);
        return credential;
    }

    @Override
    public CredentialListResult query(KapuaQuery query)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, query.getScopeId()));

        final CredentialListResult credentials = txManager.execute(tx -> credentialRepository.query(tx, query));
        credentials.getItems().forEach(credential -> credential.setCredentialKey(null));
        return credentials;
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, query.getScopeId()));
        return txManager.execute(tx -> credentialRepository.count(tx, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId credentialId)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(credentialId, "credential.id");
        ArgumentValidator.notNull(scopeId, "credential.scopeId");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.delete, scopeId));
        txManager.execute(tx -> credentialRepository.delete(tx, scopeId, credentialId));
    }

    @Override
    public CredentialListResult findByUserId(KapuaId scopeId, KapuaId userId)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(userId, "userId");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, scopeId));

        final CredentialListResult credentials = txManager.execute(tx -> credentialRepository.findByUserId(tx, scopeId, userId));
        credentials.getItems().forEach(credential -> credential.setCredentialKey(null));
        return credentials;
    }

    @Override
    public Credential findByApiKey(String apiKey) throws KapuaException {
        KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        int preLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);
        // Argument Validation
        ArgumentValidator.notEmptyOrNull(apiKey, "apiKey");
        ArgumentValidator.lengthRange(apiKey, preLength, null, "apiKey");
        // Do the find
        Credential credential = txManager.execute(tx -> {
            // Build search query
            String preSeparator = setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_SEPARATOR);
            String apiKeyPreValue = apiKey.substring(0, preLength).concat(preSeparator);
            // Build query
            KapuaQuery query = new CredentialQueryImpl();
            AttributePredicate<CredentialType> typePredicate = query.attributePredicate(CredentialAttributes.CREDENTIAL_TYPE, CredentialType.API_KEY);
            AttributePredicate<String> keyPredicate = query.attributePredicate(CredentialAttributes.CREDENTIAL_KEY, apiKeyPreValue, AttributePredicate.Operator.STARTS_WITH);

            AndPredicate andPredicate = query.andPredicate(
                    typePredicate,
                    keyPredicate
            );

            query.setPredicate(andPredicate);
            // Query
            CredentialListResult credentialListResult = credentialRepository.query(tx, query);
            // Parse the result
            return credentialListResult.getFirstItem();
        });

        ///FIXME: why the permission check here? it does not rollback!
        // Check Access
        if (credential != null) {
            authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, credential.getId()));
            credential.setCredentialKey(null);
        }

        return credential;
    }

    @Override
    public void unlock(KapuaId scopeId, KapuaId credentialId) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(credentialId, "credentialId");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, scopeId));

        txManager.execute(tx -> {
            Credential credential = credentialRepository.find(tx, scopeId, credentialId);
            credential.setLoginFailures(0);
            credential.setFirstLoginFailure(null);
            credential.setLoginFailuresReset(null);
            credential.setLockoutReset(null);
            return credentialRepository.update(tx, credential);
        });
    }

    @Override
    public int getMinimumPasswordLength(KapuaId scopeId) throws KapuaException {
        return passwordValidator.getMinimumPasswordLength(scopeId);
    }

    private long countExistingCredentials(CredentialType credentialType, KapuaId scopeId, KapuaId userId) throws KapuaException {
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

    @Override
    public void validatePassword(KapuaId scopeId, String plainPassword) throws KapuaException {
        passwordValidator.validatePassword(scopeId, plainPassword);
    }

    @Override
    public Credential findWithKey(KapuaId scopeId, KapuaId credentialId) throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(credentialId, "credentialId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, null));

        return txManager.execute(tx -> credentialRepository.find(tx, scopeId, credentialId));
    }


}
