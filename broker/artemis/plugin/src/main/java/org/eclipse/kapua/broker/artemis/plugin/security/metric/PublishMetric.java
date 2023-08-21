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
package org.eclipse.kapua.broker.artemis.plugin.security.metric;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import javax.inject.Inject;
import javax.inject.Named;

public class PublishMetric {

    public static final String PUBLISH = "publish";

    public static final String ALLOWED = "allowed";
    public static final String NOT_ALLOWED = "not_allowed";

    private final Counter allowedMessages;
    private final Counter notAllowedMessages;
    private final Timer time;
    // message size
    private final Histogram messageSizeAllowed;

    @Inject
    private PublishMetric(MetricsService metricsService,
                          @Named("metricModuleName")
                          String metricModuleName) {
        // publish/subscribe
        allowedMessages = metricsService.getCounter(metricModuleName, PUBLISH, ALLOWED);
        notAllowedMessages = metricsService.getCounter(metricModuleName, PUBLISH, NOT_ALLOWED);
        time = metricsService.getTimer(metricModuleName, PUBLISH, MetricsLabel.TIME, MetricsLabel.SECONDS);
        // message size
        messageSizeAllowed = metricsService.getHistogram(metricModuleName, PUBLISH, ALLOWED, MetricsLabel.SIZE, MetricsLabel.BYTES);
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
