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
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;
import org.eclipse.kapua.service.authentication.token.AccessToken;

/**
 * {@link AccessTokenCredentials} credential matcher implementation
 * 
 * @since 1.0
 * 
 */
public class AccessTokenCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //
        // Token data
        AccessTokenCredentialsImpl tokenApiKey = (AccessTokenCredentialsImpl) authenticationToken.getCredentials();
        String tokenTokenId = tokenApiKey.getTokenId();

        //
        // Info data
        SessionAuthenticationInfo info = (SessionAuthenticationInfo) authenticationInfo;
        AccessToken infoCredential = (AccessToken) info.getCredentials();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (infoCredential.getTokenId().equals(tokenTokenId)) {
            credentialMatch = true;

            // FIXME: if true cache token password for authentication performance improvement
        }

        return credentialMatch;
    }
}
