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
import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * CertificateCreator encapsulates all the information needed to create a new Certificate in the system.
 *
 * @since 1.0
 */
@XmlRootElement(name = "userCreator")
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
})
public interface CertificateCreator extends KapuaNamedEntityCreator<Certificate> {

    String getCertificate();

    void setCertificate(String certificate);

    String getFamily();

    void setFamily(String family);

    Integer getVersion();

    void setVersion(Integer family);

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

    String getPrivateKey();

    void setPrivateKey(String privateKey);

    Boolean getDefault();

    void setDefault(Boolean isDefault);

    Boolean getCa();

    void setCa(Boolean isCa);

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getCaId();

    void setCaId(KapuaId caId);

    String getPassword();

    void setPassword(String password);
}
