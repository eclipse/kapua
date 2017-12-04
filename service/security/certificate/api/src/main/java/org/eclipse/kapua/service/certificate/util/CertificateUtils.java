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
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.util;

import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateErrorCodes;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class CertificateUtils {

    private CertificateUtils() {
    }

    public static PrivateKey readPrivateKey(File file) throws KapuaCertificateException {
        PrivateKey privateKey;
        try {
            privateKey = stringToPrivateKey(readPrivateKeyAsString(file));
        } catch (IOException e) {
            throw new KapuaCertificateException(KapuaCertificateErrorCodes.PRIVATE_KEY_ERROR, e);
        }
        return privateKey;
    }

    public static String readPrivateKeyAsString(File file) throws IOException {
        return getBytesOnly(FileUtils.readFileToString(file));

    }

    public static X509Certificate readCertificate(File file) throws KapuaCertificateException {
        X509Certificate certificate;
        try {
            certificate = stringToCertificate(readCertificateAsString(file));
        } catch (IOException e) {
            throw new KapuaCertificateException(KapuaCertificateErrorCodes.CERTIFICATE_ERROR, e);
        }
        return certificate;
    }

    public static String readCertificateAsString(File file) throws IOException {
        return getBytesOnly(FileUtils.readFileToString(file));
    }

    public static PrivateKey stringToPrivateKey(String privateKeyString) throws KapuaCertificateException {
        try {
            byte[] decoded = Base64.getDecoder().decode(getBytesOnly(privateKeyString));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new KapuaCertificateException(KapuaCertificateErrorCodes.PRIVATE_KEY_ERROR, e);
        }
    }

    public static X509Certificate stringToCertificate(String certificateString) throws KapuaCertificateException {
        try {
            byte[] decoded = Base64.getDecoder().decode(getBytesOnly(certificateString));
            java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(decoded));
        } catch (CertificateException e) {
            throw new KapuaCertificateException(KapuaCertificateErrorCodes.CERTIFICATE_ERROR, e);
        }
    }

    private static String getBytesOnly(String keyOrCertificate) {
        String lineEnding = "(\r)?\n";
        return keyOrCertificate
                .replaceAll("-----(BEGIN|END)(.*)-----(" + lineEnding + ")?", "")
                .replaceAll(lineEnding, "");
    }
}
