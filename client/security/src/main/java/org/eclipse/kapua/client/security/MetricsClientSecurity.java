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

import com.codahale.metrics.Counter;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class MetricsClientSecurity {

    private static final String CALLBACK = "callback";
    private Counter loginCallbackError;
    private Counter loginCallbackTimeout;

    @Inject
    public MetricsClientSecurity(MetricsService metricsService,
                                 @Named("metricModuleName")
                                 String metricModuleName) {
        loginCallbackError = metricsService.getCounter(metricModuleName, CALLBACK, MetricsLabel.ERROR);
        loginCallbackTimeout = metricsService.getCounter(metricModuleName, CALLBACK, MetricsLabel.TIMEOUT);
    }

    public Counter getLoginCallbackError() {
        return loginCallbackError;
    }

    public Counter getLoginCallbackTimeout() {
        return loginCallbackTimeout;
    }

}