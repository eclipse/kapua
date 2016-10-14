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
 *
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt;

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
     * The name of the resource file from which to source the error messages.
     */
    private static final String KAPUA_ERROR_MESSAGES = "mqtt-client-error-messages";

    /**
     * Builds a {@link MqttClientException} with the given {@link MqttClientErrorCodes} and {@code null} cause and {@code null} arguments
     * 
     * @param code
     *            The {@link MqttClientErrorCodes} of this exception
     * @since 1.0.0
     */
    public MqttClientException(MqttClientErrorCodes code) {
        super(code);
    }

    /**
     * Builds a {@link MqttClientException} with the given {@link MqttClientErrorCodes} and given arguments and {@code null} cause.
     * 
     * @param code
     *            The {@link MqttClientErrorCodes} of this exception
     * @param arguments
     *            Additional optional arguments to describe the exception.
     * @since 1.0.0
     */
    public MqttClientException(MqttClientErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Builds a {@link MqttClientException} with the given {@link MqttClientErrorCodes} and given cause and given arguments
     * 
     * @param code
     *            The {@link MqttClientErrorCodes} of this exception
     * @param cause
     *            The root cause of the current exception chain.
     * @param arguments
     *            Additional optional arguments to describe the exception.
     * @since 1.0.0
     */
    public MqttClientException(MqttClientErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    /**
     * Return the resource file name from which to source the error messages for {@link MqttClientException} exceptions.
     * 
     * @since 1.0.0
     */
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
