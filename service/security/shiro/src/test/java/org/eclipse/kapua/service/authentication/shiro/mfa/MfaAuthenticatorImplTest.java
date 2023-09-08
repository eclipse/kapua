/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.mfa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaCryptoSetting;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Category(JUnitTests.class)
public class MfaAuthenticatorImplTest {

    MfaAuthenticatorImpl mfaAuthenticatorImpl;
    String[] encryptedSecrets, hashedScratchCodes, stringVerificationCodes;
    int[] verificationCodes;

    @Before
    public void initialize() throws KapuaException {
        mfaAuthenticatorImpl = new MfaAuthenticatorImpl(new KapuaAuthenticationSetting());

        final SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, e);
        }
        AuthenticationUtils authenticationUtils = new AuthenticationUtils(random, new KapuaCryptoSetting());
        encryptedSecrets = new String[]{
                authenticationUtils.encryptAes("value to encrypt"),
                authenticationUtils.encryptAes("value@#$ en-999crypt"),
                authenticationUtils.encryptAes("!<>v87a-lue to encrypt"),
                authenticationUtils.encryptAes("value_to$#encr-0y()pt"),
                authenticationUtils.encryptAes("va09l-ue|,,,.to00encrypt")
        };

        verificationCodes = new int[]{
                -2147483648,
                -100000,
                -100,
                -1,
                0,
                1,
                100,
                100000,
                2147483647
        };

        hashedScratchCodes = new String[]{
                authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "val-ue99_<11>"),
                authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "   !@#$v66a0l-ueee"),
                authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "val  *&^%087,...ueee   "),
                authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "_877V.A;;LUE")
        };

        stringVerificationCodes = new String[]{
                "0",
                " 1",
                "100",
                "100000",
                "2147483647"
        };
    }

    @Test
    public void isEnabledTest() {
        Assert.assertTrue("True expected.", mfaAuthenticatorImpl.isEnabled());
    }

    @Test
    public void authorizeEncryptedSecretVerificationCodeParametersTest() throws KapuaException {
        for (String encryptedSecret : encryptedSecrets) {
            for (int verificationCode : verificationCodes) {
                if (verificationCode >= 0) {
                    Assert.assertFalse(mfaAuthenticatorImpl.authorize(encryptedSecret, verificationCode));
                } else {
                    try {
                        mfaAuthenticatorImpl.authorize(encryptedSecret, verificationCode);

                        Assert.fail("This should have thrown KapuaIllegalArgumentException");
                    } catch (KapuaIllegalArgumentException e) {
                        Assert.assertEquals("verificationCode", e.getArgumentName());
                        Assert.assertNull(e.getArgumentValue());
                    }
                }
            }
        }
    }

    @Test
    public void authorizeNullVerificationCodeParametersTest() throws KapuaException {
        try {
            mfaAuthenticatorImpl.authorize(encryptedSecrets[0], null);

            Assert.fail("This should have thrown KapuaIllegalNullArgumentException");
        } catch (KapuaIllegalNullArgumentException e) {
            Assert.assertEquals("verificationCode", e.getArgumentName());
            Assert.assertNull(e.getArgumentValue());
        }
    }

    @Test
    public void authorizeNullEncryptedSecretVerificationCodeParametersTest() throws KapuaException {
        try {
            mfaAuthenticatorImpl.authorize(null, "123456");

            Assert.fail("This should have thrown KapuaIllegalNullArgumentException");
        } catch (KapuaIllegalNullArgumentException e) {
            Assert.assertEquals("hashedScratchCode", e.getArgumentName());
            Assert.assertNull(e.getArgumentValue());
        }
    }

    @Test
    public void authorizeHashedScratchCodeVerificationCodeParametersFalseTest() throws KapuaException {
        for (String hashedScratchCode : hashedScratchCodes) {
            for (String stringVerificationCode : stringVerificationCodes) {
                Assert.assertFalse(mfaAuthenticatorImpl.authorize(hashedScratchCode, stringVerificationCode));
            }
        }
    }

    @Test
    public void authorizeHashedScratchCodeVerificationCodeParametersTrueTest() throws KapuaException {
        Assert.assertTrue(mfaAuthenticatorImpl.authorize("$2a$12$2AZYOAvilJyNvG8b6rBDaOSIcM3mKc6iyNQUYIXOF4ZFEAYdzM7Jm", "plainValue"));
    }

    @Test
    public void generateKeyTest() {
        String generatedKey = mfaAuthenticatorImpl.generateKey();

        Assert.assertNotNull(generatedKey);
        Assert.assertEquals(32, generatedKey.length());
    }

    @Test
    public void generateCodesTest() {
        List<String> generatedScratchCodes = mfaAuthenticatorImpl.generateCodes();

        Assert.assertNotNull(generatedScratchCodes);
        Assert.assertEquals(5, generatedScratchCodes.size());
        for (String generatedScratchCode : generatedScratchCodes) {
            Assert.assertNotNull(generatedScratchCode);
        }
    }
}
