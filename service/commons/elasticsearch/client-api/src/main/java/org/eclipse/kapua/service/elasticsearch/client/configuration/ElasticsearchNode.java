/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.configuration;

import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;

/**
 * {@link ElasticsearchNode} definition.
 * <p>
 * This is the collector of host and port for the {@link ElasticsearchClient}.
 *
 * @since 1.3.0
 */
public class ElasticsearchNode {

    private final String address;
    private final int port;

    /**
     * Constructor.
     *
     * @param address The host address.
     * @param port    The host port.
     * @since 1.3.0
     */
    public ElasticsearchNode(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Gets the host address.
     *
     * @return The host address.
     * @since 1.3.0
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the host port.
     *
     * @return The host port.
     * @since 1.3.0
     */
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return address.concat(":").concat(String.valueOf(port));
    }
}
