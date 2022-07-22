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
package org.eclipse.kapua.commons.crypto;

import org.eclipse.kapua.commons.crypto.exception.AesDecryptionException;
import org.eclipse.kapua.commons.crypto.exception.AesEncryptionException;
import org.eclipse.kapua.commons.crypto.exception.InvalidSecretKeyRuntimeException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.security.NoSuchAlgorithmException;

/**
 * Test for {@link CryptoUtil}.
 *
 * @since 2.0.0
 */
@Category(JUnitTests.class)
public class CryptoUtilTest extends Assert {

    private static final String PLAIN_VALUE = "aPlainValue";
    private static final String ALTERNATIVE_KEY = "alternativeKey!!";

    //
    // SHA1
    //

    @Test
    public void testSha1Hashing() throws NoSuchAlgorithmException {
        String hashedValue = CryptoUtil.sha1Hash(PLAIN_VALUE);

        assertNotNull(hashedValue);
        assertEquals("3VAfPtmZ+ldn8WsYl+hsDlITf+k=", hashedValue);
    }


    //
    // AES
    //

    @Test
    public void testAesCryptDecrypt() throws AesEncryptionException, AesDecryptionException {
        String encryptedValue = CryptoUtil.encryptAes(PLAIN_VALUE);

        assertNotNull(encryptedValue);
        assertEquals("AVopb0Rbmz9P3XAuWmp/mA==", encryptedValue);

        String decryptedValue = CryptoUtil.decryptAes(encryptedValue);
        assertNotNull(decryptedValue);
        assertEquals(PLAIN_VALUE, decryptedValue);
    }

    @Test
    public void testAesCryptDecryptAlternativeKey() throws AesEncryptionException, AesDecryptionException {
        String encryptedValue = CryptoUtil.encryptAes(PLAIN_VALUE, ALTERNATIVE_KEY);

        assertNotNull(encryptedValue);
        assertEquals("kYwVe4immFI/SuaSupaMxw==", encryptedValue);

        String decryptedValue = CryptoUtil.decryptAes(encryptedValue, ALTERNATIVE_KEY);
        assertNotNull(decryptedValue);
        assertEquals(PLAIN_VALUE, decryptedValue);
    }

    @Test
    public void testAesCryptDecryptDifferentKeys() throws AesEncryptionException, AesDecryptionException {
        String encryptedValue1 = CryptoUtil.encryptAes(PLAIN_VALUE);
        String encryptedValue2 = CryptoUtil.encryptAes(PLAIN_VALUE, ALTERNATIVE_KEY);

        assertNotNull(encryptedValue1);
        assertNotNull(encryptedValue2);
        assertNotEquals(encryptedValue1, encryptedValue2);

        String decryptedValue1 = CryptoUtil.decryptAes(encryptedValue1);
        String decryptedValue2 = CryptoUtil.decryptAes(encryptedValue2, ALTERNATIVE_KEY);

        assertNotNull(decryptedValue1);
        assertNotNull(decryptedValue2);
        assertEquals(decryptedValue1, decryptedValue2);
    }

    @Test(expected = InvalidSecretKeyRuntimeException.class)
    public void testAesEncryptInvalidAlternativeKey() throws AesEncryptionException {
        CryptoUtil.encryptAes(PLAIN_VALUE, "notAValidKey");
    }

    @Test(expected = InvalidSecretKeyRuntimeException.class)
    public void testAesDecryptInvalidAlternativeKey() throws AesDecryptionException {
        CryptoUtil.decryptAes(PLAIN_VALUE, "notAValidValue");
    }

    @Test(expected = AesDecryptionException.class)
    public void testAesDecryptInvalidRandomValue() throws AesDecryptionException {
        CryptoUtil.decryptAes("notAValidValue");
    }

    @Test(expected = AesDecryptionException.class)
    public void testAesDecryptInvalidEncryptedValue() throws AesDecryptionException {
        CryptoUtil.decryptAes("kYwVe4immFI/SuaSupaMxw==");
    }

    //
    // Base64
    //

    @Test
    public void testBase64EncodeDecode() {
        String encodedValue = CryptoUtil.encodeBase64(PLAIN_VALUE);

        assertNotNull(encodedValue);
        assertEquals("YVBsYWluVmFsdWU=", encodedValue);

        String decodedValue = CryptoUtil.decodeBase64(encodedValue);
        assertNotNull(decodedValue);
        assertEquals(PLAIN_VALUE, decodedValue);
    }
}
