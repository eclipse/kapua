/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;

/**
 * Credential DAO.
 * 
 * @since 1.0
 *
 */
public class AccessTokenDAO extends ServiceDAO {

    /**
     * Creates and return new credential
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
        AccessTokenImpl credentialImpl = new AccessTokenImpl(accessTokenCreator.getScopeId(),
                accessTokenCreator.getUserId(),
                accessTokenCreator.getTokenId(),
                accessTokenCreator.getExpiresOn());

        return ServiceDAO.create(em, credentialImpl);
    }

    /**
     * Update the provided credential
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
     * Delete the accessToken by credential identifier
     * 
     * @param em
     * @param accessTokenId
     */
    public static void delete(EntityManager em, KapuaId accessTokenId) {
        ServiceDAO.delete(em, AccessTokenImpl.class, accessTokenId);
    }

    /**
     * Find the access token by credential identifier
     * 
     * @param em
     * @param accessTokenId
     * @return
     */
    public static AccessToken find(EntityManager em, KapuaId accessTokenId) {
        return em.find(AccessTokenImpl.class, accessTokenId);
    }

    /**
     * Return the access token list matching the provided query
     * 
     * @param em
     * @param accessTokenQuery
     * @return
     * @throws KapuaException
     */
    public static AccessTokenListResult query(EntityManager em, KapuaQuery<AccessToken> accessTokenQuery)
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
    public static long count(EntityManager em, KapuaQuery<AccessToken> accessTokenQuery)
            throws KapuaException {
        return ServiceDAO.count(em, AccessToken.class, AccessTokenImpl.class, accessTokenQuery);
    }

}
