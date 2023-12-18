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
package org.eclipse.kapua.commons.metric;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

public class CommonMetricsModule extends AbstractKapuaModule {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void configureModule() {
        bind(MetricsService.class).to(MetricsServiceImpl.class).in(Singleton.class);
        bind(CommonsMetric.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    MetricRegistry metricRegistry() {
        try {
            final MetricRegistry metricRegistry = SharedMetricRegistries.getDefault();
            logger.info("Default Metric Registry loaded");
            return metricRegistry;
        } catch (IllegalStateException e) {
            logger.warn("Unable to load Default Metric Registry - creating a new one");
            return new MetricRegistry();
        }
    }
}
