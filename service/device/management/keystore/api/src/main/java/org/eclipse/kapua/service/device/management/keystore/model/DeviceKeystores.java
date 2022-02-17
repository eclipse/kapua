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
 * {@link DeviceKeystores} definition.
 *
 * @since 1.5.0
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "deviceKeystores")
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystores")
public interface DeviceKeystores extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceKeystore}s
     *
     * @return The {@link List} of {@link DeviceKeystore}s
     * @since 1.5.0
     */
    @XmlElement(name = "keystores")
    List<DeviceKeystore> getKeystores();

    /**
     * Adds a {@link DeviceKeystore} to the {@link List}
     *
     * @param keystore The {@link DeviceKeystore} to add.
     * @since 1.5.0
     */
    void addKeystore(DeviceKeystore keystore);

    /**
     * Sets the {@link List} of {@link DeviceKeystore}s
     *
     * @param keystores The {@link List} of {@link DeviceKeystore}s
     * @since 1.5.0
     */
    void setKeystores(List<DeviceKeystore> keystores);
}
