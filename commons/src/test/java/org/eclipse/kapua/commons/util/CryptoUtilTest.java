/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.commons.crypto.CryptoUtil;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.util.Random;


@Category(JUnitTests.class)
public class CryptoUtilTest {

    private static final Random RANDOM = RandomUtils.getInstance();

    @Test
    public void testConstructor() throws Exception {
        Constructor<CryptoUtil> crypto = CryptoUtil.class.getDeclaredConstructor();
        crypto.setAccessible(true);
        crypto.newInstance();
    }

    @Test
    public void testSha1Hash() throws Exception {
        StringBuilder longTest = new StringBuilder();
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        for (int i = 0; i < 500; i++) {
            longTest.append(permittedSymbols.charAt(RANDOM.nextInt(permittedSymbols.length())));
        }
        String longName = longTest.toString();
        String[] permittedStrings = new String[]{"a", "ab", "abc", "123", longName};
        int sizeOfPermittedStrings = permittedStrings.length;
        String[] falseStrings = new String[]{null};
        int sizeOfFalseStrings = falseStrings.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                CryptoUtil.sha1Hash(falseStrings[i]);
                Assert.fail("Exception expected for: " + falseStrings[i]);
            } catch (Exception ex) {
                // Expected
            }
        }
        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                CryptoUtil.sha1Hash(permittedStrings[i]);
            } catch (Exception ex) {
                Assert.fail("No exception expected for 'test string'");
            }
        }
    }

    @Test
    public void testEncodeBase64() {
        StringBuilder longTest = new StringBuilder();
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        for (int i = 0; i < 500; i++) {
            longTest.append(permittedSymbols.charAt(RANDOM.nextInt(permittedSymbols.length())));
        }
        String longName = longTest.toString();
        String[] permittedStrings = new String[]{"a", "ab", "abc", "123", longName};
        int sizeOfPermittedStrings = permittedStrings.length;
        String[] falseStrings = new String[]{null};
        int sizeOfFalseStrings = falseStrings.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                CryptoUtil.encodeBase64(falseStrings[i]);
                Assert.fail("Exception expected for: " + falseStrings[i]);
            } catch (Exception ex) {
                // Expected
            }
        }

        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                CryptoUtil.encodeBase64(permittedStrings[i]);
            } catch (Exception ex) {
                Assert.fail("No exception expected for 'test string'");
            }
        }
    }

    @Test
    public void testDecodeBase64() {
        StringBuilder longTest = new StringBuilder();
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        for (int i = 0; i < 500; i++) {
            longTest.append(permittedSymbols.charAt(RANDOM.nextInt(permittedSymbols.length())));
        }
        String longName = longTest.toString();
        String[] permittedStrings = new String[]{"a", "ab", "abc", "123", longName};
        int sizeOfPermittedStrings = permittedStrings.length;
        String[] falseStrings = new String[]{null};
        int sizeOfFalseStrings = falseStrings.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                CryptoUtil.decodeBase64(falseStrings[i]);
                Assert.fail("Exception expected for: " + falseStrings[i]);
            } catch (Exception ex) {
                // Expected
            }
        }

        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                CryptoUtil.decodeBase64(permittedStrings[i]);
            } catch (Exception ex) {
                Assert.fail("No exception expected for 'test string'");
            }
        }
    }
}
