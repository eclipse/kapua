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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

public class CacheMetric {

    private static final Logger logger = LoggerFactory.getLogger(CacheMetric.class);

    private static final String AUTH_CACHE = "authentication_cache";

    private Counter cacheMiss;
    private Counter cacheHit;
    private Counter cachePutError;
    private Counter passwordEncryptionError;

    private static CacheMetric instance;

    public synchronized static CacheMetric getInstance() {
        if (instance == null) {
            try {
                instance = new CacheMetric();
            } catch (KapuaException e) {
                //TODO throw runtime exception
                logger.error("Creating metrics error: {}", e.getMessage(), e);
            }
        }
        return instance;
    }

    private CacheMetric() throws KapuaException {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        cacheMiss = metricsService.getCounter(CommonsMetric.module, AUTH_CACHE, "miss");
        cacheHit = metricsService.getCounter(CommonsMetric.module, AUTH_CACHE, "hit");
        cachePutError = metricsService.getCounter(CommonsMetric.module, AUTH_CACHE, "put", MetricsLabel.ERROR);
        passwordEncryptionError = metricsService.getCounter(CommonsMetric.module, AUTH_CACHE, "encryption", MetricsLabel.ERROR);
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
