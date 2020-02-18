/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.persistence.tools.profiler.PerformanceMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

/**
 * Custom JPA performance monitor. Class to be used in test mode!
 * <ul>
 * <li>Removes scheduled log</li>
 * <li>Export Counter metrics to Dropwizard</li>
 * </ul>
 * 
 * To enable this monitor add properties to the persistence unit (to override default granularity and dump time use system properties)
 * <properties>
 *     <property name="eclipselink.profiler" value="org.eclipse.kapua.commons.jpa.KapuaPerformanceMonitor"/>
 *     .....
 * </properties>
 * 
 * or add jvm properties like
 * -Declipselink.profiler=org.eclipse.kapua.commons.jpa.KapuaPerformanceMonitor
 * -Declipselink.profiler.dumpTime=10000
 * -Declipselink.profiler.profileWeight=10
 */
public class KapuaPerformanceMonitor extends PerformanceMonitor {

    private static final long serialVersionUID = -567173348345821126L;

    private static final Logger logger = LoggerFactory.getLogger(KapuaPerformanceMonitor.class);

    public static final String DUMP_TIME = "eclipselink.profiler.dumpTime";
    public static final String PROFILE_WEIGHT = "eclipselink.profiler.profileWeight";

    private static final String MODULE = "jpa";
    private static final String COMPONENT = "monitor";
    private static final String INTERNAL = "internal";
    private static final String COUNT = "count";

    private static final AtomicInteger INDEX = new AtomicInteger();
    private static Long lastPrint = new Long(-1);
    private static Counter notExportedMetric;
    private static Counter notSupportedMetric;
    private static Counter registeringMetricError;

    private static MetricsService metricService;
    private boolean isPrintTimerEnabled;
    private String profilerKey;

    private static final Map<String, Map<String, Object>> METRICS = new ConcurrentHashMap<>();

    static {
        metricService = MetricServiceFactory.getInstance();
        notExportedMetric = metricService.getCounter(MODULE, COMPONENT, INTERNAL, "not_exported_metric", COUNT);
        notSupportedMetric = metricService.getCounter(MODULE, COMPONENT, INTERNAL, "not_supported_metric", COUNT);
        registeringMetricError = metricService.getCounter(MODULE, COMPONENT, INTERNAL, "registering_metric_error", COUNT);
    }

    public KapuaPerformanceMonitor() {
        String tmp = System.getProperty(DUMP_TIME);
        if (tmp!=null) {
            setDumpTime(Long.valueOf(tmp));
        }
        tmp = System.getProperty(PROFILE_WEIGHT);
        if (tmp!=null) {
            setProfileWeight(Integer.valueOf(tmp));
        }
        //register itself to metrics map
        profilerKey = getUniqueKey();
        if (METRICS.put(profilerKey, new HashMap<>()) != null) {
            throw new RuntimeException("Already defined profiler: " + profilerKey);
        }
    }

    @Override
    public void dumpResults() {
        logger.debug("Updating JPA profiling results...");
        Map<String, Object> currentMap = METRICS.get(profilerKey);
        operationTimings.forEach((operation, value) -> {
            if (operation.startsWith(COUNTER) || (operation.startsWith(TIMER) && isPrintTimerEnabled)) {
                final String key = operation.substring(operation.indexOf(':') + 1);
                if (currentMap.put(key, value!=null ? value : Long.valueOf(0)) == null) {
                    try {
                        metricService.registerGauge(() -> currentMap.get(key), MODULE, COMPONENT, profilerKey, key, COUNT);
                    } catch (KapuaException e) {
                        registeringMetricError.inc();
                        logger.error("Cannot register JPA performance metrics for metric: {}", key, e);
                    }
                    logger.debug("   {}: {}", key, value);
                }
            }
            else {
                notExportedMetric.inc();
                logger.debug("Operation {} not exported (type not supported) (value {})!", operation, value);
            }
        });
        if (System.currentTimeMillis()-lastPrint > getDumpTime()) {
            synchronized (lastPrint) {
                if (System.currentTimeMillis()-lastPrint > getDumpTime()) {
                    //merge metrics
                    Map<String, Object> mergedMap = new HashMap<>();
                    METRICS.forEach((profilerName, profilerMap) -> {
                        profilerMap.forEach((key, value) -> {
                            Object tmp = mergedMap.get(key);
                            if (tmp==null) {
                                mergedMap.put(key, value);
                            }
                            else {
                                if (tmp instanceof String) {
                                    mergedMap.put(key, (String) tmp + value);
                                }
                                else if (tmp instanceof Number) {
                                    mergedMap.put(key, ((Number) tmp).longValue() + Long.valueOf(value.toString()));
                                }
                                else {
                                    notSupportedMetric.inc();
                                    logger.info("Metric {} has null or not handled value type ({}).... skip update values", key, value);
                                }
                            }
                        });
                    });
                    //do print
                    logger.info("=====================================================================");
                    logger.info("=====================================================================");
                    logger.info("Updated metrics:");
                    logger.info("JPA profiler count: {}", METRICS.size());
                    logger.info("\t\t not exported metric value: {}", notExportedMetric.getCount());
                    logger.info("\t\t not supported metric type: {}", notSupportedMetric.getCount());
                    logger.info("\t\t registering metric error: {}", registeringMetricError.getCount());
                    mergedMap.forEach((key, value) -> logger.info("\t\t{}: {}", key, value));
                    logger.info("=====================================================================");
                    logger.info("=====================================================================");
                    lastPrint = System.currentTimeMillis();
                }
            }
        }
        logger.debug("Updating JPA profiling results... DONE");
    }

    private static String getUniqueKey() {
        return "jpa_" + INDEX.incrementAndGet();
    }
}
