/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionCreator;

/**
 * Device connection creator service implementation.
 *
 * @since 1.0
 */
public class DeviceConnectionOptionCreatorImpl extends AbstractKapuaEntityCreator<DeviceConnectionOption> implements DeviceConnectionOptionCreator {

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
