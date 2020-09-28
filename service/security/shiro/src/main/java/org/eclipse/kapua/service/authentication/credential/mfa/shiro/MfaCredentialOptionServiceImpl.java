/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionAttributes;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionService;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticationService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * {@link MfaCredentialOptionService} implementation.
 */
@KapuaProvider
public class MfaCredentialOptionServiceImpl extends AbstractKapuaService implements MfaCredentialOptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MfaCredentialOptionServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final MfaAuthenticationService MFA_AUTH_SERVICE = LOCATOR.getService(MfaAuthenticationService.class);

    private static final int TRUST_KEY_DURATION = 30; // duration of the trust key in days

    public MfaCredentialOptionServiceImpl() {
        super(MfaCredentialOptionEntityManagerFactory.getInstance());
    }

    @Override
    public MfaCredentialOption create(MfaCredentialOptionCreator mfaCredentialOptionCreator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(mfaCredentialOptionCreator, "mfaCredentialOptionCreator");
        ArgumentValidator.notNull(mfaCredentialOptionCreator.getScopeId(), "mfaCredentialOptionCreator.scopeId");
        ArgumentValidator.notNull(mfaCredentialOptionCreator.getUserId(), "mfaCredentialOptionCreator.userId");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.MFA_CREDENTIAL_OPTION_DOMAIN, Actions.write, mfaCredentialOptionCreator.getScopeId()));

        //
        // Do create
        MfaCredentialOption mfaCredentialOption;
        EntityManager em = AuthenticationEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            String fullKey = MFA_AUTH_SERVICE.generateKey();
            mfaCredentialOptionCreator = new MfaCredentialOptionCreatorImpl(mfaCredentialOptionCreator.getScopeId(), mfaCredentialOptionCreator.getUserId(),
                    fullKey);
            mfaCredentialOption = MfaCredentialOptionDAO.create(em, mfaCredentialOptionCreator);
            mfaCredentialOption = MfaCredentialOptionDAO.find(em, mfaCredentialOption.getScopeId(), mfaCredentialOption.getId());
            em.commit();
        } catch (Exception pe) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(pe);
        } finally {
            em.close();
        }

        return mfaCredentialOption;
    }

    @Override
    public MfaCredentialOption update(MfaCredentialOption mfaCredentialOption) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(mfaCredentialOption, "mfaCredentialOption");
        ArgumentValidator.notNull(mfaCredentialOption.getId(), "mfaCredentialOption.id");
        ArgumentValidator.notNull(mfaCredentialOption.getScopeId(), "mfaCredentialOption.scopeId");
        ArgumentValidator.notNull(mfaCredentialOption.getUserId(), "mfaCredentialOption.userId");
        ArgumentValidator.notEmptyOrNull(mfaCredentialOption.getMfaCredentialKey(), "mfaCredentialOption.mfaCredentialKey");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.MFA_CREDENTIAL_OPTION_DOMAIN, Actions.write, mfaCredentialOption.getScopeId()));

        return entityManagerSession.doTransactedAction(em -> {
            MfaCredentialOption currentMfaCredentialOption = MfaCredentialOptionDAO.find(em, mfaCredentialOption.getScopeId(), mfaCredentialOption.getId());

            if (currentMfaCredentialOption == null) {
                throw new KapuaEntityNotFoundException(MfaCredentialOption.TYPE, mfaCredentialOption.getId());
            }

            // Passing attributes??
            return MfaCredentialOptionDAO.update(em, mfaCredentialOption);
        });
    }

    @Override
    public MfaCredentialOption find(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(mfaCredentialOptionId, "mfaCredentialOptionId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.MFA_CREDENTIAL_OPTION_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.doAction(em -> MfaCredentialOptionDAO.find(em, scopeId, mfaCredentialOptionId));
    }

    @Override
    public MfaCredentialOptionListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.MFA_CREDENTIAL_OPTION_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> MfaCredentialOptionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.MFA_CREDENTIAL_OPTION_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> MfaCredentialOptionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(mfaCredentialOptionId, "mfaCredentialOption.id");
        ArgumentValidator.notNull(scopeId, "mfaCredentialOption.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.MFA_CREDENTIAL_OPTION_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.doTransactedAction(em -> {
            if (MfaCredentialOptionDAO.find(em, scopeId, mfaCredentialOptionId) == null) {
                throw new KapuaEntityNotFoundException(MfaCredentialOption.TYPE, mfaCredentialOptionId);
            }
            return MfaCredentialOptionDAO.delete(em, scopeId, mfaCredentialOptionId);
        });
    }

    @Override
    public MfaCredentialOption findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(userId, MfaCredentialOptionAttributes.USER_ID);

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.MFA_CREDENTIAL_OPTION_DOMAIN, Actions.read, scopeId));

        //
        // Build query
        MfaCredentialOptionQuery query = new MfaCredentialOptionQueryImpl(scopeId);
        QueryPredicate predicate = query.attributePredicate(MfaCredentialOptionAttributes.USER_ID, userId);
        query.setPredicate(predicate);

        //
        // Query and return result
        MfaCredentialOptionListResult result = query(query);
        if (!result.isEmpty()) {
            return result.getFirstItem();
        } else {
            return null;
        }
    }

    @Override
    public void enableTrust(MfaCredentialOption mfaCredentialOption) throws KapuaException {

        // Argument Validation (fields validation is performed inside the 'update' method)
        ArgumentValidator.notNull(mfaCredentialOption, "mfaCredentialOption");

        // If MfaCredentialOption has a code set, use that one, otherwise other trusted machines won't be able to login and a new trusted machine will be
        // requested
        String trustKey = mfaCredentialOption.getTrustKey();
        if (trustKey == null || trustKey.isEmpty()) {
            trustKey = generateTrustKey();
        }

        Date expirationDate = new Date(System.currentTimeMillis());
        expirationDate = DateUtils.addDays(expirationDate, TRUST_KEY_DURATION);

        mfaCredentialOption.setTrustKey(trustKey);
        mfaCredentialOption.setTrustExpirationDate(expirationDate);
        update(mfaCredentialOption);
    }

    @Override
    public void enableTrust(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException {

        // Argument Validation
        ArgumentValidator.notNull(mfaCredentialOptionId, "mfaCredentialOption.id");
        ArgumentValidator.notNull(scopeId, "mfaCredentialOption.scopeId");

        // extracting the MfaCredentialOption
        MfaCredentialOption mfaCredentialOption = find(scopeId, mfaCredentialOptionId);
        enableTrust(mfaCredentialOption);
    }

    @Override
    public void disableTrust(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException {

        // Argument Validation
        ArgumentValidator.notNull(mfaCredentialOptionId, "mfaCredentialOption.id");
        ArgumentValidator.notNull(scopeId, "mfaCredentialOption.scopeId");

        // extracting the MfaCredentialOption
        MfaCredentialOption mfaCredentialOption = find(scopeId, mfaCredentialOptionId);

        // Reset the trust machine fields
        mfaCredentialOption.setTrustKey(null);
        mfaCredentialOption.setTrustExpirationDate(null);

        update(mfaCredentialOption);
    }

    /**
     * Generate the trust key string.
     *
     * @return String
     */
    private String generateTrustKey() {
        return UUID.randomUUID().toString();
    }

    private void deleteMfaCredentialByUserId(KapuaId scopeId, KapuaId credentialId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        MfaCredentialOptionFactory mfaCredentialOptionFactory = locator.getFactory(MfaCredentialOptionFactory.class);

        MfaCredentialOptionQuery query = mfaCredentialOptionFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(MfaCredentialOptionAttributes.USER_ID, credentialId));

        MfaCredentialOptionListResult credentialsToDelete = query(query);

        for (MfaCredentialOption c : credentialsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

    private void deleteMfaCredentialByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        MfaCredentialOptionFactory mfaCredentialOptionFactory = locator.getFactory(MfaCredentialOptionFactory.class);

        MfaCredentialOptionQuery query = mfaCredentialOptionFactory.newQuery(accountId);

        MfaCredentialOptionListResult credentialsToDelete = query(query);

        for (MfaCredentialOption c : credentialsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

}
