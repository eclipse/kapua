/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Encrypt/Decrypt and encode/decode utilities
 * 
 * @since 1.0
 *
 */
public class CryptoUtil
{

    private static final String ALGORITHM  = "AES";
    private static final byte[] SECRET_KEY = "ipsea1s214d5a6sfs8dfsdg@$%3saf".getBytes();

    /**
     * Encrypt the value using aes algorithm
     * 
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encryptAes(String value)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = c.doFinal(value.getBytes());

        String encryptedValue = DatatypeConverter.printBase64Binary(encryptedBytes);
        return encryptedValue;
    }

    /**
     * Decrypt the aes encrypted value
     * 
     * @param encryptedValue
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decryptAes(String encryptedValue)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
    {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = DatatypeConverter.parseBase64Binary(encryptedValue);
        byte[] decryptedBytes = c.doFinal(decordedValue);
        String decryptedValue = new String(decryptedBytes);
        return decryptedValue;
    }

    /**
     * Generate a key
     * 
     * @return
     */
    private static Key generateKey()
    {
        Key key = new SecretKeySpec(SECRET_KEY, ALGORITHM);
        return key;
    }

    /**
     * Evaluate the sha1 hash for the provided String
     * 
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sha1Hash(String s)
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest cript = MessageDigest.getInstance("SHA-1");
        cript.reset();
        cript.update(s.getBytes("UTF8"));

        byte[] encodedBytes = cript.digest();
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
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
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
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(encodedValue);
        String decodedValue = new String(decodedBytes, "UTF-8");
        return decodedValue;
    }
}
