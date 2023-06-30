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
package org.eclipse.kapua.consumer.lifecycle;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;

public class MetricsLifecycle {

    public static final String CONSUMER_LIFECYCLE = "consumer_lifecycle";
    public static final String CONVERSION = "conversion";
    private static final String DEVICE_MANAGEMENT_NOTIFICATION = "device_management_notification";

    //lifecycle
    private Counter converterAppMessage;
    private Counter converterBirthMessage;
    private Counter converterDcMessage;
    private Counter converterMissingMessage;
    private Counter converterNotifyMessage;

    //notification
    private Counter converterDeviceManagementNotificationMessage;

    // queues counters
    private Counter queueCommunicationError;
    private Counter queueConfigurationError;
    private Counter queueGenericError;

    // metrics
    private Counter deviceBirthMessage;
    private Counter deviceDisconnectMessage;
    private Counter deviceMissingMessage;
    private Counter deviceAppsMessage;
    private Counter deviceErrorMessage;

    private static MetricsLifecycle instance;

    public synchronized static MetricsLifecycle getInstance() {
        if (instance == null) {
            instance = new MetricsLifecycle();
        }
        return instance;
    }

    private MetricsLifecycle() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        converterAppMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, CONVERSION, MetricsLabel.MESSAGE_APPS);
        converterBirthMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, CONVERSION, MetricsLabel.MESSAGE_BIRTH);
        converterDcMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, CONVERSION, MetricsLabel.MESSAGE_DC);
        converterMissingMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, CONVERSION, MetricsLabel.MESSAGE_MISSING);
        converterNotifyMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, CONVERSION, MetricsLabel.MESSAGE_NOTIFY);

        converterDeviceManagementNotificationMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, CONVERSION, MetricsLabel.MESSAGE_NOTIFY);

        queueCommunicationError = metricsService.getCounter(CONSUMER_LIFECYCLE, DEVICE_MANAGEMENT_NOTIFICATION, MetricsLabel.COMMUNICATION, MetricsLabel.ERROR);
        queueConfigurationError = metricsService.getCounter(CONSUMER_LIFECYCLE, DEVICE_MANAGEMENT_NOTIFICATION, MetricsLabel.CONFIGURATION, MetricsLabel.ERROR);
        queueGenericError = metricsService.getCounter(CONSUMER_LIFECYCLE, DEVICE_MANAGEMENT_NOTIFICATION, MetricsLabel.GENERIC, MetricsLabel.ERROR);

        deviceBirthMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, MetricsLabel.MESSAGE_BIRTH);
        deviceDisconnectMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, MetricsLabel.MESSAGE_DC);
        deviceMissingMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, MetricsLabel.MESSAGE_MISSING);
        deviceAppsMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, MetricsLabel.MESSAGE_APPS);
        deviceErrorMessage = metricsService.getCounter(CONSUMER_LIFECYCLE, MetricsLabel.ERROR);
    }

    public Counter getConverterAppMessage() {
        return converterAppMessage;
    }

    public Counter getConverterBirthMessage() {
        return converterBirthMessage;
    }

    public Counter getConverterDcMessage() {
        return converterDcMessage;
    }

    public Counter getConverterMissingMessage() {
        return converterMissingMessage;
    }

    public Counter getConverterNotifyMessage() {
        return converterNotifyMessage;
    }

    public Counter getConverterDeviceManagementNotificationMessage() {
        return converterDeviceManagementNotificationMessage;
    }

    public Counter getQueueCommunicationError() {
        return queueCommunicationError;
    }

    public Counter getQueueConfigurationError() {
        return queueConfigurationError;
    }

    public Counter getQueueGenericError() {
        return queueGenericError;
    }

    public Counter getDeviceAppsMessage() {
        return deviceAppsMessage;
    }

    public Counter getDeviceBirthMessage() {
        return deviceBirthMessage;
    }

    public Counter getDeviceDisconnectMessage() {
        return deviceDisconnectMessage;
    }

    public Counter getDeviceErrorMessage() {
        return deviceErrorMessage;
    }

    public Counter getDeviceMissingMessage() {
        return deviceMissingMessage;
    }

    public static String getDeviceManagementNotification() {
        return DEVICE_MANAGEMENT_NOTIFICATION;
    }

}