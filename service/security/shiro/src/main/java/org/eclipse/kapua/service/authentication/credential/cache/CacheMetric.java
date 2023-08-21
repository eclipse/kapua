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
package org.eclipse.kapua.service.authentication.credential.cache;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

public class CacheMetric {

    private static final Logger logger = LoggerFactory.getLogger(CacheMetric.class);

    private static final String AUTH_CACHE = "authentication_cache";

    private Counter cacheMiss;
    private Counter cacheHit;
    private Counter cachePutError;
    private Counter passwordEncryptionError;

    @Inject
    public CacheMetric(MetricsService metricsService,
                       @Named("metricModuleName")
                       String metricModuleName) throws KapuaException {
        cacheMiss = metricsService.getCounter(metricModuleName, AUTH_CACHE, "miss");
        cacheHit = metricsService.getCounter(metricModuleName, AUTH_CACHE, "hit");
        cachePutError = metricsService.getCounter(metricModuleName, AUTH_CACHE, "put", MetricsLabel.ERROR);
        passwordEncryptionError = metricsService.getCounter(metricModuleName, AUTH_CACHE, "encryption", MetricsLabel.ERROR);
    }

    public Counter getCacheHit() {
        return cacheHit;
    }

    public Counter getCacheMiss() {
        return cacheMiss;
    }

    public Counter getCachePutError() {
        return cachePutError;
    }

    public Counter getPasswordEncryptionError() {
        return passwordEncryptionError;
    }

}
