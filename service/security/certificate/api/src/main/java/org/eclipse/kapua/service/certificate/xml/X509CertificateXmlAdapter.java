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
package org.eclipse.kapua.service.certificate.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.eclipse.kapua.service.certificate.util.CertificateUtils;

public class X509CertificateXmlAdapter extends XmlAdapter<String, X509Certificate> {

    @Override
    public X509Certificate unmarshal(String v) throws Exception {
        return v == null ? null : CertificateUtils.stringToCertificate(v);
    }

    @Override
    public String marshal(X509Certificate v) throws Exception {
        return v == null ? null : Base64.getEncoder().encodeToString(v.getEncoded());
    }
}
