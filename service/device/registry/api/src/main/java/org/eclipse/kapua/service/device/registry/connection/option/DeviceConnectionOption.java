/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;

/**
 * Device connection options entity definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceConnectionOptions")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "userCouplingMode",
        "reservedUserId",
}, //
        factoryClass = DeviceConnectionOptionXmlRegistry.class, //
        factoryMethod = "newDeviceConnectionOptions")
public interface DeviceConnectionOption extends KapuaUpdatableEntity {

    public static final String TYPE = "deviceConnectionOptions";

    public default String getType() {
        return TYPE;
    }

    /**
     * Get the device connection user coupling mode.
     *
     * @return
     */
    @XmlElement(name = "userCouplingMode")
    public ConnectionUserCouplingMode getUserCouplingMode();

    /**
     * Set the device connection user coupling mode.
     *
     * @param status
     */
    public void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode);

    /**
     * Get the reserved user identifier
     *
     * @return
     */
    @XmlElement(name = "reservedUserId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getReservedUserId();

    /**
     * Set the reserved user identifier
     *
     * @param reservedUserId
     */
    public void setReservedUserId(KapuaId reservedUserId);
}
