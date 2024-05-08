/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.utils;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.lang.codec.Base64;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.crypto.CryptoUtil;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaCryptoSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaCryptoSettingKeys;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Authentication utilities.
 *
 * @since 1.0
 */
public class AuthenticationUtils {

    private static final String CIPHER_ALGORITHM = "AES";
    //thread safe
    //consider using ThreadLocalRandom for performance reason. But it's not immediate to understand which option is the best one.
    private final SecureRandom random;
    private final KapuaCryptoSetting kapuaCryptoSetting;

    public AuthenticationUtils(SecureRandom random, KapuaCryptoSetting kapuaCryptoSetting) {
        this.random = random;
        this.kapuaCryptoSetting = kapuaCryptoSetting;
    }

    /**
     * Encrypts and return the plain credential value (unencrypted value).
     *
     * @param plainValue
     * @return the encrypted credential
     * @throws KapuaException when something goes wrong
     */
    public String cryptCredential(CryptAlgorithm algorithm, String plainValue)
            throws KapuaException {
        // Argument validator
        ArgumentValidator.notEmptyOrNull(plainValue, "plainValue");
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

    public String doSha(String plainValue) {
        int saltLength = kapuaCryptoSetting.getInt(KapuaCryptoSettingKeys.CRYPTO_SHA_SALT_LENGTH);
        String shaAlgorithm = kapuaCryptoSetting.getString(KapuaCryptoSettingKeys.CRYPTO_SHA_ALGORITHM);
        byte[] bSalt = new byte[saltLength];
        random.nextBytes(bSalt);
        String salt = Base64.encodeToString(bSalt);
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
        return salt + ":" + hashedValue;
    }

    private String doBCrypt(String plainValue) {
        int logRound = kapuaCryptoSetting.getInt(KapuaCryptoSettingKeys.CRYPTO_BCRYPT_LOG_ROUNDS);
        String salt = BCrypt.gensalt(logRound, random);
        return BCrypt.hashpw(plainValue, salt);
    }

    /**
     * Encrypt the given string using the AES algorithm provided by {@link Cipher}
     *
     * @param value the string to encrypt
     * @return the encrypted string
     * @deprecated Since 2.0.0. Please make use of {@link CryptoUtil#encryptAes(String)}.
     */
    @Deprecated
    public String encryptAes(String value) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = c.doFinal(value.getBytes());

            return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, e);
        }
    }

    /**
     * Decrypt the given string using the AES algorithm provided by {@link Cipher}
     *
     * @param encryptedValue the string to decrypt
     * @return the decrypted string
     * @deprecated Since 2.0.0. Please make use of {@link CryptoUtil#decryptAes(String)}.
     */
    @Deprecated
    public String decryptAes(String encryptedValue) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedValue = java.util.Base64.getUrlDecoder().decode(encryptedValue);
            byte[] decryptedBytes = c.doFinal(decodedValue);

            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, e);
        }
    }

    /**
     * Constructs a secret key from the given byte array from {@link KapuaCryptoSettingKeys#CIPHER_KEY}.
     *
     * @return a {@link SecretKeySpec} object
     * @deprecated Since 2.0.0. Please make use of {@link CryptoUtil}
     */
    @Deprecated
    private Key generateKey() {
        // Retrieve Cipher Settings
        byte[] cipherSecretKey = kapuaCryptoSetting.getString(KapuaCryptoSettingKeys.CIPHER_KEY).getBytes();
        return new SecretKeySpec(cipherSecretKey, CIPHER_ALGORITHM);
    }
}
