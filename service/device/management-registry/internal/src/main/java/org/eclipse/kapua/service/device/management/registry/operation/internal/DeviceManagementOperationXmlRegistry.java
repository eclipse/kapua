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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryFactory;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * DeviceManagementOperationNotificationImpl xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceManagementOperationXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceManagementOperationRegistryFactory factory = locator.getFactory(DeviceManagementOperationRegistryFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public DeviceManagementOperation newDeviceManagementOperation() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public DeviceManagementOperationCreator newDeviceManagementOperationCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new job list result instance
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
