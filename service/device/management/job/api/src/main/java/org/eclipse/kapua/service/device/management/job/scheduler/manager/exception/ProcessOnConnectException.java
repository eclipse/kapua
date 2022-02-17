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
package org.eclipse.kapua.service.device.management.job.scheduler.manager.exception;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Exception when processing the event of the Device connecting.
 *
 * @since 1.1.0
 */
public class ProcessOnConnectException extends JobDeviceManagementTriggerException {

    private final KapuaId scopeId;
    private final KapuaId deviceId;

    public ProcessOnConnectException(KapuaId scopeId, KapuaId deviceId, Object... arguments) {
        this(null, scopeId, deviceId, arguments);
    }

    public ProcessOnConnectException(Throwable cause, KapuaId scopeId, KapuaId deviceId, Object... arguments) {
        super(JobDeviceManagementTriggerErrorCodes.PROCESS_ON_CONNECT, cause, scopeId, deviceId, arguments);

        this.scopeId = scopeId;
        this.deviceId = deviceId;
    }

    /**
     * Gets the scope {@link KapuaId}.
     *
     * @return The scope {@link KapuaId}.
     * @since 1.1.0
     */
    public KapuaId getScopeId() {
        return scopeId;
    }

    /**
     * Gets the device {@link KapuaId}.
     *
     * @return The device {@link KapuaId}.
     * @since 1.1.0
     */
    public KapuaId getDeviceId() {
        return deviceId;
    }
}
