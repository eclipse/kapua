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
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceKeystoreItems} definition.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceKeystoreItems")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystoreItems")
public interface DeviceKeystoreItems extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceKeystoreItem}s
     *
     * @return The {@link List} of {@link DeviceKeystoreItem}s
     * @since 1.5.0
     */
    @XmlElement(name = "keystoreItems")
    List<DeviceKeystoreItem> getKeystoreItems();

    /**
     * Adds a {@link DeviceKeystoreItem} to the {@link List}
     *
     * @param keystoreItem The {@link DeviceKeystoreItem} to add.
     * @since 1.5.0
     */
    void addKeystoreItem(DeviceKeystoreItem keystoreItem);

    /**
     * Sets the {@link List} of {@link DeviceKeystoreItem}s
     *
     * @param keystoreItems The {@link List} of {@link DeviceKeystoreItem}s
     * @since 1.5.0
     */
    void setKeystoreItems(List<DeviceKeystoreItem> keystoreItems);
}
