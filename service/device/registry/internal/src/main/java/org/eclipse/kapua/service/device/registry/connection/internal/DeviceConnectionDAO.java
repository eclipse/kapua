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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

public class DeviceConnectionDAO extends ServiceDAO
{

    public static DeviceConnection create(EntityManager em, DeviceConnectionCreator deviceConnectionCreator)
    {
        DeviceConnection deviceConnection = new DeviceConnectionImpl(deviceConnectionCreator.getScopeId());
        deviceConnection.setStatus(DeviceConnectionStatus.CONNECTED);
        deviceConnection.setClientId(deviceConnectionCreator.getClientId());
        deviceConnection.setUserId(deviceConnectionCreator.getUserId());
        deviceConnection.setProtocol(deviceConnectionCreator.getProtocol());
        deviceConnection.setClientIp(deviceConnectionCreator.getClientIp());
        deviceConnection.setServerIp(deviceConnectionCreator.getServerIp());

        return ServiceDAO.create(em, deviceConnection);
    }

    public static DeviceConnection update(EntityManager em, DeviceConnection deviceConnection)
        throws KapuaException
    {
        DeviceConnectionImpl deviceConnectionImpl = (DeviceConnectionImpl) deviceConnection;
        return ServiceDAO.update(em, DeviceConnectionImpl.class, deviceConnectionImpl);
    }

    public static DeviceConnection find(EntityManager em, KapuaId deviceConnectionId)
    {
        return em.find(DeviceConnectionImpl.class, deviceConnectionId);
    }

    public static DeviceConnectionListResult query(EntityManager em, KapuaQuery<DeviceConnection> query)
        throws KapuaException
    {
        return ServiceDAO.query(em, DeviceConnection.class, DeviceConnectionImpl.class, new DeviceConnectionListResultImpl(), query);
    }

    public static long count(EntityManager em, KapuaQuery<DeviceConnection> query)
        throws KapuaException
    {
        return ServiceDAO.count(em, DeviceConnection.class, DeviceConnectionImpl.class, query);
    }

    public static void delete(EntityManager em, KapuaId deviceConnectionId)
    {
        ServiceDAO.delete(em, DeviceConnectionImpl.class, deviceConnectionId);
    }

}
