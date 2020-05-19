/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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

import com.codahale.metrics.Counter;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

public class ClientMetric {

    private static final ClientMetric CLIENT_METRIC = new ClientMetric();

    private Counter connectedClient;
    private Counter connectedKapuasys;
    private Counter disconnectionClient;
    private Counter disconnectionKapuasys;

    public static ClientMetric getInstance() {
        return CLIENT_METRIC;
    }

    private ClientMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        connectedClient = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_CLIENTS, SecurityMetrics.METRIC_CONNECTED, SecurityMetrics.METRIC_COUNT);
        connectedKapuasys = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_KAPUASYS, SecurityMetrics.METRIC_CONNECTED, SecurityMetrics.METRIC_COUNT);
        disconnectionClient = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_CLIENTS, SecurityMetrics.METRIC_DISCONNECTED, SecurityMetrics.METRIC_COUNT);
        disconnectionKapuasys = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_KAPUASYS, SecurityMetrics.METRIC_DISCONNECTED, SecurityMetrics.METRIC_COUNT);
    }

    public Counter getConnectedClient() {
        return connectedClient;
    }

    public Counter getConnectedKapuasys() {
        return connectedKapuasys;
    }

    public Counter getDisconnectedClient() {
        return disconnectionClient;
    }

    public Counter getDisconnectedKapuasys() {
        return disconnectionKapuasys;
    }

}
