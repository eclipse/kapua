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

import javax.inject.Inject;
import javax.jms.JMSException;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.client.security.bean.EntityRequest;
import org.eclipse.kapua.client.security.bean.EntityResponse;
import org.eclipse.kapua.client.security.bean.MessageConstants;
import org.eclipse.kapua.client.security.bean.Request;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.client.security.bean.AuthResponse;
import org.eclipse.kapua.service.camel.listener.AbstractListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@UriEndpoint(title = "authentication service listener", syntax = "bean:authenticationServiceListener", scheme = "bean")
public class AuthenticationServiceListener extends AbstractListener {

    protected static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceListener.class);

    public static final String METRIC_AUTHENTICATION = "authentication";
    public static final String METRIC_LOGIN = "login";
    public static final String METRIC_LOGOUT = "logout";
    public static final String METRIC_GET_ACCOUNT = "getAccount";

    private Counter metricLoginCount;
    private Counter metricLoginRequestCount;
    private Counter metricLogoutCount;
    private Counter metricLogoutRequestCount;
    private Counter metricGetAccountCount;
    private Counter metricGetAccountRequestCount;

    @Inject
    private AuthenticationServiceBackEndCall authenticationServiceBackEndCall;

    public AuthenticationServiceListener() {
        super(METRIC_AUTHENTICATION);
        metricLoginCount = registerCounter(MetricsLabel.MESSAGES, METRIC_LOGIN, MetricsLabel.COUNT);
        metricLoginRequestCount = registerCounter(MetricsLabel.MESSAGES, METRIC_LOGIN, MetricsLabel.REQUEST, MetricsLabel.COUNT);
        metricLogoutCount = registerCounter(MetricsLabel.MESSAGES, METRIC_LOGOUT, MetricsLabel.COUNT);
        metricLogoutRequestCount = registerCounter(MetricsLabel.MESSAGES, METRIC_LOGOUT, MetricsLabel.REQUEST, MetricsLabel.COUNT);
        metricGetAccountCount = registerCounter(MetricsLabel.MESSAGES, METRIC_GET_ACCOUNT, MetricsLabel.COUNT);
        metricGetAccountRequestCount = registerCounter(MetricsLabel.MESSAGES, METRIC_GET_ACCOUNT, MetricsLabel.REQUEST, MetricsLabel.COUNT);
    }

    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter writer = mapper.writer();//check if it's thread safe

    public void brokerConnect(Exchange exchange, AuthRequest authRequest) throws JsonProcessingException, JMSException {
        metricLoginRequestCount.inc();
        logRequest(exchange, authRequest);
        AuthResponse authResponse = authenticationServiceBackEndCall.brokerConnect(authRequest);
        updateMessage(exchange, authRequest, authResponse);
        metricLoginCount.inc();
    }

    public void brokerDisconnect(Exchange exchange, AuthRequest authRequest) throws JsonProcessingException, JMSException {
        metricLogoutRequestCount.inc();
        logRequest(exchange, authRequest);
        AuthResponse authResponse = authenticationServiceBackEndCall.brokerDisconnect(authRequest);
        updateMessage(exchange, authRequest, authResponse);
        metricLogoutCount.inc();
    }

    public void getEntity(Exchange exchange, EntityRequest accountRequest) throws JsonProcessingException, JMSException {
        metricGetAccountRequestCount.inc();
        logRequest(exchange, accountRequest);
        EntityResponse accountResponse = authenticationServiceBackEndCall.getEntity(accountRequest);
        updateMessage(exchange, accountRequest, accountResponse);
        metricGetAccountCount.inc();
    }

    public void updateMessage(Exchange exchange, AuthRequest authRequest, AuthResponse authResponse) throws JMSException, JsonProcessingException {
        Message message = exchange.getIn();
        String textPayload = null;
        if (authResponse!=null) {
            textPayload = writer.writeValueAsString(authResponse);
            message.setBody(textPayload, String.class);
        }
        message.setHeader(MessageConstants.HEADER_REQUESTER, authRequest.getRequester());
        message.setHeader(MessageConstants.HEADER_REQUEST_ID, authRequest.getRequestId());
        message.setHeader(MessageConstants.HEADER_ACTION, authRequest.getAction());
        message.setHeader(MessageConstants.HEADER_USERNAME, authRequest.getUsername());
        message.setHeader(MessageConstants.HEADER_CLIENT_ID, authRequest.getClientId());
        message.setHeader(MessageConstants.HEADER_CLIENT_IP, authRequest.getClientIp());
        message.setHeader(MessageConstants.HEADER_CONNECTION_ID, authRequest.getConnectionId());
        message.setHeader(MessageConstants.HEADER_RESULT_CODE, authResponse.getResultCode());
        message.setHeader(MessageConstants.HEADER_ERROR_CODE, authResponse.getErrorCode());
    }

    public void updateMessage(Exchange exchange, EntityRequest entityRequest, EntityResponse entityResponse) throws JMSException, JsonProcessingException {
        Message message = exchange.getIn();
        String textPayload = null;
        if (entityResponse!=null) {
            textPayload = writer.writeValueAsString(entityResponse);
            message.setBody(textPayload, String.class);
        }
        message.setHeader(MessageConstants.HEADER_REQUESTER, entityRequest.getRequester());
        message.setHeader(MessageConstants.HEADER_REQUEST_ID, entityRequest.getRequestId());
        message.setHeader(MessageConstants.HEADER_ACTION, entityRequest.getAction());
        message.setHeader(MessageConstants.HEADER_RESULT_CODE, entityResponse.getResultCode());
        message.setHeader(MessageConstants.HEADER_ERROR_CODE, entityResponse.getErrorCode());
    }

    private void logRequest(Exchange exchange, Request request) {
        logger.info("Message id: {} - request id: {} - action: {} - requester: {}",
            exchange.getIn().getMessageId(),
            request.getRequestId(), request.getAction(), request.getRequester());
    }
}
