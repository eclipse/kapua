/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin.metric;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

public class PublishMetric {

    private static final PublishMetric PUBLISH_METRIC = new PublishMetric();

    private Counter allowedMessages;
    private Counter notAllowedMessages;
    private Timer time;
    // message size
    private Histogram messageSizeAllowed;
    private Histogram messageSizeNotAllowed;

    public static PublishMetric getInstance() {
        return PUBLISH_METRIC;
    }

    private PublishMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        // publish/subscribe
        allowedMessages = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_PUBLISH, SecurityMetrics.METRIC_ALLOWED, SecurityMetrics.METRIC_COUNT);
        notAllowedMessages = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_PUBLISH, SecurityMetrics.METRIC_NOT_ALLOWED, SecurityMetrics.METRIC_COUNT);
        time = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_PUBLISH, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        // message size
        messageSizeAllowed = metricsService.getHistogram(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_PUBLISH, SecurityMetrics.METRIC_MESSAGES, SecurityMetrics.METRIC_ALLOWED, SecurityMetrics.METRIC_SIZE, SecurityMetrics.METRIC_BYTES);
        messageSizeNotAllowed = metricsService.getHistogram(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_PUBLISH, SecurityMetrics.METRIC_MESSAGES, SecurityMetrics.METRIC_NOT_ALLOWED, SecurityMetrics.METRIC_SIZE, SecurityMetrics.METRIC_BYTES);
    }

    public Counter getAllowedMessages() {
        return allowedMessages;
    }

    public Counter getNotAllowedMessages() {
        return notAllowedMessages;
    }

    public Timer getTime() {
        return time;
    }

    public Histogram getMessageSizeAllowed() {
        return messageSizeAllowed;
    }

    public Histogram getMessageSizeNotAllowed() {
        return messageSizeNotAllowed;
    }

}
