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

/**
 * The {@link ElasticsearchClientReconnectConfiguration} definition.
 * <p>
 * It contains values for configuring reconnection properties.
 * It contains default values to ease the usage of the class.
 *
 * @since 1.3.0
 */
public class ElasticsearchClientReconnectConfiguration {

    private int reconnectDelay = 30000;

    /**
     * Gets the reconnect delay after a connection lost with Elasticsearch.
     * <p>
     * Default value: 30000
     *
     * @return The reconnect delay after a connection lost with Elasticsearch
     * @since 1.3.0
     */
    public int getReconnectDelay() {
        return reconnectDelay;
    }

    /**
     * Sets the reconnect delay after a connection lost with Elasticsearch
     *
     * @param reconnectDelay The reconnect delay to wait
     * @return This {@link ElasticsearchClientReconnectConfiguration} to chain method invocation.
     * @since 1.3.0
     */
    public ElasticsearchClientReconnectConfiguration setReconnectDelay(int reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
        return this;
    }
}
