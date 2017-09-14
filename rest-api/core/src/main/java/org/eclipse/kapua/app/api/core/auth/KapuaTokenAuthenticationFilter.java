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
package org.eclipse.kapua.app.api.core.auth;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.CredentialsFactory;

public class KapuaTokenAuthenticationFilter extends AuthenticatingFilter {

    private static final String OPTIONS = "OPTIONS";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (OPTIONS.equals(((HttpServletRequest) request).getMethod())) {
            return true;
        }

        try {
            return executeLogin(request, response);
        } catch (AuthenticationException ae) {
            return onLoginFailure(null, ae, request, response);
        } catch (Exception e) {
            throw KapuaRuntimeException.internalError(e);
        }
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        //
        // Extract token
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String authorizationHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);
        String tokenId = null;
        if (authorizationHeader != null) {
            tokenId = httpRequest.getHeader(AUTHORIZATION_HEADER).replace(BEARER + " ", "");
        }
        //
        // Build AccessToken for Shiro Auth
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);
        AccessTokenCredentials accessTokenCredentials = credentialsFactory.newAccessTokenCredentials(tokenId);

        //
        // Return token
        return (AuthenticationToken) accessTokenCredentials;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

}
