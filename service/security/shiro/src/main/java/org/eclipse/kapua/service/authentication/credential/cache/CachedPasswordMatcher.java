/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.cache;

import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.kapua.commons.cache.Cache;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class CachedPasswordMatcher implements PasswordMatcher {

    private static final Cache<String, CachedCredential> CACHED_CREDENTIALS = new LocalCache<String, CachedCredential>(
            KapuaAuthenticationSetting.getInstance().getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_SIZE, 1000),
            KapuaAuthenticationSetting.getInstance().getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_TTL, 60),
            null);

    public boolean checkPassword(String tokenUsername, String tokenPassword, Credential infoCredential) {
        CachedCredential cachedCredential = CACHED_CREDENTIALS.get(tokenUsername);
        if (cachedCredential!=null && 
                cachedCredential.isStillValid(infoCredential.getModifiedOn()) &&
                cachedCredential.isTokenMatches(tokenPassword, infoCredential.getCredentialKey())) {
            return true;
        }
        else if (BCrypt.checkpw(tokenPassword, infoCredential.getCredentialKey())) {
            //should be synchronized?
            CACHED_CREDENTIALS.put(tokenUsername, new CachedCredential(
                infoCredential.getModifiedOn(),
                Base64.getEncoder().encodeToString(DigestUtils.sha3_512(tokenPassword)),
                infoCredential.getCredentialKey()));
            return true;
        }
        else {
            CACHED_CREDENTIALS.remove(tokenUsername);
            return false;
        }
    }

}