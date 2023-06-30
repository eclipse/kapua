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
package org.eclipse.kapua.client.security;

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;

public class MetricsClientSecurity {

    private static final String CALLBACK = "callback";
    private Counter loginCallbackError;
    private Counter loginCallbackTimeout;

    private static MetricsClientSecurity instance;

    public synchronized static MetricsClientSecurity getInstance() {
        if (instance == null) {
            instance = new MetricsClientSecurity();
        }
        return instance;
    }

    private MetricsClientSecurity() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        loginCallbackError = metricsService.getCounter(CommonsMetric.module, MetricLabel.COMPONENT_LOGIN, CALLBACK, MetricsLabel.ERROR);
        loginCallbackTimeout = metricsService.getCounter(CommonsMetric.module, MetricLabel.COMPONENT_LOGIN, CALLBACK, MetricsLabel.TIMEOUT);
    }

    public Counter getLoginCallbackError() {
        return loginCallbackError;
    }

    public Counter getLoginCallbackTimeout() {
        return loginCallbackTimeout;
    }

}