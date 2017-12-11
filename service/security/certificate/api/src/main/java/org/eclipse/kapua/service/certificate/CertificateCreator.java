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
import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

/**
 * CertificateCreator encapsulates all the information needed to create a new Certificate in the system.
 *
 * @since 1.0
 */
@XmlRootElement(name = "certificateCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "certificate",
        "status",
        "privateKey",
        "caId",
        "password",
        "certificateUsages"
}, factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCreator")
public interface CertificateCreator extends KapuaNamedEntityCreator<Certificate> {

    String getCertificate();

    void setCertificate(String certificate);

    CertificateStatus getStatus();

    void setStatus(CertificateStatus status);

    String getPrivateKey();

    void setPrivateKey(String privateKey);

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getCaId();

    void setCaId(KapuaId caId);

    String getPassword();

    void setPassword(String password);

    public <C extends CertificateUsage> Set<C> getCertificateUsages();

    public void setCertificateUsages(Set<CertificateUsage> certificateUsages);

}
