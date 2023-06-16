/*******************************************************************************
 * Copyright (c) 2022, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.metric;

public class MetricsLabel {

    public static final String MESSAGE_DATA = "data";
    public static final String MESSAGE_APPS = "apps";
    public static final String MESSAGE_BIRTH = "birth";
    public static final String MESSAGE_DC = "dc";
    public static final String MESSAGE_MISSING = "missing";
    public static final String MESSAGE_NOTIFY = "notify";
    public static final String MESSAGE_UNMATCHED = "unmatched";

    public static final String ATTEMPT = "attempt";

    public static final String ERROR = "error";
    public static final String FAILURE = "failure";
    public static final String RETRY = "retry";
    public static final String SUCCESS = "success";
    public static final String TIMEOUT = "timeout";
    public static final String COUNT = "count";

    public static final String COMMUNICATION = "communication";
    public static final String CONFIGURATION = "configuration";
    public static final String VALIDATION = "validation";
    public static final String GENERIC = "generic";

    public static final String LAST = "last";
    public static final String TIME = "time";
    public static final String SECONDS = "s";
    public static final String MILLI_SECONDS = "ms";
    public static final String SIZE = "size";
    public static final String BYTES = "bytes";
    public static final String TOTAL = "total";

    private MetricsLabel() {
    }

}
