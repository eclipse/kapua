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

public class DataStoreMetrics {

    private DataStoreMetrics() { }

    public static final String METRIC_MODULE_NAME = "datastore";

    public static final String METRIC_COMPONENT_NAME = "datastore";

    public static final String METRIC_STORE = "store";
    public static final String METRIC_QUEUE = "queue";
    public static final String METRIC_COMMUNICATION = "communication";
    public static final String METRIC_CONFIGURATION = "configuration";
    public static final String METRIC_GENERIC = "generic";

    public static final String METRIC_ERROR = "error";
    public static final String METRIC_COUNT = "count";

}
