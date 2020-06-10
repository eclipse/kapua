/*******************************************************************************
 * Copyright (c) 2011, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.service.authentication.JwtCredentials;

public class JwtCredentialsImpl implements JwtCredentials, AuthenticationToken {

    private static final long serialVersionUID = -5920944517814926028L;

    private String jwt;
    private String idToken;

    public JwtCredentialsImpl(String jwt, String idToken) {
        setJwt(jwt);
        setIdToken(idToken);
    }

    @Override
    public String getJwt() {
        return jwt;
    }

    @Override
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String getIdToken() {
        return idToken;
    }

    @Override
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public Object getPrincipal() {
        return getJwt();
    }

    @Override
    public Object getCredentials() {
        return getJwt();
    }
}
