/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.manager;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.manager.exception.ManagementOperationNotificationInvalidStatusException;
import org.eclipse.kapua.service.device.management.registry.manager.exception.ManagementOperationNotificationProcessingException;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationRegistryService;

import java.util.Date;

public class DeviceManagementRegistryManager {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);
    private static final DeviceManagementOperationFactory DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(DeviceManagementOperationFactory.class);

    private static final ManagementOperationNotificationRegistryService MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE = LOCATOR.getService(ManagementOperationNotificationRegistryService.class);
    private static final ManagementOperationNotificationFactory MANAGEMENT_OPERATION_NOTIFICATION_FACTORY = LOCATOR.getFactory(ManagementOperationNotificationFactory.class);

    private DeviceManagementRegistryManager() {
    }

    public static void processOperationNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, OperationStatus status, Integer progress) throws ManagementOperationNotificationProcessingException {

        try {
            switch (status) {
                case RUNNING: {
                    processRunningNotification(scopeId, operationId, updateOn, progress);
                }
                break;
                case FAILED: {
                    processFailedNotification(scopeId, operationId, updateOn);
                }
                break;
                case COMPLETED: {
                    processCompletedNotification(scopeId, operationId, updateOn);
                }
                break;
                default: {
                    throw new ManagementOperationNotificationInvalidStatusException(scopeId, operationId, status, updateOn, progress);
                }
            }
        } catch (KapuaException ke) {
            throw new ManagementOperationNotificationProcessingException(ke, scopeId, operationId, status, updateOn, progress);
        }
    }


    private static void processRunningNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, Integer progress) throws KapuaException {
        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);

        ManagementOperationNotificationCreator managementOperationNotificationCreator = MANAGEMENT_OPERATION_NOTIFICATION_FACTORY.newCreator(scopeId);
        managementOperationNotificationCreator.setOperationId(deviceManagementOperation.getId());
        managementOperationNotificationCreator.setStatus(OperationStatus.RUNNING);
        managementOperationNotificationCreator.setSentOn(updateOn);
        managementOperationNotificationCreator.setProgress(progress);

        MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE.create(managementOperationNotificationCreator);
    }

    private static void processFailedNotification(KapuaId scopeId, KapuaId operationId, Date updateOn) throws KapuaException {
        closeDeviceManagementOperarion(scopeId, operationId, updateOn, OperationStatus.FAILED);
    }

    private static void processCompletedNotification(KapuaId scopeId, KapuaId operationId, Date updateOn) throws KapuaException {
        closeDeviceManagementOperarion(scopeId, operationId, updateOn, OperationStatus.COMPLETED);
    }

    private static void closeDeviceManagementOperarion(KapuaId scopeId, KapuaId operationId, Date updateOn, OperationStatus finalStatus) throws KapuaException {
        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);
        deviceManagementOperation.setEndedOn(updateOn);
        deviceManagementOperation.setStatus(finalStatus);

        DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.update(deviceManagementOperation);

        ManagementOperationNotificationQuery query = MANAGEMENT_OPERATION_NOTIFICATION_FACTORY.newQuery(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));

        ManagementOperationNotificationListResult notifications = MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE.query(query);

        for (ManagementOperationNotification mon : notifications.getItems()) {
            MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE.delete(mon.getScopeId(), mon.getId());
        }
    }

    private static DeviceManagementOperation getDeviceManagementOperation(KapuaId scopeId, KapuaId operationId) throws KapuaException {
        DeviceManagementOperationQuery query = DEVICE_MANAGEMENT_OPERATION_FACTORY.newQuery(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.OPERATION_ID, operationId));

        DeviceManagementOperationListResult operations = DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.query(query);
        DeviceManagementOperation deviceManagementOperation = operations.getFirstItem();

        if (deviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, operationId);
        }

        return deviceManagementOperation;
    }
}
