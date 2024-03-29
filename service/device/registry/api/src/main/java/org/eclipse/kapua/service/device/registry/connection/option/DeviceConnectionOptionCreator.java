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

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;

/**
 * Device connection options creator service definition.
 *
 * @since 1.0
 */
public interface DeviceConnectionOptionCreator extends KapuaUpdatableEntityCreator<DeviceConnectionOption> {

    /**
     * Get the device connection user coupling mode.
     *
     * @return
     */
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
    KapuaId getReservedUserId();

    /**
     * Set the reserved user identifier
     *
     * @param reservedUserId
     */
    void setReservedUserId(KapuaId reservedUserId);

    /**
     * Gets the allowed authentication type.
     *
     * @return The allowed authentication type.
     * @since 2.0.0
     */
    String getAuthenticationType();

    /**
     * Sets the allowed authentication type.
     *
     * @param authenticationType The allowed authentication type.
     * @since 2.0.0
     */
    void setAuthenticationType(String authenticationType);
}
