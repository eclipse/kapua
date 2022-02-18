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

import org.eclipse.kapua.service.device.call.exception.DeviceCallSendException;
import org.eclipse.kapua.service.device.call.exception.DeviceCallTimeoutException;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;

import org.checkerframework.checker.nullness.qual.Nullable;
import javax.validation.constraints.NotNull;

/**
 * {@link DeviceCall} definition.
 * <p>
 * It can send {@link DeviceRequestMessage}s and (optionally) wait for a {@link DeviceResponseMessage}.
 *
 * @param <RQ> {@link DeviceRequestMessage} type
 * @param <RS> {@link DeviceResponseMessage} type
 * @since 1.0.0
 */
public interface DeviceCall<RQ extends DeviceRequestMessage<?, ?>, RS extends DeviceResponseMessage<?, ?>> {

    /**
     * Performs a 'read' request.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @return The {@link DeviceResponseMessage}.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    RS read(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Performs a 'create' request.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @return The {@link DeviceResponseMessage}.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    RS create(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Performs a 'write' request.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @return The {@link DeviceResponseMessage}.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    RS write(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Performs a 'delete' request.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @return The {@link DeviceResponseMessage}.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    RS delete(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Performs an 'execute' request.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @return The {@link DeviceResponseMessage}.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    RS execute(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Performs an 'options' request.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @return The {@link DeviceResponseMessage}.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    RS options(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Performs a SUBMIT request.
     * <p>
     * It does not expect a {@link DeviceResponseMessage}.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.3.0
     */
    RS submit(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Performs a CANCEL request.
     * <p>
     * It does not expect a {@link DeviceResponseMessage}.
     *
     * @param requestMessage The {@link DeviceRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @throws DeviceCallTimeoutException if waiting of the response goes on timeout.
     * @throws DeviceCallSendException    if sending the request produces any error.
     * @since 1.3.0
     */
    RS cancel(@NotNull RQ requestMessage, @Nullable Long timeout) throws DeviceCallTimeoutException, DeviceCallSendException;

    /**
     * Get the {@link DeviceMessage} type.
     *
     * @return The {@link DeviceMessage} {@code class}
     * @since 1.0.0
     */
    <M extends DeviceMessage<?, ?>> Class<M> getBaseMessageClass();
}
