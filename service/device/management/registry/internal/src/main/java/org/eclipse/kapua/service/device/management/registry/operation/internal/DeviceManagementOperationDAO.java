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
        DeviceManagementOperationImpl deviceManagementOperationImpl = (DeviceManagementOperationImpl) deviceManagementOperation;

        return ServiceDAO.update(em, DeviceManagementOperationImpl.class, deviceManagementOperationImpl);
    }

    /**
     * Deletes the stepDefinition by stepDefinition identifier
     *
     * @param em
     * @param scopeId
     * @param entityId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link DeviceManagementOperation} is not found
     */
    public static DeviceManagementOperation delete(EntityManager em, KapuaId scopeId, KapuaId entityId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, DeviceManagementOperationImpl.class, scopeId, entityId);
    }

    /**
     * Finds the stepDefinition by stepDefinition identifier
     */
    public static DeviceManagementOperation find(EntityManager em, KapuaId scopeId, KapuaId entityId) {
        return ServiceDAO.find(em, DeviceManagementOperationImpl.class, scopeId, entityId);
    }

    /**
     * Returns the stepDefinition list matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static DeviceManagementOperationListResult query(EntityManager em, KapuaQuery query)
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
    public static long count(EntityManager em, KapuaQuery query)
            throws KapuaException {
        return ServiceDAO.count(em, DeviceManagementOperation.class, DeviceManagementOperationImpl.class, query);
    }

}
