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
package org.eclipse.kapua.service.camel.application;

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;

public class MetricsCamel {

    private static final String STORED_TO_FILE = "store_to_file";
    private static final String UNKNOWN_BODY_TYPE = "unknown_body_type";
    private Counter storedToFileSuccess;
    private Counter storedToFileError;
    private Counter unknownBodyType;

    private static final String CONVERTER = "converter";
    private Counter converterErrorMessage;

    //failure processor
    public static final String GENERIC = "generic";
    public static final String UNAUTHENTICATED = "unauthenticated";
    private Counter unauthenticatedError;
    private Counter genericError;

    private static MetricsCamel instance;

    public static MetricsCamel getInstance() {
        if (instance == null) {
            synchronized (CommonsMetric.class) {
                if (instance == null) {
                    instance = new MetricsCamel();
                }
            }
        }
        return instance;
    }

    private MetricsCamel() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        //error handler
        storedToFileSuccess = metricsService.getCounter(CommonsMetric.module, MetricsLabel.ERROR, STORED_TO_FILE, MetricsLabel.SUCCESS);
        storedToFileError = metricsService.getCounter(CommonsMetric.module, MetricsLabel.ERROR, STORED_TO_FILE, MetricsLabel.ERROR);
        unknownBodyType = metricsService.getCounter(CommonsMetric.module, MetricsLabel.ERROR, UNKNOWN_BODY_TYPE);

        converterErrorMessage = metricsService.getCounter(
            CommonsMetric.module,
            CONVERTER,
            MetricsLabel.ERROR
        );

        unauthenticatedError = metricsService.getCounter(CommonsMetric.module, MetricsLabel.FAILURE, UNAUTHENTICATED);
        genericError = metricsService.getCounter(CommonsMetric.module, MetricsLabel.FAILURE, GENERIC);
    }

    public Counter getErrorStoredToFileSuccess() {
        return storedToFileSuccess;
    }

    public Counter getErrorStoredToFileError() {
        return storedToFileError;
    }

    public Counter getUnknownBodyType() {
        return unknownBodyType;
    }

    public Counter getConverterErrorMessage() {
        return converterErrorMessage;
    }

    public Counter getUnauthenticatedError() {
        return unauthenticatedError;
    }

    public Counter getGenericError() {
        return genericError;
    }
}
