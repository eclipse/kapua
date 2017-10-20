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

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;
import org.eclipse.kapua.service.certificate.xml.PrivateKeyXmlAdapter;
import org.eclipse.kapua.service.certificate.xml.X509CertificateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

@XmlRootElement(name = "certificate")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "certificate",
        "family",
        "version",
        "serial",
        "algorithm",
        "subject",
        "issuer",
        "notBefore",
        "notAfter",
        "status",
        "digest",
        "privateKey",
        "default",
        "ca",
        "caId",
        "password"
}, factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCertificate")
public interface Certificate extends KapuaNamedEntity {

    String TYPE = "account";

    default String getType() {
        return TYPE;
    }

    @XmlJavaTypeAdapter(X509CertificateXmlAdapter.class)
    X509Certificate getCertificate();

    @XmlJavaTypeAdapter(X509CertificateXmlAdapter.class)
    void setCertificate(X509Certificate certificate);

    String getFamily();

    void setFamily(String family);

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

    byte[] getDigest();

    void setDigest(byte[] digest);

    @XmlJavaTypeAdapter(PrivateKeyXmlAdapter.class)
    PrivateKey getPrivateKey();

    @XmlJavaTypeAdapter(PrivateKeyXmlAdapter.class)
    void setPrivateKey(PrivateKey privateKey);

    Boolean getDefault();

    void setDefault(Boolean isDefault);

    Boolean getCa();

    void setCa(Boolean isCa);

    Integer getCaId();

    void setCaId(Integer caId);

    String getPassword();

    void setPassword(String password);

}
