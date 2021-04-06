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

import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

/**
 * {@link InventoryManagementResponseException} to {@code throw} when the {@link KapuaResponseMessage#getResponseCode()} is not {@link KapuaResponseCode#ACCEPTED}.
 *
 * @since 1.5.0
 */
public class DeviceInventoryGetManagementException extends InventoryManagementResponseException {

    private static final long serialVersionUID = -2114589401530177749L;

    /**
     * Constructor.
     *
     * @param responseCode     The {@link KapuaResponseMessage#getResponseCode()}
     * @param exceptionMessage The {@link KapuaResponsePayload#getExceptionMessage()}
     * @param exceptionStack   The {@link KapuaResponsePayload#getExceptionStack()}
     * @since 1.5.0
     */
    public DeviceInventoryGetManagementException(KapuaResponseCode responseCode, String exceptionMessage, String exceptionStack) {
        super(InventoryManagementResponseErrorCodes.INVENTORY_GET_ERROR, responseCode, exceptionMessage, exceptionStack);
    }
}
