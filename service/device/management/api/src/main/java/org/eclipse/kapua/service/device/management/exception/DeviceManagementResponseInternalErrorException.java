/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

/**
 * {@link DeviceManagementResponseCodeException} when a {@link KapuaResponseMessage#getResponseCode()} is {@link KapuaResponseCode#INTERNAL_ERROR}.
 *
 * @since 1.0.0
 */
public class DeviceManagementResponseInternalErrorException extends DeviceManagementResponseCodeException {

    private static final long serialVersionUID = -6932782062611595148L;

    public DeviceManagementResponseInternalErrorException(KapuaResponseCode kapuaResponseCode, String errorMessage, String stacktrace) {
        super(DeviceManagementErrorCodes.RESPONSE_INTERNAL_ERROR, kapuaResponseCode, errorMessage, stacktrace);
    }
}
