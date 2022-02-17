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

import java.util.Date;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;

/**
 * Access token implementation
 *
 * @since 1.0
 *
 */
public class AccessTokenCreatorImpl extends AbstractKapuaEntityCreator<AccessToken> implements AccessTokenCreator {

    private static final long serialVersionUID = -27718046815190710L;

    private String tokenId;
    private KapuaId userId;
    private Date expiresOn;
    private String refreshToken;
    private Date refreshExpiresOn;

    /**
     * Constructor
     *
     * @param scopeId
     */
    protected AccessTokenCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;

    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = userId;
    }

    @Override
    public Date getExpiresOn() {
        return expiresOn;
    }

    @Override
    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;

    }

    @Override
    public Date getRefreshExpiresOn() {
        return refreshExpiresOn;
    }

    @Override
    public void setRefreshExpiresOn(Date refreshExpiresOn) {
        this.refreshExpiresOn = refreshExpiresOn;
    }

}
