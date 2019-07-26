/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link Certificate} {@link org.eclipse.kapua.model.KapuaEntity} definition
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "certificate")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCertificate")
public interface Certificate extends CertificateInfo {

    String TYPE = "certificate";

    @Override
    default String getType() {
        return TYPE;
    }

    @XmlElement(name = "privateKey")
    String getPrivateKey();

    void setPrivateKey(String privateKey);

    @XmlTransient
    String getPassword();

    void setPassword(String password);
}
