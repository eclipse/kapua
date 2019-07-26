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
package org.eclipse.kapua.service.certificate;

import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.Set;

/**
 * {@link CertificateGenerator} encapsulates all the information needed to generate a new {@link Certificate} in the system.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "certificateGenerator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCertificateGenerator")
public interface CertificateGenerator {

    String getName();

    void setName(String name);

    String getSubject();

    void setSubject(String subject);

    String getIssuer();

    void setIssuer(String issuer);

    int getKeyLength();

    void setKeyLength(int keyLength);

    Date getNotBefore();

    void setNotBefore(Date notBefore);

    Date getNotAfter();

    void setNotAfter(Date notAfter);

    CertificateStatus getStatus();

    void setStatus(CertificateStatus status);

    @XmlElementWrapper(name = "certificateUsages")
    @XmlElement(name = "certificateUsage")
    Set<CertificateUsage> getCertificateUsages();

    void setCertificateUsages(Set<CertificateUsage> certificateUsages);

    Boolean getForwardable();

    void setForwardable(Boolean forwardable);
}
