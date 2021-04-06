/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.internal.exception;

import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseException;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

/**
 * Base {@link DeviceManagementResponseException} for {@link DeviceInventoryManagementService}.
 *
 * @since 1.5.0
 */
public abstract class InventoryManagementResponseException extends DeviceManagementResponseException {

    private static final long serialVersionUID = -146419005237479192L;

    /**
     * Constructor.
     *
     * @param code             The {@link InventoryManagementResponseErrorCodes}.
     * @param responseCode     The {@link KapuaResponseMessage#getResponseCode()}
     * @param exceptionMessage The {@link KapuaResponsePayload#getExceptionMessage()}
     * @param exceptionStack   The {@link KapuaResponsePayload#getExceptionStack()}
     * @param arguments        The additional argument associated with the {@link InventoryManagementResponseException}.
     * @since 1.5.0
     */
    public InventoryManagementResponseException(InventoryManagementResponseErrorCodes code, KapuaResponseCode responseCode, String exceptionMessage, String exceptionStack, Object... arguments) {
        super(code, responseCode, exceptionMessage, exceptionStack, arguments);
    }
}
