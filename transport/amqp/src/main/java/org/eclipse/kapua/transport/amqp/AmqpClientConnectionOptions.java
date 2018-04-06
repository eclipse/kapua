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

import java.net.URI;

import org.eclipse.kapua.transport.TransportClientConnectOptions;

/**
 * Implementation of {@link TransportClientConnectOptions} API for Amqp transport facade.
 * 
 * @since 1.0.0
 */
public class AmqpClientConnectionOptions implements TransportClientConnectOptions {

    /**
     * The AMQP client id to use.
     * 
     * @since 1.0.0
     */
    private String clientId;

    /**
     * The AMQP username id to use.
     * 
     * @since 1.0.0
     */
    private String username;

    /**
     * The AMQP password id to use.
     * 
     * @since 1.0.0
     */
    private char[] password;

    /**
     * The AMQP broker URI id to use.
     * 
     * @since 1.0.0
     */
    private URI endpointURI;

    /**
     * Gets the clientID to use for the AMQP client connection.
     * 
     * @since 1.0.0
     */
    @Override
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID to use for the AMQP client connection.
     * 
     * @since 1.0.0
     */
    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the username to use for the AMQP client connection.
     * 
     * @since 1.0.0
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username to use for the AMQP client connection.
     * 
     * @since 1.0.0
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password to use for the AMQP client connection.
     * 
     * @since 1.0.0
     */
    @Override
    public char[] getPassword() {
        return password;
    }

    /**
     * Sets the password to use for the AMQP client connection.
     * 
     * @since 1.0.0
     */
    @Override
    public void setPassword(char[] password) {
        this.password = password;
    }

    /**
     * Gets the {@link URI} to use for the AMQP client connection.
     */
    @Override
    public URI getEndpointURI() {
        return endpointURI;
    }

    /**
     * Sets the {@link URI} to use for the AQMP client connection.
     */
    @Override
    public void setEndpointURI(URI endpointURI) {
        this.endpointURI = endpointURI;
    }

}
