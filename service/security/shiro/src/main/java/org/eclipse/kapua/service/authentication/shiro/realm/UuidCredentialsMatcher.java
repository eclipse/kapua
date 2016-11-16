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
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;

/**
 * BCrypt credential matcher implementation
 * 
 * òsince 1.0
 * 
 */
public class UuidCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //
        // Token data
        AccessTokenCredentialsImpl token = (AccessTokenCredentialsImpl) authenticationToken;
        String tokenTokenId = token.getTokenId();

        //
        // Info data
        SimpleAuthenticationInfo info = (SimpleAuthenticationInfo) authenticationInfo;
        String infoTokenId = (String) info.getPrincipals().getPrimaryPrincipal();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (tokenTokenId.equals(infoTokenId)) {
            credentialMatch = true;
            // FIXME: if true cache token password for authentication performance improvement
        }

        return credentialMatch;
    }

}
