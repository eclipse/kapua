/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.KapuaException;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceManagementException} definition.
 * <p>
 * Base class for {@code kapua-device-call-api} {@link Exception}s.
 *
 * @since 1.1.0
 */
public abstract class DeviceManagementException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "device-management-error-messages";
    private static final long serialVersionUID = 8255502521649641255L;

    /**
     * @since 1.1.0
     */
    protected DeviceManagementException(@NotNull DeviceManagementErrorCodes code) {
        super(code);
    }

    /**
     * @since 1.1.0
     */
    protected DeviceManagementException(@NotNull DeviceManagementErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /*
     * @since 1.1.0
     */
    protected DeviceManagementException(@NotNull DeviceManagementErrorCodes code, @NotNull Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
