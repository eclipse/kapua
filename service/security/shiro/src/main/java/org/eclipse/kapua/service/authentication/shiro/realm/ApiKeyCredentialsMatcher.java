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
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.ApiKeyCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * {@link UsernamePasswordCredentials} credential matcher implementation
 * 
 * @since 1.0
 * 
 */
public class ApiKeyCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //
        // Token data
        ApiKeyCredentialsImpl tokenApiKey = (ApiKeyCredentialsImpl) authenticationToken.getCredentials();
        String tokenApiFullKey = tokenApiKey.getApiKey();

        //
        // Info data
        LoginAuthenticationInfo info = (LoginAuthenticationInfo) authenticationInfo;
        Credential infoCredential = (Credential) info.getCredentials();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (CredentialType.API_KEY.equals(infoCredential.getCredentialType())) {
            String fullApiKey = infoCredential.getCredentialKey();

            KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();

            int preLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_APIKEY_PRE_LENGTH);
            String tokenPre = tokenApiFullKey.substring(0, preLength);
            String tokenKey = tokenApiFullKey.substring(preLength, tokenApiFullKey.length() - 1);

            String preSeparator = setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_APIKEY_PRE_SEPARATOR);
            String infoPre = fullApiKey.split(preSeparator)[0];
            String infoHashedKey = fullApiKey.split(preSeparator)[1];

            if (tokenPre.equals(infoPre) && BCrypt.checkpw(tokenKey, infoHashedKey)) {
                credentialMatch = true;

                // FIXME: if true cache token password for authentication performance improvement
            }
        }

        return credentialMatch;
    }
}
