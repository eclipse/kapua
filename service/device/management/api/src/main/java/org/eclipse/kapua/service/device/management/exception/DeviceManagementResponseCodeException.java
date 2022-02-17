/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * {@link DeviceManagementResponseException} when a {@link KapuaResponseMessage#getResponseCode()} is not {@link KapuaResponseCode#ACCEPTED}
 *
 * @since 1.5.0
 */
public abstract class DeviceManagementResponseCodeException extends DeviceManagementResponseException {

    private final KapuaResponseCode responseCode;
    private final String errorMessage;
    private final String stacktrace;

    /**
     * @since 1.5.0
     */
    protected DeviceManagementResponseCodeException(@NotNull DeviceManagementErrorCodes code, @NotNull KapuaResponseCode responseCode, @Null String errorMessage, @Null String stacktrace) {
        super(code, null, responseCode, errorMessage, stacktrace);

        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
        this.stacktrace = stacktrace;
    }

    public KapuaResponseCode getResponseCode() {
        return responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStacktrace() {
        return stacktrace;
    }
}
