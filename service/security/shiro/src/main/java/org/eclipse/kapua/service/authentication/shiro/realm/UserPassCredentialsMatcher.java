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
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * {@link ApiKeyCredentials} credential matcher implementation
 * 
 * @since 1.0
 * 
 */
public class UserPassCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //
        // Token data
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String tokenUsername = token.getUsername();
        String tokenPassword = new String(token.getPassword());

        //
        // Info data
        SimpleAuthenticationInfo info = (SimpleAuthenticationInfo) authenticationInfo;
        String infoUsername = (String) info.getPrincipals().getPrimaryPrincipal();
        String infoPassword = (String) info.getCredentials();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (tokenUsername.equals(infoUsername) && BCrypt.checkpw(tokenPassword, infoPassword)) {
            credentialMatch = true;

            // FIXME: if true cache token password for authentication performance improvement
        }
        return credentialMatch;
    }

}
