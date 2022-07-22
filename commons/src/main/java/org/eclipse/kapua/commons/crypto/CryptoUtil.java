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
package org.eclipse.kapua.commons.crypto;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.crypto.exception.AesDecryptionException;
import org.eclipse.kapua.commons.crypto.exception.AesEncryptionException;
import org.eclipse.kapua.commons.crypto.exception.AlgorihmNotAvailableRuntimeException;
import org.eclipse.kapua.commons.crypto.exception.DefaultSecretKeyDetectedRuntimeException;
import org.eclipse.kapua.commons.crypto.exception.InvalidSecretKeyRuntimeException;
import org.eclipse.kapua.commons.crypto.setting.CryptoSettingKeys;
import org.eclipse.kapua.commons.crypto.setting.CryptoSettings;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilities to crypt and decrypt values, and related tasks.
 *
 * @since 1.0.0
 */
public class CryptoUtil {

    private static final CryptoSettings CRYPTO_SETTINGS = CryptoSettings.getInstance();

    private static final String SECRET_KEY = CRYPTO_SETTINGS.getString(CryptoSettingKeys.CRYPTO_SECRET_KEY);
    private static final String SECRET_KEY_LEGACY = CRYPTO_SETTINGS.getString(CryptoSettingKeys.CIPHER_KEY);
    private static final Boolean SECRET_KEY_ENFORCE = CRYPTO_SETTINGS.getBoolean(CryptoSettingKeys.CRYPTO_SECRET_ENFORCE_CHANGE);

    private static final String AES_ALGORITHM = "AES";
    private static final Cipher DEFAULT_AES_CIPHER_DECRYPT;
    private static final Cipher DEFAULT_AES_CIPHER_ENCRYPT;

    private static final Map<String, Cipher> ALTERNATIVES_AES_CIPHERS_DECRYPT = new HashMap<>();
    private static final Map<String, Cipher> ALTERNATIVES_AES_CIPHERS_ENCRYPT = new HashMap<>();

    static {
        String defaultSecretKey = SECRET_KEY_LEGACY;

        if (!Strings.isNullOrEmpty(SECRET_KEY)) {
            defaultSecretKey = SECRET_KEY;
        }

        if (("changeMePlease!!".equals(defaultSecretKey) ||
                "rv;ipse329183!@#".equals(defaultSecretKey)) &&
                SECRET_KEY_ENFORCE) {
            throw new DefaultSecretKeyDetectedRuntimeException(defaultSecretKey);
        }

        Key aesKey = new SecretKeySpec(defaultSecretKey.getBytes(), AES_ALGORITHM);

        try {
            DEFAULT_AES_CIPHER_DECRYPT = Cipher.getInstance(AES_ALGORITHM);
            DEFAULT_AES_CIPHER_DECRYPT.init(Cipher.DECRYPT_MODE, aesKey);

            DEFAULT_AES_CIPHER_ENCRYPT = Cipher.getInstance(AES_ALGORITHM);
            DEFAULT_AES_CIPHER_ENCRYPT.init(Cipher.ENCRYPT_MODE, aesKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AlgorihmNotAvailableRuntimeException(e, AES_ALGORITHM);
        } catch (InvalidKeyException e) {
            throw new InvalidSecretKeyRuntimeException(e, AES_ALGORITHM, defaultSecretKey);
        }
    }

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private CryptoUtil() {
    }

    //
    // SHA-1
    //

    /**
     * Evaluates the SHA-1 hash for the provided String
     *
     * @param value The {@link String} to process.
     * @return The SHA-1 digest of the given value, in Base64 format.
     * @throws NoSuchAlgorithmException If {@code SHA-1} algorithm is not available. See {@link MessageDigest#getAlgorithm()} documentation
     * @see MessageDigest
     * @since 1.0.0
     */
    public static String sha1Hash(@NotNull String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] encodedBytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printBase64Binary(encodedBytes);
    }

    //
    // AES
    //

    /**
     * Decrypts an AES-encrypted value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to decrypt the value.
     *
     * @param value The value to decrypt.
     * @return The decrypted value.
     * @throws AesDecryptionException When an error occurs when decrypting.
     * @see #decryptAes(String, Cipher)
     * @since 2.0.0
     */
    public static String decryptAes(@NotNull String value) throws AesDecryptionException {
        return decryptAes(value, DEFAULT_AES_CIPHER_DECRYPT);
    }

    /**
     * Decrpts an AES-encrypted value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to decrypt the value.
     *
     * @param value                The value to decrypt.
     * @param alternativeSecretKey An externally provided secret key.
     * @return The decrypted value.
     * @throws AesDecryptionException When an error occurs when decrypting.
     * @see #decryptAes(String, Cipher)
     * @since 2.0.0
     */
    public static String decryptAes(@NotNull String value, @NotNull String alternativeSecretKey) throws AesDecryptionException {
        Cipher decryptCipher = getDecryptCipherForKey(alternativeSecretKey);

        return decryptAes(value, decryptCipher);
    }

