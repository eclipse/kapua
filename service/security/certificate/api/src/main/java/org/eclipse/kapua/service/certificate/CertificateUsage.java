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

import org.eclipse.kapua.service.certificate.xml.CertificateXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @since 1.0.0
 */
@XmlRootElement(name = "certificateUsage")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateXmlRegistry.class, factoryMethod = "newCertificateUsage")
public interface CertificateUsage {

    /**
     * @return
     * @since 1.0.0
     */
    String getName();

    /**
     * @param name
     * @since 1.0.0
     */
    void setName(String name);

}
