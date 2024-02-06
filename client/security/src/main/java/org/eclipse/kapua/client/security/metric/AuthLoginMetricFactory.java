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
package org.eclipse.kapua.client.security.metric;

import org.eclipse.kapua.commons.metric.MetricsService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class AuthLoginMetricFactory {
    private final String metricModuleName;
    private final MetricsService metricsService;

    @Inject
    public AuthLoginMetricFactory(@Named("metricModuleName") String metricModuleName, MetricsService metricsService) {
        this.metricModuleName = metricModuleName;
        this.metricsService = metricsService;
    }

    public AuthLoginMetric authLoginMetric(String type) {
        return new AuthLoginMetric(metricModuleName, metricsService, type);
    }
}
