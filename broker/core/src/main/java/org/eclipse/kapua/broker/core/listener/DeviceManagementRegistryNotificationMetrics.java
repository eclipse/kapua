/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

public class DeviceManagementRegistryNotificationMetrics {

    private DeviceManagementRegistryNotificationMetrics() { }

    public static final String METRIC_MODULE_NAME = "device_management_registry";

    public static final String METRIC_COMPONENT_NOTIFICATION = "notification";
    public static final String METRIC_COMPONENT_DEVICE_LIFE_CYCLE = "deviceLifeCycle";

    public static final String METRIC_MESSAGES = "messages";
    public static final String METRIC_PROCESS_QUEUE = "process_queue";
    public static final String METRIC_COMMUNICATION = "communication";
    public static final String METRIC_CONFIGURATION = "configuration";
    public static final String METRIC_GENERIC = "generic";

    public static final String METRIC_APPS = "apps";
    public static final String METRIC_BIRTH = "birth";
    public static final String METRIC_DC = "dc";
    public static final String METRIC_MISSING = "missing";
    public static final String METRIC_UNMATCHED = "unmatched";

    public static final String METRIC_ERROR = "error";
    public static final String METRIC_COUNT = "count";

}
