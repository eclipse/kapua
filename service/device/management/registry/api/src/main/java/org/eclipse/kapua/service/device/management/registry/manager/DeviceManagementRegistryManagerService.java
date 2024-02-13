/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.manager;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;
import org.eclipse.kapua.service.device.management.registry.manager.exception.ManagementOperationNotificationProcessingException;

import java.util.Date;

public interface DeviceManagementRegistryManagerService extends KapuaService {

    void processOperationNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource, NotifyStatus status, Integer progress, String message) throws ManagementOperationNotificationProcessingException;
}
