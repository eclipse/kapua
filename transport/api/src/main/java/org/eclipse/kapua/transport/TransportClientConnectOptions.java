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
package org.eclipse.kapua.transport;

import java.net.URI;

/**
 * Container for connection options of {@link TransportFacade}.
 *
 * @author alberto.codutti
 * @since 1.0.0
 */
public interface TransportClientConnectOptions {

    /**
     * Gets the id to use for the connection in the transport layer.
     *
     * @return The id to use.
     * @since 1.0.0
     */
    public String getClientId();

    /**
     * Sets the id to be used for connection in the transport layer.
     *
     * @param clientId The id to use.
     * @since 1.0.0
     */
    public void setClientId(String clientId);

    /**
     * Gets the username to use for the connection in the transport layer.
     *
     * @return The username to use.
     * @since 1.0.0
     */
    public String getUsername();

    /**
     * Sets the username to be used for the connection in the transport layer.
     *
     * @param username The username to use.
     * @since 1.0.0
     */
    public void setUsername(String username);

    /**
     * Gets the password to user for the connection in the transport layer.
     *
     * @return The password to use.
     * @since 1.0.0
     */
    public char[] getPassword();

    /**
     * Sets the password to be used for the connection in the transport layer.
     *
     * @param password The password to use.
     * @since 1.0.0
     */
    public void setPassword(char[] password);

    /**
     * Gets the endpoint URI to use for the connection in the transport layer.
     *
     * @return The endpoint URI to use.
     * @since 1.0.0
     */
    public URI getEndpointURI();

    /**
     * Sets the endpint URI to be use for the connection in the transport layer.
     *
     * @param endpontURI The endpoint URI to use.
     * @since 1.0.0
     */
    public void setEndpointURI(URI endpontURI);
}
