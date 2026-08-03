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
import org.opensearch.client.RestClientBuilder;

/**
 * {@link LowLevelSearchClientBuilder} backed by the OpenSearch low-level REST client.
 *
 * @since 2.1.0
 */
class OpensearchLowLevelSearchClientBuilder implements LowLevelSearchClientBuilder {

    private final RestClientBuilder restClientBuilder;

    OpensearchLowLevelSearchClientBuilder(RestClientBuilder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Override
    public LowLevelSearchClientBuilder setHttpClientConfigCallback(UnaryOperator<HttpAsyncClientBuilder> callback) {
        restClientBuilder.setHttpClientConfigCallback(callback::apply);
        return this;
    }

    @Override
    public LowLevelSearchClientBuilder setRequestConfigCallback(UnaryOperator<RequestConfig.Builder> callback) {
        restClientBuilder.setRequestConfigCallback(callback::apply);
        return this;
    }

    @Override
    public LowLevelSearchClient build() {
        return new OpensearchLowLevelSearchClient(restClientBuilder.build());
    }
}
