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
package org.eclipse.kapua.service.device.call;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceCallback} definition.
 *
 * @param <RSM> The {@link org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage} type
 * @since 1.0.0
 */
public interface DeviceCallback<RSM extends DeviceMessage<?, ?>> {

    /**
     * Action to be invoked on {@link DeviceMessage} received.
     *
     * @param response The received {@link DeviceMessage}.
     * @since 1.0.0
     */
    void responseReceived(@NotNull RSM response);

    /**
     * Action to be invoked on timed out
     *
     * @since 1.0.0
     */
    void timedOut();

}
