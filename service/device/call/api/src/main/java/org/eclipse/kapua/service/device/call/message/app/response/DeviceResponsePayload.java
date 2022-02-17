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
package org.eclipse.kapua.service.device.call.message.app.response;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppPayload;

/**
 * {@link DeviceResponsePayload} definition.
 *
 * @since 1.0.0
 */
public interface DeviceResponsePayload extends DeviceAppPayload {

    /**
     * Gets the {@link DeviceResponseCode}.
     *
     * @return The {@link DeviceResponseCode}.
     * @since 1.0.0
     */
    <C extends DeviceResponseCode> C getResponseCode();

    /**
     * Gets the response exception message, if {@link #getResponseCode()} is not {@link DeviceResponseCode#isAccepted()}.
     *
     * @return The response exception message.
     * @since 1.0.0
     */
    String getExceptionMessage();

    /**
     * Gets the response exception stack, if {@link #getResponseCode()} is not {@link DeviceResponseCode#isAccepted()}
     *
     * @return The response exception stack.
     * @since 1.0.0
     */
    String getExceptionStack();

    /**
     * Gets the response body.
     *
     * @return The response body.
     * @since 1.0.0
     */
    byte[] getResponseBody();
}
