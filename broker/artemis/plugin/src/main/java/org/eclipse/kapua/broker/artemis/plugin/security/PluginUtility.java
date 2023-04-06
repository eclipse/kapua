/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.artemis.plugin.security;

import org.apache.activemq.artemis.core.protocol.mqtt.MQTTConnection;
import org.apache.activemq.artemis.core.server.ServerConsumer;
import org.apache.activemq.artemis.core.server.ServerSession;
import org.apache.activemq.artemis.protocol.amqp.broker.ActiveMQProtonRemotingConnection;
import org.apache.activemq.artemis.spi.core.protocol.RemotingConnection;
import org.apache.activemq.artemis.spi.core.remoting.Connection;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSetting;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginUtility {

    protected static Logger logger = LoggerFactory.getLogger(SecurityPlugin.class);

    private static String amqpInternalConectorPort;
    private static String amqpInternalConnectorName;
    private static String mqttInternalConectorPort;
    private static String mqttInternalConnectorName;

    static {
        BrokerSetting brokerSetting = BrokerSetting.getInstance();
        amqpInternalConectorPort = ":" + brokerSetting.getString(BrokerSettingKey.INTERNAL_AMQP_ACCEPTOR_PORT);
        amqpInternalConnectorName = brokerSetting.getString(BrokerSettingKey.INTERNAL_AMQP_ACCEPTOR_NAME);
        mqttInternalConectorPort = ":" + brokerSetting.getString(BrokerSettingKey.INTERNAL_MQTT_ACCEPTOR_PORT);
        mqttInternalConnectorName = brokerSetting.getString(BrokerSettingKey.INTERNAL_MQTT_ACCEPTOR_NAME);
    }

    private PluginUtility() {
    }

    public static String getClientId(RemotingConnection remotingConnection) {
        return remotingConnection.getClientID();
    }

    public static String getConnectionId(RemotingConnection remotingConnection) {
        return remotingConnection.getID().toString();
    }

    public static String getConnectionId(ServerConsumer consumer) {
        return consumer.getConnectionID().toString();
    }

    public static String getConnectionId(ServerSession session) {
        return getConnectionId(session.getRemotingConnection());
    }

    public static boolean isInternal(RemotingConnection remotingConnection) {
        String protocolName = remotingConnection.getProtocolName();
        if (remotingConnection instanceof ActiveMQProtonRemotingConnection) {
//            AMQPConnectionContext connectionContext = ((ActiveMQProtonRemotingConnection)remotingConnection).getAmqpConnection();
            Connection connection = ((ActiveMQProtonRemotingConnection)remotingConnection).getAmqpConnection().getConnectionCallback().getTransportConnection();
            if (logger.isDebugEnabled()) {
                logger.debug("Protocol: {} - Remote container: {} - connection id: {} - local address: {}",
                    protocolName, ((ActiveMQProtonRemotingConnection)remotingConnection).getAmqpConnection().getRemoteContainer(), connection.getID(), connection.getLocalAddress());
            }
            return isAmqpInternal(connection.getLocalAddress(), protocolName);//and connector name as expected
        }
        else if(remotingConnection instanceof MQTTConnection) {
            Connection connection = ((MQTTConnection)remotingConnection).getTransportConnection();
            if (logger.isDebugEnabled()) {
                logger.debug("Protocol: {} - Remote address: {} - connection id: {} - local address: {}",
                    protocolName, connection.getRemoteAddress(), connection.getID(), connection.getLocalAddress());
            }
            return isMqttInternal(connection.getLocalAddress(), protocolName);//and connector name as expected
        }
        else {
            return false;
        }
    }

    protected static boolean isAmqpInternal(String localAddress, String protocolName) {
        //is internal if the inbound connection is coming from the amqp connector
        //are the first check redundant? If the connector name is what is expected should be enough?
        return
            (localAddress.endsWith(amqpInternalConectorPort) && //local port amqp
                amqpInternalConnectorName.equalsIgnoreCase(protocolName));
    }

    protected static boolean isMqttInternal(String localAddress, String protocolName) {
        //is internal if the inbound connection is coming from the mqtt internal connector
        //are the first check redundant? If the connector name is what is expected should be enough?
        return
            (localAddress.endsWith(mqttInternalConectorPort) && //local port internal mqtt
                mqttInternalConnectorName.equalsIgnoreCase(protocolName));
    }
}
