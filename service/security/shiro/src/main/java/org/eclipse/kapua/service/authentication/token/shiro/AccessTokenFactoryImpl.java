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
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token.shiro;

import java.util.Date;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.AccessTokenQuery;

/**
 * Credential factory service implementation.
 * 
 * @since 1.0
 * 
 */
@KapuaProvider
public class AccessTokenFactoryImpl implements AccessTokenFactory {

    @Override
    public AccessTokenCreatorImpl newCreator(KapuaId scopeId, KapuaId userId, String tokenId, Date expiresOn, String refreshToken, Date refreshExpiresOn) {
        AccessTokenCreatorImpl accessTokenCreator = new AccessTokenCreatorImpl(scopeId);
        accessTokenCreator.setUserId(userId);
        accessTokenCreator.setTokenId(tokenId);
        accessTokenCreator.setExpiresOn(expiresOn);
        accessTokenCreator.setRefreshToken(refreshToken);
        accessTokenCreator.setRefreshExpiresOn(refreshExpiresOn);
        return accessTokenCreator;
    }

    @Override
    public AccessToken newEntity(KapuaId scopeId) {
        return new AccessTokenImpl(scopeId);
    }

    @Override
    public AccessTokenCreator newCreator(KapuaId scopeId) {
        return new AccessTokenCreatorImpl(scopeId);
    }

    @Override
    public AccessTokenQuery newQuery(KapuaId scopeId) {
        return new AccessTokenQueryImpl(scopeId);
    }

    @Override
    public AccessTokenListResult newListResult() {
        return new AccessTokenListResultImpl();
    }
}
