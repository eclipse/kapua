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
package org.eclipse.kapua.service.authentication;

import java.util.Date;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Access token entity implementation.
 * 
 * @since 1.0
 * 
 */
public class AccessTokenImpl extends AbstractKapuaEntity implements AccessToken {

    private static final long serialVersionUID = -6003387376828196787L;

    private String tokenId;
    private KapuaId userId;
    private KapuaId userScopeId;

    /**
     * Constructor
     * 
     * @param userId
     *            user identifier
     * @param scopeId
     *            scope identifier
     * @param userScopeId
     *            user acts on a different scope identifier (on which he has the right)
     * @param tokenId
     *            token identifier
     */
    public AccessTokenImpl(KapuaId userId, KapuaId scopeId, KapuaId userScopeId, String tokenId) {
        super();
        setScopeId(scopeId);
        setUserId(userId);
        setUserScopeId(userScopeId);
        setTokenId(tokenId);
        setCreatedOn(new Date());
        setCreatedBy(id);
    }

    @Override
    public KapuaId getId() {
        return id;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public Date getCreatedOn() {
        return createdOn;
    }

    @Override
    public KapuaId getCreatedBy() {
        return createdBy;
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    protected void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public KapuaId getUserScopeId() {
        return userScopeId;
    }

    protected void setUserScopeId(KapuaId userScopeId) {
        this.userScopeId = new KapuaEid(userScopeId);
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    protected void setUserId(KapuaId userId) {
        this.userId = new KapuaEid(userId);
    }

}
