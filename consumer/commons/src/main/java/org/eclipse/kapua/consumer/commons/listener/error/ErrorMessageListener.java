/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.commons.listener.error;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.broker.client.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.client.message.MessageConstants;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.consumer.commons.CommonMetrics;
import org.eclipse.kapua.consumer.commons.listener.AbstractListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

@UriEndpoint(title = "error message processor", syntax = "bean:ecErrorMessageListener", scheme = "bean")
public class ErrorMessageListener extends AbstractListener {

    private static final String ERROR_MESSAGE_LOGGER_NAME = "errorMessage";
    private static final Logger ERROR_MESSAGE_LOGGER = LoggerFactory.getLogger(ERROR_MESSAGE_LOGGER_NAME);
    private static final String ERROR_DATE_MESSAGE_PATTERN = "DATE_ERR:%s";

    private static final Logger logger = LoggerFactory.getLogger(ErrorMessageListener.class);

    // use already defined constants on the new versions
    public static final String JMS_EXCHANGE_REDELIVERY_COUNTER = org.apache.camel.Exchange.REDELIVERY_COUNTER;
    public static final String HEADER_DYNAMIC_ROUTE_CAMEL_EXCEPTION_CAUGHT = org.apache.camel.Exchange.EXCEPTION_CAUGHT;
    public static final String HEADER_DYNAMIC_ROUTE_CAMEL_FAILURE_ENDPOINT = org.apache.camel.Exchange.FAILURE_ENDPOINT;
    private static final String EMPTY_ENCODED_MESSAGE = "N/A";
    private static final String EMPTY_FIELD = "N/A";

    private Counter metricError;
    private Counter metricErrorStoredToFile;
    private Counter metricErrorStoredToFileError;
    private Counter metricErrorUnknownBodyType;

    public ErrorMessageListener() {
        super(CommonMetrics.ERROR);
        metricError = registerCounter(CommonMetrics.MESSAGES, "generic", CommonMetrics.COUNT);
        metricErrorStoredToFile = registerCounter(CommonMetrics.MESSAGES, "file_store", CommonMetrics.COUNT);
        metricErrorStoredToFileError = registerCounter(CommonMetrics.MESSAGES, "file_store", CommonMetrics.ERROR, CommonMetrics.COUNT);
        metricErrorUnknownBodyType = registerCounter(CommonMetrics.MESSAGES, "message_conversion_unknown_type", CommonMetrics.ERROR, CommonMetrics.COUNT);
    }

    /**
     * Process an error condition for an elaboration of a generic message
     *
     * @param exchange
     * @param message
     */
    public void processMessage(Exchange exchange, Object message) {
        metricError.inc();
        logToFile(exchange, message);
    }

    public void logToFile(Exchange exchange, Object value) {
        try {
            String messageLine = getMessage(exchange);
            ERROR_MESSAGE_LOGGER.info(messageLine);
            metricErrorStoredToFile.inc();
        } catch (Exception e) {
            metricErrorStoredToFileError.inc();
            logger.error("Error while storing errored message to file!", e);
        }
    }

    private String getMessage(Exchange exchange) {
        Message message = exchange.getIn();
        String clientId = message.getHeader(MessageConstants.HEADER_KAPUA_CLIENT_ID, String.class);
        Long timestamp = message.getHeader(MessageConstants.HEADER_KAPUA_RECEIVED_TIMESTAMP, Long.class);
        String encodedMsg = getMessageBody(message.getBody());
        String messageLogged = String.format("%s %s %s",
                clientId!=null ? clientId : EMPTY_FIELD,
                getDate(timestamp),
                encodedMsg);
        if (logger.isDebugEnabled()) {
            logger.debug(messageLogged);
        }
        return messageLogged;
    }

    private String getDate(Long timestamp) {
        try {
            return timestamp!=null ? KapuaDateUtils.formatDate(new Date(timestamp)) : EMPTY_FIELD;
        } catch (ParseException e) {
            return String.format(ERROR_DATE_MESSAGE_PATTERN, timestamp);
        }
    }

    private String getMessageBody(Object body) {
        if (body instanceof CamelKapuaMessage<?>) {
            return getBody(((CamelKapuaMessage<?>) body).getMessage());
        }
        else {
            return getBody(body);
        }
    }

    private String getBody(Object body) {
        if (body instanceof byte[]) {
            return Base64.getEncoder().encodeToString((byte[])body);
        }
        else if (body instanceof String) {
            return Base64.getEncoder().encodeToString(((String) body).getBytes());
        }
        else {
            //something wrong happened! Anyway try to get the message to be stored
            logger.error("Wrong message type! Cannot convert message of type {} to byte[]", body!=null ? body.getClass() : "N/A");
            metricErrorUnknownBodyType.inc();
            return EMPTY_ENCODED_MESSAGE;
        }
     }

    protected int getDelivery(Exchange exchange) {
        return exchange.getIn().getHeader(JMS_EXCHANGE_REDELIVERY_COUNTER, 0, Integer.class);
    }

    protected String getMessageId(Exchange exchange) {
        return exchange.getIn().getMessageId();
    }

}
