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
