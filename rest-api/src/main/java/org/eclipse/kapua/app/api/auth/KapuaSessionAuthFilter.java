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
 *******************************************************************************/
package org.eclipse.kapua.app.api.auth;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenImpl;

/**
 * {@link ShiroFilter} override. Used to intercept the http request execute and plug into thread context the Kapua session
 *
 */
public class KapuaSessionAuthFilter extends ShiroFilter {

    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain origChain)
            throws IOException, ServletException {
        // bind kapua session

        // TODO workaround to fix the null kapua session on webconsole requests.
        // to be removed and substitute with getToken or another solution?
        KapuaSession kapuaSession = null;
        Subject shiroSubject = SecurityUtils.getSubject();
        if (shiroSubject != null && shiroSubject.isAuthenticated()) {
            Session s = shiroSubject.getSession();
            KapuaEid scopeId = (KapuaEid) s.getAttribute("scopeId");
            KapuaEid userId = (KapuaEid) s.getAttribute("userId");

            // create the access token
            String generatedTokenKey = UUID.randomUUID().toString();
            AccessToken accessToken = new AccessTokenImpl(scopeId, userId, generatedTokenKey, new Date());

            kapuaSession = new KapuaSession(accessToken, scopeId, userId, "");
        }
        try {
            KapuaSecurityUtils.setSession(kapuaSession);

            super.executeChain(request, response, origChain);
        } finally {
            // unbind kapua session
            KapuaSecurityUtils.clearSession();
        }
    }

}
