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

public class ClientMetric {

    private final static ClientMetric CLIENT_METRIC = new ClientMetric();

    private Counter connectedClient;
    private Counter connectedKapuasys;
    private Counter disconnectionClient;
    private Counter disconnectionKapuasys;

    public static ClientMetric getInstance() {
        return CLIENT_METRIC;
    }

    private ClientMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        connectedClient = metricsService.getCounter("security", "login", "clients", "connected", "count");
        connectedKapuasys = metricsService.getCounter("security", "login", "kapuasys", "connected", "count");
        disconnectionClient = metricsService.getCounter("security", "login", "clients", "disconnected", "count");
        disconnectionKapuasys = metricsService.getCounter("security", "login", "kapuasys", "disconnected", "count");
    }

    public Counter getConnectedClient() {
        return connectedClient;
    }

    public Counter getConnectedKapuasys() {
        return connectedKapuasys;
    }

    public Counter getDisconnectionClient() {
        return disconnectionClient;
    }

    public Counter getDisconnectionKapuasys() {
        return disconnectionKapuasys;
    }

}
