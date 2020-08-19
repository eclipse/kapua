/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaConnectionContextTest extends Assert {

    @Test
    public void firstConstructorTest() {
        KapuaConnectionContext connectionContext = new KapuaConnectionContext(Long.parseLong("1"), "1", "client1");
        assertEquals("client1", connectionContext.getFullClientId());
        assertEquals("1", connectionContext.getClientId());
        assertEquals(Long.parseLong("0"), connectionContext.getScopeIdAsLong());
        assertEquals("1", connectionContext.getClientId());
    }

    @Test
    public void secondConstructorTest() {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setUserName("testuser");
        connectionInfo.setClientId("1");
        connectionInfo.setClientIp("192.168.1.2");
        connectionInfo.setConnectionId(new ConnectionId("1"));
        KapuaConnectionContext connectionContext = new KapuaConnectionContext("broker1", connectionInfo);
        assertEquals("broker1" ,connectionContext.getBrokerId());
        assertEquals("1" ,connectionContext.getClientId());
        assertEquals("testuser" ,connectionContext.getUserName());
        assertEquals("192.168.1.2" ,connectionContext.getClientIp());
        assertEquals("1" ,connectionContext.getConnectionId());
    }

    @Test
    public void thirdConstructorTest() {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setUserName("testuser");
        connectionInfo.setClientId("1");
        connectionInfo.setClientIp("192.168.1.2");
        connectionInfo.setConnectionId(new ConnectionId("1"));
        AccessToken accessToken = new AccessTokenImpl();
        accessToken.setScopeId(KapuaId.ONE);
        KapuaPrincipal kapuaPrincipal = new KapuaPrincipalImpl(accessToken, "user1", "client1", "192.168.2.2");
        KapuaConnectionContext connectionContext = new KapuaConnectionContext("broker1", "192.168.1.2", kapuaPrincipal, "account1", connectionInfo, "{0}:{1}", true);
        assertEquals("broker1" ,connectionContext.getBrokerId());
        assertEquals("client1" ,connectionContext.getClientId());
        assertEquals("testuser" ,connectionContext.getUserName());
        assertEquals("192.168.1.2" ,connectionContext.getClientIp());
        assertEquals("1" ,connectionContext.getConnectionId());
        assertTrue(connectionContext.isMissing());
    }

    @Test
    public void updateTest() {
        KapuaConnectionContext connectionContext = new KapuaConnectionContext(Long.parseLong("1"), "1", "client1");
        AccessToken accessToken = new AccessTokenImpl();
        accessToken.setScopeId(KapuaId.ONE);
        connectionContext.update(accessToken, "account1", KapuaId.ONE, KapuaId.ONE, "connector1", "192.168.1.2", "{0}:{1}");
        assertEquals(KapuaId.ONE, connectionContext.getScopeId());
        assertEquals(KapuaId.ONE, connectionContext.getUserId());
        assertEquals("192.168.1.2", connectionContext.getBrokerIpOrHostName());
    }

    @Test
    public void updatePermissionsTest() {
        KapuaConnectionContext connectionContext = new KapuaConnectionContext(Long.parseLong("1"), "1", "client1");
        connectionContext.updatePermissions(new boolean[]{true, false, true});
        assertEquals(true, connectionContext.getHasPermissions()[0]);
        assertEquals(false, connectionContext.getHasPermissions()[1]);
        assertEquals(true, connectionContext.getHasPermissions()[2]);
        assertEquals(3, connectionContext.getHasPermissions().length);
    }

    @Test
    public void updateKapuaConnectionIdNullTest() {
        KapuaConnectionContext connectionContext = new KapuaConnectionContext(Long.parseLong("1"), "1", "client1");
        connectionContext.updateKapuaConnectionId(null);
        assertNull(connectionContext.getKapuaConnectionId());
    }

    @Test
    public void updateOldConnectionIdTest() {
        KapuaConnectionContext connectionContext = new KapuaConnectionContext(Long.parseLong("1"), "1", "client1");
        connectionContext.updateOldConnectionId("con1");
        assertEquals("con1", connectionContext.getOldConnectionId());
    }

    @Test
    public void gettersTest() {
        KapuaConnectionContext connectionContext = new KapuaConnectionContext(Long.parseLong("1"), "1", "client1");
        assertEquals("client1", connectionContext.getFullClientId());
        assertNull(connectionContext.getAccountName());
        assertNull(connectionContext.getBrokerId());
        assertNull(connectionContext.getClientCertificates());
        assertNull(connectionContext.getUserName());
        assertNull(connectionContext.getScopeId());
        assertEquals("1", connectionContext.getClientId());
        assertNull(connectionContext.getScopeId());
        assertEquals(Long.parseLong("0"), connectionContext.getScopeIdAsLong());
        assertEquals("1", connectionContext.getClientId());
        assertNull(connectionContext.getClientIp());
        assertNull(connectionContext.getPrincipal());
        assertNull(connectionContext.getConnectionId());
        assertNull(connectionContext.getOldConnectionId());
        assertNull(connectionContext.getConnectorDescriptor());
        assertNull(connectionContext.getKapuaConnectionId());
        assertNull(connectionContext.getUserId());
        assertNull(connectionContext.getHasPermissions());
        assertNull(connectionContext.getBrokerIpOrHostName());
        assertFalse(connectionContext.isMissing());
    }

    @Test
    public void logAuthDestinationTest() {
        KapuaConnectionContext connectionContext = new KapuaConnectionContext(Long.parseLong("1"), "1", "client1");
        try {
            connectionContext.logAuthDestinationToLog();
            connectionContext.addAuthDestinationToLog("test");
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }
}
