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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.OperationStatus;

import java.util.Date;

public interface DeviceManagementOperationNotification extends KapuaEntity {

    public static final String TYPE = "deviceManagementOperationNotification";

    @Override
    public default String getType() {
        return TYPE;
    }

    public KapuaId getOperationId();

    public void setOperationId(KapuaId operationId);

    public Date getSentOn();

    public void setSentOn(Date sentOn);

    public OperationStatus getOperationStatus();

    public void setOperationStatus(OperationStatus operationStatus);

    public Integer getProgress();

    public void setProgress(Integer progress);

}
