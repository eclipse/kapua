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

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.BinaryXmlAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Set;

@XmlRootElement(name = "certificate")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "certificate",
        "version",
        "serial",
        "algorithm",
        "subject",
        "issuer",
        "notBefore",
        "notAfter",
        "status",
        "signature",
        "privateKey",
        "ca",
        "caId",
        "password",
        "certificateUsages",
        "keyUsageSettings",
        "forwardable"
}, factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCertificate")
public interface Certificate extends KapuaNamedEntity {

    String TYPE = "certificate";

    @Override
    default String getType() {
        return TYPE;
    }

    String getCertificate();

    void setCertificate(String certificate);

    Integer getVersion();

    void setVersion(Integer version);

    String getSerial();

    void setSerial(String serial);

    String getAlgorithm();

    void setAlgorithm(String algorithm);

    String getSubject();

    void setSubject(String subject);

    String getIssuer();

    void setIssuer(String issuer);

    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotBefore();

    void setNotBefore(Date notBefore);

    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotAfter();

    void setNotAfter(Date notAfter);

    CertificateStatus getStatus();

    void setStatus(CertificateStatus status);

    @XmlElement(name = "signature")
    @XmlJavaTypeAdapter(BinaryXmlAdapter.class)
    byte[] getSignature();

    void setSignature(byte[] signature);

    String getPrivateKey();

    void setPrivateKey(String privateKey);

    Boolean getCa();

    void setCa(Boolean isCa);

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getCaId();

    void setCaId(KapuaId caId);

    String getPassword();

    void setPassword(String password);

    <K extends KeyUsageSetting> Set<K> getKeyUsageSettings();

    void setKeyUsageSettings(Set<KeyUsageSetting> keyUsages);

    void addKeyUsageSetting(KeyUsageSetting keyUsage);

    void removeKeyUsageSetting(KeyUsageSetting keyUsage);

    <C extends CertificateUsage> Set<C> getCertificateUsages();

    void setCertificateUsages(Set<CertificateUsage> certificateUsages);

    void addCertificateUsage(CertificateUsage certificateUsage);

    void removeCertificateUsage(CertificateUsage certificateUsage);

    Boolean getForwardable();

    void setForwardable(Boolean forwardable);
}
