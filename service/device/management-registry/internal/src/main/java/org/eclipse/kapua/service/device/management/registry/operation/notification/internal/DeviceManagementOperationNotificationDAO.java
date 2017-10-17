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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationListResult;

/**
 * DeviceManagementOperationNotification DAO
 *
 * @since 1.0
 */
public class DeviceManagementOperationNotificationDAO {

    private DeviceManagementOperationNotificationDAO() {
    }

    /**
     * Creates and return new DeviceManagementOperationNotification
     *
     * @param em
     * @param deviceManagementOperationCreator
     * @return
     * @throws KapuaException
     */
    public static DeviceManagementOperationNotification create(EntityManager em, DeviceManagementOperationNotificationCreator deviceManagementOperationCreator)
            throws KapuaException {
        //
        // Create DeviceManagementOperationNotification
        DeviceManagementOperationNotificationImpl deviceManagementOperationNotificationImpl = new DeviceManagementOperationNotificationImpl(deviceManagementOperationCreator.getScopeId());
        deviceManagementOperationNotificationImpl.setOperationId(deviceManagementOperationCreator.getOperationId());
        deviceManagementOperationNotificationImpl.setSentOn(deviceManagementOperationCreator.getSentOn());
        deviceManagementOperationNotificationImpl.setOperationStatus(deviceManagementOperationCreator.getOperationStatus());
        deviceManagementOperationNotificationImpl.setProgress(deviceManagementOperationCreator.getProgress());

        return ServiceDAO.create(em, deviceManagementOperationNotificationImpl);
    }

    /**
     * Deletes the stepDefinition by stepDefinition identifier
     *
     * @param em
     * @param stepDefinitionId
     * @throws KapuaEntityNotFoundException If the {@link DeviceManagementOperationNotification} is not found
     */
    public static void delete(EntityManager em, KapuaId stepDefinitionId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, DeviceManagementOperationNotificationImpl.class, stepDefinitionId);
    }

    /**
     * Finds the stepDefinition by stepDefinition identifier
     */
    public static DeviceManagementOperationNotification find(EntityManager em, KapuaId stepDefinitionId) {
        return em.find(DeviceManagementOperationNotificationImpl.class, stepDefinitionId);
    }

    /**
     * Returns the stepDefinition list matching the provided query
     *
     * @param em
     * @param stepDefinitionQuery
     * @return
     * @throws KapuaException
     */
    public static DeviceManagementOperationNotificationListResult query(EntityManager em, KapuaQuery<DeviceManagementOperationNotification> stepDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, DeviceManagementOperationNotification.class, DeviceManagementOperationNotificationImpl.class, new DeviceManagementOperationNotificationListResultImpl(),
                stepDefinitionQuery);
    }

    /**
     * Returns the stepDefinition count matching the provided query
     *
     * @param em
     * @param stepDefinitionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<DeviceManagementOperationNotification> stepDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, DeviceManagementOperationNotification.class, DeviceManagementOperationNotificationImpl.class, stepDefinitionQuery);
    }

}
