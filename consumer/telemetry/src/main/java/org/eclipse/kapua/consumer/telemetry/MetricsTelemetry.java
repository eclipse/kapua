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
package org.eclipse.kapua.consumer.telemetry;

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;

public class MetricsTelemetry {

    public static final String CONSUMER_TELEMETRY = "consumer_telemetry";
    private static final String CONVERSION = "conversion";
    private static final String STORE = "store";

    private Counter converterDataMessage;
    // queues counters
    private Counter queueCommunicationError;
    private Counter queueConfigurationError;
    private Counter queueGenericError;

    private static MetricsTelemetry instance;

    public static MetricsTelemetry getInstance() {
        if (instance == null) {
            synchronized (CommonsMetric.class) {
                if (instance == null) {
                    instance = new MetricsTelemetry();
                }
            }
        }
        return instance;
    }

    private MetricsTelemetry() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        converterDataMessage = metricsService.getCounter(CONSUMER_TELEMETRY, CONVERSION, MetricsLabel.MESSAGE_DATA);

        queueCommunicationError = metricsService.getCounter(CONSUMER_TELEMETRY, STORE, MetricsLabel.COMMUNICATION, MetricsLabel.ERROR);
        queueConfigurationError = metricsService.getCounter(CONSUMER_TELEMETRY, STORE, MetricsLabel.CONFIGURATION, MetricsLabel.ERROR);
        queueGenericError = metricsService.getCounter(CONSUMER_TELEMETRY, STORE, MetricsLabel.GENERIC, MetricsLabel.ERROR);
    }

    public Counter getConverterDataMessage() {
        return converterDataMessage;
    }

    public Counter getQueueCommunicationError() {
        return queueCommunicationError;
    }

    public Counter getQueueConfigurationError() {
        return queueConfigurationError;
    }

    public Counter getQueueGenericError() {
        return queueGenericError;
    }

}