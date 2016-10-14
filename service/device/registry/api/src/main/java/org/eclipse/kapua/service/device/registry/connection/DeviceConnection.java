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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

public interface DeviceConnection extends KapuaUpdatableEntity
{
    public static final String TYPE = "deviceConnection";

    default public String getType()
    {
        return TYPE;
    }

    public DeviceConnectionStatus getStatus();

    public void setStatus(DeviceConnectionStatus connectionStatus);

    public String getClientId();

    public void setClientId(String clientId);

    public KapuaId getUserId();

    public void setUserId(KapuaId userId);

    public String getProtocol();

    public void setProtocol(String protocol);

    public String getClientIp();

    public void setClientIp(String clientIp);

    public String getServerIp();

    public void setServerIp(String serverIp);
}
