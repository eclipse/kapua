/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication;

import java.io.IOException;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.client.security.bean.EntityRequest;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.service.camel.converter.AbstractKapuaConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Kapua message converter used to convert authentication messages.
 *
 */
public class AuthenticationServiceConverter extends AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceConverter.class);

    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectReader reader = mapper.reader();//check if it's thread safe

    private Counter metricConverterAuthenticationMessage;
    private Counter metricConverterAuthenticationMessageError;

    public AuthenticationServiceConverter() {
        super();
        metricConverterAuthenticationMessage = METRICS_SERVICE.getCounter(MetricsLabel.MODULE_CONVERTER, MetricsLabel.COMPONENT_KAPUA, AuthenticationServiceListener.METRIC_AUTHENTICATION, MetricsLabel.MESSAGES, MetricsLabel.COUNT);
        metricConverterAuthenticationMessageError = METRICS_SERVICE.getCounter(MetricsLabel.MODULE_CONVERTER, MetricsLabel.COMPONENT_KAPUA, AuthenticationServiceListener.METRIC_AUTHENTICATION, MetricsLabel.MESSAGES, MetricsLabel.ERROR, MetricsLabel.COUNT);
    }

    /**
     * Convert incoming message to a authentication request message
     *
     * @param exchange
     * @param value
     * @return Authorization request bean
     * @throws KapuaException
     *             if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public AuthRequest convertToAuthRequest(Exchange exchange, Object value) throws KapuaException {
        try {
            String body = ((JmsMessage)exchange.getIn()).getBody(String.class);
            AuthRequest authRequest = reader.readValue(body, AuthRequest.class);
            metricConverterAuthenticationMessage.inc();
            return authRequest;
        } catch (IOException e) {
            metricConverterAuthenticationMessageError.inc();
            throw KapuaException.internalError(e, "Error while converting authentication message");
        }
    }

    @Converter
    public EntityRequest convertToGetEntity(Exchange exchange, Object value) throws KapuaException {
        try {
            String body = ((JmsMessage)exchange.getIn()).getBody(String.class);
            EntityRequest entityRequest = reader.readValue(body, EntityRequest.class);
            metricConverterAuthenticationMessage.inc();
            return entityRequest;
        } catch (IOException e) {
            metricConverterAuthenticationMessageError.inc();
            throw KapuaException.internalError(e, "Error while converting getEntity message");
        }
    }
}
