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

    private static final String STORED_TO_FILE = "stored_to_file";
    private static final String MESSAGE_UNKNOWN = "message_conversion_unknown_type";
    private Counter errorTotal;
    private Counter errorStoredToFileSuccess;
    private Counter errorStoredToFileError;
    private Counter errorUnknownBodyType;

    private static final String CONVERTER = "converter";
    private static final String CONVERTER_JMS = "converter_jms";

    private Counter converterJmsAttemptMessage;
    private Counter converterJmsErrorMessage;
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
        errorTotal = metricsService.getCounter(CommonsMetric.module, MetricsLabel.ERROR, MetricsLabel.TOTAL);
        errorStoredToFileSuccess = metricsService.getCounter(CommonsMetric.module, MetricsLabel.ERROR, STORED_TO_FILE, MetricsLabel.SUCCESS);
        errorStoredToFileError = metricsService.getCounter(CommonsMetric.module, MetricsLabel.ERROR, STORED_TO_FILE, MetricsLabel.ERROR);
        errorUnknownBodyType = metricsService.getCounter(CommonsMetric.module, MetricsLabel.ERROR, MESSAGE_UNKNOWN, MetricsLabel.ERROR);

        //converter
        converterJmsAttemptMessage = metricsService.getCounter(
            CommonsMetric.module,
            CONVERTER_JMS,
            MetricsLabel.ATTEMPT
        );

        converterJmsErrorMessage = metricsService.getCounter(
            CommonsMetric.module,
            CONVERTER_JMS,
            MetricsLabel.ERROR
        );

        converterErrorMessage = metricsService.getCounter(
            CommonsMetric.module,
            CONVERTER,
            MetricsLabel.ERROR
        );

        unauthenticatedError = metricsService.getCounter(CommonsMetric.module, MetricsLabel.FAILURE, UNAUTHENTICATED);
        genericError = metricsService.getCounter(CommonsMetric.module, MetricsLabel.FAILURE, GENERIC);
    }

    public Counter getErrorTotal() {
        return errorTotal;
    }

    public Counter getErrorStoredToFileSuccess() {
        return errorStoredToFileSuccess;
    }

    public Counter getErrorStoredToFileError() {
        return errorStoredToFileError;
    }

    public Counter getErrorUnknownBodyType() {
        return errorUnknownBodyType;
    }

    public Counter getConverterErrorMessage() {
        return converterErrorMessage;
    }

    public Counter getConverterJmsAttemptMessage() {
        return converterJmsAttemptMessage;
    }

    public Counter getConverterJmsErrorMessage() {
        return converterJmsErrorMessage;
    }

    public Counter getUnauthenticatedError() {
        return unauthenticatedError;
    }

    public Counter getGenericError() {
        return genericError;
    }
}
