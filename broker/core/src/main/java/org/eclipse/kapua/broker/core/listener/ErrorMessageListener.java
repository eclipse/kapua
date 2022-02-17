/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

import com.codahale.metrics.Counter;
import org.apache.camel.Exchange;
import org.apache.camel.spi.UriEndpoint;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

@UriEndpoint(title = "error message processor", syntax = "bean:errorMessageListener", scheme = "bean")
/**
 * Error message listener endpoint.
 *
 * @since 1.0
 */
public class ErrorMessageListener extends AbstractListener {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorMessageListener.class);

    private static final String NO_EXCEPTION_FOUND_MESSAGE = "NO EXCEPTION FOUND!";

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

    private void logError(Exchange exchange, Object message, String serviceName) {
        Throwable t = null;
        // looking at the property filled by the KapuaCamelFilter#bridgeError)
        String encodedException = exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class);
        if (encodedException != null) {
            t = SerializationUtils.deserialize(Base64.getDecoder().decode(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)));
        } else {
            // otherwise fallback to the exchange property or the exchange exception
            t = ((Throwable) exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION));
            if (t == null) {
                t = exchange.getException();
            }
        }
        LOG.warn("Processing error message for service {}... Message type {} - Endpoint {} - Error message {}",
                serviceName,
                (message != null ? message.getClass().getName() : null),
                exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_ENDPOINT),
                t != null ? t.getMessage() : NO_EXCEPTION_FOUND_MESSAGE);
        LOG.warn("Exception: ", t);
    }
}
