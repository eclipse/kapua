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
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class SubscribeMetric {

    public static final String SUBSCRIBE = "subscribe";

    public static final String ALLOWED = "allowed";
    public static final String NOT_ALLOWED = "not_allowed";

    private final Counter allowedMessages;
    private final Counter notAllowedMessages;
    private final Timer time;

    @Inject
    private SubscribeMetric(MetricsService metricsService,
                            @Named("metricModuleName")
                            String metricModuleName) {
        allowedMessages = metricsService.getCounter(metricModuleName, SUBSCRIBE, ALLOWED);
        notAllowedMessages = metricsService.getCounter(metricModuleName, SUBSCRIBE, NOT_ALLOWED);
        time = metricsService.getTimer(metricModuleName, SUBSCRIBE, MetricsLabel.TIME, MetricsLabel.SECONDS);
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
