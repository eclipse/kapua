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
package org.eclipse.kapua.client.security.bean;

import java.security.Principal;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;

/**
 * Kapua {@link Principal} implementation
 *
 * @since 1.0
 */
public class KapuaPrincipalImpl implements KapuaPrincipal {

    private static final long serialVersionUID = -2120917772921438176L;

    private String name;
    private String clientId;
    private boolean internal;
    /**
     * Token Id coming from the KapuaSession. It's used to keep the KapuaPrincipal aligned with the KapuaSession
     */
    private String accessTokenId;
    private KapuaId userId;
    private KapuaId accountId;
    private String clientIp;
    private String connectionId;

    /**
     * Create a KapuaPrincipal gathering infos from the supplied authResponse.
     *
     * @param authResponse
     */
    public KapuaPrincipalImpl(AuthResponse authResponse) {
        name = authResponse.getUsername();
        clientId = authResponse.getClientId();
        accessTokenId = authResponse.getAccessTokenId();
        userId = KapuaEid.parseCompactId(authResponse.getUserId());
        accountId = KapuaEid.parseCompactId(authResponse.getScopeId());
        clientIp = authResponse.getClientIp();
        connectionId = authResponse.getConnectionId();
    }

    /**
     * Create a KapuaPrincipal for internal connectors
     *
     * @param accountId
     * @param clientId
     */
    public KapuaPrincipalImpl(KapuaId accountId, String clientId) {
        internal = true;
        this.clientId = clientId;
        this.accountId = accountId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTokenId() {
        return accessTokenId;
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
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public boolean isInternal() {
        return internal;
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
