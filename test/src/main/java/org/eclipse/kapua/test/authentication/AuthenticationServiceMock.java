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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

@TestService
@KapuaProvider
public class AuthenticationServiceMock implements AuthenticationService {

    public AuthenticationServiceMock() {
    }

    @Override
    public AccessToken login(LoginCredentials authenticationToken)
            throws KapuaException {
        if (!(authenticationToken instanceof UsernamePasswordCredentialsMock))
            throw KapuaException.internalError("Unmanaged credentials type");

        UsernamePasswordCredentialsMock usrPwdCredentialsMock = (UsernamePasswordCredentialsMock) authenticationToken;

        KapuaLocator serviceLocator = KapuaLocator.getInstance();
        UserService userService = serviceLocator.getService(UserService.class);
        User user = userService.findByName(usrPwdCredentialsMock.getUsername());

        KapuaSession kapuaSession = new KapuaSession(null, user.getScopeId(), user.getId());
        KapuaSecurityUtils.setSession(kapuaSession);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void logout()
            throws KapuaException {
        KapuaSecurityUtils.clearSession();
    }

    @Override
    public AccessToken findAccessToken(String tokenId)
            throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void authenticate(SessionCredentials sessionCredentials) throws KapuaException {
        // TODO Auto-generated method stub

    }

    @Override
    public AccessToken refreshAccessToken(String tokenId, String refreshToken) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

}
