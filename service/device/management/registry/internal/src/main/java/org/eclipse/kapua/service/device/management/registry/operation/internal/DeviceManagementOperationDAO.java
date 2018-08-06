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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;

/**
 * DeviceManagementOperationNotification DAO
 *
 * @since 1.0
 */
public class DeviceManagementOperationDAO {

    private DeviceManagementOperationDAO() {
    }

    /**
     * Creates and return new DeviceManagementOperationNotification
     *
     * @param em
     * @param deviceManagementOperationCreator
     * @return
     * @throws KapuaException
     */
    public static DeviceManagementOperation create(EntityManager em, DeviceManagementOperationCreator deviceManagementOperationCreator)
            throws KapuaException {
        //
        // Create DeviceManagementOperationNotification

        DeviceManagementOperationImpl deviceManagementOperationImpl = new DeviceManagementOperationImpl(deviceManagementOperationCreator.getScopeId());
        deviceManagementOperationImpl.setStartedOn(deviceManagementOperationCreator.getStartedOn());
        deviceManagementOperationImpl.setDeviceId(deviceManagementOperationCreator.getDeviceId());
        deviceManagementOperationImpl.setOperationId(deviceManagementOperationCreator.getOperationId());
        deviceManagementOperationImpl.setAppId(deviceManagementOperationCreator.getAppId());
        deviceManagementOperationImpl.setAction(deviceManagementOperationCreator.getAction());
        deviceManagementOperationImpl.setResource(deviceManagementOperationCreator.getResource());
        deviceManagementOperationImpl.setStatus(deviceManagementOperationCreator.getStatus());
        deviceManagementOperationImpl.setStatus(deviceManagementOperationCreator.getStatus());
        deviceManagementOperationImpl.setInputProperties(deviceManagementOperationCreator.getInputProperties());

        return ServiceDAO.create(em, deviceManagementOperationImpl);
    }

    /**
     * Updates the provided stepDefinition
     *
     * @param em
     * @param deviceManagementOperation
     * @return
     * @throws KapuaException
     */
    public static DeviceManagementOperation update(EntityManager em, DeviceManagementOperation deviceManagementOperation)
            throws KapuaException {
        //
        // Update stepDefinition
        DeviceManagementOperationImpl deviceManagementOperationImpl = (DeviceManagementOperationImpl) deviceManagementOperation;

        return ServiceDAO.update(em, DeviceManagementOperationImpl.class, deviceManagementOperationImpl);
    }

    /**
     * Deletes the stepDefinition by stepDefinition identifier
     *
     * @param em
     * @param entityId
     * @throws KapuaEntityNotFoundException If the {@link DeviceManagementOperation} is not found
     */
    public static void delete(EntityManager em, KapuaId entityId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, DeviceManagementOperationImpl.class, entityId);
    }

    /**
     * Finds the stepDefinition by stepDefinition identifier
     */
    public static DeviceManagementOperation find(EntityManager em, KapuaId entityId) {
        return em.find(DeviceManagementOperationImpl.class, entityId);
    }

    /**
     * Returns the stepDefinition list matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static DeviceManagementOperationListResult query(EntityManager em, KapuaQuery<DeviceManagementOperation> query)
            throws KapuaException {
        return ServiceDAO.query(em, DeviceManagementOperation.class, DeviceManagementOperationImpl.class, new DeviceManagementOperationListResultImpl(), query);
    }

    /**
     * Returns the stepDefinition count matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<DeviceManagementOperation> query)
            throws KapuaException {
        return ServiceDAO.count(em, DeviceManagementOperation.class, DeviceManagementOperationImpl.class, query);
    }

}
