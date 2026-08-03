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
package org.eclipse.kapua.service.elasticsearch.client.rest.lowlevel;

import java.io.IOException;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

/**
 * {@link LowLevelSearchClient} backed by the Elasticsearch low-level REST client.
 *
 * @since 2.1.0
 */
class ElasticsearchLowLevelSearchClient implements LowLevelSearchClient {

    private final RestClient restClient;

    ElasticsearchLowLevelSearchClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public LowLevelSearchRequest newRequest(String method, String endpoint) {
        return new ElasticsearchLowLevelSearchRequest(new Request(method, endpoint));
    }

    @Override
    public LowLevelSearchResponse performRequest(LowLevelSearchRequest request) throws IOException {
        try {
            return new ElasticsearchLowLevelSearchResponse(restClient.performRequest(((ElasticsearchLowLevelSearchRequest) request).unwrap()));
        } catch (ResponseException e) {
            throw new LowLevelSearchResponseException(new ElasticsearchLowLevelSearchResponse(e.getResponse()), e);
        }
    }

    @Override
    public void close() throws IOException {
        restClient.close();
    }
}
