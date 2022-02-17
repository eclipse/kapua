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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * {@link DeviceConnectionOption} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "DeviceConnectionOptions")
@Table(name = "dvc_device_connection")
public class DeviceConnectionOptionImpl extends AbstractKapuaUpdatableEntity implements DeviceConnectionOption {

    private static final long serialVersionUID = 8928343233144731836L;

    @Basic
    @Column(name = "allow_user_change", nullable = false, updatable = true)
    private boolean allowUserChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_coupling_mode", nullable = false)
    private ConnectionUserCouplingMode userCouplingMode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "reserved_user_id", nullable = true, updatable = true))
    })
    private KapuaEid reservedUserId;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected DeviceConnectionOptionImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link DeviceConnectionOption}
     * @since 1.0.0
     */
    public DeviceConnectionOptionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param deviceConnectionOptions
     * @throws KapuaException
     * @since 1.0.0
     */
    public DeviceConnectionOptionImpl(DeviceConnectionOption deviceConnectionOptions) throws KapuaException {
        super(deviceConnectionOptions);

        setAllowUserChange(deviceConnectionOptions.getAllowUserChange());
        setUserCouplingMode(deviceConnectionOptions.getUserCouplingMode());
        setReservedUserId(deviceConnectionOptions.getReservedUserId());
    }

    @Override
    public boolean getAllowUserChange() {
        return allowUserChange;
    }

    @Override
    public void setAllowUserChange(boolean allowUserChange) {
        this.allowUserChange = allowUserChange;
    }

    @Override
    public ConnectionUserCouplingMode getUserCouplingMode() {
        return userCouplingMode;
    }

    @Override
    public void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode) {
        this.userCouplingMode = userCouplingMode;
    }

    @Override
    public KapuaId getReservedUserId() {
        return reservedUserId;
    }

    @Override
    public void setReservedUserId(KapuaId reservedUserId) {
        this.reservedUserId = KapuaEid.parseKapuaId(reservedUserId);
    }
}
