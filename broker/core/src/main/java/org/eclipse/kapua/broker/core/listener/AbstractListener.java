/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.listener;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

/**
 * Default camel pojo endpoint listener.
 *
 * @since 1.0
 */
public abstract class AbstractListener {

    // metrics
    private String metricComponentName = "listener";
    private static final MetricsService METRICS_SERVICE = MetricServiceFactory.getInstance();

    protected String name;

    /**
     * Create a listener with the specific name.<BR>
     * The "listener" constant will be used as metricComponentName.
     *
     * @param name First level name to categorize the metrics inside the listener
     */
    protected AbstractListener(String name) {
        this.name = name;
    }

    /**
     * Create a listener with the specific metricComponentName and name
     *
     * @param metricComponentName Root name to categorize the metrics inside the listener
     * @param name                First level name to categorize the metrics inside the listener
     */
    protected AbstractListener(String metricComponentName, String name) {
        this(name);
        this.metricComponentName = metricComponentName;
    }

    /**
     * Register a Counter with the specified names as suffix.<BR>
     * The prefix is described by a combination of constructor parameters name and metricComponentName depending on which constructor will be used.
     *
     * @param names
     * @return
     */
    protected Counter registerCounter(String... names) {
        return METRICS_SERVICE.getCounter(metricComponentName, name, names);
    }

    /**
     * Register a Timer with the specified names as suffix.<BR>
     * The prefix is described by a combination of constructor parameters name and metricComponentName depending on which constructor will be used.
     *
     * @param names
     * @return
     */
    protected Timer registerTimer(String... names) {
        return METRICS_SERVICE.getTimer(metricComponentName, name, names);
    }

}
