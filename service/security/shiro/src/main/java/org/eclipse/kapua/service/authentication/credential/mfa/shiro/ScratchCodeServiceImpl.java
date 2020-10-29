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
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeAttributes;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeService;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticationService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * {@link ScratchCodeService} implementation.
 */
@KapuaProvider
public class ScratchCodeServiceImpl extends AbstractKapuaService implements ScratchCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScratchCodeServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final MfaAuthenticationService MFA_AUTH_SERVICE = LOCATOR.getService(MfaAuthenticationService.class);

    public ScratchCodeServiceImpl() {
        super(ScratchCodeEntityManagerFactory.getInstance());
    }

    @Override
    public ScratchCode create(ScratchCodeCreator scratchCodeCreator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scratchCodeCreator, "scratchCodeCreator");
        ArgumentValidator.notNull(scratchCodeCreator.getScopeId(), "scratchCodeCreator.scopeId");
        ArgumentValidator.notNull(scratchCodeCreator.getMfaCredentialOptionId(), "scratchCodeCreator.mfaCredentialOptionId");
        ArgumentValidator.notEmptyOrNull(scratchCodeCreator.getCode(), "credentialCreator.code");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.SCRATCH_CODE_DOMAIN, Actions.write, scratchCodeCreator.getScopeId()));

        //
        // Do create
        ScratchCode scratchCode = null;
        EntityManager em = AuthenticationEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            //
            // Do pre persist magic on key values
            String fullKey = scratchCodeCreator.getCode();
            scratchCode = ScratchCodeDAO.create(em, scratchCodeCreator);
            scratchCode = ScratchCodeDAO.find(em, scratchCode.getScopeId(), scratchCode.getId());
            em.commit();

            // Do post persist magic on key values
            scratchCode.setCode(fullKey);
        } catch (Exception pe) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(pe);
        } finally {
            em.close();
        }

        return scratchCode;
    }

    @Override
    public ScratchCode update(ScratchCode scratchCode) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scratchCode, "scratchCode");
        ArgumentValidator.notNull(scratchCode.getId(), "scratchCode.id");
        ArgumentValidator.notNull(scratchCode.getScopeId(), "scratchCode.scopeId");
        ArgumentValidator.notNull(scratchCode.getMfaCredentialOptionId(), "scratchCode.mfaCredentialOptionId");
        ArgumentValidator.notEmptyOrNull(scratchCode.getCode(), "scratchCode.code");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.SCRATCH_CODE_DOMAIN, Actions.write, scratchCode.getScopeId()));

        return entityManagerSession.doTransactedAction(em -> {
            ScratchCode currentscratchCode = ScratchCodeDAO.find(em, scratchCode.getScopeId(), scratchCode.getId());

            if (currentscratchCode == null) {
                throw new KapuaEntityNotFoundException(ScratchCode.TYPE, scratchCode.getId());
            }

            // Passing attributes??
            return ScratchCodeDAO.update(em, scratchCode);
        });
    }

    @Override
    public ScratchCode find(KapuaId scopeId, KapuaId scratchCodeId) throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(scratchCodeId, "scratchCodeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.SCRATCH_CODE_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.doAction(em -> ScratchCodeDAO.find(em, scopeId, scratchCodeId));
    }

    @Override
    public ScratchCodeListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.SCRATCH_CODE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> ScratchCodeDAO.query(em, query));
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
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.SCRATCH_CODE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> ScratchCodeDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId scratchCodeId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scratchCodeId, "scratchCode.id");
        ArgumentValidator.notNull(scopeId, "scratchCode.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.SCRATCH_CODE_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.doTransactedAction(em -> {
            if (ScratchCodeDAO.find(em, scopeId, scratchCodeId) == null) {
                throw new KapuaEntityNotFoundException(ScratchCode.TYPE, scratchCodeId);
            }
            return ScratchCodeDAO.delete(em, scopeId, scratchCodeId);
        });
    }

    @Override
    public ScratchCodeListResult createAllScratchCodes(ScratchCodeCreator scratchCodeCreator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scratchCodeCreator, "scratchCodeCreator");
        ArgumentValidator.notNull(scratchCodeCreator.getScopeId(), "scratchCodeCreator.scopeId");
        ArgumentValidator.notNull(scratchCodeCreator.getMfaCredentialOptionId(), "scratchCodeCreator.mfaCredentialOptionId");

        List<String> codes = MFA_AUTH_SERVICE.generateCodes();
        ScratchCodeListResult scratchCodeListResult = new ScratchCodeListResultImpl();

        for (String code : codes) {
            scratchCodeCreator.setCode(code);
            ScratchCode scratchCode = create(scratchCodeCreator);
            scratchCodeListResult.addItem(scratchCode);
        }

        return scratchCodeListResult;
    }

    @Override
    public ScratchCodeListResult findByMfaCredentialOptionId(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(mfaCredentialOptionId, ScratchCodeAttributes.MFA_CREDENTIAL_OPTION_ID);

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.SCRATCH_CODE_DOMAIN, Actions.read, scopeId));

        //
        // Build query
        ScratchCodeQuery query = new ScratchCodeQueryImpl(scopeId);
        QueryPredicate predicate = query.attributePredicate(ScratchCodeAttributes.MFA_CREDENTIAL_OPTION_ID, mfaCredentialOptionId);
        query.setPredicate(predicate);

        //
        // Query and return result
        return query(query);
    }

    private void deleteScratchCodeByMfaCredentialOptionId(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        ScratchCodeFactory scratchCodeFactory = locator.getFactory(ScratchCodeFactory.class);

        ScratchCodeQuery query = scratchCodeFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(ScratchCodeAttributes.MFA_CREDENTIAL_OPTION_ID, mfaCredentialOptionId));

        ScratchCodeListResult credentialsToDelete = query(query);

        for (ScratchCode c : credentialsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

    private void deleteScratchCodeByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        ScratchCodeFactory scratchCodeFactory = locator.getFactory(ScratchCodeFactory.class);

        ScratchCodeQuery query = scratchCodeFactory.newQuery(accountId);

        ScratchCodeListResult credentialsToDelete = query(query);

        for (ScratchCode c : credentialsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

}
