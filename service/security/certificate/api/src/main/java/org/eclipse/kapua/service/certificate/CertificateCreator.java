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
package org.eclipse.kapua.service.certificate;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

/**
 * {@link Certificate} {@link org.eclipse.kapua.model.KapuaEntityCreator}encapsulates all the information needed to create a new {@link Certificate} in the system.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "certificateCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCreator")
public interface CertificateCreator extends KapuaNamedEntityCreator<Certificate> {

    @XmlElement(name = "certificate")
    String getCertificate();

    void setCertificate(String certificate);

    @XmlElement(name = "status")
    CertificateStatus getStatus();

    void setStatus(CertificateStatus status);

    @XmlElement(name = "privateKey")
    String getPrivateKey();

    void setPrivateKey(String privateKey);

    @XmlElement(name = "caId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getCaId();

    void setCaId(KapuaId caId);

    @XmlElement(name = "password")
    String getPassword();

    void setPassword(String password);

    @XmlElementWrapper(name = "certificateUsages")
    @XmlElement(name = "certificateUsage")
    <C extends CertificateUsage> Set<C> getCertificateUsages();

    void setCertificateUsages(Set<CertificateUsage> certificateUsages);

    @XmlElement(name = "forwardable")
    Boolean getForwardable();

    void setForwardable(Boolean forwardable);
}
