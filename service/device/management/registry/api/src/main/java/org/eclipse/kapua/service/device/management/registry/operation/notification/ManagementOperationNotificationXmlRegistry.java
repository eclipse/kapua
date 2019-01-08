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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link ManagementOperationNotification} xml MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_FACTORY class
 *
 * @since 1.0
 */
@XmlRegistry
public class ManagementOperationNotificationXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final ManagementOperationNotificationFactory MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_FACTORY = LOCATOR.getFactory(ManagementOperationNotificationFactory.class);

    ManagementOperationNotification newManagementOperationNotification() {
        return MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_FACTORY.newEntity(null);
    }

    ManagementOperationNotificationCreator newManagementOperationNotificationCreator() {
        return MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_FACTORY.newCreator(null);
    }


    ManagementOperationNotificationListResult newManagementOperationNotificationListResult() {
        return MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_FACTORY.newListResult();
    }

    ManagementOperationNotificationQuery newQuery() {
        return MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_FACTORY.newQuery(null);
    }
}
