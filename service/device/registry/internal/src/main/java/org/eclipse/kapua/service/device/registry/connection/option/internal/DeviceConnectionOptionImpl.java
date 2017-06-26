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
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;

/**
 * {@link DeviceConnectionOption} entity.
 * 
 * @since 1.0.0
 */

@Entity(name = "DeviceConnectionOptions")
@Table(name = "dvc_device_connection")
public class DeviceConnectionOptionImpl extends AbstractKapuaUpdatableEntity implements DeviceConnectionOption {

    private static final long serialVersionUID = 8928343233144731836L;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_coupling_mode", nullable = false)
    private ConnectionUserCouplingMode userCouplingMode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "reserved_user_id", nullable = true, updatable = true))
    })
    private KapuaEid reservedUserId;

    /**
     * Constructor
     */
    protected DeviceConnectionOptionImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public DeviceConnectionOptionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public DeviceConnectionOptionImpl(DeviceConnectionOption deviceConnection) throws KapuaException {
        super((KapuaUpdatableEntity) deviceConnection);

        setUserCouplingMode(deviceConnection.getUserCouplingMode());
        setReservedUserId(deviceConnection.getReservedUserId());
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
        this.reservedUserId = KapuaEid.parseKapuaId(reservedUserId);
    }

}
