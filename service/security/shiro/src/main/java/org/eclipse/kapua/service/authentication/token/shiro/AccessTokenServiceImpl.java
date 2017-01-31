/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token.shiro;

import java.util.Date;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.AccessTokenPredicates;
import org.eclipse.kapua.service.authentication.token.AccessTokenQuery;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

/**
 * Access token service implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class AccessTokenServiceImpl extends AbstractKapuaService implements AccessTokenService {

    private static final Domain accessTokenDomain = new AccessTokenDomain();

    /**
     * Constructor
     */
    public AccessTokenServiceImpl() {
        super(AccessTokenEntityManagerFactory.getInstance());
    }

    @Override
    public AccessToken create(AccessTokenCreator accessTokenCreator)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accessTokenCreator, "accessTokenCreator");
        ArgumentValidator.notNull(accessTokenCreator.getScopeId(), "accessTokenCreator.scopeId");
        ArgumentValidator.notNull(accessTokenCreator.getSubjectType(), "accessTokenCreator.subjectType");
        // ArgumentValidator.notNull(accessTokenCreator.getSubjectId(), "accessTokenCreator.subjectId");
        ArgumentValidator.notNull(accessTokenCreator.getCredentialId(), "accessTokenCreator.credentialId");
        ArgumentValidator.notNull(accessTokenCreator.getTokenId(), "accessTokenCreator.tokenId");
        ArgumentValidator.notNull(accessTokenCreator.getExpiresOn(), "accessTokenCreator.expiresOn");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.write, accessTokenCreator.getScopeId()));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> AccessTokenDAO.create(em, accessTokenCreator));
    }

    @Override
    public AccessToken update(AccessToken accessToken)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accessToken, "accessToken");
        ArgumentValidator.notNull(accessToken.getId(), "accessToken.id");
        ArgumentValidator.notNull(accessToken.getScopeId(), "accessToken.scopeId");
        ArgumentValidator.notNull(accessToken.getSubject().getSubjectType(), "accessTokenCreator.subject.type");
        ArgumentValidator.notNull(accessToken.getSubject().getId(), "accessTokenCreator.subject.id");
        ArgumentValidator.notNull(accessToken.getExpiresOn(), "accessToken.expiresOn");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.write, accessToken.getScopeId()));

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> {
            AccessToken currentAccessToken = AccessTokenDAO.find(em, accessToken.getId());
            if (currentAccessToken == null) {
                throw new KapuaEntityNotFoundException(AccessToken.TYPE, accessToken.getId());
            }

            return AccessTokenDAO.update(em, accessToken);
        });
    }

    @Override
    public AccessToken find(KapuaId scopeId, KapuaId accessTokenId)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accessTokenId, "accessTokenId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> AccessTokenDAO.find(em, accessTokenId));
    }

    @Override
    public AccessTokenListResult query(KapuaQuery<AccessToken> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> AccessTokenDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<AccessToken> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> AccessTokenDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessTokenId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accessTokenId, "accessTokenId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            if (AccessTokenDAO.find(em, accessTokenId) == null) {
                throw new KapuaEntityNotFoundException(AccessToken.TYPE, accessTokenId);
            }

            AccessTokenDAO.delete(em, accessTokenId);
        });
    }

    @Override
    public AccessTokenListResult findBySubject(KapuaId scopeId, Subject subject)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(subject, "subject");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.read, scopeId));

        //
        // Build query
        AccessTokenQuery query = new AccessTokenQueryImpl(scopeId);
        KapuaPredicate predicate = new AttributePredicate<>(AccessTokenPredicates.SUBJECT, subject);
        query.setPredicate(predicate);

        //
        // Query and return result
        return query(query);
    }

    @Override
    public AccessToken findByTokenId(String tokenId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(tokenId, "tokenId");

        //
        // Do the find
        AccessToken accessToken = entityManagerSession.onResult(em -> AccessTokenDAO.findByTokenId(em, tokenId));

        //
        // Check Access
        if (accessToken != null) {
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.read, accessToken.getScopeId()));
        }

        return accessToken;
    }

    @Override
    public void invalidate(KapuaId scopeId, KapuaId accessTokenId) throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accessTokenId, "accessTokenId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(accessTokenDomain, Actions.read, scopeId));

        //
        // Do find
        entityManagerSession.onTransactedResult(em -> {
            AccessToken accessToken = AccessTokenDAO.find(em, accessTokenId);
            accessToken.setExpiresOn(new Date());
            return AccessTokenDAO.update(em, accessToken);
        });
    }
}
