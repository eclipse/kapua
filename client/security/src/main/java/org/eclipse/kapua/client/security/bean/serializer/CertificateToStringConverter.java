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
package org.eclipse.kapua.client.security.bean.serializer;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.util.StdConverter;

public class CertificateToStringConverter extends StdConverter<Certificate[], String> {

    private static Logger logger = LoggerFactory.getLogger(CertificateToStringConverter.class);

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Base64.Encoder encoder = Base64.getEncoder();//TODO is thread safe?

    @Override
    public String convert(Certificate[] value) {
        StringBuilder certificateStr = new StringBuilder();
        if (value!=null) {
            for (Certificate certificate : value) {
                try {
                    certificateStr.append(new String(encoder.encode(certificate.getEncoded()))).append(LINE_SEPARATOR);
                } catch (CertificateEncodingException e) {
                    //do nothing
                    //TODO add metrics?
                    logger.warn("Certificate serialization error: ", e.getMessage());
                }
            }
        }
        return certificateStr.toString();
    }

}
