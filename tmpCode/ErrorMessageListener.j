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
package org.eclipse.kapua.consumer.commons.listener;

import com.codahale.metrics.Counter;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.UriEndpoint;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.client.message.MessageConstants;
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

    private static final Logger logger = LoggerFactory.getLogger(ErrorMessageListener.class);

    //use already defined constants on the new versions
    public static String JMS_EXCHANGE_REDELIVERY_COUNTER = org.apache.camel.Exchange.REDELIVERY_COUNTER;
    public static final String HEADER_DYNAMIC_ROUTE_CAMEL_EXCEPTION_CAUGHT = org.apache.camel.Exchange.EXCEPTION_CAUGHT;
    public static final String HEADER_DYNAMIC_ROUTE_CAMEL_FAILURE_ENDPOINT = org.apache.camel.Exchange.FAILURE_ENDPOINT;
    private static final String EMPTY_ENCODED_MESSAGE = "N/A";
    private static final String EMPTY_FIELD = "N/A";
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
        logger.warn("Processing error message for service {}... Message type {} - Endpoint {} - Error message {}",
                serviceName,
                (message != null ? message.getClass().getName() : null),
                exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_ENDPOINT),
                t != null ? t.getMessage() : NO_EXCEPTION_FOUND_MESSAGE);
        logger.warn("Exception: ", t);
    }

    public void logToFile(Exchange exchange, Object value) {
        try {
            Message message = exchange.getIn();
            Object body = message.getBody();
            String encodedMsg = getBody(body);
            String failureEndpoint = getFailureEndpoint(exchange);
            Exception e = getException(exchange);
            String exceptionMessage = getExceptionMessage(exchange);
            if (logger.isDebugEnabled()) {
                logger.debug(errorLogLine);
            }
            loggerDynamicRouteError.info(errorLogLine);
        } catch (Exception e) {
            MetricRouteStatisticsHelper.inc(mc.getRp(), 1, COMPONENT_NAME_DLQ, DynamicRouteDlqMetric.TO_FILE_ERROR.getValue());
            logger.error("Error while storing errored message to file!", e);
        }
    }

    public static Exception getException(Exchange exchange) {
        if (exchange.getException() != null) {
            return exchange.getException();
        }
        else {
            return (Exception)exchange.getProperty(HEADER_DYNAMIC_ROUTE_CAMEL_EXCEPTION_CAUGHT);
        }
    }

    public static String getExceptionMessage(Exchange exchange) {
        Exception e = null;
        if (exchange.getException() != null) {
            e = exchange.getException();
        }
        else {
            e = (Exception)exchange.getProperty(HEADER_DYNAMIC_ROUTE_CAMEL_EXCEPTION_CAUGHT);
        }
        return e != null ? e.getMessage() : EMPTY_FIELD;
    }

    public static String getFailureEndpoint(Exchange exchange) {
        return exchange.getProperty(HEADER_DYNAMIC_ROUTE_CAMEL_FAILURE_ENDPOINT, String.class);
    }

    protected int getDelivery(Exchange exchange) {
        return exchange.getIn().getHeader(JMS_EXCHANGE_REDELIVERY_COUNTER, 0, Integer.class);
    }

    protected String getMessageId(Exchange exchange) {
        return exchange.getIn().getMessageId();
    }

    private String getBody(Object body) {
        if (body instanceof byte[]) {
            return Base64.getEncoder().encodeToString((byte[])body);
        }
        else if (body instanceof String) {
            //TODO encode as 64
            return (String) body;
        }
        else if (body instanceof CamelKapuaMessage<?>) {
            //TODO encode as 64
            return ((CamelKapuaMessage<?>) body).getMessage();
        }
        else {
            //something wrong happened! Anyway try to get the message to be stored
            logger.error("Wrong message type! Cannot convert message of type {} to byte[]", body!=null ? body.getClass() : "N/A");
            return EMPTY_ENCODED_MESSAGE;
        }
    }
}
