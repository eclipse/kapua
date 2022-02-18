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
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionCreator;

/**
 * Device connection creator service implementation.
 *
 * @since 1.0
 */
public class DeviceConnectionOptionCreatorImpl extends AbstractKapuaUpdatableEntityCreator<DeviceConnectionOption> implements DeviceConnectionOptionCreator {

    private static final long serialVersionUID = 2740394157765904615L;

    private ConnectionUserCouplingMode userCouplingMode;
    private KapuaId reservedUserId;

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceConnectionOptionCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public ConnectionUserCouplingMode getUserCouplingMode() {
        return userCouplingMode;
    }

    public void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode) {
        this.userCouplingMode = userCouplingMode;
    }

    public KapuaId getReservedUserId() {
        return reservedUserId;
    }

    public void setReservedUserId(KapuaId reservedUserId) {
        this.reservedUserId = reservedUserId;
    }
}
