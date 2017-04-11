/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * Encrypt/Decrypt and encode/decode utilities
 * 
 * @since 1.0
 *
 */
public class CryptoUtil {
    /**
     * Evaluate the sha1 hash for the provided String
     * 
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sha1Hash(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] encodedBytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printBase64Binary(encodedBytes);
    }

    /**
     * Encode a base 64 String
     * 
     * @param stringValue
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String encodeBase64(String stringValue)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytesValue = stringValue.getBytes("UTF-8");
        String encodedValue = DatatypeConverter.printBase64Binary(bytesValue);
        return encodedValue;

    }

    /**
     * Decode a base 64 String
     * 
     * @param encodedValue
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String decodeBase64(String encodedValue)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(encodedValue);
        String decodedValue = new String(decodedBytes, "UTF-8");
        return decodedValue;
    }
}
