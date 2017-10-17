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
import java.security.PrivateKey;
import java.util.Base64;

import org.eclipse.kapua.service.certificate.util.CertificateUtils;

public class PrivateKeyXmlAdapter extends XmlAdapter<String, PrivateKey> {

    @Override
    public PrivateKey unmarshal(String v) throws Exception {
        return v == null ? null : CertificateUtils.stringToPrivateKey(v);
    }

    @Override
    public String marshal(PrivateKey v) throws Exception {
        return v == null ? null : Base64.getEncoder().encodeToString(v.getEncoded());
    }
}
