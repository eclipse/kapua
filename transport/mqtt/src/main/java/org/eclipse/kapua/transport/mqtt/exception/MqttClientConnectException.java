/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.mqtt.MqttClient;

import java.net.URI;

/**
 * {@link Exception} to {@code throw} when the {@link MqttClient} cannot connect to the given URI with the given {@link TransportClientConnectOptions}
 *
 * @since 1.2.0
 */
public class MqttClientConnectException extends MqttClientException {

    final String username;
    final URI uri;

    /**
     * Constructor.
     *
     * @param uri      The {@link URI} of the server which the {@link MqttClient} was connecting to.
     * @param clientId The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientConnectException}.
     * @param username The username used to authenticate into the server.
     * @since 1.2.0
     */
    public MqttClientConnectException(String clientId, String username, URI uri) {
        this(null, clientId, username, uri);
    }

    /**
     * Constructor.
     *
     * @param cause    The root {@link Throwable} that caused the error.
     * @param uri      The {@link URI} of the server which the {@link MqttClient} was connecting to.
     * @param clientId The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientConnectException}.
     * @param username The username used to authenticate into the server.
     * @since 1.2.0
     */
    public MqttClientConnectException(Throwable cause, String clientId, String username, URI uri) {
        super(MqttClientErrorCodes.CONNECT_ERROR, cause, clientId, username, uri);

        this.uri = uri;
        this.username = username;
    }

    /**
     * Gets the username used to authenticate into the server.
     *
     * @return The username used to authenticate into the server.
     * @since 1.2.0
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the {@link URI} of the server which the {@link MqttClient} was connecting to.
     *
     * @return The {@link URI} of the server which the {@link MqttClient} was connecting to.
     * @since 1.2.0
     */
    public URI getUri() {
        return uri;
    }
}
