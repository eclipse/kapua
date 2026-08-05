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
 * {@link DeviceStoreClient} backed by the OpenSearch low-level REST client.
 *
 * @since 2.1.0
 */
class OpensearchDeviceStoreClient implements DeviceStoreClient {

    private final RestClient restClient;

    OpensearchDeviceStoreClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DeviceStoreClientRequest newRequest(String method, String endpoint) {
        return new OpensearchDeviceStoreClientRequest(new Request(method, endpoint));
    }

    @Override
    public DeviceStoreClientResponse performRequest(DeviceStoreClientRequest request) throws IOException {
        try {
            return new OpensearchDeviceStoreClientResponse(restClient.performRequest(((OpensearchDeviceStoreClientRequest) request).unwrap()));
        } catch (ResponseException e) {
            throw new LowLevelSearchResponseException(new OpensearchDeviceStoreClientResponse(e.getResponse()), e);
        }
    }

    @Override
    public void close() throws IOException {
        restClient.close();
    }
}
