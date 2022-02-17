/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.listener;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class DeviceManagementRegistryNotificationMetricsTest extends Assert {

    @Test
    public void deviceManagementRegistryNotificationMetricsTest() throws Exception {
        Constructor<DeviceManagementRegistryNotificationMetrics> deviceManagementRegistryNotificationMetrics = DeviceManagementRegistryNotificationMetrics.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(deviceManagementRegistryNotificationMetrics.getModifiers()));
        deviceManagementRegistryNotificationMetrics.setAccessible(true);
        deviceManagementRegistryNotificationMetrics.newInstance();
    }

    @Test
    public void checkConstantsTest() {
        assertEquals("Expected and actual values should be the same.", "device_management_registry", DeviceManagementRegistryNotificationMetrics.METRIC_MODULE_NAME);
        assertEquals("Expected and actual values should be the same.", "notification", DeviceManagementRegistryNotificationMetrics.METRIC_COMPONENT_NOTIFICATION);
        assertEquals("Expected and actual values should be the same.", "deviceLifeCycle", DeviceManagementRegistryNotificationMetrics.METRIC_COMPONENT_DEVICE_LIFE_CYCLE);
        assertEquals("Expected and actual values should be the same.", "messages", DeviceManagementRegistryNotificationMetrics.METRIC_MESSAGES);
        assertEquals("Expected and actual values should be the same.", "process_queue", DeviceManagementRegistryNotificationMetrics.METRIC_PROCESS_QUEUE);
        assertEquals("Expected and actual values should be the same.", "communication", DeviceManagementRegistryNotificationMetrics.METRIC_COMMUNICATION);
        assertEquals("Expected and actual values should be the same.", "configuration", DeviceManagementRegistryNotificationMetrics.METRIC_CONFIGURATION);
        assertEquals("Expected and actual values should be the same.", "generic", DeviceManagementRegistryNotificationMetrics.METRIC_GENERIC);
        assertEquals("Expected and actual values should be the same.", "apps", DeviceManagementRegistryNotificationMetrics.METRIC_APPS);
        assertEquals("Expected and actual values should be the same.", "birth", DeviceManagementRegistryNotificationMetrics.METRIC_BIRTH);
        assertEquals("Expected and actual values should be the same.", "dc", DeviceManagementRegistryNotificationMetrics.METRIC_DC);
        assertEquals("Expected and actual values should be the same.", "missing", DeviceManagementRegistryNotificationMetrics.METRIC_MISSING);
        assertEquals("Expected and actual values should be the same.", "unmatched", DeviceManagementRegistryNotificationMetrics.METRIC_UNMATCHED);
        assertEquals("Expected and actual values should be the same.", "error", DeviceManagementRegistryNotificationMetrics.METRIC_ERROR);
        assertEquals("Expected and actual values should be the same.", "count", DeviceManagementRegistryNotificationMetrics.METRIC_COUNT);
    }
}