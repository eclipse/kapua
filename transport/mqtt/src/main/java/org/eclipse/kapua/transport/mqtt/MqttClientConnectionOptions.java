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
package org.eclipse.kapua.transport.mqtt;

import org.eclipse.kapua.transport.TransportClientConnectOptions;

import javax.validation.constraints.NotNull;
import java.net.URI;

/**
 * Implementation of {@link TransportClientConnectOptions} API for Mqtt {@link org.eclipse.kapua.transport.TransportFacade}.
 *
 * @since 1.0.0
 */
public class MqttClientConnectionOptions implements TransportClientConnectOptions {

    /**
     * The MQTT client id to use.
     *
     * @since 1.0.0
     */
    private String clientId;

    /**
     * The MQTT username id to use.
     *
     * @since 1.0.0
     */
    private String username;

    /**
     * The MQTT password id to use.
     *
     * @since 1.0.0
     */
    private char[] password;

    /**
     * The MQTT broker URI id to use.
     *
     * @since 1.0.0
     */
    private URI endpointURI;

    /**
     * Gets the clientID to use for the MQTT client connection.
     *
     * @since 1.0.0
     */
    @Override
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID to use for the MQTT client connection.
     * <p>
     * This client ID must follow the specification of MQTT v3.1.1
     * </p>
     *
     * @see <a href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/csprd02/mqtt-v3.1.1-csprd02.html">MQTT v3.1.1 specification</a>
     * @since 1.0.0
     */
    @Override
    public void setClientId(@NotNull String clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the username to use for the MQTT client connection.
     *
     * @since 1.0.0
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username to use for the MQTT client connection.
     * <p>
     * This username must follow the specification of MQTT v3.1.1
     * </p>
     *
     * @see <a href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/csprd02/mqtt-v3.1.1-csprd02.html">MQTT v3.1.1 specification</a>
     * @since 1.0.0
     */
    @Override
    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    /**
     * Gets the password to use for the MQTT client connection.
     *
     * @since 1.0.0
     */
    @Override
    public char[] getPassword() {
        return password;
    }

    /**
     * Sets the password to use for the MQTT client connection.
     * <p>
     * This password must follow the specification of MQTT v3.1.1
     * </p>
     *
     * @see <a href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/csprd02/mqtt-v3.1.1-csprd02.html">MQTT v3.1.1 specification</a>
     * @since 1.0.0
     */
    @Override
    public void setPassword(@NotNull char[] password) {
        this.password = password;
    }

    /**
     * Gets the {@link URI} to use for the MQTT client connection.
     */
    @Override
    public URI getEndpointURI() {
        return endpointURI;
    }

    /**
     * Sets the {@link URI} to use for the MQTT client connection.
     */
    @Override
    public void setEndpointURI(@NotNull URI endpointURI) {
        this.endpointURI = endpointURI;
    }

}
