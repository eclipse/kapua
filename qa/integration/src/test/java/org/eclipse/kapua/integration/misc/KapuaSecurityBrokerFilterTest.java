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
package org.eclipse.kapua.integration.misc;

import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.Message;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.Connection;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.security.SecurityContext;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class KapuaSecurityBrokerFilterTest {

    Broker broker;
    BrokerId brokerId;
    ConnectionContext connectionContext;
    ConnectionInfo connectionInfo;
    Connection connection;
    TransportConnector connector;
    ConsumerInfo consumerInfo;
    KapuaSecurityBrokerFilter kapuaSecurityBrokerFilter;
    KapuaSecurityBrokerFilter nullKapuaSecurityBrokerFilter;

    @Before
    public void initialize() {
        broker = Mockito.mock(Broker.class);
        brokerId = new BrokerId();
        connectionContext = new ConnectionContext();
        connectionInfo = new ConnectionInfo();
        connection = Mockito.mock(Connection.class);
        connector = Mockito.mock(TransportConnector.class);
        consumerInfo = Mockito.mock(ConsumerInfo.class);
        kapuaSecurityBrokerFilter = new KapuaSecurityBrokerFilter(broker);
        nullKapuaSecurityBrokerFilter = new KapuaSecurityBrokerFilter(null);
    }

    @Test
    public void startTest() {
        try {
            Mockito.when(broker.getBrokerId()).thenReturn(brokerId);
            kapuaSecurityBrokerFilter.start();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test(expected = NullPointerException.class)
    public void startNullBrokerTest() throws Exception {
        nullKapuaSecurityBrokerFilter.start();
    }

    @Test
    public void stopTest() {
        try {
            kapuaSecurityBrokerFilter.stop();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test(expected = NullPointerException.class)
    public void stopNullBrokerTest() throws Exception {
        nullKapuaSecurityBrokerFilter.stop();
    }

    @Test
    public void addConnectionWithoutConnectorTest() {
        connectionInfo.setUserName("kapua-sys");
        connectionInfo.setPassword("kapua-password");
        System.setProperty("broker.connector.internal.username", "kapua-sys");
        System.setProperty("broker.connector.internal.password", "kapua-password");

        try {
            kapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }


    @Test
    public void addConnectionWithConnectorAndTrueConnectionTest() {
        connectionContext.setConnector(connector);
        connectionContext.setConnection(connection);

        Mockito.when(connection.isNetworkConnection()).thenReturn(true);
        try {
            kapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }

        try {
            nullKapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test
    public void addConnectionWithConnectorNameVmTest() {
        ConnectionContext connectionContext = Mockito.mock(ConnectionContext.class);

        Mockito.when(connectionContext.getConnector()).thenReturn(connector);
        Mockito.when((connector).getName()).thenReturn("vm://message-broker");
        Mockito.when(connectionContext.getSecurityContext()).thenReturn(Mockito.mock(SecurityContext.class));

        try {
            kapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test
    public void addConnectionWithConnectorNameInternalTest() {
        ConnectionContext connectionContext = Mockito.mock(ConnectionContext.class);
        connectionInfo.setUserName("kapua-sys");
        connectionInfo.setPassword("kapua-password");
        System.setProperty("broker.connector.internal.username", "kapua-sys");
        System.setProperty("broker.connector.internal.password", "kapua-password");

        Mockito.when(connectionContext.getConnector()).thenReturn(connector);
        Mockito.when((connector).getName()).thenReturn("internalMqtt");
        try {
            kapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test
    public void addConnectionNullInfoTest() {
        connectionContext.setConnector(connector);
        connectionContext.setConnection(connection);

        Mockito.when(connection.isNetworkConnection()).thenReturn(true);

        try {
            kapuaSecurityBrokerFilter.addConnection(connectionContext, null);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.addConnection(connectionContext, null);
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test(expected = SecurityException.class)
    public void addConnectionDifferentUsernamesTest() throws Exception {
        connectionInfo.setUserName("username");
        connectionInfo.setPassword("kapua-password");
        System.setProperty("broker.connector.internal.username", "kapua-sys");
        System.setProperty("broker.connector.internal.password", "kapua-password");

        kapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
    }

    @Test(expected = SecurityException.class)
    public void addConnectionDifferentPasswordsTest() throws Exception {
        ConnectionContext connectionContext = new ConnectionContext();
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setUserName("kapua-sys");
        connectionInfo.setPassword("password");
        System.setProperty("broker.connector.internal.username", "kapua-sys");
        System.setProperty("broker.connector.internal.password", "kapua-password");

        kapuaSecurityBrokerFilter.addConnection(connectionContext, connectionInfo);
    }

    @Test
    public void removeConnectionTest() {
        try {
            kapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }

        try {
            nullKapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test
    public void removeConnectionWithNetworkConnectionTest() {
        ConnectionContext connectionContext = Mockito.mock(ConnectionContext.class);
        Connection connection = Mockito.mock(Connection.class);

        Mockito.when(connectionContext.getConnector()).thenReturn(connector);
        Mockito.when((connector).getName()).thenReturn("Connector name");
        Mockito.when(connectionContext.getConnection()).thenReturn(connection);
        Mockito.doReturn(true).when(connection).isNetworkConnection();
        Mockito.when(connectionContext.getSecurityContext()).thenReturn(Mockito.mock(SecurityContext.class));

        try {
            kapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test
    public void removeConnectionConnectorNameVmTest() {
        ConnectionContext connectionContext = Mockito.mock(ConnectionContext.class);

        Mockito.when(connectionContext.getConnector()).thenReturn(connector);
        Mockito.when((connector).getName()).thenReturn("vm://message-broker");
        Mockito.when(connectionContext.getConnection()).thenReturn(connection);
        Mockito.doReturn(true).when(connection).isNetworkConnection();
        Mockito.when(connectionContext.getSecurityContext()).thenReturn(Mockito.mock(SecurityContext.class));

        try {
            kapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test
    public void removeConnectorConnectionNameInternalTest() {
        ConnectionContext connectionContext = Mockito.mock(ConnectionContext.class);

        Mockito.when(connectionContext.getConnector()).thenReturn(connector);
        Mockito.when((connector).getName()).thenReturn("internalMqtt");
        Mockito.when(connectionContext.getConnection()).thenReturn(connection);
        Mockito.doReturn(true).when(connection).isNetworkConnection();
        Mockito.when(connectionContext.getSecurityContext()).thenReturn(Mockito.mock(SecurityContext.class));

        try {
            kapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }

    @Test(expected = SecurityException.class)
    public void removeInvalidSecurityContextTest() throws Exception {
        ConnectionContext connectionContext = Mockito.mock(ConnectionContext.class);

        Mockito.when(connectionContext.getConnector()).thenReturn(connector);
        Mockito.when((connector).getName()).thenReturn("Connector name");
        Mockito.when(connectionContext.getConnection()).thenReturn(connection);
        Mockito.doReturn(false).when(connection).isNetworkConnection();
        Mockito.when(connectionContext.getSecurityContext()).thenReturn(Mockito.mock(SecurityContext.class));
        kapuaSecurityBrokerFilter.removeConnection(connectionContext, connectionInfo, new Throwable());
    }

    @Test(expected = SecurityException.class)
    public void sendInvalidCharactersInDestinationNameTest() throws Exception {
        String[] destinationName = {"Destination#", "+destination"};
        ProducerBrokerExchange producerExchange = Mockito.mock(ProducerBrokerExchange.class);
        Message messageSend = Mockito.mock(Message.class);
        ActiveMQDestination activeMQDestination = Mockito.mock(ActiveMQDestination.class);
        Mockito.when(messageSend.getDestination()).thenReturn(activeMQDestination);
        for (String name : destinationName) {
            Mockito.when(activeMQDestination.getPhysicalName()).thenReturn(name);
            kapuaSecurityBrokerFilter.send(producerExchange, messageSend);
        }
    }

    @Test
    public void addConsumerTest() {
        ConnectionContext connectionContext = Mockito.mock(ConnectionContext.class);
        try {
            kapuaSecurityBrokerFilter.addConsumer(connectionContext, consumerInfo);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
        try {
            nullKapuaSecurityBrokerFilter.addConsumer(connectionContext, consumerInfo);
            Assert.fail("Exception expected.");
        } catch (Exception ex) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), ex.toString());
        }
    }
}