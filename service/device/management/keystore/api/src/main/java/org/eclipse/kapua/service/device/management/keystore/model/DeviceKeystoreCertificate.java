/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
 * {@link DeviceKeystoreCertificate} definition.
 * <p>
 * Represent a certificate to be created on the {@link Device}
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceKeystoreCertificate")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystoreCertificate")
public interface DeviceKeystoreCertificate extends KapuaSerializable {

    /**
     * Gets the keystore id.
     *
     * @return The keystore id.
     * @since 1.5.0
     */
    @XmlElement(name = "keystoreId")
    String getKeystoreId();

    /**
     * Sets the keystore id.
     *
     * @param keystoreId The keystore id.
     * @since 1.5.0
     */
    void setKeystoreId(String keystoreId);

    /**
     * Gets the alias.
     *
     * @return The alias.
     * @since 1.5.0
     */
    @XmlElement(name = "alias")
    String getAlias();

    /**
     * Sets the alias.
     *
     * @param alias The alias.
     * @since 1.5.0
     */
    void setAlias(String alias);

    /**
     * Gets the certificate.
     *
     * @return The certificate.
     * @since 1.5.0
     */
    @XmlElement(name = "certificate")
    String getCertificate();

    /**
     * Sets the certificate.
     *
     * @param certificate The certificate.
     * @since 1.5.0
     */
    void setCertificate(String certificate);
}
