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
package org.eclipse.kapua.commons.metric;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.JmxReporter.Builder;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * Metric report exporter handler.
 * It provides methods for register/unregister metrics in the context
 *
 * @since 1.0
 */
public class MetricsServiceImpl implements MetricsService {

    private static final Logger logger = LoggerFactory.getLogger(MetricsServiceImpl.class);

    public static final String METRICS_NAME_FORMAT = "{0}.{1}.{2}";
    public static final String METRICS_SHORT_NAME_FORMAT = "{0}.{1}";
    private static final char SEPARATOR = '.';

    enum MetricType {
        count
    }

    private MetricRegistry metricRegistry;

    private JmxReporter jmxReporter;

    /**
     * Default metric service constructor
     */
    @Inject
    public MetricsServiceImpl(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;

        if (isJmxEnabled()) {
            enableJmxSupport();
        }
    }

    private void enableJmxSupport() {
        final Builder builder = JmxReporter.forRegistry(this.metricRegistry);
        builder.convertDurationsTo(TimeUnit.MILLISECONDS);
        builder.convertRatesTo(TimeUnit.SECONDS);
        builder.inDomain("org.eclipse.kapua");
        this.jmxReporter = builder.build();
        this.jmxReporter.start();
        /*
         * As Kapua services don't have any proper lifecycle management we can only
         * start the reporter but never stop it.
         */
    }

    @Override
    public Counter getCounter(String module, String component, String... names) {
        String name = getMetricName(MetricType.count, module, component, names);
        Counter counter = metricRegistry.getCounters().get(name);
        if (counter == null) {
            logger.debug("Creating a Counter: {}", name);
            counter = metricRegistry.counter(name);
        }
        return counter;
    }

    @Override
    public Histogram getHistogram(String module, String component, String... names) {
        String name = getMetricName(module, component, names);
        Histogram histogram = metricRegistry.getHistograms().get(name);
        if (histogram == null) {
            logger.debug("Creating a Histogram: {}", name);
            histogram = metricRegistry.histogram(name);
        }
        return histogram;
    }

    @Override
    public Timer getTimer(String module, String component, String... names) {
        String name = getMetricName(module, component, names);
        Timer timer = metricRegistry.getTimers().get(name);
        if (timer == null) {
            logger.debug("Creating a Timer: {}", name);
            timer = metricRegistry.timer(name);
        }
        return timer;
    }

    @Override
    public void registerGauge(Gauge<?> gauge, String module, String component, String... names) throws KapuaException {
        String name = getMetricName(module, component, names);
        if (metricRegistry.getGauges().get(name) != null) {
            throw KapuaException.internalError(MessageFormat.format("A metric with the name {0} is already defined!", name));
        } else {
            metricRegistry.register(name, gauge);
        }
    }

    private String getMetricName(String module, String component, String... metricsName) {
        return MessageFormat.format(METRICS_NAME_FORMAT, module, component, convertToDotNotation(null, metricsName));
    }

    /**
     * Build the metric name based on module, component and metric names
     *
     * @param module
     * @param component
     * @param metricsName
     * @return
     */
    private String getMetricName(MetricType metricType, String module, String component, String... metricsName) {
        if (metricsName == null || metricsName.length <= 0) {
            return MessageFormat.format(METRICS_SHORT_NAME_FORMAT, module, component, metricType != null ? SEPARATOR + metricType.name() : "");
        } else {
            return MessageFormat.format(METRICS_NAME_FORMAT, module, component, convertToDotNotation(metricType, metricsName));
        }
    }

    /**
     * Convert the metric names to a concatenated dot separated string
     *
     * @param metricsName
     * @return
     */
    private String convertToDotNotation(MetricType metricType, String... metricsName) {
        StringBuilder builder = new StringBuilder();
        boolean firstMetricName = true;
        for (String s : metricsName) {
            if (!firstMetricName) {
                builder.append(SEPARATOR);
            }
            firstMetricName = false;
            builder.append(s);
        }
        if (metricType != null) {
            builder.append(SEPARATOR).append(metricType.name());
        }
        return builder.toString();
    }

    /**
     * Tests is JMX is enabled
     * <p>
     * The default is that JMX support is enabled
     * </p>
     *
     * @return {@code true} JMX should be enabled, {@code false} otherwise
     */
    private static boolean isJmxEnabled() {
        return SystemSetting.getInstance().getBoolean(SystemSettingKey.METRICS_ENABLE_JMX, false);
    }

}
