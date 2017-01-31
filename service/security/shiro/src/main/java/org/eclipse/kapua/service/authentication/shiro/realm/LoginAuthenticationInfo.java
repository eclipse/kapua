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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.service.authentication.credential.Credential;

/**
 * Kapua {@link AuthenticationInfo} implementation
 * 
 * @since 1.0.0
 *
 */
public class LoginAuthenticationInfo implements AuthenticationInfo {

    private static final long serialVersionUID = -8682457531010599453L;

    private String realmName;
    private Credential credentials;

    /**
     * Constructor.
     * 
     * @param realmName
     *            The realm name
     * @param credentials
     *            The {@link Credential} info from the system.
     */
    public LoginAuthenticationInfo(String realmName, Credential credentials) {
        this.realmName = realmName;
        this.credentials = credentials;
    }

    public String getRealmName() {
        return realmName;
    }

    /**
     * Returns the scope id.
     * 
     * @return The scope id.
     */
    public KapuaId getScopeId() {
        return credentials.getScopeId();
    }

    public Subject getSubject() {
        if (getCredentials() != null) {
            return ((Credential) getCredentials()).getSubject();
        } else {
            return null;
        }
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(getSubject(), getRealmName());
    }
}
