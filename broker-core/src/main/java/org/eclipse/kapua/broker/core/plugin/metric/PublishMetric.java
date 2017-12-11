/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.metric;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;

public class PublishMetric {

    private final static PublishMetric PUBLISH_METRIC = new PublishMetric();

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
        allowedMessages = metricsService.getCounter("security", "publish", "allowed", "count");
        notAllowedMessages = metricsService.getCounter("security", "publish", "not_allowed", "count");
        time = metricsService.getTimer("security", "publish", "time", "s");
        // message size
        messageSizeAllowed = metricsService.getHistogram("security", "publish", "messages", "allowed", "size", "bytes");
        messageSizeNotAllowed = metricsService.getHistogram("security", "publish", "messages", "not_allowed", "size", "bytes");
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
