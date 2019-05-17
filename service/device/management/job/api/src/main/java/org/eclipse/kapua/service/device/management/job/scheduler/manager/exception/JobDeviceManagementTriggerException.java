/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
