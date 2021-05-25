/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.keystore.model;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.service.device.registry.Device;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link DeviceKeystoreSignedCertificate} definition.
 * <p>
 * Contains the result of the {@link DeviceKeystoreCSR}.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceKeystoreSignedCertificate")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystoreSignedCertificate")
public interface DeviceKeystoreSignedCertificate extends KapuaSerializable {

    /**
     * Gets the certificate signed from the {@link Device#getId()}.
     *
     * @return The certificate signed from the {@link Device#getId()}.
     * @since 1.5.0
     */
    @XmlElement(name = "signedCertificate")
    String getSignedCertificate();

    /**
     * Sets the certificate signed from the {@link Device#getId()}.
     *
     * @param signedCertificate The certificate signed from the {@link Device#getId()}.
     * @since 1.5.0
     */
    void setSignedCertificate(String signedCertificate);
}
