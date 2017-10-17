/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
import org.eclipse.kapua.service.device.management.registry.operation.OperationStatus;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "DeviceManagementOperation")
@Table(name = "dvcm_device_management_operation")
public class DeviceManagementOperationImpl extends AbstractKapuaUpdatableEntity implements DeviceManagementOperation {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "device_id", nullable = false, updatable = false))
    })
    public KapuaEid deviceId;

    @Basic
    @Column(name = "app_id", nullable = false, updatable = false)
    public String appId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, updatable = false)
    public KapuaMethod action;

    @Basic
    @Column(name = "resource", nullable = false, updatable = false)
    public String resource;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status", nullable = false, updatable = true)
    public OperationStatus operationStatus;

    @ElementCollection
    @CollectionTable(name = "dvcm_device_management_operation_input_property", joinColumns = @JoinColumn(name = "operation_id", referencedColumnName = "id"))
    public List<DeviceManagementOperationPropertyImpl> inputProperties;

    protected DeviceManagementOperationImpl() {
        super();
    }

    public DeviceManagementOperationImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(KapuaId deviceId) {
        this.deviceId = KapuaEid.parseKapuaId(deviceId);
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public KapuaMethod getAction() {
        return action;
    }

    @Override
    public void setAction(KapuaMethod action) {
        this.action = action;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    @Override
    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    @Override
    public List<DeviceManagementOperationPropertyImpl> getInputProperties() {
        if (inputProperties == null) {
            inputProperties = new ArrayList<>();
        }

        return inputProperties;
    }

    @Override
    public void setInputProperties(List<DeviceManagementOperationProperty> inputProperties) {
        this.inputProperties = new ArrayList<>();
        if (inputProperties != null) {
            for (DeviceManagementOperationProperty sp : inputProperties) {
                this.inputProperties.add(DeviceManagementOperationPropertyImpl.parse(sp));
            }
        }
    }
}
