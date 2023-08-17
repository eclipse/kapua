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
package org.eclipse.kapua.consumer.telemetry;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.locator.KapuaLocator;

public class MetricsTelemetry {

    public static final String CONSUMER_TELEMETRY = "consumer_telemetry";
    private static final String CONVERTER = "converter";

    private Counter converterDataMessage;

    private static MetricsTelemetry instance;

    //TODO: FIXME: singletons should not be handled manually, we have DI for that
    public synchronized static MetricsTelemetry getInstance() {
        if (instance == null) {
            instance = new MetricsTelemetry(KapuaLocator.getInstance().getComponent(MetricsService.class));
        }
        return instance;
    }

    private MetricsTelemetry(MetricsService metricsService) {
        converterDataMessage = metricsService.getCounter(CONSUMER_TELEMETRY, CONVERTER, MetricsLabel.MESSAGE_DATA);
    }

    public Counter getConverterDataMessage() {
        return converterDataMessage;
    }

}