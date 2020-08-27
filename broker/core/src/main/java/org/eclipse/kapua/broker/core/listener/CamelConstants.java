/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

/**
 * Camel Header constants
 *
 * @since 1.0
 */
public class CamelConstants {

    private CamelConstants() { }

    public static final String JMS_EXCHANGE_FAILURE_ENDPOINT = "CamelFailureEndpoint";
    public static final String JMS_EXCHANGE_FAILURE_EXCEPTION = "CamelExceptionCaught";
    public static final String JMS_EXCHANGE_REDELIVERED = "JMSRedelivered";

    public static final String JMS_HEADER_TIMESTAMP = "JMSTimestamp";
    public static final String JMS_HEADER_DESTINATION = "JMSDestination";

    public static final String JMS_CORRELATION_ID = "JMSCorrelationID";

}
