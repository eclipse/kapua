/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event.bus.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventBusException;
import org.eclipse.kapua.service.event.KapuaEventBusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @since 0.3.0
 */
public class JMSEventBus implements KapuaEventBus {

    private final static Logger LOGGER = LoggerFactory.getLogger(JMSEventBus.class);

    private Connection jmsConnection;
    private Session jmsSession;
    private Map<String, MessageProducer> jmsProducers;
    private Map<String, KapuaEventBusListener> kapuaListeners;

    public JMSEventBus() throws KapuaEventBusException {
        jmsProducers = new HashMap<>();
        kapuaListeners = new HashMap<>();
    }

    public void start() throws KapuaEventBusException {
        try {

            String eventbusUrl = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_URL);
            String eventbusUsername = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_USERNAME);
            String eventbusPassword = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_PASSWORD);

            // TODO Make connection pluggable and provide Qpid JMS library
            ConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory(eventbusUrl);
            jmsConnection = jmsConnectionFactory.createConnection(eventbusUsername, eventbusPassword);
            jmsConnection.start();

            jmsSession = jmsConnection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new KapuaEventBusException(e);
        }
    }

    @Override
    public synchronized void publish(String address, KapuaEvent kapuaEvent) {
        address = String.format("events.%s", address);
        try {
            MessageProducer jmsProducer = jmsProducers.get(address);
            if (jmsProducer == null) {
                Queue jmsQueue = jmsSession.createQueue(address);
                jmsProducer = jmsSession.createProducer(jmsQueue);
                jmsProducers.put(address, jmsProducer);
            }

            // TODO Serialize outgoing kapua event ? Binary/JSON ?
            TextMessage message = jmsSession.createTextMessage();
            message.setText(XmlUtil.marshal(kapuaEvent));
            jmsProducer.send(message);
            message.acknowledge();

        } catch (JMSException | JAXBException e) {
            LOGGER.error("Message publish interrupted: {}", e.getMessage());
        }
    }

    @Override
    public synchronized void subscribe(String address, final KapuaEventBusListener kapuaEventListener)
            throws KapuaEventBusException {
        try {
            address = String.format("events.%s", address);
            if (kapuaListeners.get(address) == null) {
                Queue jmsQueue = jmsSession.createQueue(address);
                MessageConsumer jmsConsumer = jmsSession.createConsumer(jmsQueue);
                jmsConsumer.setMessageListener(new MessageListener() {

                    @Override
                    public void onMessage(Message message) {
                        try {
                            if (message instanceof TextMessage) {
                                TextMessage textMessage = (TextMessage) message;
                                final KapuaEvent kapuaEvent = XmlUtil.unmarshal(textMessage.getText(), KapuaEvent.class);
                                if (kapuaEvent == null) {
                                    // TODO Manage error
                                }
                                setSession(kapuaEvent);
                                if (kapuaEvent != null) {
                                    KapuaSecurityUtils.doPrivileged(() -> {
                                        kapuaEventListener.onKapuaEvent(kapuaEvent);
                                    });
                                }
                            }
                            else {
                                // TODO Manage error
                            }
                        } catch (KapuaException | JMSException | JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
                            LOGGER.error(e.getMessage(), e);
                        } finally {
                            try {
                                message.acknowledge();
                            } catch (JMSException e) {
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }
                });

                kapuaListeners.put(address, kapuaEventListener);
            } else {
                // TODO check that this is ok
            }
        } catch (JMSException e) {
            throw new KapuaEventBusException(e);
        }
    }

    private final void setSession(KapuaEvent kapuaEvent) {
        KapuaSession.createFrom(kapuaEvent.getScopeId(), kapuaEvent.getUserId());
    }

    public void stop() throws KapuaEventbusException {
        try {
            if (jmsSession != null) {
                jmsSession.close();
            }
            if (jmsConnection != null) {
                jmsConnection.close();
            }
        } catch (JMSException e) {
            throw new KapuaEventBusException(e);
        }
    }
}
