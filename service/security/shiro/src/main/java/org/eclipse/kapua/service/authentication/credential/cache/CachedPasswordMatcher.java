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

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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
    private static final String HMAC_SHA512 = "HmacSHA512";

    private SecureRandom random;
    private Mac sha512Hmac;

    public CachedPasswordMatcher() throws NoSuchAlgorithmException, InvalidKeyException {
        //session key. before modifying this code be sure this key, once generated, will not be changed at runtime
        random = SecureRandom.getInstance("SHA1PRNG");
        byte[] keyBytes = new byte[64];
        random.nextBytes(keyBytes);
        sha512Hmac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, HMAC_SHA512);
        sha512Hmac.init(keySpec);
    }

    public boolean checkPassword(String tokenUsername, String tokenPassword, Credential infoCredential) {
        CachedCredential cachedCredential = CACHED_CREDENTIALS.get(tokenUsername);
        if (cachedCredential!=null && 
                cachedCredential.isStillValid(infoCredential.getModifiedOn()) &&
                cachedCredential.isTokenMatches(encodePassword(tokenPassword), infoCredential.getCredentialKey())) {
            return true;
        }
        else if (BCrypt.checkpw(tokenPassword, infoCredential.getCredentialKey())) {
            //should be synchronized?
            CACHED_CREDENTIALS.put(tokenUsername, new CachedCredential(
                infoCredential.getModifiedOn(),
                encodePassword(tokenPassword),
                infoCredential.getCredentialKey()));
            return true;
        }
        else {
            CACHED_CREDENTIALS.remove(tokenUsername);
            return false;
        }
    }

    private String encodePassword(String password) {
        return Base64.getEncoder().encodeToString(sha512Hmac.doFinal(password.getBytes(StandardCharsets.UTF_8)));
    }

}