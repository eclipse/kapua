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
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authorization.subject.Subject;

/**
 * Kapua {@link AuthenticationInfo} implementation
 * 
 * @since 1.0.0
 *
 */
public class SessionAuthenticationInfo implements AuthenticationInfo {

    private static final long serialVersionUID = -8682457531010599453L;

    private String realmName;
    private AccessToken accessToken;

    /**
     * Constructor.
     * 
     * @param realmName
     * @param accessToken
     */
    public SessionAuthenticationInfo(String realmName, AccessToken accessToken) {
        this.realmName = realmName;
        this.accessToken = accessToken;
    }

    public String getRealmName() {
        return realmName;
    }

    public Subject getSubject() {
        if (getAccessToken() != null) {
            return getAccessToken().getSubject();
        } else {
            return null;
        }
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(getSubject(), getRealmName());
    }

    @Override
    public Object getCredentials() {
        return getAccessToken();
    }
}
