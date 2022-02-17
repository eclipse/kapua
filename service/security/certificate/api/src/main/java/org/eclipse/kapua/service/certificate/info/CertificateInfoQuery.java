/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate.info;

import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.info.xml.CertificateInfoXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link CertificateInfo} {@link KapuaQuery} definition.
 *
 * @see KapuaQuery
 * @since 1.1.0
 */
@XmlRootElement(name = "query")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateInfoXmlRegistry.class, factoryMethod = "newQuery")
public interface CertificateInfoQuery extends KapuaQuery {

    /**
     * Gets whether or not to get also inherited {@link CertificateInfo}s
     *
     * @return {@code true} if set to get inherited {@link CertificateInfo}s, {@code false} otherwise.
     * @since 1.1.0
     */
    @XmlElement(name = "includeInherited")
    Boolean getIncludeInherited();

    /**
     * Sets whether or not to get also inherited {@link CertificateInfo}s
     *
     * @param includeInherited {@code true} to get inherited {@link CertificateInfo}s, {@code false} otherwise.
     * @since 1.1.0
     */
    void setIncludeInherited(Boolean includeInherited);
}
