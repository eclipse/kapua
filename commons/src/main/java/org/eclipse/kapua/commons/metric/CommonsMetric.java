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

import com.codahale.metrics.Counter;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Helper class to handle commons metrics.
 * TODO inject when injection will be available
 */
public class CommonsMetric {

    private static final Logger logger = LoggerFactory.getLogger(CommonsMetric.class);

    //TODO this value should be injected instead of set by the application entrypoint!
    //TODO to be injected!!!
    public static String module = "undefined";
    //cache
    private static final String CACHE_MANAGER = "cache_manager";
    private Integer cacheStatus = new Integer(0);
    private Counter registeredCache;

    //raise service events
    private static final String EVENT_DATA_FILLER = "event_data_filler";
    private Counter raiseEventWrongId;
    private Counter raiseEventWrongEntity;

    //event bus
    private static final String EVENT_BUS_CONNECTION = "event_bus_connection";
    private Counter eventBusConnectionRetry;
    private Counter eventBusConnectionError;

    //cache
    private static final String CACHE_ENTITY = "cache_entity";
    private Counter cacheMiss;
    private Counter cacheHit;
    private Counter cacheRemoval;
    private Counter cacheError;

    //events
    public static final String EVENT = "event";
    private Counter processedEvent;
    private Counter enqueuedEvent;
    private Counter dequeuedEvent;

    private static CommonsMetric instance;

    //TODO: FIXME: singletons should not be handled manually, we have DI for that
    public synchronized static CommonsMetric getInstance() {
        if (instance == null) {
            try {
                instance = new CommonsMetric(KapuaLocator.getInstance().getComponent(MetricsService.class));
            } catch (KapuaException e) {
                //TODO throw runtime exception
                logger.error("Creating metrics error: {}", e.getMessage(), e);
            }
        }
        return instance;
    }

    @Inject
    public CommonsMetric(MetricsService metricsService) throws KapuaException {
        metricsService.registerGauge(() -> cacheStatus, module, CACHE_MANAGER, "cache_status");
        registeredCache = metricsService.getCounter(module, CACHE_MANAGER, "available_cache");

        raiseEventWrongId = metricsService.getCounter(module, EVENT_DATA_FILLER, "wrong_id");
        raiseEventWrongEntity = metricsService.getCounter(module, EVENT_DATA_FILLER, "wrong_entity");

        eventBusConnectionRetry = metricsService.getCounter(module, EVENT_BUS_CONNECTION, MetricsLabel.RETRY);
        eventBusConnectionError = metricsService.getCounter(module, EVENT_BUS_CONNECTION, MetricsLabel.ERROR);

        cacheMiss = metricsService.getCounter(module, CACHE_ENTITY, "miss");
        cacheHit = metricsService.getCounter(module, CACHE_ENTITY, "hit");
        cacheRemoval = metricsService.getCounter(module, CACHE_ENTITY, "removed");
        cacheError = metricsService.getCounter(module, CACHE_ENTITY, "error");

        processedEvent = metricsService.getCounter(module, EVENT, "processed");
        dequeuedEvent = metricsService.getCounter(module, EVENT, "dequeued");
        enqueuedEvent = metricsService.getCounter(module, EVENT, "enqueued");
    }

    //TODO should be synchronized?
    public void setCacheStatus(int value) {
        cacheStatus = value;
    }

    public Counter getRegisteredCache() {
        return registeredCache;
    }

    public Counter getRaiseEventWrongId() {
        return raiseEventWrongId;
    }

    public Counter getRaiseEventWrongEntity() {
        return raiseEventWrongEntity;
    }

    public Counter getEventBusConnectionRetry() {
        return eventBusConnectionRetry;
    }

    public Counter getEventBusConnectionError() {
        return eventBusConnectionError;
    }

    public Counter getCacheMiss() {
        return cacheMiss;
    }

    public Counter getCacheHit() {
        return cacheHit;
    }

    public Counter getCacheRemoval() {
        return cacheRemoval;
    }

    public Counter getCacheError() {
        return cacheError;
    }

    public Counter getProcessedEvent() {
        return processedEvent;
    }

    public Counter getDequeuedEvent() {
        return dequeuedEvent;
    }

    public Counter getEnqueuedEvent() {
        return enqueuedEvent;
    }

}