    /**
     * Encrypts a value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to encrypt the value.
     *
     * @param value The value to encrypt.
     * @return The encrypted value.
     * @throws AesEncryptionException When an error occurs when encrypting.
     * @see Cipher#doFinal()
     * @since 2.0.0
     */
    public static String encryptAes(@NotNull String value) throws AesEncryptionException {
        return encryptAes(value, DEFAULT_AES_CIPHER_ENCRYPT);
    }

    /**
     * Encrypts a value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to encrypt the value.
     *
     * @param value                The value to encrypt.
     * @param alternativeSecretKey An externally provided secret key.
     * @return The encrypted value.
     * @throws AesEncryptionException When an error occurs when encrypting.
     * @see Cipher#doFinal()
     * @since 2.0.0
     */
    public static String encryptAes(@NotNull String value, @NotNull String alternativeSecretKey) throws AesEncryptionException {
        Cipher encryptCipher = getEncryptCipherForKey(alternativeSecretKey);

        return encryptAes(value, encryptCipher);
    }


    //
    // Base 64
    //

    /**
     * Decodes the given Base64 {@link String} value in a {@link String}.
     *
     * @param value The {@link Base64} encoded {@link String} to process.
     * @return The decoded {@link String} value.
     * @since 1.0.0
     */
    public static String decodeBase64(@NotNull String value) {
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(value);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Encodes the given {@link String} value in a {@link Base64} format.
     *
     * @param value The {@link String} to process.
     * @return The encoded {@link Base64} value.
     * @since 1.0.0
     */
    public static String encodeBase64(@NotNull String value) {
        byte[] bytesValue = value.getBytes(StandardCharsets.UTF_8);
        return DatatypeConverter.printBase64Binary(bytesValue);
    }

    //
    // Private Methods
    //

    /**
     * Decrypts an AES-encrypted value using the given {@link Cipher}
     *
     * @param value         The value to decrypt.
     * @param decryptCipher The {@link Cipher} to use to decrypt.
     * @return The decrypted value.
     * @throws AesDecryptionException When an error occurs when decrypting.
     * @see Cipher#doFinal(byte[])
     * @since 2.0.0
     */
    private static String decryptAes(@NotNull String value, @NotNull Cipher decryptCipher) throws AesDecryptionException {
        try {
            return new String(decryptCipher.doFinal(DatatypeConverter.parseBase64Binary(value)), StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new AesDecryptionException(e);
        }
    }

    /**
     * Encrypts a value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to encrypt the value.
     *
     * @param value         The value to encrypt.
     * @param encryptCipher The {@link Cipher} to use to encrypt.
     * @return The encrypted value.
     * @throws AesEncryptionException When an error occurs when encrypting.
     * @see Cipher#doFinal()
     * @since 2.0.0
     */
    private static String encryptAes(@NotNull String value, @NotNull Cipher encryptCipher) throws AesEncryptionException {
        try {
            return DatatypeConverter.printBase64Binary(encryptCipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new AesEncryptionException(e);
        }
    }

    /**
     * Gets correct {@link Cipher} for the given secret key.
     * <p>
     * If not present it will initialize a new {@link Cipher} with the given secret ket and any subsequent usage will get the initialized {@link Cipher}.
     *
     * @param alternativeSecretKey A secret key which is not the {@link CryptoSettingKeys#CRYPTO_SECRET_KEY}.
     * @return The {@link Cipher} for the given secret key.
     * @since 2.0.0
     */
    private static synchronized Cipher getDecryptCipherForKey(@NotNull String alternativeSecretKey) {
        return ALTERNATIVES_AES_CIPHERS_DECRYPT.computeIfAbsent(alternativeSecretKey, secretKey -> {
            try {
                Key key = new SecretKeySpec(secretKey.getBytes(), AES_ALGORITHM);

                Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);

                return cipher;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                throw new AlgorihmNotAvailableRuntimeException(e, AES_ALGORITHM);
            } catch (InvalidKeyException e) {
                throw new InvalidSecretKeyRuntimeException(e, AES_ALGORITHM, secretKey);
            }
        });
    }

    /**
     * Gets correct {@link Cipher} for the given secret key.
     * <p>
     * If not present it will initialize a new {@link Cipher} with the given secret ket and any subsequent usage will get the initialized {@link Cipher}.
     *
     * @param alternativeSecretKey A secret key which is not the {@link CryptoSettingKeys#CRYPTO_SECRET_KEY}.
     * @return The {@link Cipher} for the given secret key.
     * @since 2.0.0
     */
    private static synchronized Cipher getEncryptCipherForKey(@NotNull String alternativeSecretKey) {
        return ALTERNATIVES_AES_CIPHERS_ENCRYPT.computeIfAbsent(alternativeSecretKey, secretKey -> {
            try {
                Key key = new SecretKeySpec(secretKey.getBytes(), AES_ALGORITHM);

                Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, key);

                return cipher;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                throw new AlgorihmNotAvailableRuntimeException(e, AES_ALGORITHM);
            } catch (InvalidKeyException e) {
                throw new InvalidSecretKeyRuntimeException(e, AES_ALGORITHM, secretKey);
            }
        });
    }
}
