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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationQuery;

@KapuaProvider
public class ManagementOperationNotificationFactoryImpl implements ManagementOperationNotificationFactory {

    @Override
    public ManagementOperationNotification newEntity(KapuaId scopeId) {
        return new ManagementOperationNotificationImpl(scopeId);
    }

    @Override
    public ManagementOperationNotificationCreator newCreator(KapuaId scopeId) {
        return new ManagementOperationNotificationCreatorImpl(scopeId);
    }

    @Override
    public ManagementOperationNotificationQuery newQuery(KapuaId scopeId) {
        return new ManagementOperationNotificationQueryImpl(scopeId);
    }

    @Override
    public ManagementOperationNotificationListResult newListResult() {
        return new ManagementOperationNotificationListResultImpl();
    }

}
