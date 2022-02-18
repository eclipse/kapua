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
package org.eclipse.kapua.transport.mqtt.exception;

import org.eclipse.kapua.KapuaException;

/**
 * Class that extends {@link KapuaException} with a specialized set of exceptions
 * for the {@link org.eclipse.kapua.transport.mqtt} implementation.
 *
 * @since 1.0.0
 */
public class MqttClientException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    /**
     * The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientException}.
     *
     * @since 1.2.0
     */
    private final String clientId;

    /**
     * The name of the resource file from which to source the error messages.
     */
    private static final String KAPUA_ERROR_MESSAGES = "mqtt-client-error-messages";

    /**
     * Builds a {@link MqttClientException} with the given {@link MqttClientErrorCodes} and {@code null} cause and {@code null} arguments
     *
     * @param code The {@link MqttClientErrorCodes} of this exception
     * @since 1.0.0
     */
    public MqttClientException(MqttClientErrorCodes code, String clientId) {
        this(code, clientId, (Object) null);
    }

    /**
     * Builds a {@link MqttClientException} with the given {@link MqttClientErrorCodes} and given arguments and {@code null} cause.
     *
     * @param code      The {@link MqttClientErrorCodes} of this exception
     * @param arguments Additional optional arguments to describe the exception.
     * @since 1.0.0
     */
    public MqttClientException(MqttClientErrorCodes code, String clientId, Object... arguments) {
        this(code, null, clientId, arguments);
    }

    /**
     * Builds a {@link MqttClientException} with the given {@link MqttClientErrorCodes} and given cause and given arguments
     *
     * @param code      The {@link MqttClientErrorCodes} of this exception
     * @param cause     The root cause of the current exception chain.
     * @param arguments Additional optional arguments to describe the exception.
     * @since 1.0.0
     */
    public MqttClientException(MqttClientErrorCodes code, Throwable cause, String clientId, Object... arguments) {
        super(code, cause, clientId, arguments);

        this.clientId = clientId;
    }

    /**
     * Gets the clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientException}.
     *
     * @return The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientException}.
     * @since 1.2.0
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Return the resource file name from which to source the error messages for {@link MqttClientException} exceptions.
     *
     * @since 1.0.0
     */
    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
