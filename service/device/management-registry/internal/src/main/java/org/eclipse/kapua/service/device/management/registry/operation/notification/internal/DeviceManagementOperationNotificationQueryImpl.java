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

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationQuery;

/**
 * {@link DeviceManagementOperationNotificationQueryImpl} definition.
 *
 * @since 1.0
 */
public class DeviceManagementOperationNotificationQueryImpl extends AbstractKapuaQuery<DeviceManagementOperationNotification> implements DeviceManagementOperationNotificationQuery {

    private DeviceManagementOperationNotificationQueryImpl() {
        super();
    }

    public DeviceManagementOperationNotificationQueryImpl(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

}
