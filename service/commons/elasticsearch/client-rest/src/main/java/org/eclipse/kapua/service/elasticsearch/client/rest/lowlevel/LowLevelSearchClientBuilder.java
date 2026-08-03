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

/**
 * Vendor-agnostic view of the low-level REST client builder, be it the Elasticsearch or the OpenSearch one.
 * <p>
 * The callbacks are expressed in terms of Apache HttpComponents types since both vendors' builders customize the very same underlying HTTP client, unlike
 * {@link LowLevelSearchClient}/{@link LowLevelSearchRequest}/{@link LowLevelSearchResponse} which each vendor forked into its own package.
 *
 * @since 2.1.0
 */
public interface LowLevelSearchClientBuilder {

    LowLevelSearchClientBuilder setHttpClientConfigCallback(UnaryOperator<HttpAsyncClientBuilder> callback);

    LowLevelSearchClientBuilder setRequestConfigCallback(UnaryOperator<RequestConfig.Builder> callback);

    LowLevelSearchClient build();
}
