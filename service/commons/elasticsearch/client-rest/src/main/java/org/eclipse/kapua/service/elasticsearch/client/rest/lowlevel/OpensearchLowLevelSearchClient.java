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

import org.opensearch.client.Request;
import org.opensearch.client.ResponseException;
import org.opensearch.client.RestClient;

/**
 * {@link LowLevelSearchClient} backed by the OpenSearch low-level REST client.
 *
 * @since 2.1.0
 */
class OpensearchLowLevelSearchClient implements LowLevelSearchClient {

    private final RestClient restClient;

    OpensearchLowLevelSearchClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public LowLevelSearchRequest newRequest(String method, String endpoint) {
        return new OpensearchLowLevelSearchRequest(new Request(method, endpoint));
    }

    @Override
    public LowLevelSearchResponse performRequest(LowLevelSearchRequest request) throws IOException {
        try {
            return new OpensearchLowLevelSearchResponse(restClient.performRequest(((OpensearchLowLevelSearchRequest) request).unwrap()));
        } catch (ResponseException e) {
            throw new LowLevelSearchResponseException(new OpensearchLowLevelSearchResponse(e.getResponse()), e);
        }
    }

    @Override
    public void close() throws IOException {
        restClient.close();
    }
}
