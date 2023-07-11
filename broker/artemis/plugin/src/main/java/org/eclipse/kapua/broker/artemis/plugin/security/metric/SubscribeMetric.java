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
import com.codahale.metrics.Timer;

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

public class SubscribeMetric {

    private static final SubscribeMetric SUBSCRIBE_METRIC = new SubscribeMetric();

    public static final String SUBSCRIBE = "subscribe";

    public static final String ALLOWED = "allowed";
    public static final String NOT_ALLOWED = "not_allowed";

    private Counter allowedMessages;
    private Counter notAllowedMessages;
    private Timer time;

    public static SubscribeMetric getInstance() {
        return SUBSCRIBE_METRIC;
    }

    private SubscribeMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        allowedMessages = metricsService.getCounter(CommonsMetric.module, SUBSCRIBE, ALLOWED);
        notAllowedMessages = metricsService.getCounter(CommonsMetric.module, SUBSCRIBE, NOT_ALLOWED);
        time = metricsService.getTimer(CommonsMetric.module, SUBSCRIBE, MetricsLabel.TIME, MetricsLabel.SECONDS);
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

}
