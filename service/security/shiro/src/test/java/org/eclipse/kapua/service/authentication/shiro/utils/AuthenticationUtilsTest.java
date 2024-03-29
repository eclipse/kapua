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
package org.eclipse.kapua.service.authentication.shiro.utils;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaCryptoSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaCryptoSettingKeys;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Category(JUnitTests.class)
public class AuthenticationUtilsTest {

    String[] plainValues, encryptedValues;
    private String cypherKeyPropKey = KapuaCryptoSettingKeys.CIPHER_KEY.key();
    private String cryptoShaAlgorithmPropKey = KapuaCryptoSettingKeys.CRYPTO_SHA_ALGORITHM.key();
    private AuthenticationUtils authenticationUtils;

    @Before
    public void initialize() {
        plainValues = new String[]{"plain_..val9&^%ue123!!", "   value#999 ?><,,..;''a  ", "valu   e plain*&^%     $#45", "value,,,,va?><  ", "... s_er%%67nsaa4356&^%   a *(me"};
        encryptedValues = new String[]{"2c3mAagxwaEuAhmR1UzyafpKdA8R-poaS2upJPj4kzE", "QprB8vCeyft4pU8AJdxSWlIFL1b02s-UqTQwirKj9Dw", "RrRtzYPLFDgVdmKo9kOipZv723WBs2J3IxSoPwSJM7g",
                "gcGjWNELoVl9R-71-Nm8aAoNgf3lxr5FziYhj8dmML0", "lCysXXE00k64hm_FzQ8aK1GlVMFqR6So3knfnb5R_CQKDYH95ca-Rc4mIY_HZjC9"};
        final SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, e);
        }
        authenticationUtils = new AuthenticationUtils(random, new KapuaCryptoSetting());
    }

    @After
    public void tearDown() {
        System.clearProperty(cypherKeyPropKey);
        System.clearProperty(cryptoShaAlgorithmPropKey);
    }

    @Test
    public void cryptCredentialBCRYPTAlgorithmTest() throws KapuaException {
        for (String plainValue : plainValues) {
            Assert.assertTrue("True expected.", authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, plainValue).startsWith("$2a$12$"));
            Assert.assertEquals("Expected and actual values should be the same.", 60, authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "plain value").length());
        }
    }

    @Test
    public void cryptCredentialSHAAlgorithmTest() throws KapuaException {
        String[] shaAlgorithm = {"SHA256", "SHA512", "algorithm", ""};
        int[] expectedLength = {77, 141, 141, 141};

        for (int i = 0; i < shaAlgorithm.length; i++) {
            System.setProperty(cryptoShaAlgorithmPropKey, shaAlgorithm[i]);
            for (String plainValue : plainValues) {
                Assert.assertTrue("True expected.", authenticationUtils.cryptCredential(CryptAlgorithm.SHA, plainValue).contains("=:"));
                Assert.assertEquals("Expected and actual values should be the same.", expectedLength[i], authenticationUtils.cryptCredential(CryptAlgorithm.SHA, plainValue).length());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void cryptCredentialNullAlgorithmTest() throws KapuaException {
        authenticationUtils.cryptCredential(null, "plain value");
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void cryptCredentialNullPlainValueTest() throws KapuaException {
        authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, null);
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void cryptCredentialEmptyPlainValueTest() throws KapuaException {
        authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "");
    }

    @Test
    public void encryptAesTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse329183!@#");
        for (String plainValue : plainValues) {
            try {
                authenticationUtils.encryptAes(plainValue);
            } catch (Exception e) {
                Assert.fail("Exception not expected.");
            }
        }
    }

    @Test(expected = KapuaRuntimeException.class)
    public void encryptAesIncorrectKeyTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse32918@#");
        for (String plainValue : plainValues) {
            authenticationUtils.encryptAes(plainValue);
        }
    }

    @Test(expected = NullPointerException.class)
    public void encryptAesNullTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse329183!@#");
        authenticationUtils.encryptAes(null);
    }

    @Test
    public void encryptAesEmptyValueTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse329183!@#");
        try {
            authenticationUtils.encryptAes("");
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void encryptAesEmptyKeyTest() {
        System.setProperty(cypherKeyPropKey, "");
        authenticationUtils.encryptAes("plain value");
    }

    @Test
    public void decryptAesTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse329183!@#");
        for (String encryptedValue : encryptedValues) {
            try {
                authenticationUtils.decryptAes(encryptedValue);
            } catch (Exception e) {
                Assert.fail("Exception not expected.");
            }
        }
    }

    @Test(expected = KapuaRuntimeException.class)
    public void decryptAesIncorrectKeyTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse32918@#");
        for (String encryptedValue : encryptedValues) {
            authenticationUtils.decryptAes(encryptedValue);
        }
    }

    @Test(expected = NullPointerException.class)
    public void decryptAesNullTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse329183!@#");
        authenticationUtils.decryptAes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decryptAesNllTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse329183!@#");
        authenticationUtils.decryptAes("value");
    }

    @Test
    public void decryptAesEmptyValueTest() {
        System.setProperty(cypherKeyPropKey, "rv;ipse329183!@#");
        try {
            authenticationUtils.decryptAes("");
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void decryptAesEmptyKeyTest() {
        System.setProperty(cypherKeyPropKey, "");
        authenticationUtils.decryptAes("2c3mAagxwaEuAhmR1UzyafpKdA8R-poaS2upJPj4kzE");
    }
}
