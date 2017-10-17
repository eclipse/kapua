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
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.KapuaMethod;

import java.util.List;

public interface DeviceManagementOperation extends KapuaUpdatableEntity {

    public static final String TYPE = "deviceManagementOperation";

    @Override
    public default String getType() {
        return TYPE;
    }

    public KapuaId getDeviceId();

    public void setDeviceId(KapuaId deviceId);

    public String getAppId();

    public void setAppId(String appId);

    public KapuaMethod getAction();

    public void setAction(KapuaMethod action);

    public String getResource();

    public void setResource(String resource);

    public OperationStatus getOperationStatus();

    public void setOperationStatus(OperationStatus operationStatus);

    public <P extends DeviceManagementOperationProperty> List<P> getInputProperties();

    public void setInputProperties(List<DeviceManagementOperationProperty> inputProperties);

}
