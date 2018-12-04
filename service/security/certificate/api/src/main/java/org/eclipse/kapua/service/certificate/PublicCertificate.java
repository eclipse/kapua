/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Set;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.BinaryXmlAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "publicCertificate")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateXmlRegistry.class, factoryMethod = "newPublicCertificate")
public interface PublicCertificate extends KapuaNamedEntity {

    String TYPE = "publicCertificate";

    @Override
    default String getType() {
        return TYPE;
    }

    @XmlElement(name = "certificate")
    String getCertificate();

    void setCertificate(String certificate);

    @XmlElement(name = "version")
    Integer getVersion();

    void setVersion(Integer version);

    @XmlElement(name = "serial")
    String getSerial();

    void setSerial(String serial);

    @XmlElement(name = "algorithm")
    String getAlgorithm();

    void setAlgorithm(String algorithm);

    @XmlElement(name = "signature")
    @XmlJavaTypeAdapter(BinaryXmlAdapter.class)
    @ApiModelProperty(dataType = "string")
    byte[] getSignature();

    void setSignature(byte[] signature);

    @XmlElement(name = "subject")
    String getSubject();

    void setSubject(String subject);

    @XmlElement(name = "issuer")
    String getIssuer();

    void setIssuer(String issuer);

    @XmlElement(name = "notBefore")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotBefore();

    void setNotBefore(Date notBefore);

    @XmlElement(name = "notAfter")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotAfter();

    void setNotAfter(Date notAfter);

    @XmlElement(name = "status")
    CertificateStatus getStatus();

    void setStatus(CertificateStatus status);

    @XmlElement(name = "ca")
    Boolean getCa();

    void setCa(Boolean isCa);

    @XmlElement(name = "caId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getCaId();

    void setCaId(KapuaId caId);

    @XmlElementWrapper(name = "keyUsageSettings")
    @XmlElement(name = "keyUsageSetting")
    <K extends KeyUsageSetting> Set<K> getKeyUsageSettings();

    void setKeyUsageSettings(Set<KeyUsageSetting> keyUsages);

    void addKeyUsageSetting(KeyUsageSetting keyUsage);

    void removeKeyUsageSetting(KeyUsageSetting keyUsage);

    @XmlElementWrapper(name = "certificateUsages")
    @XmlElement(name = "certificateUsage")
    <C extends CertificateUsage> Set<C> getCertificateUsages();

    void setCertificateUsages(Set<CertificateUsage> certificateUsages);

    void addCertificateUsage(CertificateUsage certificateUsage);

    void removeCertificateUsage(CertificateUsage certificateUsage);

    @XmlElement(name = "forwardable")
    Boolean getForwardable();

    void setForwardable(Boolean forwardable);
}
