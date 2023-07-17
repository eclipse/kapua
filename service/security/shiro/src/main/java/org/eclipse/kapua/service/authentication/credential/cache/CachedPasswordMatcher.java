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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.Cache;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class CachedPasswordMatcher implements PasswordMatcher {

    protected static final Logger logger = LoggerFactory.getLogger(CachedPasswordMatcher.class);

    private static final Cache<String, CachedCredential> CACHED_CREDENTIALS = new LocalCache<String, CachedCredential>(
            KapuaAuthenticationSetting.getInstance().getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_SIZE, 1000),
            KapuaAuthenticationSetting.getInstance().getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_TTL, 60),
            null);

    //TODO inject!!!
    private CacheMetric cacheMetric;
    private SecretKey secret;
    private byte[] salt;
    private byte[] iv;
    private int saltIvLength;

    public CachedPasswordMatcher() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        cacheMetric = CacheMetric.getInstance();
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] passwordBytes = new byte[64];
        random.nextBytes(passwordBytes);
        String password = new String(passwordBytes, "UTF-8");
        salt = new byte[32];
        random.nextBytes(salt);
        iv = new byte[16];
        random.nextBytes(iv);
        saltIvLength = iv.length + salt.length;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        int iterationCount = 65536;
        int keyLength = 256;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
        //session key. before modifying this code be sure this key, once generated, will not be changed at runtime
        secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public boolean checkPassword(String tokenUsername, String tokenPassword, Credential infoCredential) {
        CachedCredential cachedCredential = CACHED_CREDENTIALS.get(tokenUsername);
        try {
            checkFromCache(cachedCredential, infoCredential, tokenPassword);
            cacheMetric.getCacheHit().inc();
            return true;
        }
        catch (Exception e) {
            if (BCrypt.checkpw(tokenPassword, infoCredential.getCredentialKey())) {
                //should be synchronized?
                try {
                    CACHED_CREDENTIALS.put(tokenUsername, new CachedCredential(
                        infoCredential.getModifiedOn(),
                        encodeText(tokenPassword.getBytes()),
                        infoCredential.getCredentialKey()));
                } catch (KapuaException ke) {
                    //cannot cache password so no problem, we can return true (since password is matching) and ignore the error
                    cacheMetric.getCachePutError().inc();
                }
                cacheMetric.getCacheMiss().inc();
                return true;
            }
        }
        CACHED_CREDENTIALS.remove(tokenUsername);
        return false;
    }

    private void checkFromCache(CachedCredential cachedCredential, Credential infoCredential, String tokenPassword) throws KapuaException {
        if (cachedCredential==null ||
                !cachedCredential.isStillValid(infoCredential.getModifiedOn()) ||
                !cachedCredential.isTokenMatches(encodeText(tokenPassword.getBytes()), infoCredential.getCredentialKey())) {
            //not the proper exception btw
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.AUTHENTICATION_ERROR);
        }
    }

    private String encodeText(byte[] text) throws KapuaException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));
            byte[] cipherText = cipher.doFinal(text);
            byte[] cipherTextWithIvSalt = ByteBuffer.allocate(saltIvLength + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();
            return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
        }
        catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            cacheMetric.getPasswordEncryptionError().inc();
            throw KapuaException.internalError(e);
        }
    }

}