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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link Device} extended property definition.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceExtendedProperty")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceXmlRegistry.class, factoryMethod = "newDeviceExtendedProperty")
public interface DeviceExtendedProperty extends KapuaSerializable {

    /**
     * Gets the group name.
     *
     * @return The group name.
     * @since 1.5.0
     */
    String getGroupName();

    /**
     * Sets the group name.
     *
     * @param groupName The group name.
     * @since 1.5.0
     */
    void setGroupName(String groupName);

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.5.0
     */
    String getName();

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 1.5.0
     */
    void setName(String name);

    /**
     * Gets the value.
     *
     * @return The value.
     * @since 1.5.0
     */
    String getValue();

    /**
     * Sets the value.
     *
     * @param value The value.
     * @since 1.5.0
     */
    void setValue(String value);
}
