/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util;

import java.lang.reflect.Constructor;
import java.util.Random;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class CryptoUtilTest extends Assert {

    @Test
    public void testConstructor() throws Exception {
        Constructor<CryptoUtil> crypto = CryptoUtil.class.getDeclaredConstructor();
        crypto.setAccessible(true);
        crypto.newInstance();
    }

    @Test
    public void testSha1Hash() throws Exception {
        StringBuilder longTest = new StringBuilder();
        Random random = new Random();
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        for (int i = 0; i < 500; i++) {
            longTest.append(permittedSymbols.charAt(random.nextInt(permittedSymbols.length())));
        }
        String longName = longTest.toString();
        String[] permittedStrings = new String[] { "a", "ab", "abc", "123", longName };
        int sizeOfPermittedStrings = permittedStrings.length;
        String[] falseStrings = new String[] { null };
        int sizeOfFalseStrings = falseStrings.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                CryptoUtil.sha1Hash(falseStrings[i]);
                fail("Exception expected for: " + falseStrings[i]);
            } catch (Exception ex) {
                // Expected
            }
        }
        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                CryptoUtil.sha1Hash(permittedStrings[i]);
            } catch (Exception ex) {
                fail("No exception expected for 'test string'");
            }
        }
    }

    @Test
    public void testEncodeBase64() {
        StringBuilder longTest = new StringBuilder();
        Random random = new Random();
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        for (int i = 0; i < 500; i++) {
            longTest.append(permittedSymbols.charAt(random.nextInt(permittedSymbols.length())));
        }
        String longName = longTest.toString();
        String[] permittedStrings = new String[] { "a", "ab", "abc", "123", longName };
        int sizeOfPermittedStrings = permittedStrings.length;
        String[] falseStrings = new String[] { null };
        int sizeOfFalseStrings = falseStrings.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                CryptoUtil.encodeBase64(falseStrings[i]);
                fail("Exception expected for: " + falseStrings[i]);
            } catch (Exception ex) {
                // Expected
            }
        }

        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                CryptoUtil.encodeBase64(permittedStrings[i]);
            } catch (Exception ex) {
                fail("No exception expected for 'test string'");
            }
        }
    }

    @Test
    public void testDecodeBase64() {
        StringBuilder longTest = new StringBuilder();
        Random random = new Random();
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        for (int i = 0; i < 500; i++) {
            longTest.append(permittedSymbols.charAt(random.nextInt(permittedSymbols.length())));
        }
        String longName = longTest.toString();
        String[] permittedStrings = new String[] { "a", "ab", "abc", "123", longName };
        int sizeOfPermittedStrings = permittedStrings.length;
        String[] falseStrings = new String[] { null };
        int sizeOfFalseStrings = falseStrings.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                CryptoUtil.decodeBase64(falseStrings[i]);
                fail("Exception expected for: " + falseStrings[i]);
            } catch (Exception ex) {
                // Expected
            }
        }

        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                CryptoUtil.decodeBase64(permittedStrings[i]);
            } catch (Exception ex) {
                fail("No exception expected for 'test string'");
            }
        }
    }
}
