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
package org.eclipse.kapua.service.camel.listener.error;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.service.camel.application.MetricsCamel;
import org.eclipse.kapua.service.client.message.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor called before sending the message to the dlq processor chain.
 * So this is the last chance to modify the object or take actions before the message will be discarded.
 *
 */
public class FailureProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FailureProcessor.class);

    public static enum MessageProcessResult {

        Error("ERR");

        private String asUrl;

        MessageProcessResult(String asUrl) {
            this.asUrl = asUrl;
        }

        public String getAsUrl() {
            return asUrl;
        }
    }

    //TODO inject!!!
    private MetricsCamel metrics;

    public FailureProcessor() {
        metrics = MetricsCamel.getInstance();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(MessageConstants.HEADER_KAPUA_PROCESS_RESULT, MessageProcessResult.Error.getAsUrl());
        if (isUnauthenticatedException(exchange)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Detected unauthenticated error on message processing retry!");
            }
            metrics.getUnauthenticatedError().inc();
        }
        else {
            metrics.getGenericError().inc();
        }
    }

    private boolean isUnauthenticatedException(Exchange exchange) {
        Exception e = getException(exchange);
        return e instanceof KapuaUnauthenticatedException || e.getCause() instanceof KapuaUnauthenticatedException;
    }

    private Exception getException(Exchange exchange) {
        if (exchange.getException() != null) {
            return exchange.getException();
        }
        else {
            return (Exception)exchange.getProperty(org.apache.camel.Exchange.EXCEPTION_CAUGHT);
        }
    }
}
