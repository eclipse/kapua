/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.amqp;

import org.eclipse.kapua.KapuaException;

/**
 * Class that extends {@link KapuaException} with a specialized set of exceptions
 * for the {@link org.eclipse.kapua.transport.amqp} implementation.
 *
 * @since 1.0.0
 */
public class AmqpClientException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    /**
     * The name of the resource file from which to source the error messages.
     */
    private static final String KAPUA_ERROR_MESSAGES = "amqp-client-error-messages";

    /**
     * Builds a {@link AmqpClientException} with the given {@link AmqpClientErrorCodes} and {@code null} cause and {@code null} arguments
     * 
     * @param code
     *            The {@link AmqpClientErrorCodes} of this exception
     * @since 1.0.0
     */
    public AmqpClientException(AmqpClientErrorCodes code) {
        super(code);
    }

    /**
     * Builds a {@link AmqpClientException} with the given {@link AmqpClientErrorCodes} and given arguments and {@code null} cause.
     * 
     * @param code
     *            The {@link AmqpClientErrorCodes} of this exception
     * @param arguments
     *            Additional optional arguments to describe the exception.
     * @since 1.0.0
     */
    public AmqpClientException(AmqpClientErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Builds a {@link AmqpClientException} with the given {@link AmqpClientErrorCodes} and given cause and given arguments
     * 
     * @param code
     *            The {@link AmqpClientErrorCodes} of this exception
     * @param cause
     *            The root cause of the current exception chain.
     * @param arguments
     *            Additional optional arguments to describe the exception.
     * @since 1.0.0
     */
    public AmqpClientException(AmqpClientErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    /**
     * Return the resource file name from which to source the error messages for {@link AmqpClientException} exceptions.
     * 
     * @since 1.0.0
     */
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
