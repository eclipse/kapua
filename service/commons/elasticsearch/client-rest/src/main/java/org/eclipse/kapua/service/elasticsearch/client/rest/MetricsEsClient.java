/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.rest;

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;

public class MetricsEsClient {

    public static final String REST_CLIENT = "rest_client";
    public static final String TIMEOUT_RETRY = "timeout_retry";
    public static final String TIMEOUT_RETRY_LIMIT_REACHED = "timeout_retry_limit_reached";
    private static final String RUNTIME_ERROR = "runtime_error";
    private static final String RECONNECT_CALL = "reconnect_call";

    // metrics
    private Counter exception;
    private Counter runtimeException;
    private Counter clientReconnectCall;

    private Counter timeoutRetry;
    private Counter timeoutRetryLimitReached;

    private static MetricsEsClient instance;

    public synchronized static MetricsEsClient getInstance() {
        if (instance == null) {
            instance = new MetricsEsClient();
        }
        return instance;
    }

    private MetricsEsClient() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        //timeout
        timeoutRetry = metricsService.getCounter(CommonsMetric.module, REST_CLIENT, TIMEOUT_RETRY);
        timeoutRetryLimitReached = metricsService.getCounter(CommonsMetric.module, REST_CLIENT, TIMEOUT_RETRY_LIMIT_REACHED);
        clientReconnectCall = metricsService.getCounter(CommonsMetric.module, REST_CLIENT, RECONNECT_CALL);

        //exception
        exception = metricsService.getCounter(CommonsMetric.module, REST_CLIENT, MetricsLabel.ERROR);
        runtimeException = metricsService.getCounter(CommonsMetric.module, REST_CLIENT, RUNTIME_ERROR);
    }

    public Counter getException() {
        return exception;
    }

    public Counter getRuntimeException() {
        return runtimeException;
    }

    public Counter getClientReconnectCall() {
        return clientReconnectCall;
    }

    public Counter getTimeoutRetry() {
        return timeoutRetry;
    }

    public Counter getTimeoutRetryLimitReached() {
        return timeoutRetryLimitReached;
    }
}
