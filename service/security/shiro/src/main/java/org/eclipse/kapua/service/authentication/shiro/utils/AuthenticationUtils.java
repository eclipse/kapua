/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaCryptoSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaCryptoSettingKeys;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Authentication utilities.
 * 
 * @since 1.0
 * 
 */
public class AuthenticationUtils {

    private AuthenticationUtils() {
    }

    /**
     * Encrypts and return the plain credential value (unencrypted value).
     * 
     * @param plainValue
     * @return
     * @throws KapuaException
     */
    public static String cryptCredential(CryptAlgorithm algorithm, String plainValue)
            throws KapuaException {
        //
        // Argument validator
        ArgumentValidator.notEmptyOrNull(plainValue, "plainValue");

        //
        // Do crypt
        String cryptedValue;
        switch (algorithm) {
        case BCRYPT:
            cryptedValue = doBCrypt(plainValue);
            break;
        case SHA:
            cryptedValue = doSha(plainValue);
            break;
        default:
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, null, (Object[]) null);
        }

        return cryptedValue;
    }

    private static String doSha(String plainValue) {
        try {
            //
            // Retrieve Crypt Settings
            KapuaCryptoSetting settings = KapuaCryptoSetting.getInstance();
            int saltLength = settings.getInt(KapuaCryptoSettingKeys.CRYPTO_SHA_SALT_LENGTH);
            String shaAlgorithm = settings.getString(KapuaCryptoSettingKeys.CRYPTO_SHA_ALGORITHM);

            //
            // Generate salt
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bSalt = new byte[saltLength];
            random.nextBytes(bSalt);
            String salt = Base64.encodeToString(bSalt);

            //
            // Hash value
            String hashedValue;
            switch (shaAlgorithm) {
            case "SHA256":
                hashedValue = (new Sha256Hash(plainValue, salt)).toHex();
                break;
            case "SHA512":
            default:
                hashedValue = (new Sha512Hash(plainValue, salt)).toHex();
                break;
            }

            //
            // Return value
            return salt + ":" + hashedValue;
        } catch (NoSuchAlgorithmException e) {
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, e);
        }
    }

    private static String doBCrypt(String plainValue) {
        try {
            //
            // Retrieve Crypt Settings
            KapuaCryptoSetting settings = KapuaCryptoSetting.getInstance();
            int logRound = settings.getInt(KapuaCryptoSettingKeys.CRYPTO_BCRYPT_LOG_ROUNDS);

            //
            // Generate salt
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            String salt = BCrypt.gensalt(logRound, random);

            //
            // Hash and return value
            return BCrypt.hashpw(plainValue, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, e, (Object[]) null);
        }
    }
}
