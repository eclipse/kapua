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
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessToken;

import com.auth0.jwt.JWTVerifier;

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
        String tokenTokenId = (String) authenticationToken.getCredentials();

        //
        // Info data
        SessionAuthenticationInfo info = (SessionAuthenticationInfo) authenticationInfo;
        AccessToken infoCredential = info.getAccessToken();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (tokenTokenId.equals(infoCredential.getTokenId())) {
            KapuaAuthenticationSetting settings = KapuaAuthenticationSetting.getInstance();
            String secret = settings.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_TOKEN_JWT_SECRET);
            try {
                final JWTVerifier verifier = new JWTVerifier(secret);
                verifier.verify(tokenTokenId);

                credentialMatch = true;

                // FIXME: if true cache token password for authentication performance improvement
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return credentialMatch;
    }
}
