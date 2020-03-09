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
package org.eclipse.kapua.broker.core.converter;

public class ConverterMetrics {

    private ConverterMetrics() { }

    public static final String METRIC_MODULE_NAME = "converter";

    public static final String METRIC_COMPONENT_NAME = "kapua";

    public static final String METRIC_JMS = "jms";
    public static final String METRIC_MESSAGE = "message";
    public static final String METRIC_MESSAGES = "messages";
    public static final String METRIC_ERROR = "error";
    public static final String METRIC_COUNT = "count";
    public static final String METRIC_KAPUA_MESSAGE = "kapua_message";
    public static final String METRIC_DATA = "data";

    public static final String METRIC_APP = "app";
    public static final String METRIC_BIRTH = "birth";
    public static final String METRIC_DC = "dc";
    public static final String METRIC_MISSING = "missing";
    public static final String METRIC_NOTIFY = "notify";

}
