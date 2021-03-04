/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Properties;

@Category(Categories.junitTests.class)
public class AuthenticationUtilsTest extends Assert {

    String[] plainValues, encryptedValues;
    Properties prop;

    @Before
    public void initialize() {
        plainValues = new String[]{"plain_..val9&^%ue123!!", "   value#999 ?><,,..;''a  ", "valu   e plain*&^%     $#45", "value,,,,va?><  ", "... s_er%%67nsaa4356&^%   a *(me"};
        encryptedValues = new String[]{"2c3mAagxwaEuAhmR1UzyafpKdA8R-poaS2upJPj4kzE", "QprB8vCeyft4pU8AJdxSWlIFL1b02s-UqTQwirKj9Dw", "RrRtzYPLFDgVdmKo9kOipZv723WBs2J3IxSoPwSJM7g",
                "gcGjWNELoVl9R-71-Nm8aAoNgf3lxr5FziYhj8dmML0", "lCysXXE00k64hm_FzQ8aK1GlVMFqR6So3knfnb5R_CQKDYH95ca-Rc4mIY_HZjC9"};
    }

    @Test
    public void authenticationUtilsTest() throws Exception {
        Constructor<AuthenticationUtils> authenticationUtils = AuthenticationUtils.class.getDeclaredConstructor();
        authenticationUtils.setAccessible(true);
        authenticationUtils.newInstance();
        assertTrue("True expected.", Modifier.isPrivate(authenticationUtils.getModifiers()));
    }

    @Test
    public void cryptCredentialBCRYPTAlgorithmTest() throws KapuaException {
        for (String plainValue : plainValues) {
            assertTrue("True expected.", AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, plainValue).startsWith("$2a$12$"));
            assertEquals("Expected and actual values should be the same.", 60, AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "plain value").length());
        }
    }

    @Test
    public void cryptCredentialSHAAlgorithmTest() throws KapuaException {
        String[] shaAlgorithm = {"SHA512", "algorithm", ""};
        int[] expectedLength = {141, 141, 141};

        for (int i = 0; i < shaAlgorithm.length; i++) {
            System.setProperty("crypto.sha.algorithm", shaAlgorithm[i]);
            for (String plainValue : plainValues) {
                assertTrue("True expected.", AuthenticationUtils.cryptCredential(CryptAlgorithm.SHA, plainValue).contains("=:"));
                assertEquals("Expected and actual values should be the same.", expectedLength[i], AuthenticationUtils.cryptCredential(CryptAlgorithm.SHA, plainValue).length());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void cryptCredentialNullAlgorithmTest() throws KapuaException {
        AuthenticationUtils.cryptCredential(null, "plain value");
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void cryptCredentialNullPlainValueTest() throws KapuaException {
        AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, null);
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void cryptCredentialEmptyPlainValueTest() throws KapuaException {
        AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, "");
    }

    @Test
    public void encryptAesTest() {
        System.setProperty("cipher.key", "rv;ipse329183!@#");
        for (String plainValue : plainValues) {
            try {
                AuthenticationUtils.encryptAes(plainValue);
            } catch (Exception e) {
                fail("Exception not expected.");
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void encryptAesNullTest() {
        System.setProperty("cipher.key", "rv;ipse329183!@#");
        AuthenticationUtils.encryptAes(null);
    }

    @Test
    public void encryptAesEmptyValueTest() {
        System.setProperty("cipher.key", "rv;ipse329183!@#");
        try {
            AuthenticationUtils.encryptAes("");
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void decryptAesTest() {
        System.setProperty("cipher.key", "rv;ipse329183!@#");
        for (String encryptedValue : encryptedValues) {
            try {
                AuthenticationUtils.decryptAes(encryptedValue);
            } catch (Exception e) {
                fail("Exception not expected.");
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void decryptAesNullTest() {
        System.setProperty("cipher.key", "rv;ipse329183!@#");
        AuthenticationUtils.decryptAes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decryptAesNllTest() {
        System.setProperty("cipher.key", "rv;ipse329183!@#");
        AuthenticationUtils.decryptAes("value");
    }

    @Test
    public void decryptAesEmptyValueTest() {
        System.setProperty("cipher.key", "rv;ipse329183!@#");
        try {
            AuthenticationUtils.decryptAes("");
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }
}