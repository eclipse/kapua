/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateListResult;
import org.eclipse.kapua.service.certificate.CertificateQuery;
import org.eclipse.kapua.service.certificate.CertificateUsage;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class CertificateXmlRegistry {

    private final CertificateFactory certificateFactory = KapuaLocator.getInstance().getFactory(CertificateFactory.class);

    public Certificate newCertificate() {
        return certificateFactory.newEntity(null);
    }

    public CertificateCreator newCreator() {
        return certificateFactory.newCreator(null);
    }

    public CertificateQuery newQuery() {
        return certificateFactory.newQuery(null);
    }

    public CertificateListResult newListResult() {
        return certificateFactory.newListResult();
    }

    public CertificateGenerator newCertificateGenerator() {
        return certificateFactory.newCertificateGenerator();
    }

    public CertificateUsage newCertificateUsage() {
        return certificateFactory.newCertificateUsage(null);
    }
}
