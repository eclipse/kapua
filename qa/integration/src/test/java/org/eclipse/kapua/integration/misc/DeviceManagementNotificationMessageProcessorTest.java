/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.misc;

import com.codahale.metrics.Counter;
import org.apache.camel.Exchange;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.message.CamelKapuaMessage;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.consumer.lifecycle.listener.DeviceManagementNotificationMessageProcessor;
import org.eclipse.kapua.consumer.lifecycle.listener.DeviceManagementRegistryNotificationMetrics;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.UUID;

@Category(JUnitTests.class)
public class DeviceManagementNotificationMessageProcessorTest extends Assert {

    DeviceManagementNotificationMessageProcessor deviceManagementNotificationMessageProcessor;
    CamelKapuaMessage camelKapuaMessage;
    Exchange exchange;
    KapuaMessage message;
    Counter queueConfigurationErrorCount, queueGenericErrorCount;

    @Before
    public void initialize() {
        deviceManagementNotificationMessageProcessor = new DeviceManagementNotificationMessageProcessor();
        camelKapuaMessage = Mockito.mock(CamelKapuaMessage.class);
        exchange = Mockito.mock(Exchange.class);
        message = Mockito.mock(KapuaMessage.class);
        queueConfigurationErrorCount = MetricServiceFactory.getInstance().getCounter(DeviceManagementRegistryNotificationMetrics.METRIC_MODULE_NAME, DeviceManagementRegistryNotificationMetrics.METRIC_COMPONENT_NOTIFICATION, DeviceManagementRegistryNotificationMetrics.METRIC_PROCESS_QUEUE, DeviceManagementRegistryNotificationMetrics.METRIC_CONFIGURATION, DeviceManagementRegistryNotificationMetrics.METRIC_ERROR, DeviceManagementRegistryNotificationMetrics.METRIC_COUNT);
        queueGenericErrorCount = MetricServiceFactory.getInstance().getCounter(DeviceManagementRegistryNotificationMetrics.METRIC_MODULE_NAME, DeviceManagementRegistryNotificationMetrics.METRIC_COMPONENT_NOTIFICATION, DeviceManagementRegistryNotificationMetrics.METRIC_PROCESS_QUEUE, DeviceManagementRegistryNotificationMetrics.METRIC_GENERIC, DeviceManagementRegistryNotificationMetrics.METRIC_ERROR, DeviceManagementRegistryNotificationMetrics.METRIC_COUNT);

        Mockito.when(camelKapuaMessage.getDatastoreId()).thenReturn("datastoreID");
        Mockito.when(camelKapuaMessage.getMessage()).thenReturn(message);
        Mockito.when(message.getId()).thenReturn(new UUID(1, 10));
    }

    @Test
    public void processConfigurationErrorMessageTest() throws KapuaException {
        assertEquals("Expected and actual values should be the same.", 0L, queueConfigurationErrorCount.getCount());
        deviceManagementNotificationMessageProcessor.processConfigurationErrorMessage(exchange, camelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", -1L, queueConfigurationErrorCount.getCount());

        queueConfigurationErrorCount.inc();
    }

    @Test
    public void processConfigurationErrorMessageNullExchangeTest() throws KapuaException {
        assertEquals("Expected and actual values should be the same.", 0L, queueConfigurationErrorCount.getCount());
        deviceManagementNotificationMessageProcessor.processConfigurationErrorMessage(null, camelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", -1L, queueConfigurationErrorCount.getCount());

        queueConfigurationErrorCount.inc();
    }

    @Test
    public void processConfigurationErrorNullMessageTest() {
        assertEquals("Expected and actual values should be the same.", 0L, queueConfigurationErrorCount.getCount());
        try {
            deviceManagementNotificationMessageProcessor.processConfigurationErrorMessage(exchange, null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, queueConfigurationErrorCount.getCount());
    }

    @Test
    public void processConfigurationErrorNullTest() {
        assertEquals("Expected and actual values should be the same.", 0L, queueConfigurationErrorCount.getCount());
        try {
            deviceManagementNotificationMessageProcessor.processConfigurationErrorMessage(null, null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, queueConfigurationErrorCount.getCount());
    }

    @Test
    public void processGenericErrorMessageTest() throws KapuaException {
        assertEquals("Expected and actual values should be the same.", 0L, queueGenericErrorCount.getCount());
        deviceManagementNotificationMessageProcessor.processGenericErrorMessage(exchange, camelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", -1L, queueGenericErrorCount.getCount());

        queueGenericErrorCount.inc();
    }

    @Test
    public void processGenericErrorMessageNullExchangeTest() throws KapuaException {
        assertEquals("Expected and actual values should be the same.", 0L, queueGenericErrorCount.getCount());
        deviceManagementNotificationMessageProcessor.processGenericErrorMessage(null, camelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", -1L, queueGenericErrorCount.getCount());

        queueGenericErrorCount.inc();
    }

    @Test
    public void processGenericErrorNullMessageTest() {
        assertEquals("Expected and actual values should be the same.", 0L, queueGenericErrorCount.getCount());
        try {
            deviceManagementNotificationMessageProcessor.processGenericErrorMessage(exchange, null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, queueGenericErrorCount.getCount());
    }

    @Test
    public void processGenericErrorNullTest() {
        assertEquals("Expected and actual values should be the same.", 0L, queueGenericErrorCount.getCount());
        try {
            deviceManagementNotificationMessageProcessor.processGenericErrorMessage(null, null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, queueGenericErrorCount.getCount());
    }
}