/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
