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
package org.eclipse.kapua.service.elasticsearch.client.rest;

public class DatastoreRestClientMetrics {

    private DatastoreRestClientMetrics() {
    }

    public static final String METRIC_COMPONENT_NAME = "rest-client";

    public static final String METRIC_RUNTIME_EXEC = "runtime_exc";
    public static final String METRIC_TIMEOUT_RETRY = "timeout_retry";
    public static final String TIMEOUT_RETRY_LIMIT_REACHED = "timeout_retry_limit_reached";
    public static final String METRIC_COUNT = "count";
}
