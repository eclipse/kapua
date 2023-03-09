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
package org.eclipse.kapua.service.authentication.token.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenAttributes;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.AccessTokenQuery;
import org.eclipse.kapua.service.authentication.token.AccessTokenRepository;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.storage.TxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Date;

/**
 * {@link AccessTokenService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class AccessTokenServiceImpl implements AccessTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenServiceImpl.class);
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final TxManager txManager;
    private final AccessTokenRepository accessTokenRepository;
    private final AccessTokenFactory accessTokenFactory;

    public AccessTokenServiceImpl(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            TxManager txManager,
            AccessTokenRepository accessTokenRepository,
            AccessTokenFactory accessTokenFactory) {
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.txManager = txManager;
        this.accessTokenRepository = accessTokenRepository;
        this.accessTokenFactory = accessTokenFactory;
    }

    @Override
    public AccessToken create(AccessTokenCreator accessTokenCreator) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(accessTokenCreator, "accessTokenCreator");
        ArgumentValidator.notNull(accessTokenCreator.getScopeId(), "accessTokenCreator.scopeId");
        ArgumentValidator.notNull(accessTokenCreator.getTokenId(), "accessTokenCreator.tokenId");
        ArgumentValidator.notNull(accessTokenCreator.getUserId(), "accessTokenCreator.userId");
        ArgumentValidator.notNull(accessTokenCreator.getExpiresOn(), "accessTokenCreator.expiresOn");

        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.write, accessTokenCreator.getScopeId()));

        // Do create
        AccessToken at = accessTokenFactory.newEntity(accessTokenCreator.getScopeId());
        at.setUserId(accessTokenCreator.getUserId());
        at.setTokenId(accessTokenCreator.getTokenId());
        at.setExpiresOn(accessTokenCreator.getExpiresOn());
        at.setRefreshToken(accessTokenCreator.getRefreshToken());
        at.setRefreshExpiresOn(accessTokenCreator.getRefreshExpiresOn());
        return txManager.executeWithResult(tx -> accessTokenRepository.create(tx, at));
    }

    @Override
    public AccessToken update(AccessToken accessToken) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accessToken, "accessToken");
        ArgumentValidator.notNull(accessToken.getId(), "accessToken.id");
        ArgumentValidator.notNull(accessToken.getScopeId(), "accessToken.scopeId");
        ArgumentValidator.notNull(accessToken.getUserId(), "accessToken.userId");
        ArgumentValidator.notNull(accessToken.getExpiresOn(), "accessToken.expiresOn");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.write, accessToken.getScopeId()));

        //
        // Check existence
        if (find(accessToken.getScopeId(), accessToken.getId()) == null) {
            throw new KapuaEntityNotFoundException(AccessToken.TYPE, accessToken.getId());
        }

        //
        // Do update
        return txManager.executeWithResult(tx -> accessTokenRepository.update(tx, accessToken));
    }

    @Override
    public AccessToken find(KapuaId scopeId, KapuaId accessTokenId) throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessTokenId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return txManager.executeWithResult(tx -> accessTokenRepository.find(tx, scopeId, accessTokenId));
    }

    @Override
    public AccessTokenListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return txManager.executeWithResult(tx -> accessTokenRepository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return txManager.executeWithResult(tx -> accessTokenRepository.count(tx, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessTokenId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessTokenId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, accessTokenId) == null) {
            throw new KapuaEntityNotFoundException(AccessToken.TYPE, accessTokenId);
        }

        //
        // Do delete
        txManager.executeWithResult(tx -> accessTokenRepository.delete(tx, scopeId, accessTokenId));
    }

    @Override
    public AccessTokenListResult findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(userId, "userId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.read, scopeId));

        //
        // Build query
        AccessTokenQuery query = new AccessTokenQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(AccessTokenAttributes.USER_ID, userId));

        //
        // Do query
        return query(query);
    }

    @Override
    public AccessToken findByTokenId(String tokenId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(tokenId, "tokenId");

        //
        // Do find
        AccessToken accessToken = txManager.executeWithResult(tx -> accessTokenRepository.findByTokenId(tx, tokenId));

        //
        // Check Access
        if (accessToken != null) {
            authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.read, accessToken.getScopeId()));
        }

        return accessToken;
    }

    @Override
    public void invalidate(KapuaId scopeId, KapuaId accessTokenId) throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessTokenId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.ACCESS_TOKEN_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        txManager.executeWithResult(tx -> {
            AccessToken accessToken = accessTokenRepository.find(tx, scopeId, accessTokenId);
            if (accessToken != null) {
                accessToken.setInvalidatedOn(new Date());

                return accessTokenRepository.update(tx, accessToken);
            } else {
                throw new KapuaEntityNotFoundException(AccessToken.TYPE, scopeId);
            }
        });
    }

    //@ListenServiceEvent(fromAddress="account")
    //@ListenServiceEvent(fromAddress="user")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }

        LOGGER.info("AccessTokenService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("user".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteAccessTokenByUserId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        } else if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteAccessTokenByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteAccessTokenByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {

        AccessTokenQuery query = new AccessTokenQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(AccessTokenAttributes.USER_ID, userId));

        AccessTokenListResult accessTokensToDelete = query(query);

        for (AccessToken at : accessTokensToDelete.getItems()) {
            delete(at.getScopeId(), at.getId());
        }
    }

    private void deleteAccessTokenByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {

        AccessTokenQuery query = new AccessTokenQueryImpl(accountId);

        AccessTokenListResult accessTokensToDelete = query(query);

        for (AccessToken at : accessTokensToDelete.getItems()) {
            delete(at.getScopeId(), at.getId());
        }
    }
}
