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
package org.eclipse.kapua.client.security.bean.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.util.StdConverter;

public class StringToCertificateConverter extends StdConverter<String, Certificate[]> {

    private static Logger logger = LoggerFactory.getLogger(StringToCertificateConverter.class);

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public Certificate[] convert(String value) {
        Certificate[] certificate = null;
        if (value!=null) {
            List<Certificate> certificateList = new ArrayList<>();
            String[] certificatesStr = value.split(LINE_SEPARATOR);
            for (String tmp : certificatesStr) {
                try (InputStream is = new ByteArrayInputStream(decoder.decode(tmp))) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    certificateList.add((X509Certificate)cf.generateCertificate(is));
                } catch (CertificateException | IOException e) {
                    //do nothing
                    //TODO add metrics?
                    logger.warn("Certificate deserialization error: ", e.getMessage());
                }
            }
            certificate = new Certificate[certificateList.size()];
            certificateList.toArray(certificate);
        }
        return certificate;
    }

}
