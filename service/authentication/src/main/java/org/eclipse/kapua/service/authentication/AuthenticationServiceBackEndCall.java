/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication;

import org.apache.shiro.util.ThreadContext;

import javax.inject.Inject;

import org.eclipse.kapua.client.security.ServiceClient.ResultCode;
import org.eclipse.kapua.client.security.bean.AccountRequest;
import org.eclipse.kapua.client.security.bean.AccountResponse;
import org.eclipse.kapua.client.security.bean.AuthContext;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.client.security.bean.AuthResponse;
import org.eclipse.kapua.client.security.metric.LoginMetric;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.authentication.Authenticator;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer.Context;

public class AuthenticationServiceBackEndCall {

    protected static Logger logger = LoggerFactory.getLogger(AuthenticationServiceBackEndCall.class);

    private LoginMetric loginMetric = LoginMetric.getInstance();

    @Inject
    private Authenticator authenticator;

    private KapuaLocator locator;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private CredentialsFactory credentialFactory;
    private UserService userService;

    public AuthenticationServiceBackEndCall() {
        locator = KapuaLocator.getInstance();
        authenticationService = locator.getService(AuthenticationService.class);
        accountService = locator.getService(AccountService.class);
        credentialFactory = locator.getFactory(CredentialsFactory.class);
        userService = locator.getService(UserService.class);
    }

    public AuthResponse brokerConnect(AuthRequest authRequest) {
        try {
            logger.info("Login for user: {} - clientId: {}", authRequest.getUsername(), authRequest.getClientId());
            ThreadContext.unbindSubject();
            UsernamePasswordCredentials credentials = credentialFactory.newUsernamePasswordCredentials(
                authRequest.getUsername(), 
                authRequest.getPassword());
            AccessToken accessToken = authenticationService.login(credentials);
            Account account = KapuaSecurityUtils.doPrivileged(()-> accountService.find(accessToken.getScopeId()));
            AuthResponse authResponse = buildLoginResponseAuthorized(authRequest, accessToken, account);
            AuthContext authContext = new AuthContext(authRequest, authResponse);
            authResponse.setAcls(authenticator.connect(authContext));
            authResponse.update(authContext);
            return authResponse;
        } catch (Exception e) {
            //TODO add metric
            logger.warn("Login error: {}", e.getMessage(), e);
            return buildLoginResponseNotAuthorized(authRequest);
        }
        finally {
            Context loginShiroLogoutTimeContext = loginMetric.getShiroLogoutTime().time();
            try {
                authenticationService.logout();
            } catch (Exception e) {
                //error while cleaning up the logged user
                //do nothing
                //TODO add metric?
                logger.warn("Logout error: {}", e.getMessage(), e);
            }
            loginShiroLogoutTimeContext.stop();
            ThreadContext.unbindSubject();
        }
    }

    public AuthResponse brokerDisconnect(AuthRequest authRequest) {
        try {
            logger.info("Logout for user: {} - clientId: {}", authRequest.getUsername(), authRequest.getClientId());
            authenticator.disconnect(new AuthContext(authRequest));
            return buildLogoutResponse(authRequest, ResultCode.authorized);
        } catch (Exception e) {
            //TODO add metric
            logger.warn("Login error: {}", e.getMessage(), e);
            return buildLogoutResponseNotAuthorized(authRequest);
        }
    }

    public AccountResponse getAccount(AccountRequest accountRequest) {
        logger.info("Get account for user: {}", accountRequest.getUsername());
        try {
            User user = KapuaSecurityUtils.doPrivileged(()-> userService.findByName(accountRequest.getUsername()));
            return buildGetAccountResponseAuthorized(accountRequest, user);
        } catch (Exception e) {
            return buildGetAccountResponseNotAuthorized(accountRequest, e);
        }
    }

    private AuthResponse buildLoginResponseAuthorized(AuthRequest authRequest, AccessToken accessToken, Account account) {
        AuthResponse authResponse = buildAuthResponse(authRequest, ResultCode.authorized);
        //if authorized should be not null, anyway do a check
        if (accessToken!=null) {
            authResponse.setScopeId(accessToken.getScopeId().toCompactId());
            authResponse.setUserId(accessToken.getUserId().toCompactId());
            authResponse.setAccessTokenId(accessToken.getTokenId());
        }
        //if authorized should be not null, anyway do a check
        if (account!=null) {
            authResponse.setAccountName(account.getName());
        }
        return authResponse;
    }

    private AuthResponse buildLoginResponseNotAuthorized(AuthRequest authRequest) {
        return buildAuthResponse(authRequest, ResultCode.notAuthorized);
    }

    private AuthResponse buildLogoutResponse(AuthRequest authRequest, ResultCode resultCode) {
        return buildAuthResponse(authRequest, resultCode);
    }

    private AuthResponse buildLogoutResponseNotAuthorized(AuthRequest authRequest) {
        return buildAuthResponse(authRequest, ResultCode.notAuthorized);
    }

    private AuthResponse buildAuthResponse(AuthRequest authRequest, ResultCode resultCode) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAction(authRequest.getAction());
        authResponse.setRequestId(authRequest.getRequestId());
        authResponse.setResultCode(resultCode.name());
        authResponse.setUsername(authRequest.getUsername());
        authResponse.setClientId(authRequest.getClientId());
        authResponse.setClientIp(authRequest.getClientIp());
        return authResponse;
    }

    private AccountResponse buildGetAccountResponseAuthorized(AccountRequest accountRequest, User user) {
        AccountResponse accountResponse = buildGetAccountResponse(accountRequest, ResultCode.authorized);
        accountResponse.setScopeId(user.getScopeId().toCompactId());
        return accountResponse;
    }

    private AccountResponse buildGetAccountResponseNotAuthorized(AccountRequest accountRequest, Exception exception) {
        AccountResponse accountResponse = buildGetAccountResponse(accountRequest, ResultCode.notAuthorized);
        accountResponse.setErrorCode(exception.getMessage());
        return accountResponse;
    }

    private AccountResponse buildGetAccountResponse(AccountRequest accountRequest, ResultCode resultCode) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAction(accountRequest.getAction());
        accountResponse.setRequestId(accountRequest.getRequestId());
        accountResponse.setUsername(accountRequest.getUsername());
        accountResponse.setResultCode(resultCode.name());
        return accountResponse;
    }
}
