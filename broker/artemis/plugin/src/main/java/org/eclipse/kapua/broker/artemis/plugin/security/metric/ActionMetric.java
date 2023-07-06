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
package org.eclipse.kapua.broker.artemis.plugin.security.metric;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;

public class ActionMetric {

    private Counter attempt;
    private Counter success;
    private Counter failure;

    public ActionMetric(String module, String component, String type) {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        attempt = metricsService.getCounter(module, component, type, MetricsLabel.ATTEMPT);
        success = metricsService.getCounter(module, component, type, MetricsLabel.SUCCESS);
        failure = metricsService.getCounter(module, component, type, MetricsLabel.FAILURE);
    }

    public Counter getAttempt() {
        return attempt;
    }

    public Counter getSuccess() {
        return success;
    }

    public Counter getFailure() {
        return failure;
    }
}
