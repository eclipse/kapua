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
package org.eclipse.kapua.service.device.registry.connection.option;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Device connection options entity definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceConnectionOption")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "allowUserChange",
        "userCouplingMode",
        "reservedUserId",
}, //
        factoryClass = DeviceConnectionOptionXmlRegistry.class, //
        factoryMethod = "newDeviceConnectionOption")
public interface DeviceConnectionOption extends KapuaUpdatableEntity {

    String TYPE = "deviceConnectionOption";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Gets whether or not the {@link DeviceConnection} can change user on the next login.
     *
     * @return <code>true</code> if device can changhe user to connect, <code>false</code> if not.
     */
    @XmlElement(name = "allowUserChange")
    boolean getAllowUserChange();

    /**
     * Sets whether or not the {@link DeviceConnection} can change user on the next login.
     *
     * @param allowUserChange
     */
    void setAllowUserChange(boolean allowUserChange);

    /**
     * Get the device connection user coupling mode.
     *
     * @return
     */
    @XmlElement(name = "userCouplingMode")
    ConnectionUserCouplingMode getUserCouplingMode();

    /**
     * Set the device connection user coupling mode.
     *
     * @param userCouplingMode
     */
    void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode);

    /**
     * Get the reserved user identifier
     *
     * @return
     */
    @XmlElement(name = "reservedUserId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getReservedUserId();

    /**
     * Set the reserved user identifier
     *
     * @param reservedUserId
     */
    void setReservedUserId(KapuaId reservedUserId);
}
