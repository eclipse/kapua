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
package org.eclipse.kapua.client.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.qpid.jms.message.JmsTextMessage;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.client.security.ServiceClient.SecurityAction;
import org.eclipse.kapua.client.security.amqpclient.ClientMessageListener;
import org.eclipse.kapua.client.security.bean.AuthResponse;
import org.eclipse.kapua.client.security.bean.EntityResponse;
import org.eclipse.kapua.client.security.bean.MessageConstants;
import org.eclipse.kapua.client.security.bean.Response;
import org.eclipse.kapua.client.security.bean.ResponseContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.JMSException;
import jakarta.jms.Message;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class MessageListener extends ClientMessageListener {

    protected static Logger logger = LoggerFactory.getLogger(MessageListener.class);

    private final Map<String, ResponseContainer<?>> callbacks;//is not needed the synchronization
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectReader reader = mapper.reader();//check if it's thread safe

    private MetricsClientSecurity metrics;

    @Inject
    public MessageListener(MetricsClientSecurity metricsClientSecurity) {
        logger.debug("Starting MessageListener");
        this.metrics = metricsClientSecurity;
        callbacks = new ConcurrentHashMap<>();
    }

    @Override
    public void onMessage(Message message) {
        try {
            SecurityAction securityAction = SecurityAction.valueOf(message.getStringProperty(MessageConstants.HEADER_ACTION));
            switch (securityAction) {
                case brokerConnect:
                    updateResponseContainer(buildAuthResponseFromMessage((JmsTextMessage) message));
                    break;
                case brokerDisconnect:
                    updateResponseContainer(buildAuthResponseFromMessage((JmsTextMessage) message));
                    break;
                case getEntity:
                    updateResponseContainer(buildAccountResponseFromMessage((JmsTextMessage) message));
                    break;
                default:
                    throw new KapuaRuntimeException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "action");
            }
        } catch (JMSException | IOException e) {
            metrics.getLoginCallbackError().inc();
            logger.error("Error while processing Authentication/Authorization message: {}", e.getMessage(), e);
        }
    }

    private <R extends Response> void updateResponseContainer(R response) throws JMSException, IOException {
        logger.debug("update callback {} on instance {}, map size: {}", response.getRequestId(), this, callbacks.size());
        ResponseContainer<R> responseContainer = (ResponseContainer<R>) callbacks.get(response.getRequestId());
        if (responseContainer == null) {
            //internal error
            logger.error("Cannot find request container for requestId {}", response.getRequestId());
            metrics.getLoginCallbackTimeout().inc();
        } else {
            synchronized (responseContainer) {
                responseContainer.setResponse(response);
                responseContainer.notify();
            }
        }
    }

    private AuthResponse buildAuthResponseFromMessage(JmsTextMessage message) throws JMSException, IOException {
        String body = message.getBody(String.class);
        return reader.readValue(body, AuthResponse.class);
    }

    private EntityResponse buildAccountResponseFromMessage(JmsTextMessage message) throws JMSException, IOException {
        String body = message.getBody(String.class);
        return reader.readValue(body, EntityResponse.class);
    }

    public void registerCallback(String requestId, ResponseContainer<?> responseContainer) {
        callbacks.put(requestId, responseContainer);
        logger.debug("registered callback {} on instance {}, map size: {}", requestId, this, callbacks.size());
    }

    public void removeCallback(String requestId) {
        callbacks.remove(requestId);
        logger.debug("removed callback {} from instance {}, map size: {}", requestId, this, callbacks.size());
    }

}
