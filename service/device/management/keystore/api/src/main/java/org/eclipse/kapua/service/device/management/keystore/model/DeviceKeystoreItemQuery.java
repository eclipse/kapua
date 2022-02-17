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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link DeviceKeystoreItemQuery} definition.
 * <p>
 * Is used to filter result from the keystore.
 * Only one of the filter can be used at a time.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceKeystoreItemQuery")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystoreItemQuery")
public interface DeviceKeystoreItemQuery extends KapuaSerializable {

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
     * Gets whether or not there is a filter specified.
     *
     * @return {@code true} if there is at least one filter, {@code false} otherwise.
     * @since 1.5.0
     */
    @XmlTransient
    boolean hasFilters();
}
