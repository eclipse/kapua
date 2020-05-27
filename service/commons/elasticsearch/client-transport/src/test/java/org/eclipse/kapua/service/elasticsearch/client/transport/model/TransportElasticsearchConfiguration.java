/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.transport.model;

import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchNode;

/**
 * {@link ElasticsearchClientConfiguration} to be used when testing {@link org.eclipse.kapua.service.elasticsearch.client.transport.TransportElasticsearchClient}.
 *
 * @since 1.3.0
 */
public class TransportElasticsearchConfiguration extends ElasticsearchClientConfiguration {
    public TransportElasticsearchConfiguration() {
        this(new ElasticsearchNode("127.0.0.1", 9300));
    }

    public TransportElasticsearchConfiguration(ElasticsearchNode elasticsearchNode) {
        setModuleName("elasticsearch-client-transport");

        getNodes().add(elasticsearchNode);
    }
}
