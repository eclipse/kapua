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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Enum with all possible values for exceptions for the {@link org.eclipse.kapua.transport.amqp} implementation.
 * Each value must be matched with a value in the error messages resource file.
 *
 * @since 1.0.0
 */
public enum AmqpClientErrorCodes implements KapuaErrorCode {

    /**
     * Error while publishing a message.
     * 
     * @since 1.0.0
     */
    CLIENT_PUBLISH_ERROR,

    /**
     * Error while subscribing to a topic.
     * 
     * @since 1.0.0
     */
    CLIENT_SUBSCRIBE_ERROR,

    /**
     * Error while unsubscribing from a topic.
     * 
     * @since 1.0.0
     */
    CLIENT_UNSUBSCRIBE_ERROR,

    /**
     * Error while adding callback to the client or while awaiting for notify observer.
     * 
     * @since 1.0.0
     */
    CLIENT_CALLBACK_ERROR,

    /**
     * Timeout expired while waiting for device answer.
     * 
     * @since 1.0.0
     */
    CLIENT_TIMEOUT_EXCEPTION,

    /**
     * Error while connecting client.
     * 
     * @since 1.0.0
     */
    CLIENT_CONNECT_ERROR,

    /**
     * Error while cleaning client.
     * 
     * @since 1.0.0
     */
    CLIENT_CLEAN_ERROR,

    /**
     * Error while disconnecting client.
     * 
     * @since 1.0.0
     */
    CLIENT_DISCONNECT_ERROR,

    /**
     * Client has lost connection.
     */
    CLIENT_CONNECTION_LOST,

    /**
     * Error while terminating client.
     * 
     * @since 1.0.0
     */
    CLIENT_TERMINATE_ERROR,

    /**
     * Client is not connected while doing operation that requires client connected.
     * 
     * @since 1.0.0
     */
    CLIENT_NOT_CONNECTED,

    /**
     * Client is connected while doing operation that requires client not connected.
     * 
     * @since 1.0.0
     */
    CLIENT_ALREADY_CONNECTED,

    /**
     * Generic exception while sending a request to a device.
     * 
     * @since 1.0.0
     */
    SEND_ERROR
}
