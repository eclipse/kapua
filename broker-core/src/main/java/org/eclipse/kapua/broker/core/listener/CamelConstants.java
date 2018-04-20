/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
public interface CamelConstants {

    public String JMS_EXCHANGE_FAILURE_ENDPOINT = "CamelFailureEndpoint";
    public String JMS_EXCHANGE_FAILURE_EXCEPTION = "CamelExceptionCaught";
    public String JMS_EXCHANGE_REDELIVERED = "JMSRedelivered";

    public String JMS_HEADER_TIMESTAMP = "JMSTimestamp";
    public String JMS_HEADER_DESTINATION = "JMSDestination";

    public String JMS_CORRELATION_ID = "JMSCorrelationID";

}
