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
package org.eclipse.kapua.service.certificate.api.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.certificate.api.Certificate;
import org.eclipse.kapua.service.certificate.api.CertificateFactory;
import org.eclipse.kapua.service.certificate.api.CertificateListResult;
import org.eclipse.kapua.service.certificate.api.CertificateQuery;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class CertificateXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final CertificateFactory FACTORY = LOCATOR.getFactory(CertificateFactory.class);

    public Certificate newCertificate() {
        return FACTORY.newEntity(null);
    }

    public CertificateQuery newQuery() {
        return FACTORY.newQuery(null);
    }

    public CertificateListResult newListResult() {
        return FACTORY.newListResult();
    }
}
