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
package org.eclipse.kapua.service.elasticsearch.client.rest;

public class DatastoreRestClientMetrics {

    private DatastoreRestClientMetrics() { }

    public static final String METRIC_MODULE_NAME = "datastore-rest-client";

    public static final String METRIC_COMPONENT_NAME = "rest-client";

    public static final String METRIC_RUNTIME_EXEC = "runtime_exc";
    public static final String METRIC_TIMEOUT_RETRY = "timeout_retry";
    public static final String TIMEOUT_RETRY_LIMIT_REACHED = "timeout_retry_limit_reached";
    public static final String METRIC_COUNT = "count";
}
