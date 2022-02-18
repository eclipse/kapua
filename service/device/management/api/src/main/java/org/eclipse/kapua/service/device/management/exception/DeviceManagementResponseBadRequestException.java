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
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;

/**
 * {@link DeviceManagementResponseCodeException} when a {@link KapuaResponseMessage#getResponseCode()} is {@link KapuaResponseCode#BAD_REQUEST}.
 *
 * @since 1.0.0
 */
public class DeviceManagementResponseBadRequestException extends DeviceManagementResponseCodeException {

    private static final long serialVersionUID = 768897121791826654L;

    public DeviceManagementResponseBadRequestException(KapuaResponseCode kapuaResponseCode, String errorMessage, String stacktrace) {
        super(DeviceManagementErrorCodes.RESPONSE_BAD_REQUEST, kapuaResponseCode, errorMessage, stacktrace);
    }
}
