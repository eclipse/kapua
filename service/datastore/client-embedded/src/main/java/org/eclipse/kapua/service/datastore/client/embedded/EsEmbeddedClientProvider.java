/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.embedded;

import java.io.IOException;

import org.eclipse.kapua.service.datastore.client.ClientProvider;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elasticsearch embedded node client implementation.<br>
 * <b>To be used for test scope.</b><br>
 * To use this client provider please set properly the configuration key 'datastore.elasticsearch.client.provider' to 'org.eclipse.kapua.service.datastore.client.embedded.EsEmbeddedClientProvider' in
 * the kapua-datastore-client-setting.properties.
 * 
 * @since 1.0
 *
 */
public class EsEmbeddedClientProvider implements ClientProvider<Client> {

    private static final String PROVIDER_NOT_INITIALIZED_MSG = "Provider not configured! please call initi method before use it!";
    private static final String PROVIDER_ALREADY_INITIALIZED_MSG = "Provider already initialized! closing it before initialize the new one!";
    private static final String PROVIDER_FAILED_TO_CONFIGURE_MSG = "Failed to configure ElasticSearch embedded node";

    private static final String PROVIDER_CANNOT_CLOSE_CLIENT_LOG = "Cannot close ElasticSearch client. Client is already stopped or not initialized!";

    private static final Logger logger = LoggerFactory.getLogger(EsEmbeddedClientProvider.class);

    private static EsEmbeddedClientProvider instance;

    private EsEmbeddedEngine embeddedEngine;

    /**
     * Get the {@link EsEmbeddedClientProvider} instance
     * 
     * @return
     * @throws ClientUnavailableException
     */
    public static EsEmbeddedClientProvider getInstance() throws ClientUnavailableException {
        if (instance == null) {
            throw new ClientUnavailableException(PROVIDER_NOT_INITIALIZED_MSG);
        }
        return instance;
    }

    /**
     * Initialize the {@link EsEmbeddedClientProvider} singleton instance<br>
     * <b>NOTE. The init methods can be called more than once in order to reinitialize the underlying datastore connection. It the datastore was already initialized this method close the old one
     * before initializing the new one.</b>
     * 
     * @throws ClientUnavailableException
     */
    public static void init() throws ClientUnavailableException {
        synchronized (EsEmbeddedClientProvider.class) {
            if (instance != null) {
                logger.warn(PROVIDER_ALREADY_INITIALIZED_MSG);
                close();
            }
            try {
                instance = new EsEmbeddedClientProvider();
            } catch (NodeValidationException e) {
                throw new ClientUnavailableException(PROVIDER_FAILED_TO_CONFIGURE_MSG, e);
            }
        }
    }

    /**
     * Close the ES transport client
     * 
     * @throws ClientUnavailableException
     */
    public static void close() throws ClientUnavailableException {
        synchronized (EsEmbeddedClientProvider.class) {
            if (instance != null) {
                try {
                    instance.closeClient();
                } catch (IOException e) {
                    throw new ClientUnavailableException(PROVIDER_FAILED_TO_CONFIGURE_MSG, e);
                }
            } else {
                logger.warn(PROVIDER_CANNOT_CLOSE_CLIENT_LOG);
            }
        }
    }

    private void closeClient() throws IOException {
        if (embeddedEngine != null) {
            try {
                embeddedEngine.close();
            } finally {
                embeddedEngine = null;
            }
        }
    }

    private EsEmbeddedClientProvider() throws NodeValidationException {
        embeddedEngine = new EsEmbeddedEngine();
    }

    @Override
    public Client getClient() {
        return embeddedEngine.getClient();
    }

}
