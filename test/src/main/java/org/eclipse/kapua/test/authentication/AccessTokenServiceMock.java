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
package org.eclipse.kapua.test.authentication;

import java.util.Date;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.authorization.subject.Subject;
import org.eclipse.kapua.test.authorization.subject.SubjectMock;

/**
 * {@link AccessTokenService} mock implementation for tests.
 * 
 * @since 1.0.0
 *
 */
@TestService
@KapuaProvider
public class AccessTokenServiceMock implements AccessTokenService {

    private AccessToken accessToken = null;

    public AccessTokenServiceMock() {
    }

    @Override
    public AccessToken create(AccessTokenCreator accessTokenCreator)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accessTokenCreator, "accessTokenCreator");
        ArgumentValidator.notNull(accessTokenCreator.getScopeId(), "accessTokenCreator.scopeId");
        ArgumentValidator.notNull(accessTokenCreator.getSubjectType(), "accessTokenCreator.subjectType");
        ArgumentValidator.notNull(accessTokenCreator.getSubjectId(), "accessTokenCreator.subjectId");
        ArgumentValidator.notNull(accessTokenCreator.getTokenId(), "accessTokenCreator.tokenId");
        ArgumentValidator.notNull(accessTokenCreator.getExpiresOn(), "accessTokenCreator.expiresOn");

        accessToken = new AccessTokenMock(accessTokenCreator.getScopeId(),
                new SubjectMock(accessTokenCreator.getSubjectType(), accessTokenCreator.getSubjectId()),
                accessTokenCreator.getTokenId(),
                accessTokenCreator.getExpiresOn());

        accessToken.setId(new KapuaEid(IdGenerator.generate()));

        return accessToken;
    }

    @Override
    public AccessToken update(AccessToken accessToken)
            throws KapuaException {
        return this.accessToken;
    }

    @Override
    public AccessToken find(KapuaId scopeId, KapuaId accessTokenId)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accessTokenId, "accessTokenId");

        if (accessToken != null) {
            if (!accessTokenId.getId().equals(accessTokenId)) {
                return null;
            }
        }
        return accessToken;
    }

    @Override
    public AccessTokenListResult query(KapuaQuery<AccessToken> query)
            throws KapuaException {
        return null;
    }

    @Override
    public long count(KapuaQuery<AccessToken> query)
            throws KapuaException {
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessTokenId)
            throws KapuaException {
        if (accessToken != null) {
            if (accessToken.getId().equals(accessTokenId)) {
                accessToken = null;
            }
        }
    }

    @Override
    public AccessTokenListResult findBySubject(KapuaId scopeId, Subject subject)
            throws KapuaException {
        return null;
    }

    @Override
    public AccessToken findByTokenId(String tokenId)
            throws KapuaException {
        if (accessToken != null) {
            if (!accessToken.getTokenId().equals(tokenId)) {
                accessToken = null;
            }
        }
        return accessToken;
    }

    @Override
    public void invalidate(KapuaId scopeId, KapuaId accessTokenId) throws KapuaException {
        if (accessToken != null) {
            if (accessToken.getId().equals(accessTokenId)) {
                accessToken.setExpiresOn(new Date());
            }
        }
    }
}
