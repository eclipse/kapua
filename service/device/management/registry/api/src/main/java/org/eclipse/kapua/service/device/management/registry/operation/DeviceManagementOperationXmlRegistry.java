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
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceManagementOperation} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceManagementOperationXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceManagementOperationFactory factory = locator.getFactory(DeviceManagementOperationFactory.class);

    DeviceManagementOperation newDeviceManagementOperation() {
        return factory.newEntity(null);
    }

    DeviceManagementOperationCreator newDeviceManagementOperationCreator() {
        return factory.newCreator(null);
    }

    DeviceManagementOperationListResult newDeviceManagementOperationListResult() {
        return factory.newListResult();
    }

    DeviceManagementOperationQuery newQuery() {
        return factory.newQuery(null);
    }
}
