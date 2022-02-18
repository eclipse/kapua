/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.job.manager;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.job.targets.JobTarget;

import java.util.Date;

/**
 * Default logic to process a {@link org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification}
 * from a Device after a DEPLOY-V2 has started.
 *
 * @since 1.1.0
 */
public interface JobDeviceManagementOperationManagerService extends KapuaService {


    /**
     * When a {@link ManagementOperationNotification} with {@link NotifyStatus#COMPLETED} or {@link NotifyStatus#FAILED} tries to look if this {@link DeviceManagementOperation}
     * is associated with a {@link org.eclipse.kapua.service.job.Job}.
     * <p>
     * If the associaton is found, the related {@link JobTarget#getStatus()} is updated with the result and eventually starts the {@link org.eclipse.kapua.service.job.Job} to continue
     * the processing of the {@link JobTarget}.
     *
     * @param scopeId     The scope {@link KapuaId} of the {@link ManagementOperationNotification}.
     * @param operationId The {@link ManagementOperationNotification#getOperationId()}.
     * @param updateOn    The {@link Date} that the {@link ManagementOperationNotification} arrived.
     * @param resource    The {@link ManagementOperationNotification#getResource()}.
     * @param status      The {@link ManagementOperationNotification#getStatus()}
     * @throws KapuaException If something goes bad.
     * @since 1.1.0
     */
    void processJobTargetOnNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource, NotifyStatus status) throws KapuaException;
}
