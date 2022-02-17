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
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class MfaAuthenticatorImplTest {

    MfaAuthenticatorImpl mfaAuthenticatorImpl;
    String[] encryptedSecrets, hashedScratchCodes, stringVerificationCodes;
    int[] verificationCodes;

    @Before
    public void initialize() throws KapuaException {
        mfaAuthenticatorImpl = new MfaAuthenticatorImpl();
        encryptedSecrets = new String[]{AuthenticationUtils.encryptAes("value to encrypt"), AuthenticationUtils.encryptAes("value@#$ en-999crypt"), AuthenticationUtils.encryptAes("!<>v87a-lue to encrypt"),
                AuthenticationUtils.encryptAes("value_to$#encr-0y()pt"), AuthenticationUtils.encryptAes("va09l-ue|,,,.to00encrypt")};
        verificationCodes = new int[]{-2147483648, -100000, -100, -1, 0, 1, 100, 100000, 2147483647};
        hashedScratchCodes = new String[]{AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "val-ue99_<11>"), AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "   !@#$v66a0l-ueee"),
                AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "val  *&^%087,...ueee   "), AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "_877V.A;;LUE")};
        stringVerificationCodes = new String[]{"-2147483648", "-100000", "-100", "-1", "0", " 1", "100", "100000", "2147483647"};
    }

    @Test
    public void isEnabledTest() {
        Assert.assertTrue("True expected.", mfaAuthenticatorImpl.isEnabled());
    }

    @Test
    public void authorizeEncryptedSecretVerificationCodeParametersTest() {
        for (String encryptedSecret : encryptedSecrets) {
            for (int verificationCode : verificationCodes) {
                Assert.assertFalse("False expected.", mfaAuthenticatorImpl.authorize(encryptedSecret, verificationCode));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void authorizeNullEncryptedSecretVerificationCodeParametersTest() {
        for (int verificationCode : verificationCodes) {
            Assert.assertFalse("False expected.", mfaAuthenticatorImpl.authorize(null, verificationCode));
        }
    }

    @Test
    public void authorizeHasedScratchCodeVerificationCodeParametersFalseTest() {
        for (String hasedScratchCode : hashedScratchCodes) {
            for (String stringVerificationCode : stringVerificationCodes) {
                Assert.assertFalse("False expected.", mfaAuthenticatorImpl.authorize(hasedScratchCode, stringVerificationCode));
            }
        }
    }

    @Test
    public void authorizeHasedScratchCodeVerificationCodeParametersTrueTest() {
        Assert.assertTrue("True expected.", mfaAuthenticatorImpl.authorize("$2a$12$2AZYOAvilJyNvG8b6rBDaOSIcM3mKc6iyNQUYIXOF4ZFEAYdzM7Jm", "plainValue"));
    }

    @Test
    public void generateKeyTest() {
        Assert.assertEquals("Expected and actual values should be the same.", 32, mfaAuthenticatorImpl.generateKey().length());
    }

    @Test
    public void generateCodesTest() {
        Assert.assertEquals("Expected and actual values should be the same.", 5, mfaAuthenticatorImpl.generateCodes().size());
    }
}
