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
package org.eclipse.kapua.service.device.call.exception;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceCallException} to throw when the response is not received within the given timeout.
 *
 * @since 1.1.0
 */
public class DeviceCallTimeoutException extends DeviceCallException {

    private final Long timeout;

    /**
     * Constructor.
     *
     * @param cause   the root cause of the {@link Exception}.
     * @param timeout The timeout of the request.
     * @since 1.1.0
     */
    public DeviceCallTimeoutException(@NotNull Throwable cause, Long timeout) {
        super(DeviceCallErrorCodes.TIMEOUT, cause, timeout);

        this.timeout = timeout;
    }

    /**
     * Gets the timeout of the request.
     *
     * @return The timeout of the request.
     * @since 1.1.0
     */
    public Long getTimeout() {
        return timeout;
    }
}
