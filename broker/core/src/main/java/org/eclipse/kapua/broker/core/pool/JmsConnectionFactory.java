/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.pool;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide connection factory for embedded broker connection.
 *
 * @since 1.0
 */
public class JmsConnectionFactory {

    private JmsConnectionFactory() {
    }

    private static final Logger logger = LoggerFactory.getLogger(JmsConnectionFactory.class);

    /**
     * ActiveMQ vm connection factory instance
     */
    public static final ActiveMQConnectionFactory VM_CONN_FACTORY;

    // the workers used the same string connection without asynch=true
    static {
        logger.info("Instantiate amq embedded connection factory...");

        // By default, the embedded broker operates in asynchronous mode, so that calls to a send method return immediately (in other words, messages are dispatched to consumers in a separate
        // thread). If you turn off asynchronous mode, however, you can reduce the amount of context switching. For example, you can disable asynchronous mode on a VM endpoint as follows:
        // https://access.redhat.com/documentation/en-US/Fuse_ESB_Enterprise/7.1/html/ActiveMQ_Tuning_Guide/files/GenTuning-Colocate.html
        // TODO parameter to be added to configuration
        // connectionFactoryUri = "vm://" + KapuaEnvironmentConfig.getInstance().getString(KapuaEnvironmentConfigKeys.BROKER_NAME) + "?create=false&waitForStart=3600000&async=true";
        String connectionFactoryUri = String.format("vm://%s?create=false&waitForStart=3600000&async=true", BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_NAME));
        logger.info("Using connection factory url: " + connectionFactoryUri);

        // connection factory
        VM_CONN_FACTORY = new ActiveMQConnectionFactory(connectionFactoryUri);

        // In this case, it is possible to optimize away the session threading layer and the MessageConsumer threads can then pull messages directly from the transport layer.
        // https://access.redhat.com/documentation/en-US/Fuse_ESB_Enterprise/7.1/html/ActiveMQ_Tuning_Guide/files/GenTuning-Consumer-ContextSwitch.html
        VM_CONN_FACTORY.setAlwaysSessionAsync(false);

        // When true a MessageProducer will always use Sync sends when sending a Message even if it is not required for the Delivery Mode
        // http://activemq.apache.org/connection-configuration-uri.html
        VM_CONN_FACTORY.setAlwaysSyncSend(false);

        // Should a JMS message be copied to a new JMS Message object as part of the send() method in JMS. This is enabled by default to be compliant with the JMS specification. You can disable it
        // if you do not mutate JMS messages after they are sent for a performance boost.
        // http://activemq.apache.org/connection-configuration-uri.html
        VM_CONN_FACTORY.setCopyMessageOnSend(false);

        // If you are sure that your consumers are always fast, however, you could achieve better performance by disabling asynchronous dispatch on the broker (thereby avoiding the cost of
        // unnecessary context switching).
        // https://access.redhat.com/documentation/en-US/Fuse_ESB_Enterprise/7.1/html/ActiveMQ_Tuning_Guide/files/GenTuning-Consumer-ContextSwitch.html
        VM_CONN_FACTORY.setDispatchAsync(false);

        // Enables an optimised acknowledgement mode where messages are acknowledged in batches rather than individually. Alternatively, you could use Session.DUPS_OK_ACKNOWLEDGE acknowledgement
        // mode for the consumers which can often be faster. WARNING enabling this issue could cause some issues with auto-acknowledgement on reconnection
        // http://activemq.apache.org/connection-configuration-uri.html
        VM_CONN_FACTORY.setOptimizeAcknowledge(false);

        // If this flag is set then an larger prefetch limit is used - only applicable for durable topic subscribers
        // http://activemq.apache.org/connection-configuration-uri.html
        VM_CONN_FACTORY.setOptimizedMessageDispatch(true);

        // Forces the use of Async Sends which adds a massive performance boost; but means that the send() method will return immediately whether the message has been sent or not which could lead
        // to message loss.
        // http://activemq.apache.org/connection-configuration-uri.html
        VM_CONN_FACTORY.setUseAsyncSend(true);

        logger.info("Instantiate activemq embedded connection factory... done");
    }

}
