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
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import javax.inject.Inject;

public class PublishMetric {

    public static final String PUBLISH = "publish";

    public static final String ALLOWED = "allowed";
    public static final String NOT_ALLOWED = "not_allowed";

    private Counter allowedMessages;
    private Counter notAllowedMessages;
    private Timer time;
    // message size
    private Histogram messageSizeAllowed;

    @Inject
    private PublishMetric(MetricsService metricsService) {
        // publish/subscribe
        allowedMessages = metricsService.getCounter(CommonsMetric.module, PUBLISH, ALLOWED);
        notAllowedMessages = metricsService.getCounter(CommonsMetric.module, PUBLISH, NOT_ALLOWED);
        time = metricsService.getTimer(CommonsMetric.module, PUBLISH, MetricsLabel.TIME, MetricsLabel.SECONDS);
        // message size
        messageSizeAllowed = metricsService.getHistogram(CommonsMetric.module, PUBLISH, ALLOWED, MetricsLabel.SIZE, MetricsLabel.BYTES);
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
