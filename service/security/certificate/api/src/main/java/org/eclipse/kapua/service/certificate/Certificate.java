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
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlInlineBinaryData;
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
        "keyUsageSettings"
}, factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCertificate")
public interface Certificate extends KapuaNamedEntity {

    String TYPE = "certificate";

    @Override
    default String getType() {
        return TYPE;
    }

    public String getCertificate();

    public void setCertificate(String certificate);

    public Integer getVersion();

    public void setVersion(Integer version);

    public String getSerial();

    public void setSerial(String serial);

    public String getAlgorithm();

    public void setAlgorithm(String algorithm);

    public String getSubject();

    public void setSubject(String subject);

    public String getIssuer();

    public void setIssuer(String issuer);

    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    public Date getNotBefore();

    public void setNotBefore(Date notBefore);

    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    public Date getNotAfter();

    public void setNotAfter(Date notAfter);

    public CertificateStatus getStatus();

    public void setStatus(CertificateStatus status);

    @XmlInlineBinaryData
    @XmlElement(name = "signature")
    public byte[] getSignature();

    public void setSignature(byte[] signature);

    public String getPrivateKey();

    public void setPrivateKey(String privateKey);

    public Boolean getCa();

    public void setCa(Boolean isCa);

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getCaId();

    public void setCaId(KapuaId caId);

    public String getPassword();

    public void setPassword(String password);

    public <K extends KeyUsageSetting> Set<K> getKeyUsageSettings();

    public void setKeyUsageSettings(Set<KeyUsageSetting> keyUsages);

    public void addKeyUsageSetting(KeyUsageSetting keyUsage);

    public void removeKeyUsageSetting(KeyUsageSetting keyUsage);

    public <C extends CertificateUsage> Set<C> getCertificateUsages();

    public void setCertificateUsages(Set<CertificateUsage> certificateUsages);

    public void addCertificateUsage(CertificateUsage certificateUsage);

    public void removeCertificateUsage(CertificateUsage certificateUsage);
}
