/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.broker.core.pool;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide connection factory for embedded broker connection.
 *
 * @since 1.0
 */
public class JmsConnectionFactory {

    private static Logger s_logger = LoggerFactory.getLogger(JmsConnectionFactory.class);

    /**
     * ActiveMQ vm connection factory instance
     */
    public static ActiveMQConnectionFactory vmConnFactory;

    // the workers used the same string connection without asynch=true
    static {
        s_logger.info("Instantiate amq embedded connection factory...");
        String connectionFactoryUri = null;

        // By default, the embedded broker operates in asynchronous mode, so that calls to a send method return immediately (in other words, messages are dispatched to consumers in a separate
        // thread). If you turn off asynchronous mode, however, you can reduce the amount of context switching. For example, you can disable asynchronous mode on a VM endpoint as follows:
        // https://access.redhat.com/documentation/en-US/Fuse_ESB_Enterprise/7.1/html/ActiveMQ_Tuning_Guide/files/GenTuning-Colocate.html
        // TODO parameter to be added to configuration
        // connectionFactoryUri = "vm://" + KapuaEnvironmentConfig.getInstance().getString(KapuaEnvironmentConfigKeys.BROKER_NAME) + "?create=false&waitForStart=3600000&async=true";
        connectionFactoryUri = "vm://kapua?create=false&waitForStart=3600000&async=true";
        s_logger.info("Using connection factory url: " + connectionFactoryUri);

        // connection factory
        vmConnFactory = new ActiveMQConnectionFactory(connectionFactoryUri);

        // In this case, it is possible to optimize away the session threading layer and the MessageConsumer threads can then pull messages directly from the transport layer.
        // https://access.redhat.com/documentation/en-US/Fuse_ESB_Enterprise/7.1/html/ActiveMQ_Tuning_Guide/files/GenTuning-Consumer-ContextSwitch.html
        vmConnFactory.setAlwaysSessionAsync(false);

        // When true a MessageProducer will always use Sync sends when sending a Message even if it is not required for the Delivery Mode
        // http://activemq.apache.org/connection-configuration-uri.html
        vmConnFactory.setAlwaysSyncSend(false);

        // Should a JMS message be copied to a new JMS Message object as part of the send() method in JMS. This is enabled by default to be compliant with the JMS specification. You can disable it
        // if you do not mutate JMS messages after they are sent for a performance boost.
        // http://activemq.apache.org/connection-configuration-uri.html
        vmConnFactory.setCopyMessageOnSend(false);

        // If you are sure that your consumers are always fast, however, you could achieve better performance by disabling asynchronous dispatch on the broker (thereby avoiding the cost of
        // unnecessary context switching).
        // https://access.redhat.com/documentation/en-US/Fuse_ESB_Enterprise/7.1/html/ActiveMQ_Tuning_Guide/files/GenTuning-Consumer-ContextSwitch.html
        vmConnFactory.setDispatchAsync(false);

        // Enables an optimised acknowledgement mode where messages are acknowledged in batches rather than individually. Alternatively, you could use Session.DUPS_OK_ACKNOWLEDGE acknowledgement
        // mode for the consumers which can often be faster. WARNING enabling this issue could cause some issues with auto-acknowledgement on reconnection
        // http://activemq.apache.org/connection-configuration-uri.html
        vmConnFactory.setOptimizeAcknowledge(false);

        // If this flag is set then an larger prefetch limit is used - only applicable for durable topic subscribers
        // http://activemq.apache.org/connection-configuration-uri.html
        vmConnFactory.setOptimizedMessageDispatch(true);

        // Forces the use of Async Sends which adds a massive performance boost; but means that the send() method will return immediately whether the message has been sent or not which could lead
        // to message loss.
        // http://activemq.apache.org/connection-configuration-uri.html
        vmConnFactory.setUseAsyncSend(true);

        s_logger.info("Instantiate activemq embedded connection factory... done");
    }

}
