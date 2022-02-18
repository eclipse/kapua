/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceManagementOperationCreatorImpl extends AbstractKapuaEntityCreator<DeviceManagementOperation> implements DeviceManagementOperationCreator {

    private Date startedOn;
    private KapuaEid deviceId;
    private KapuaEid operationId;
    private String appId;
    private KapuaMethod action;
    private String resource;
    private NotifyStatus status;
    private List<DeviceManagementOperationProperty> inputProperties;

    public DeviceManagementOperationCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public Date getStartedOn() {
        return startedOn;
    }

    @Override
    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    @Override
    public KapuaEid getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(KapuaId deviceId) {
        this.deviceId = KapuaEid.parseKapuaId(deviceId);
    }

    @Override
    public KapuaEid getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(KapuaId operationId) {
        this.operationId = KapuaEid.parseKapuaId(operationId);
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
        return action.normalizeAction();
    }

    @Override
    public void setAction(KapuaMethod action) {
        this.action = action.normalizeAction();
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
    public NotifyStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(NotifyStatus status) {
        this.status = status;
    }

    @Override
    public List<DeviceManagementOperationProperty> getInputProperties() {
        if (inputProperties == null) {
            inputProperties = new ArrayList<>();
        }

        return inputProperties;
    }

    @Override
    public void setInputProperties(List<DeviceManagementOperationProperty> inputProperties) {
        this.inputProperties = inputProperties;
    }
}
