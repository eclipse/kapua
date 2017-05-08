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

import org.apache.camel.Exchange;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

@UriEndpoint(title = "error message processor", syntax = "bean:errorMessageListener", scheme = "bean")
/**
 * Error message listener endpoint.
 * 
 * @since 1.0
 */
public class ErrorMessageListener extends AbstractListener {

    private static final Logger s_logger = LoggerFactory.getLogger(ErrorMessageListener.class);

    private Counter metricError;
    private Counter metricErrorLifeCycleMessage;

    public ErrorMessageListener() {
        super("error");
        metricError = registerCounter("messages", "generic", "count");
        metricErrorLifeCycleMessage = registerCounter("messages", "life_cycle", "count");
    }

    /**
     * Process an error condition for an elaboration of a generic message
     * 
     * @param exchange
     * @param message
     * @throws KapuaException
     */
    public void processMessage(Exchange exchange, Object message) throws KapuaException {
        metricError.inc();
        logError(exchange, message, "generic");
    }

    /**
     * Process an error condition for an elaboration of a life cycle message
     * 
     * @param exchange
     * @param message
     * @throws KapuaException
     */
    public void lifeCycleMessage(Exchange exchange, Object message) throws KapuaException {
        metricErrorLifeCycleMessage.inc();
        logError(exchange, message, "LifeCycle");
    }

    /**
     * Process an error condition for an elaboration of a camel routing unmatched message
     * 
     * @param exchange
     * @param message
     * @throws KapuaException
     */
    public void unmatchedMessage(Exchange exchange, Object message) throws KapuaException {
        metricErrorLifeCycleMessage.inc();
        logUnmatched(exchange, message, "unmatched");
    }

    private void logError(Exchange exchange, Object message, String serviceName) {
        Throwable t = ((Throwable) exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION));
        s_logger.warn("Processing error message for service {}... Message type {} - Endpoint {} - Error message {}",
                new Object[] {
                        serviceName,
                        (message != null ? message.getClass().getName() : null),
                        exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_ENDPOINT),
                        t.getMessage() });
        s_logger.warn("Exception: ", t);
    }

    private void logUnmatched(Exchange exchange, Object message, String serviceName) {
        s_logger.warn("Processing unmatched message for service {}... Message type {} - Endpoint {}",
                new Object[] {
                        serviceName,
                        (message != null ? message.getClass().getName() : null),
                        exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_ENDPOINT) });
    }

}
