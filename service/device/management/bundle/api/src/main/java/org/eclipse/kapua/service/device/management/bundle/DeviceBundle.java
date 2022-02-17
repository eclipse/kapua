/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.bundle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link DeviceBundle} definition.
 * <p>
 * This entity is used to get information about bundles installed in the device.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "bundle")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryClass = DeviceBundleXmlRegistry.class, factoryMethod = "newDeviceBundle")
public interface DeviceBundle {

    /**
     * Gets the bundle identifier.
     *
     * @return The bundle identifier.
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    long getId();

    /**
     * Sets the bundle identifier.
     *
     * @param id The bundle identifier.
     * @since 1.0.0
     */
    void setId(long id);

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the state.
     *
     * @return The state.
     * @since 1.0.0
     */
    @XmlElement(name = "state")
    String getState();

    /**
     * Sets the state.
     *
     * @param state The state.
     * @since 1.0.0
     */
    void setState(String state);

    /**
     * Gets the bundle version.
     *
     * @return The version.
     * @since 1.0.0
     */
    @XmlElement(name = "version")
    String getVersion();

    /**
     * Sets the version.
     *
     * @param version The version.
     * @since 1.0.0
     */
    void setVersion(String version);

}
