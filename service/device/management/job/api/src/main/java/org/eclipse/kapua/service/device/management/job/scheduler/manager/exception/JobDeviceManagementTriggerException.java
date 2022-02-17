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

import org.eclipse.kapua.KapuaException;

public abstract class JobDeviceManagementTriggerException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "job-device-management-trigger-service-error-messages";

    public JobDeviceManagementTriggerException(JobDeviceManagementTriggerErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    public JobDeviceManagementTriggerException(JobDeviceManagementTriggerErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
