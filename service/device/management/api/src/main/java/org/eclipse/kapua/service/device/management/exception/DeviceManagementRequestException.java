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

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceManagementException} for request-related problems.
 *
 * @since 1.5.0
 */
public abstract class DeviceManagementRequestException extends DeviceManagementException {

    /**
     * @since 1.5.0
     */
    protected DeviceManagementRequestException(@NotNull DeviceManagementErrorCodes code) {
        super(code);
    }

    /**
     * @since 1.5.0
     */
    protected DeviceManagementRequestException(@NotNull DeviceManagementErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * @since 1.5.0
     */
    protected DeviceManagementRequestException(@NotNull DeviceManagementErrorCodes code, @NotNull Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
