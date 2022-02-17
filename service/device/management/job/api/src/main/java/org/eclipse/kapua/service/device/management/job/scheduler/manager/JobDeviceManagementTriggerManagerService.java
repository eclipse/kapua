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
package org.eclipse.kapua.service.device.management.job.scheduler.manager;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.job.scheduler.manager.exception.ProcessOnConnectException;

/**
 * Manager service that handles interactions between Device, {@link org.eclipse.kapua.service.job.Job} and {@link org.eclipse.kapua.service.scheduler.trigger.Trigger}s
 *
 * @since 1.1.0
 */
public interface JobDeviceManagementTriggerManagerService extends KapuaService {

    /**
     * Starts the processing of a {@link org.eclipse.kapua.service.job.Job} for the given Device id, if there are any.
     *
     * @param scopeId  The scope {@link KapuaId} of the Device.
     * @param deviceId The KapuaId of the Device.
     * @throws ProcessOnConnectException when some errors occurs during the processing.
     * @since 1.1.0
     */
    void processOnConnect(KapuaId scopeId, KapuaId deviceId) throws ProcessOnConnectException;
}
