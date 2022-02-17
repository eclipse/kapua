/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.exception;

import org.eclipse.kapua.KapuaException;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceCallException} definition.
 * <p>
 * Base class for {@code kapua-device-call-api} {@link Exception}s.
 *
 * @since 1.1.0
 */
public abstract class DeviceCallException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "device-call-error-messages";

    /**
     * @since 1.1.0
     */
    protected DeviceCallException(@NotNull DeviceCallErrorCodes code) {
        super(code);
    }

    /**
     * @since 1.1.0
     */
    protected DeviceCallException(@NotNull DeviceCallErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /*
     * @since 1.1.0
     */
    protected DeviceCallException(@NotNull DeviceCallErrorCodes code, @NotNull Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
