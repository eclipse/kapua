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
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * DeviceManagementOperationNotification xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceManagementOperationXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceManagementOperationRegistryFactory factory = locator.getFactory(DeviceManagementOperationRegistryFactory.class);

    /**
     * Creates a new DeviceManagementOperationNotification instance
     *
     * @return
     */
    public DeviceManagementOperation newDeviceManagementOperation() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new DeviceManagementOperationNotificationCreator instance
     *
     * @return
     */
    public DeviceManagementOperationCreator newDeviceManagementOperationCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new DeviceManagementOperationNotificationListResult instance
     *
     * @return
     */
    public DeviceManagementOperationListResult newDeviceManagementOperationListResult() {
        return factory.newListResult();
    }

    public DeviceManagementOperationQuery newQuery() {
        return factory.newQuery(null);
    }
}
