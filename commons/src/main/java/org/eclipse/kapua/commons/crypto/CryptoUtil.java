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

import org.eclipse.kapua.commons.crypto.exception.AesDecryptionException;
import org.eclipse.kapua.commons.crypto.exception.AesEncryptionException;
import org.eclipse.kapua.commons.crypto.setting.CryptoSettingKeys;

import javax.crypto.Cipher;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public interface CryptoUtil {
    /**
     * Evaluates the SHA-1 hash for the provided String
     *
     * @param value The {@link String} to process.
     * @return The SHA-1 digest of the given value, in Base64 format.
     * @throws NoSuchAlgorithmException If {@code SHA-1} algorithm is not available. See {@link MessageDigest#getAlgorithm()} documentation
     * @see MessageDigest
     * @since 1.0.0
     */
    String sha1Hash(@NotNull String value) throws NoSuchAlgorithmException;

    /**
     * Decrypts an AES-encrypted value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to decrypt the value.
     *
     * @param value The value to decrypt.
     * @return The decrypted value.
     * @throws AesDecryptionException When an error occurs when decrypting.
     * @see CryptoUtilImpl#decryptAes(String, Cipher)
     * @since 2.0.0
     */
    String decryptAes(@NotNull String value) throws AesDecryptionException;

    /**
     * Decrpts an AES-encrypted value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to decrypt the value.
     *
     * @param value                The value to decrypt.
     * @param alternativeSecretKey An externally provided secret key.
     * @return The decrypted value.
     * @throws AesDecryptionException When an error occurs when decrypting.
     * @see CryptoUtilImpl#decryptAes(String, Cipher)
     * @since 2.0.0
     */
    String decryptAes(@NotNull String value, @NotNull String alternativeSecretKey) throws AesDecryptionException;

    /**
     * Encrypts a value. It uses {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} to encrypt the value.
     *
     * @param value The value to encrypt.
     * @return The encrypted value.
     * @throws AesEncryptionException When an error occurs when encrypting.
     * @see Cipher#doFinal()
     * @since 2.0.0
     */
    String encryptAes(@NotNull String value) throws AesEncryptionException;

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
    String encryptAes(@NotNull String value, @NotNull String alternativeSecretKey) throws AesEncryptionException;

    /**
     * Decodes the given Base64 {@link String} value in a {@link String}.
     *
     * @param value The {@link Base64} encoded {@link String} to process.
     * @return The decoded {@link String} value.
     * @since 1.0.0
     */
    String decodeBase64(@NotNull String value);

    /**
     * Encodes the given {@link String} value in a {@link Base64} format.
     *
     * @param value The {@link String} to process.
     * @return The encoded {@link Base64} value.
     * @since 1.0.0
     */
    String encodeBase64(@NotNull String value);
}
