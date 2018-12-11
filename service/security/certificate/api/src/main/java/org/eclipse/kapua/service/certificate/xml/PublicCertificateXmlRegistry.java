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
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.PublicCertificate;
import org.eclipse.kapua.service.certificate.PublicCertificateCreator;
import org.eclipse.kapua.service.certificate.PublicCertificateFactory;
import org.eclipse.kapua.service.certificate.PublicCertificateListResult;
import org.eclipse.kapua.service.certificate.PublicCertificateQuery;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class PublicCertificateXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final PublicCertificateFactory FACTORY = LOCATOR.getFactory(PublicCertificateFactory.class);

    public PublicCertificate newPublicCertificate() {
        return FACTORY.newEntity(null);
    }

    public PublicCertificateCreator newCreator() {
        return FACTORY.newCreator(null);
    }

    public PublicCertificateQuery newQuery() {
        return FACTORY.newQuery(null);
    }

    public PublicCertificateListResult newListResult() {
        return FACTORY.newListResult();
    }

    public CertificateUsage newCertificateUsage() {
        return FACTORY.newCertificateUsage(null);
    }

}
