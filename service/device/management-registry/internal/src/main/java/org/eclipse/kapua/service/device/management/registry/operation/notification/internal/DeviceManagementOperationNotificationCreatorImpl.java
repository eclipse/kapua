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
package org.eclipse.kapua.service.device.management.registry.operation.notification.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationCreator;

import java.util.Date;

public class DeviceManagementOperationNotificationCreatorImpl extends AbstractKapuaEntityCreator<DeviceManagementOperationNotification> implements DeviceManagementOperationNotificationCreator {

    private KapuaId operationId;
    private Date sentOn;
    private OperationStatus status;
    private Integer progress;

    public DeviceManagementOperationNotificationCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(KapuaId operationId) {
        this.operationId = operationId;
    }

    @Override
    public Date getSentOn() {
        return sentOn;
    }

    @Override
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    @Override
    public OperationStatus getOperationStatus() {
        return status;
    }

    @Override
    public void setOperationStatus(OperationStatus status) {
        this.status = status;
    }

    @Override
    public Integer getProgress() {
        return progress;
    }

    @Override
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
