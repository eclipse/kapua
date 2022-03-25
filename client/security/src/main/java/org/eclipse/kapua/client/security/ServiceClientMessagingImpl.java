/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security;

import java.util.UUID;

import javax.jms.JMSException;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.client.security.amqpclient.Client;
import org.eclipse.kapua.client.security.bean.EntityRequest;
import org.eclipse.kapua.client.security.bean.EntityResponse;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.client.security.bean.AuthResponse;
import org.eclipse.kapua.client.security.bean.Request;
import org.eclipse.kapua.client.security.bean.ResponseContainer;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Security service. Implementation through AMQP messaging layer.
 *
 */
public class ServiceClientMessagingImpl implements ServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ServiceClientMessagingImpl.class);

    public static final String REQUEST_QUEUE = "auth_request";
    public static final String RESPONSE_QUEUE_PREFIX = "auth_response_";

    private static final int TIMEOUT = 5000;

    private Client client;

    public ServiceClientMessagingImpl(String requester) {
        //TODO change configuration (use service event broker for now)
        String clientId = "auth-" + UUID.randomUUID().toString();
        String host = SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_HOST, "events-broker");
        int port = SystemSetting.getInstance().getInt(SystemSettingKey.SERVICE_BUS_PORT, 5672);
        String username = SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_USERNAME, "username");
        String password = SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_PASSWORD, "password");
        try {
            client = new Client(username, password, host, port, clientId,
                REQUEST_QUEUE, RESPONSE_QUEUE_PREFIX + requester, new MessageListener());
        } catch (JMSException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, (Object[])null);
        }
    }

    @Override
    public AuthResponse brokerConnect(AuthRequest authRequest) throws InterruptedException, JMSException, JsonProcessingException {//TODO review exception when Kapua code will be linked (throw KapuaException)
        client.checkAuthServiceConnection();
        String requestId = MessageHelper.getNewRequestId();
        authRequest.setRequestId(requestId);
        authRequest.setAction(SecurityAction.brokerConnect.name());
        ResponseContainer<AuthResponse> responseContainer = ResponseContainer.createAnRegisterNewMessageContainer(authRequest);
        logRequest(authRequest);
        client.sendMessage(MessageHelper.getBrokerConnectMessage(client.createTextMessage(), authRequest));
        synchronized (responseContainer) {
            responseContainer.wait(TIMEOUT);
        }
        MessageListener.removeCallback(requestId);
        return responseContainer.getResponse();
    }

    @Override
    public AuthResponse brokerDisconnect(AuthRequest authRequest) throws JMSException, InterruptedException, JsonProcessingException {
        client.checkAuthServiceConnection();
        String requestId = MessageHelper.getNewRequestId();
        authRequest.setRequestId(requestId);
        authRequest.setAction(SecurityAction.brokerDisconnect.name());
        ResponseContainer<AuthResponse> responseContainer = ResponseContainer.createAnRegisterNewMessageContainer(authRequest);
        logRequest(authRequest);
        client.sendMessage(MessageHelper.getBrokerDisconnectMessage(client.createTextMessage(), authRequest));
        synchronized (responseContainer) {
            responseContainer.wait(TIMEOUT);
        }
        MessageListener.removeCallback(requestId);
        return responseContainer.getResponse();
    }

    @Override
    public EntityResponse getEntity(EntityRequest entityRequest) throws JMSException, InterruptedException, JsonProcessingException {
        client.checkAuthServiceConnection();
        String requestId = MessageHelper.getNewRequestId();
        entityRequest.setRequestId(requestId);
        ResponseContainer<EntityResponse> responseContainer = ResponseContainer.createAnRegisterNewMessageContainer(entityRequest);
        logRequest(entityRequest);
        client.sendMessage(MessageHelper.getEntityMessage(client.createTextMessage(), entityRequest));
        synchronized (responseContainer) {
            responseContainer.wait(TIMEOUT);
        }
        MessageListener.removeCallback(requestId);
        return responseContainer.getResponse();
    }

    private void logRequest(Request request) {
        logger.info("Request id: {} - action: {} - requester: {}",
            request.getRequestId(), request.getAction(), request.getRequester());
    }
}
