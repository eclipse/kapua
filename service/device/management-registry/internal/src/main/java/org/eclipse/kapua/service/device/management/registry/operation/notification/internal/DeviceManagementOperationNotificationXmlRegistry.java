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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationRegistryFactory;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * DeviceManagementOperationNotificationImpl xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceManagementOperationNotificationXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceManagementOperationNotificationRegistryFactory factory = locator.getFactory(DeviceManagementOperationNotificationRegistryFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public DeviceManagementOperationNotification newDeviceManagementOperation() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public DeviceManagementOperationNotificationCreator newDeviceManagementOperationCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public DeviceManagementOperationNotificationListResult newDeviceManagementOperationListResult() {
        return factory.newListResult();
    }

    public DeviceManagementOperationNotificationQuery newQuery() {
        return factory.newQuery(null);
    }
}
