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
package org.eclipse.kapua.service.camel;

public class CommonMetrics {

    private CommonMetrics() {
    }

    public static final String MESSAGES = "messages";
    public static final String REQUEST = "request";
    public static final String ERROR = "error";
    public static final String COUNT = "count";
    public static final String PROCESSOR = "processor";
    public static final String FAILURE = "failure";
    public static final String UNAUTHENTICATED = "unauthenticated";
}
