/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.event.jms;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.qpid.jms.jndi.JmsInitialContextFactory;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.event.ServiceEventBusDriver;
import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.commons.event.ServiceEventMarshaler;
import org.eclipse.kapua.commons.event.ServiceEventScope;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.event.ServiceEventBusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.event.ServiceEventBusDriver;
import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.commons.event.ServiceEventMarshaler;
import org.eclipse.kapua.commons.event.ServiceEventScope;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.event.ServiceEventBusListener;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.qpid.jms.jndi.JmsInitialContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JMS event bus implementation
 *
 * @since 1.0
 */
public class JMSServiceEventBus implements ServiceEventBus, ServiceEventBusDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMSServiceEventBus.class);

    private static final int PRODUCER_POOL_MIN_SIZE = SystemSetting.getInstance().getInt(SystemSettingKey.EVENT_BUS_PRODUCER_POOL_MIN_SIZE);
    private static final int PRODUCER_POOL_MAX_SIZE = SystemSetting.getInstance().getInt(SystemSettingKey.EVENT_BUS_PRODUCER_POOL_MAX_SIZE);
    private static final int PRODUCER_POOL_BORROW_WAIT = SystemSetting.getInstance().getInt(SystemSettingKey.EVENT_BUS_PRODUCER_POOL_BORROW_WAIT_MAX);
    private static final int PRODUCER_POOL_EVICTION_INTERVAL = SystemSetting.getInstance().getInt(SystemSettingKey.EVENT_BUS_PRODUCER_EVICTION_INTERVAL);
    private static final int CONSUMER_POOL_SIZE = SystemSetting.getInstance().getInt(SystemSettingKey.EVENT_BUS_CONSUMER_POOL_SIZE);
    private static final String MESSAGE_SERIALIZER = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_MESSAGE_SERIALIZER);
    private static final String TRANSPORT_USE_EPOLL = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_TRANSPORT_USE_EPOLL);

    private List<Subscription> subscriptionList = new ArrayList<>();
    private EventBusJMSConnectionBridge eventBusJMSConnectionBridge;
    private ServiceEventMarshaler eventBusMarshaler;

    private Counter reconnectionRetryCount;
    private Counter connectionErrorCount;

    /**
     * Default constructor
     *
     * @throws JMSException
     * @throws NamingException
     */
    public JMSServiceEventBus() throws JMSException, NamingException {
        reconnectionRetryCount = MetricServiceFactory.getInstance().getCounter("event_bus", "handler", "reconnection_retry", "count");
        connectionErrorCount = MetricServiceFactory.getInstance().getCounter("event_bus", "handler", "connection_error", "count");
        eventBusJMSConnectionBridge = new EventBusJMSConnectionBridge();
    }

    @Override
    public String getType() {
        return ServiceEventBusManager.JMS_20_EVENT_BUS;
    }

    /**
     * Start the event bus
     *
     * @throws ServiceEventBusException
     */
    @Override
    public void start() throws ServiceEventBusException {
        try {
            // initialize event bus marshaler
            Class<?> messageSerializerClazz = Class.forName(MESSAGE_SERIALIZER);
            if (ServiceEventMarshaler.class.isAssignableFrom(messageSerializerClazz)) {
                eventBusMarshaler = (ServiceEventMarshaler) messageSerializerClazz.newInstance();
            } else {
                throw new ServiceEventBusException(String.format("Wrong message serializer Object type ('%s')!", messageSerializerClazz));
            }
            eventBusJMSConnectionBridge.start();
        } catch (JMSException | ClassNotFoundException | NamingException | InstantiationException | IllegalAccessException e) {
            throw new ServiceEventBusException(e);
        }
    }

    @Override
    public void publish(String address, ServiceEvent kapuaEvent)
            throws ServiceEventBusException {
        eventBusJMSConnectionBridge.publish(address, kapuaEvent);
    }

    @Override
    public synchronized void subscribe(String address, String name, final ServiceEventBusListener kapuaEventListener)
            throws ServiceEventBusException {
        try {
            Subscription subscription = new Subscription(address, name, kapuaEventListener);
            subscriptionList.add(subscription);
            eventBusJMSConnectionBridge.subscribe(subscription);
        } catch (ServiceEventBusException e) {
            throw new ServiceEventBusException(e);
        }
    }

    private void setSession(ServiceEvent kapuaEvent) {
        KapuaSession.createFrom(kapuaEvent.getScopeId(), kapuaEvent.getUserId());
    }

    /**
     * Stop the event bus
     *
     * @throws ServiceEventBusException
     */
    @Override
    public void stop() throws ServiceEventBusException {
        eventBusJMSConnectionBridge.stop();
    }

    @Override
    public ServiceEventBus getEventBus() {
        return this;
    }

    private void waitBeforeRetry() {
        // wait a bit
        try {
            Thread.sleep(2000);// TODO move to configuration
        } catch (InterruptedException e) {
            LOGGER.error("Wait for connect interrupted!", e);
        }
    }

    private synchronized void restart() throws ServiceEventBusException, JMSException {
        // restart the event bus connection bridge with a new instance
        // so no synchronization is needed
        EventBusJMSConnectionBridge instanceToCleanUp = null;
        EventBusJMSConnectionBridge newInstance = null;
        try {
            newInstance = new EventBusJMSConnectionBridge();
            newInstance.start();
            // restore subscriptions
            for (Subscription subscription : subscriptionList) {
                newInstance.subscribe(subscription);
            }
            instanceToCleanUp = eventBusJMSConnectionBridge;
            eventBusJMSConnectionBridge = newInstance;
        } catch (Exception e) {
            LOGGER.warn("Error while creating new Service Event Bus instance: {}", e.getMessage());
            //try to cleanup the messy instance
            if (newInstance!=null) {
                try {
                    LOGGER.warn("Stopping new Service Event Bus instance...");
                    newInstance.stop();
                    LOGGER.warn("Stopping new Service Event Bus instance... DONE");
                }
                catch(Exception e1) {
                    //don't throw this exception since the real exception is the first one
                    LOGGER.warn("Stopping new Service Event Bus instance error: {}", e1.getMessage(), e1);
                }
            }
            throw new ServiceEventBusException(e);
        } finally {
            try {
                if (instanceToCleanUp != null) {
                    LOGGER.info("Stopping old Service Event Bus instance...");
                    instanceToCleanUp.stop();
                } else {
                    LOGGER.warn("Stopping old Service Event Bus instance. Null instance found so nothig to do...");
                }
            } catch (ServiceEventBusException e) {
                LOGGER.error("Stopping old Service Event Bus instance. Cannot destroy instance: {}", e.getMessage(), e);
            } finally {
                instanceToCleanUp = null;
            }
        }
    }

    private class EventBusJMSConnectionBridge {

        private Connection jmsConnection;
        private Map<String, SenderPool> senders = new HashMap<>();
        private ExceptionListenerImpl exceptionListener;

        public EventBusJMSConnectionBridge() {
            this.exceptionListener = new ExceptionListenerImpl();
        }

        void start() throws JMSException, NamingException, ServiceEventBusException {
            stop();
            String eventbusUrl = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_URL);
            String eventbusUsername = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_USERNAME);
            String eventbusPassword = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_PASSWORD);

            Hashtable<String, String> environment = new Hashtable<>();
            environment.put("connectionfactory.eventBusUrl", eventbusUrl);
            environment.put("transport.useEpoll", TRANSPORT_USE_EPOLL);

            JmsInitialContextFactory initialContextFactory = new JmsInitialContextFactory();
            Context context = initialContextFactory.getInitialContext(environment);
            ConnectionFactory jmsConnectionFactory = (ConnectionFactory) context.lookup("eventBusUrl");

            jmsConnection = jmsConnectionFactory.createConnection(eventbusUsername, eventbusPassword);
            jmsConnection.setExceptionListener(exceptionListener);
            jmsConnection.start();
        }

        void stop() throws ServiceEventBusException {
            try {
                if (jmsConnection != null) {
                    exceptionListener.stop();
                    jmsConnection.setExceptionListener(null);
                    jmsConnection.close();
                }
            } catch (JMSException e) {
                throw new ServiceEventBusException(e);
            } finally {
                jmsConnection = null;
            }

            // iterate over all possibles entries
            Iterator<String> senderIterator = senders.keySet().iterator();
            while (senderIterator.hasNext()) {
                SenderPool senderPool = senders.get(senderIterator.next());
                senderPool.close();
                senderPool.clear();
                // borrowed object will be returned to the pool soon (since the connection is gone bad) and then destroyed by the pool (since the pool is stopped)
                senderIterator.remove();
            }
        }

        void publish(String address, ServiceEvent kapuaEvent)
                throws ServiceEventBusException {
            if (address != null && address.trim().length() > 0) {
                SenderPool senderPool = senders.get(address);
                Sender sender = null;
                try {
                    if (senderPool == null) {
                        synchronized (SenderPool.class) {
                            senderPool = senders.get(address);
                            if (senderPool == null) {
                                senderPool = new SenderPool(new PooledSenderFactory(address));
                                senders.put(address, senderPool);
                            }
                        }
                    }
                    sender = senderPool.borrowObject();
                    sender.sendMessage(kapuaEvent);
                } catch (Exception e) {
                    throw new ServiceEventBusException(e);
                } finally {
                    if (sender != null) {
                        senderPool.returnObject(sender);
                    }
                }
            } else {
                LOGGER.warn("Discarded event publish since the publish address is empty!");
            }
        }

        synchronized void subscribe(Subscription subscription)
                throws ServiceEventBusException {
            try {
                String subscriptionStr = String.format("events.%s", subscription.getAddress());
                // create a bunch of sessions to allow parallel event processing
                LOGGER.info("Subscribing to address {} - name {} ...", subscriptionStr, subscription.getName());
                for (int i = 0; i < CONSUMER_POOL_SIZE; i++) {
                    final Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Topic jmsTopic = jmsSession.createTopic(subscriptionStr);
                    MessageConsumer jmsConsumer = jmsSession.createSharedDurableConsumer(jmsTopic, subscription.getName());
                    jmsConsumer.setMessageListener(message -> {
                        try {
                            if (message instanceof TextMessage) {
                                TextMessage textMessage = (TextMessage) message;
                                final ServiceEvent kapuaEvent = eventBusMarshaler.unmarshal(textMessage.getText());
                                setSession(kapuaEvent);
                                KapuaSecurityUtils.doPrivileged(() -> {
                                    try {
                                        // restore event context
                                        ServiceEventScope.set(kapuaEvent);
                                        subscription.getKapuaEventListener().onKapuaEvent(kapuaEvent);
                                    } finally {
                                        ServiceEventScope.end();
                                    }
                                });

                            } else {
                                LOGGER.error("Discarding wrong event message type '{}'", message != null ? message.getClass() : "null");
                            }
                        } catch (Throwable t) {
                            LOGGER.error(t.getMessage(), t);
                            // throwing the exception to prevent the message acknowledging (https://docs.oracle.com/javaee/7/api/javax/jms/Session.html#AUTO_ACKNOWLEDGE)
                            throw KapuaRuntimeException.internalError(t);
                        }
                    });
                }
                LOGGER.info("Subscribing to address {} - name {} - pool size {} ...DONE", subscriptionStr, subscription.getName(), CONSUMER_POOL_SIZE);
            } catch (JMSException e) {
                throw new ServiceEventBusException(e);
            }
        }

        private class Sender {

            // TODO manage the session/producer in a stronger way (if the client disconnects due to a network error the connection will not be restored)
            private Session jmsSession;
            private MessageProducer jmsProducer;

            public Sender(Connection jmsConnection, String address) throws JMSException {
                address = String.format("events.%s", address);
                jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic jmsTopic = jmsSession.createTopic(address);
                jmsProducer = jmsSession.createProducer(jmsTopic);
            }

            public void sendMessage(ServiceEvent kapuaEvent) throws Exception {
                try {
                    TextMessage message = jmsSession.createTextMessage();
                    // Serialize outgoing kapua event based on platform configuration
                    message.setText(eventBusMarshaler.marshal(kapuaEvent));
                    message.setStringProperty(ServiceEventMarshaler.CONTENT_TYPE_KEY, eventBusMarshaler.getContentType());
                    jmsProducer.send(message);
                } catch (JMSException | KapuaException e) {
                    LOGGER.error("Message publish interrupted: {}", e.getMessage());
                    throw e;
                }
            }

            public void close() {
                try {
                    jmsSession.close();
                } catch (JMSException e) {
                    LOGGER.warn("Cannot close the Sender connection!", e);
                }
            }

        }

        private class PooledSenderFactory extends BasePooledObjectFactory<Sender> {

            private String address;

            public PooledSenderFactory(String address) {
                this.address = address;
            }

            @Override
            public Sender create()
                    throws Exception {
                try {
                    return new Sender(jmsConnection, address);
                } catch (JMSException e) {
                    throw new ServiceEventBusException(e);
                }
            }

            @Override
            public PooledObject<Sender> wrap(Sender sender) {
                return new DefaultPooledObject<>(sender);
            }

            @Override
            public void destroyObject(PooledObject<Sender> pooledSender)
                    throws Exception {
                Sender sender = pooledSender.getObject();
                if (sender != null) {
                    sender.close();
                }
                super.destroyObject(pooledSender);
            }

        }

        private class SenderPool extends GenericObjectPool<Sender> {

            public SenderPool(PooledSenderFactory factory) {
                super(factory);

                GenericObjectPoolConfig senderPoolConfig = new GenericObjectPoolConfig();
                senderPoolConfig.setMinIdle(PRODUCER_POOL_MIN_SIZE);
                senderPoolConfig.setMaxIdle(PRODUCER_POOL_MAX_SIZE);
                senderPoolConfig.setMaxTotal(PRODUCER_POOL_MAX_SIZE);
                senderPoolConfig.setMaxWaitMillis(PRODUCER_POOL_BORROW_WAIT);
                senderPoolConfig.setTestOnReturn(true);
                senderPoolConfig.setTestOnBorrow(true);
                senderPoolConfig.setTestWhileIdle(false);
                senderPoolConfig.setBlockWhenExhausted(true);
                senderPoolConfig.setTimeBetweenEvictionRunsMillis(PRODUCER_POOL_EVICTION_INTERVAL);
                setConfig(senderPoolConfig);
            }

        }

        private class ExceptionListenerImpl implements ExceptionListener {

            private boolean active = true;

            @Override
            public void onException(JMSException e) {
                LOGGER.error("EventBus Listener {} -  Connection thrown exception: {}", this, e.getMessage(), e);
                connectionErrorCount.inc();
                int i = 1;
                while (active) {
                    LOGGER.info("EventBus Listener {} - restarting attempt... {}", this, i);
                    try {
                        reconnectionRetryCount.inc();
                        restart();
                        LOGGER.info("EventBus Listener {} - EventBus restarting attempt... {} DONE (Connection restored)", this, i);
                        break;
                    } catch (ServiceEventBusException | JMSException e1) {
                        LOGGER.error("EventBus Listener {} - Cannot start new event bus connection... try again...", this, e1);
                        waitBeforeRetry();
                    }
                    i++;
                }
            }

            public void stop() {
                active = false;
            }
        }
    }

    private class Subscription {

        String name;
        String address;
        ServiceEventBusListener kapuaEventListener;

        public Subscription(String address, String name, ServiceEventBusListener kapuaEventListener) {
            this.name = name;
            this.address = address;
            this.kapuaEventListener = kapuaEventListener;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public ServiceEventBusListener getKapuaEventListener() {
            return kapuaEventListener;
        }

    }

}
