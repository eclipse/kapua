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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Enum with all possible values for exceptions for the {@link org.eclipse.kapua.transport.mqtt} implementation.
 * Each value must be matched with a value in the error messages resource file.
 *
 * @since 1.0.0
 */
public enum MqttClientErrorCodes implements KapuaErrorCode {

    /**
     * Client is connected while doing operation that requires client not connected.
     *
     * @since 1.0.0
     */
    ALREADY_CONNECTED,

    /**
     * Error while adding callback to the client or while awaiting for notify observer.
     *
     * @since 1.2.0
     */
    CALLBACK_SET_ERROR,

    /**
     * Error while cleaning client.
     *
     * @since 1.0.0
     */
    CLEAN_ERROR,

    /**
     * Error while connecting client.
     *
     * @since 1.0.0
     */
    CONNECT_ERROR,

    /**
     * Error while disconnecting client.
     *
     * @since 1.0.0
     */
    DISCONNECT_ERROR,

    /**
     * Client is not connected while doing operation that requires client connected.
     *
     * @since 1.0.0
     */
    NOT_CONNECTED,

    /**
     * Exception while publishing a message.
     *
     * @since 1.0.0
     */
    PUBLISH_EXCEPTION,

    /**
     * Error while subscribing to a topic.
     *
     * @since 1.0.0
     */
    SUBSCRIBE_ERROR,

    /**
     * Error while terminating client.
     *
     * @since 1.0.0
     */
    TERMINATE_ERROR,

    /**
     * Error while unsubscribing from a topic.
     *
     * @since 1.0.0
     */
    UNSUBSCRIBE_ERROR,
}
