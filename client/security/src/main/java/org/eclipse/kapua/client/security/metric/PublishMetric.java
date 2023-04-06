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
package org.eclipse.kapua.client.security.metric;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

public class PublishMetric {

    private static final PublishMetric PUBLISH_METRIC = new PublishMetric();

    private Counter allowedMessages;
    private Counter notAllowedMessages;
    private Timer time;
    // message size
    private Histogram messageSizeAllowed;

    public static PublishMetric getInstance() {
        return PUBLISH_METRIC;
    }

    private PublishMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        // publish/subscribe
        allowedMessages = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_PUBLISH, MetricsLabel.ALLOWED, MetricsLabel.COUNT);
        notAllowedMessages = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_PUBLISH, MetricsLabel.NOT_ALLOWED, MetricsLabel.COUNT);
        time = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_PUBLISH, MetricsLabel.TIME, MetricsLabel.SECONDS);
        // message size
        messageSizeAllowed = metricsService.getHistogram(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_PUBLISH, MetricsLabel.MESSAGES, MetricsLabel.ALLOWED, MetricsLabel.SIZE, MetricsLabel.BYTES);
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

}
