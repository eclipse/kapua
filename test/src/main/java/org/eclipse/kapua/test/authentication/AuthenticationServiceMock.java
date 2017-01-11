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
package org.eclipse.kapua.test.authentication;

import java.math.BigInteger;
import java.util.Date;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;

@TestService
@KapuaProvider
public class AuthenticationServiceMock implements AuthenticationService {

    private AccessToken accessToken = null;

    public AuthenticationServiceMock() {
    }

    @Override
    public AccessToken login(LoginCredentials authenticationToken)
            throws KapuaException {
        if (!(authenticationToken instanceof UsernamePasswordCredentialsMock))
            throw KapuaException.internalError("Unmanaged credentials type");

        UsernamePasswordCredentialsMock usrPwdCredentialsMock = (UsernamePasswordCredentialsMock) authenticationToken;

        KapuaLocator serviceLocator = KapuaLocator.getInstance();
        AccessTokenCreator accessTokenCreator = new AccessTokenCreatorMock(new KapuaEid(BigInteger.ONE));
        accessTokenCreator.setSubjectType(usrPwdCredentialsMock.getSubjectType());
        accessTokenCreator.setSubjectId(new KapuaEid(BigInteger.ONE));
        accessTokenCreator.setTokenId("tokenId");
        accessTokenCreator.setExpiresOn(new Date(new Date().getTime() + 18000000));

        AccessTokenService accessTokenService = serviceLocator.getService(AccessTokenService.class);
        accessToken = accessTokenService.create(accessTokenCreator);

        KapuaSession kapuaSession = new KapuaSession(accessToken, accessToken.getScopeId(), accessToken.getSubject());
        KapuaSecurityUtils.setSession(kapuaSession);

        return accessToken;
    }

    @Override
    public void logout()
            throws KapuaException {
        if (accessToken != null) {
            KapuaLocator serviceLocator = KapuaLocator.getInstance();
            AccessTokenService accessTokenService = serviceLocator.getService(AccessTokenService.class);
            accessTokenService.invalidate(accessToken.getScopeId(), accessToken.getId());
            accessToken = null;
        }

        KapuaSecurityUtils.clearSession();
    }

    @Override
    public AccessToken findAccessToken(String tokenId)
            throws KapuaException {
        KapuaLocator serviceLocator = KapuaLocator.getInstance();
        AccessTokenService accessTokenService = serviceLocator.getService(AccessTokenService.class);
        return accessTokenService.findByTokenId(tokenId);
    }

    @Override
    public void authenticate(SessionCredentials sessionCredentials) throws KapuaException {
        if (!(sessionCredentials instanceof AccessTokenCredentialsMock))
            throw KapuaException.internalError("Unmanaged credentials type");

        AccessTokenCredentialsMock accessTokenCredentialsMock = (AccessTokenCredentialsMock) sessionCredentials;

        if (accessToken == null) {
            KapuaException.internalError("null Access Token in session!");
        }

        if (!accessToken.getTokenId().equals(accessTokenCredentialsMock.getTokenId())) {
            KapuaException.internalError("Invalid AccessToken provided: " + accessTokenCredentialsMock.getTokenId());
        }

    }

}
