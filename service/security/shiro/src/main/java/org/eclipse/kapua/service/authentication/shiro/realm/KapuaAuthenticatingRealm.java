/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.realm;

import java.util.Date;

import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.shiro.exceptions.ExpiredAccountException;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserStatus;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public abstract class KapuaAuthenticatingRealm extends AuthenticatingRealm {

    @Override
    protected KapuaAuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {

        final KapuaAuthenticationInfo authenticationInfo = buildAuthenticationInfo(authenticationToken);

        User user = authenticationInfo.getUser();
        Account account = authenticationInfo.getAccount();
        Credential credential = null;

        // USER //
        //
        // Check user existence
        if (user == null) {
            throw new UnknownAccountException();
        }

        // Check user disabled
        if (UserStatus.DISABLED.equals(user.getStatus())) {
            throw new DisabledAccountException();
        }

        // Check user expired
        if (user.getExpirationDate() != null && !user.getExpirationDate().after(new Date())) {
            throw new ExpiredCredentialsException();
        }

        // ACCOUNT //
        //
        // Check account existence
        if (account == null) {
            throw new UnknownAccountException();
        }

        // Check account expired
        if (account.getExpirationDate() != null && !account.getExpirationDate().after(new Date())) {
            throw new ExpiredAccountException(account.getExpirationDate());
        }

        if (authenticationInfo instanceof LoginAuthenticationInfo) {
            LoginAuthenticationInfo loginAuthenticationInfo = (LoginAuthenticationInfo)authenticationInfo;
            credential = loginAuthenticationInfo.getCredential();
            // CREDENTIAL //
            //
            // Check credential existence
            if (credential == null) {
                throw new UnknownAccountException();
            }

            // Check credential disabled
            if (CredentialStatus.DISABLED.equals(credential.getStatus())) {
                throw new DisabledAccountException();
            }

            // Check if credential expired
            if (credential.getExpirationDate() != null && !credential.getExpirationDate().after(new Date())) {
                throw new ExpiredCredentialsException();
            }

        } else if (authenticationInfo instanceof SessionAuthenticationInfo) {
            SessionAuthenticationInfo sessionAuthenticationInfo = (SessionAuthenticationInfo)authenticationInfo;
            AccessToken accessToken = sessionAuthenticationInfo.getAccessToken();
            // ACCESS TOKEN //
            //
            // Check existence
            if (accessToken == null) {
                throw new UnknownAccountException();
            }

            // Check validity
            if ((accessToken.getExpiresOn() != null && accessToken.getExpiresOn().before(new Date())) ||
                    (accessToken.getInvalidatedOn() != null && accessToken.getInvalidatedOn().before(new Date()))) {
                throw new ExpiredCredentialsException();
            }
        }

        return authenticationInfo;
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        super.assertCredentialsMatch(token, info);
        KapuaAuthenticationInfo kapuaAuthenticationInfo = (KapuaAuthenticationInfo) info;
        Subject currentSubject = SecurityUtils.getSubject();

        //
        // Set shiro session
        Session session = currentSubject.getSession();
        session.setAttribute("scopeId", kapuaAuthenticationInfo.getUser().getScopeId());
        session.setAttribute("userId", kapuaAuthenticationInfo.getUser().getId());

        if (info instanceof SessionAuthenticationInfo) {
            SessionAuthenticationInfo sessionAuthenticationInfo = (SessionAuthenticationInfo) info;

            //
            // Set kapua session
            AccessToken accessToken = sessionAuthenticationInfo.getAccessToken();
            KapuaSession kapuaSession = new KapuaSession(accessToken, accessToken.getScopeId(), accessToken.getUserId());
            KapuaSecurityUtils.setSession(kapuaSession);
            session.setAttribute(KapuaSession.KAPUA_SESSION_KEY, kapuaSession);
        }
    }

    protected abstract KapuaAuthenticationInfo buildAuthenticationInfo(AuthenticationToken authenticationToken);
}
