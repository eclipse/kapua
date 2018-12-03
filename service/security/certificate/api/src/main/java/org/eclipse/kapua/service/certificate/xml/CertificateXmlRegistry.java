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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.certificate.PrivateCertificate;
import org.eclipse.kapua.service.certificate.PrivateCertificateCreator;
import org.eclipse.kapua.service.certificate.PrivateCertificateFactory;
import org.eclipse.kapua.service.certificate.PrivateCertificateListResult;
import org.eclipse.kapua.service.certificate.CertificateQuery;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.CertificateGenerator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class CertificateXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final PrivateCertificateFactory FACTORY = LOCATOR.getFactory(PrivateCertificateFactory.class);

    public PrivateCertificate newCertificate() {
        return FACTORY.newEntity(null);
    }

    public PrivateCertificateCreator newCreator() {
        return FACTORY.newCreator(null);
    }

    public CertificateQuery newQuery() {
        return FACTORY.newQuery(null);
    }

    public PrivateCertificateListResult newListResult() {
        return FACTORY.newListResult();
    }

    public CertificateUsage newCertificateUsage() {
        return FACTORY.newCertificateUsage(null);
    }

    public CertificateGenerator newCertificateGenerator() {
        return FACTORY.newCertificateGenerator();
    }
}
