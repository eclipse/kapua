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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.eclipse.kapua.service.authentication.token.AccessToken;

/**
 * {@link KapuaPrincipal} implementation
 *
 * @since 1.0.0
 */
public class KapuaPrincipalImpl implements KapuaPrincipal {

    private static final long serialVersionUID = -3999313290528918167L;

    private String name;
    /**
     * Access Token coming from the KapuaSession.
     * It's used to keep the KapuaPrincipal aligned with the KapuaSession
     */
    private AccessToken accessToken;

    private String username;
    private String clientId;
    private String clientIp;

    /**
     * Create a KapuaPrincipal with the supplied name.
     * 
     * @param accessToken
     * @param username
     * @param clientId
     * @param clientIp
     */
    public KapuaPrincipalImpl(AccessToken accessToken, String username, String clientId, String clientIp) {
        this.accessToken = accessToken;

        this.username = username;
        this.clientId = clientId;
        this.clientIp = clientIp;

        this.name = (new StringBuilder()).append(accessToken.getScopeId() != null ? accessToken.getScopeId() : "null").append(":").append(username).toString();
    }

    public String getName() {
        return name;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((accessToken.getScopeId() == null) ? 0 : accessToken.getScopeId().hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KapuaPrincipalImpl other = (KapuaPrincipalImpl) obj;
        if (accessToken.getScopeId() == null) {
            if (other.accessToken.getScopeId() != null)
                return false;
        } else if (!accessToken.getScopeId().equals(other.accessToken.getScopeId()))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
