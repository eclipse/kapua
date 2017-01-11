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
        String tokenApiFullKey = (String) authenticationToken.getCredentials();

        //
        // Info data
        LoginAuthenticationInfo info = (LoginAuthenticationInfo) authenticationInfo;
        Credential infoCredential = (Credential) info.getCredentials();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (CredentialType.API_KEY.equals(infoCredential.getCredentialType())) {

            KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();

            int keyLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_KEY_LENGTH);
            String tokenKey = tokenApiFullKey.substring(0, keyLength);
            String tokenSecret = tokenApiFullKey.substring(keyLength, tokenApiFullKey.length());

            String infoKey = infoCredential.getKey();
            String infoHashedSecret = infoCredential.getSecret();

            if (tokenKey.equals(infoKey) && BCrypt.checkpw(tokenSecret, infoHashedSecret)) {
                credentialMatch = true;

                // FIXME: if true cache token password for authentication performance improvement
            }
        }

        return credentialMatch;
    }
}
