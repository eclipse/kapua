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

    private Counter metricAuth;
    private Counter metricAuthError;

    public AuthenticationServiceConverter() {
        super(MetricLabel.SERVICE_AUTHENTICATION);
        metricAuth = METRICS_SERVICE.getCounter(MetricLabel.SERVICE_AUTHENTICATION, MetricLabel.CONVERSION, MetricsLabel.SUCCESS);
        metricAuthError = METRICS_SERVICE.getCounter(MetricLabel.SERVICE_AUTHENTICATION, MetricLabel.CONVERSION, MetricsLabel.ERROR);
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
            metricAuth.inc();
            return authRequest;
        } catch (IOException e) {
            metricAuthError.inc();
            throw KapuaException.internalError(e, "Error while converting authentication message");
        }
    }

    @Converter
    public EntityRequest convertToGetEntity(Exchange exchange, Object value) throws KapuaException {
        try {
            String body = ((JmsMessage)exchange.getIn()).getBody(String.class);
            EntityRequest entityRequest = reader.readValue(body, EntityRequest.class);
            metricAuth.inc();
            return entityRequest;
        } catch (IOException e) {
            metricAuthError.inc();
            throw KapuaException.internalError(e, "Error while converting getEntity message");
        }
    }
}
