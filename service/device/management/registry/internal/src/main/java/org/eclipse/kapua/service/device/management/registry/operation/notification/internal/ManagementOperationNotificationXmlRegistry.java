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
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationQuery;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link ManagementOperationNotification} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class ManagementOperationNotificationXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final ManagementOperationNotificationFactory factory = locator.getFactory(ManagementOperationNotificationFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public ManagementOperationNotification newDeviceManagementOperation() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public ManagementOperationNotificationCreator newDeviceManagementOperationCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public ManagementOperationNotificationListResult newDeviceManagementOperationListResult() {
        return factory.newListResult();
    }

    public ManagementOperationNotificationQuery newQuery() {
        return factory.newQuery(null);
    }
}
