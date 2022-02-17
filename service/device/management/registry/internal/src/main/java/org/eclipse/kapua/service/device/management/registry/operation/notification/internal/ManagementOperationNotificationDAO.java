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
package org.eclipse.kapua.service.device.management.registry.operation.notification.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;

/**
 * DeviceManagementOperationNotification DAO
 *
 * @since 1.0
 */
public class ManagementOperationNotificationDAO {

    private ManagementOperationNotificationDAO() {
    }

    /**
     * Creates and return new DeviceManagementOperationNotification
     *
     * @param em
     * @param managementOperationNotificationCreator
     * @return
     * @throws KapuaException
     */
    public static ManagementOperationNotification create(EntityManager em, ManagementOperationNotificationCreator managementOperationNotificationCreator)
            throws KapuaException {
        //
        // Create DeviceManagementOperationNotification
        ManagementOperationNotificationImpl managementOperationNotification = new ManagementOperationNotificationImpl(managementOperationNotificationCreator.getScopeId());
        managementOperationNotification.setOperationId(managementOperationNotificationCreator.getOperationId());
        managementOperationNotification.setSentOn(managementOperationNotificationCreator.getSentOn());
        managementOperationNotification.setStatus(managementOperationNotificationCreator.getStatus());
        managementOperationNotification.setResource(managementOperationNotificationCreator.getResource());
        managementOperationNotification.setProgress(managementOperationNotificationCreator.getProgress());
        managementOperationNotification.setMessage(managementOperationNotificationCreator.getMessage());

        return ServiceDAO.create(em, managementOperationNotification);
    }

    /**
     * Deletes the stepDefinition by stepDefinition identifier
     *
     * @param em
     * @param scopeId
     * @param stepDefinitionId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link ManagementOperationNotification} is not found
     */
    public static ManagementOperationNotification delete(EntityManager em, KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, ManagementOperationNotificationImpl.class, scopeId, stepDefinitionId);
    }

    /**
     * Finds the stepDefinition by stepDefinition identifier
     */
    public static ManagementOperationNotification find(EntityManager em, KapuaId scopeId, KapuaId stepDefinitionId) {
        return ServiceDAO.find(em, ManagementOperationNotificationImpl.class, scopeId, stepDefinitionId);
    }

    /**
     * Returns the stepDefinition list matching the provided query
     *
     * @param em
     * @param stepDefinitionQuery
     * @return
     * @throws KapuaException
     */
    public static ManagementOperationNotificationListResult query(EntityManager em, KapuaQuery stepDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, ManagementOperationNotification.class, ManagementOperationNotificationImpl.class, new ManagementOperationNotificationListResultImpl(), stepDefinitionQuery);
    }

    /**
     * Returns the stepDefinition count matching the provided query
     *
     * @param em
     * @param stepDefinitionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery stepDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, ManagementOperationNotification.class, ManagementOperationNotificationImpl.class, stepDefinitionQuery);
    }

}
