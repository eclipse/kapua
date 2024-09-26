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
package org.eclipse.kapua.service.elasticsearch.client.configuration;

import org.eclipse.kapua.service.elasticsearch.client.model.Request;

/**
 * The {@link ElasticsearchClientRequestConfiguration} definition.
 * <p>
 * It contains values for configuring request properties.
 * It contains default values to ease the usage of the class.
 */
public class ElasticsearchClientRequestConfiguration {

    private int requestRetryAttemptMax = 3;
    private int requestRetryAttemptWait = 2500;

    private int queryTimeout = 15000;
    private int scrollTimeout = 60000;

    /**
     * Gets the number of maximum attempts to retry a {@link Request}.
     * <p>
     * Default value: 3
     *
     * @return The number of maximum attempts to retry a {@link Request}.
     * @since 1.3.0
     */
    public int getRequestRetryAttemptMax() {
        return requestRetryAttemptMax;
    }

    /**
     * Sets the number of maximum attempts to retry a {@link Request}.
     *
     * @param requestRetryAttemptMax The number of maximum attempts to retry a {@link Request}.
     * @return This {@link ElasticsearchClientRequestConfiguration} to chain method invocation.
     * @since 1.3.0
     */
    public ElasticsearchClientRequestConfiguration setRequestRetryAttemptMax(int requestRetryAttemptMax) {
        this.requestRetryAttemptMax = requestRetryAttemptMax;
        return this;
    }

    /**
     * Gets the wait time between {@link Request} retries.
     * <p>
     * Default value: 2500
     *
     * @return The wait time between {@link Request} retries.
     * @since 1.3.0
     */
    public int getRequestRetryAttemptWait() {
        return requestRetryAttemptWait;
    }

    /**
     * Sets the wait time between {@link Request} retries.
     *
     * @param requestRetryAttemptWait The wait time between {@link Request} retries.
     * @return This {@link ElasticsearchClientRequestConfiguration} to chain method invocation.
     * @since 1.3.0
     */
    public ElasticsearchClientRequestConfiguration setRequestRetryAttemptWait(int requestRetryAttemptWait) {
        this.requestRetryAttemptWait = requestRetryAttemptWait;
        return this;
    }

    /**
     * Gets the query {@link Request} timeout.
     * <p>
     * Default value: 15000
     *
     * @return The query {@link Request} timeout.
     * @since 1.3.0
     */
    public int getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * Sets the query {@link Request} timeout.
     *
     * @param queryTimeout The query {@link Request} timeout.
     * @return This {@link ElasticsearchClientRequestConfiguration} to chain method invocation.
     * @since 1.3.0
     */
    public ElasticsearchClientRequestConfiguration setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return this;
    }

    /**
     * Gets the scroll {@link Request} timeout.
     * <p>
     * Default value: 60000
     *
     * @return The scroll {@link Request} timeout.
     * @since 1.3.0
     */
    public int getScrollTimeout() {
        return scrollTimeout;
    }

    /**
     * Sets the scroll {@link Request} timeout.
     *
     * @param scrollTimeout The scroll {@link Request} timeout.
     * @return This {@link ElasticsearchClientRequestConfiguration} to chain method invocation.
     * @since 1.3.0
     */
    public ElasticsearchClientRequestConfiguration setScrollTimeout(int scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
        return this;
    }
}
