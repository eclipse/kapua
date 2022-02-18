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
 * {@link DeviceKeystoreCSRInfo} definition.
 * <p>
 * Represent a certificate signing request to be sent to the {@link Device}
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceKeystoreCSRInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystoreCSRInfo")
public interface DeviceKeystoreCSRInfo extends KapuaSerializable {

    /**
     * Gets the {@link DeviceKeystoreItem#getKeystoreId()} target to be used for signing.
     *
     * @return The {@link DeviceKeystoreItem#getKeystoreId()} target to be used for signing.
     * @since 1.5.0
     */
    @XmlElement(name = "keystoreId")
    String getKeystoreId();

    /**
     * Sets the {@link DeviceKeystoreItem#getKeystoreId()} target to be used for signing.
     *
     * @param keystoreId The {@link DeviceKeystoreItem#getKeystoreId()} target to be used for signing.
     * @since 1.5.0
     */
    void setKeystoreId(String keystoreId);

    /**
     * Gets the {@link DeviceKeystoreItem#getAlias()} target to be used for signing.
     *
     * @return The {@link DeviceKeystoreItem#getAlias()} target to be used for signing.
     * @since 1.5.0
     */
    @XmlElement(name = "alias")
    String getAlias();

    /**
     * Sets the {@link DeviceKeystoreItem#getAlias()} target to be used for signing.
     *
     * @param alias The {@link DeviceKeystoreItem#getAlias()} target to be used for signing.
     * @since 1.5.0
     */
    void setAlias(String alias);

    /**
     * Gets the signature algorithm.
     *
     * @return The signature algorithm.
     * @since 1.5.0
     */
    @XmlElement(name = "signatureAlgorithm")
    String getSignatureAlgorithm();

    /**
     * Sets the signature algorithm.
     *
     * @param signatureAlgorithm The signature algorithm.
     * @since 1.5.0
     */
    void setSignatureAlgorithm(String signatureAlgorithm);

    /**
     * Gets the attributes to sign.
     *
     * @return The attributes to sign.
     * @since 1.5.0
     */
    @XmlElement(name = "attributes")
    String getAttributes();

    /**
     * Sets the attributes to sign.
     *
     * @param attributes The attributes to sign.
     * @since 1.5.0
     */
    void setAttributes(String attributes);
}
