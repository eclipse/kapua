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
package org.eclipse.kapua.broker.core.plugin;

import java.security.Principal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.eclipse.kapua.service.authentication.token.AccessToken;

/**
 * Kapua {@link Principal} implementation
 *
 * @since 1.0
 */
public class KapuaPrincipalImpl implements KapuaPrincipal {

    private static final long serialVersionUID = -3999313290528918167L;

    private final String name;
    /**
     * Token Id coming from the KapuaSession. It's used to keep the KapuaPrincipal aligned with the KapuaSession
     */
    private final String tokenId;
    private final KapuaId userId;
    private final KapuaId accountId;
    private final String clientId;
    private final String clientIp;

    /**
     * Create a KapuaPrincipal with the supplied name.
     *
     * @param accessToken
     * @param username
     * @param clientId
     * @param clientIp
     */
    public KapuaPrincipalImpl(AccessToken accessToken, String username, String clientId, String clientIp) {
        name = username;
        tokenId = accessToken.getTokenId();
        userId = accessToken.getUserId();
        accountId = accessToken.getScopeId();
        this.clientId = clientId;
        this.clientIp = clientIp;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public KapuaId getAccountId() {
        return accountId;
    }

    @Override
    public String getClientIp() {
        return clientIp;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (accountId == null ? 0 : accountId.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        KapuaPrincipalImpl other = (KapuaPrincipalImpl) obj;
        if (accountId == null) {
            if (other.accountId != null) {
                return false;
            }
        } else if (!accountId.equals(other.accountId)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
