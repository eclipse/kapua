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
import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeValidationException;

/**
 * Elasticsearch embedded client implementation.<br>
 * Used for test scope
 *
 * @since 1.0
 */
public class EsEmbeddedClientProvider implements ClientProvider<Client> {

    private final EsEmbeddedEngine embeddedEngine;

    public EsEmbeddedClientProvider() throws NodeValidationException {
        embeddedEngine = new EsEmbeddedEngine();
    }

    @Override
    public Client getClient() {
        return embeddedEngine.getClient();
    }

    @Override
    public void close() throws IOException {
        embeddedEngine.close();
    }

}
