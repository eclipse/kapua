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
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenAttributes;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;

/**
 * {@link AccessToken} {@link ServiceDAO}.
 *
 * @since 1.0
 */
public class AccessTokenDAO extends ServiceDAO {

    /**
     * Creates and return new access token
     *
     * @param em
     * @param accessTokenCreator
     * @return
     * @throws KapuaException
     */
    public static AccessToken create(EntityManager em, AccessTokenCreator accessTokenCreator)
            throws KapuaException {
        //
        // Create User
        AccessTokenImpl accessTokenImpl = new AccessTokenImpl(accessTokenCreator.getScopeId(),
                accessTokenCreator.getUserId(),
                accessTokenCreator.getTokenId(),
                accessTokenCreator.getExpiresOn(),
                accessTokenCreator.getRefreshToken(),
                accessTokenCreator.getRefreshExpiresOn());

        return ServiceDAO.create(em, accessTokenImpl);
    }

    /**
     * Update the provided access token
     *
     * @param em
     * @param accessToken
     * @return
     * @throws KapuaException
     */
    public static AccessToken update(EntityManager em, AccessToken accessToken)
            throws KapuaException {
        //
        // Update user
        AccessTokenImpl accessTokenImpl = (AccessTokenImpl) accessToken;

        return ServiceDAO.update(em, AccessTokenImpl.class, accessTokenImpl);
    }

    /**
     * Find the access token by access token identifier
     *
     * @param em
     * @param scopeId
     * @param accessTokenId
     * @return
     */
    public static AccessToken find(EntityManager em, KapuaId scopeId, KapuaId accessTokenId) {
        return ServiceDAO.find(em, AccessTokenImpl.class, scopeId, accessTokenId);
    }

    /**
     * Find the access token by the token string id
     *
     * @param em
     * @param tokenId
     * @return
     */
    public static AccessToken findByTokenId(EntityManager em, String tokenId) {
        return ServiceDAO.findByField(em, AccessTokenImpl.class, AccessTokenAttributes.TOKEN_ID, tokenId);
    }

    /**
     * Return the access token list matching the provided query
     *
     * @param em
     * @param accessTokenQuery
     * @return
     * @throws KapuaException
     */
    public static AccessTokenListResult query(EntityManager em, KapuaQuery accessTokenQuery)
            throws KapuaException {
        return ServiceDAO.query(em, AccessToken.class, AccessTokenImpl.class, new AccessTokenListResultImpl(), accessTokenQuery);
    }

    /**
     * Return the access token count matching the provided query
     *
     * @param em
     * @param accessTokenQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery accessTokenQuery)
            throws KapuaException {
        return ServiceDAO.count(em, AccessToken.class, AccessTokenImpl.class, accessTokenQuery);
    }

    /**
     * Delete the accessToken by access token identifier
     *
     * @param em
     * @param scopeId
     * @param accessTokenId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If {@link AccessToken} is not found.
     */
    public static AccessToken delete(EntityManager em, KapuaId scopeId, KapuaId accessTokenId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, AccessTokenImpl.class, scopeId, accessTokenId);
    }
}
