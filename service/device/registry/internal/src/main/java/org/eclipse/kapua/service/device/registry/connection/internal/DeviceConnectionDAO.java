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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;

/**
 * Device connection DAO
 *
 * @since 1.0
 */
public class DeviceConnectionDAO extends ServiceDAO {

    /**
     * Create a new device connection
     *
     * @param em
     * @param deviceConnectionCreator
     * @return
     */
    public static DeviceConnection create(EntityManager em, DeviceConnectionCreator deviceConnectionCreator) {
        DeviceConnection deviceConnection = new DeviceConnectionImpl(deviceConnectionCreator.getScopeId());
        deviceConnection.setStatus(deviceConnectionCreator.getStatus());
        deviceConnection.setClientId(deviceConnectionCreator.getClientId());
        deviceConnection.setUserId(deviceConnectionCreator.getUserId());
        deviceConnection.setUserCouplingMode(deviceConnectionCreator.getUserCouplingMode());
        deviceConnection.setReservedUserId(deviceConnectionCreator.getReservedUserId());
        deviceConnection.setAllowUserChange(deviceConnectionCreator.getAllowUserChange());
        deviceConnection.setProtocol(deviceConnectionCreator.getProtocol());
        deviceConnection.setClientIp(deviceConnectionCreator.getClientIp());
        deviceConnection.setServerIp(deviceConnectionCreator.getServerIp());

        return ServiceDAO.create(em, deviceConnection);
    }

    /**
     * Update the provided device connection
     *
     * @param em
     * @param deviceConnection
     * @return
     * @throws KapuaEntityNotFoundException If the {@link DeviceConnection} is not found.
     */
    public static DeviceConnection update(EntityManager em, DeviceConnection deviceConnection)
            throws KapuaException {
        DeviceConnectionImpl deviceConnectionImpl = (DeviceConnectionImpl) deviceConnection;
        return ServiceDAO.update(em, DeviceConnectionImpl.class, deviceConnectionImpl);
    }

    /**
     * Find the device connection by device connection identifier
     *
     * @param em
     * @param scopeId
     * @param deviceConnectionId
     * @return
     */
    public static DeviceConnection find(EntityManager em, KapuaId scopeId, KapuaId deviceConnectionId) {
        return ServiceDAO.find(em, DeviceConnectionImpl.class, scopeId, deviceConnectionId);
    }

    /**
     * Return the device connection list matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static DeviceConnectionListResult query(EntityManager em, KapuaQuery query)
            throws KapuaException {
        return ServiceDAO.query(em, DeviceConnection.class, DeviceConnectionImpl.class, new DeviceConnectionListResultImpl(), query);
    }

    /**
     * Return the device connection count matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery query)
            throws KapuaException {
        return ServiceDAO.count(em, DeviceConnection.class, DeviceConnectionImpl.class, query);
    }

    /**
     * Delete the device connection by device connection identifier
     *
     * @param em
     * @param scopeId
     * @param deviceConnectionId
     * @return the deleted {@link DeviceConnection}
     * @throws KapuaEntityNotFoundException If the {@link DeviceConnection} is not found.
     */
    public static DeviceConnection delete(EntityManager em, KapuaId scopeId, KapuaId deviceConnectionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, DeviceConnectionImpl.class, scopeId, deviceConnectionId);
    }

}
