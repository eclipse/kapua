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

import java.util.function.UnaryOperator;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;

/**
 * {@link DeviceStoreClientBuilder} backed by the Elasticsearch low-level REST client.
 *
 * @since 2.1.0
 */
class ElasticsearchDeviceStoreClientBuilder implements DeviceStoreClientBuilder {

    private final RestClientBuilder restClientBuilder;

    ElasticsearchDeviceStoreClientBuilder(RestClientBuilder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Override
    public DeviceStoreClientBuilder setHttpClientConfigCallback(UnaryOperator<HttpAsyncClientBuilder> callback) {
        restClientBuilder.setHttpClientConfigCallback(callback::apply);
        return this;
    }

    @Override
    public DeviceStoreClientBuilder setRequestConfigCallback(UnaryOperator<RequestConfig.Builder> callback) {
        restClientBuilder.setRequestConfigCallback(callback::apply);
        return this;
    }

    @Override
    public DeviceStoreClient build() {
        return new ElasticsearchDeviceStoreClient(restClientBuilder.build());
    }
}
