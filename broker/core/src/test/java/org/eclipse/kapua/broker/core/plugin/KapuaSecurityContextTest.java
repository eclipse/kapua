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
package org.eclipse.kapua.broker.core.plugin;

import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.security.AuthorizationMap;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenImpl;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.HashSet;
import java.util.Set;

@Category(JUnitTests.class)
public class KapuaSecurityContextTest extends Assert {

    String clientId;
    Long[] scopeId;
    String[] expectedFullClientId;
    KapuaSession kapuaSession;
    AccessToken accessToken;
    ConnectionInfo connectionInfo;
    ConnectionId connectionId;
    KapuaPrincipal kapuaPrincipal;
    Certificate[] certificates;
    KapuaSecurityContext securityContext1;
    KapuaSecurityContext securityContext2;
    Set<Principal> expectedPrincipals;
    ConnectorDescriptor expectedConnectorDescriptor;
    String key;
    String value;
    String defaultValue;

    @Before
    public void initialize() {
        clientId = "client1";
        scopeId = new Long[]{-1000000L, -10L, -1L, 0L, 1L, 10L, 10000000000L};
        expectedFullClientId = new String[]{"-1,000,000:client1", "-10:client1", "-1:client1", "0:client1", "1:client1", "10:client1", "10,000,000,000:client1"};
        kapuaSession = new KapuaSession();
        KapuaSecurityUtils.setSession(kapuaSession);
        accessToken = new AccessTokenImpl();
        connectionInfo = new ConnectionInfo();
        connectionId = new ConnectionId("connection id");
        connectionInfo.setConnectionId(connectionId);
        connectionInfo.setClientId("client ID");
        connectionInfo.setClientIp("client IP");
        accessToken.setTokenId("token id");
        accessToken.setUserId(KapuaId.ONE);
        accessToken.setScopeId(KapuaId.ONE);
        kapuaPrincipal = new KapuaPrincipalImpl(accessToken, "username", "clientId", "clientIp");
        certificates = new Certificate[]{Mockito.mock(Certificate.class), Mockito.mock(Certificate.class)};
        securityContext1 = new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", "accountName", connectionInfo, "connectorName");
        securityContext2 = new KapuaSecurityContext(10L, "clientID");
        expectedPrincipals = new HashSet<Principal>();
        expectedPrincipals.add(kapuaPrincipal);
        expectedConnectorDescriptor = ConnectorDescriptorProviders.getDescriptor("connectorName");
        key = "Key";
        value = "value";
        defaultValue = "defaultValue";
    }

    @Test
    public void kapuaSecurityContextScopeIdClientIdTest() {
        for (int i = 0; i < scopeId.length; i++) {
            KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(scopeId[i], clientId);
            assertEquals("Expected and actual values should be the same.", new KapuaEid(BigInteger.valueOf(scopeId[i])), kapuaSecurityContext.getScopeId());
            assertEquals("Expected and actual values should be the same.", clientId, kapuaSecurityContext.getClientId());
            assertNull("Null expected.", kapuaSecurityContext.getUserName());
            assertEquals("Expected and actual values should be the same.", expectedFullClientId[i], kapuaSecurityContext.getFullClientId());
        }
    }

    @Test
    public void kapuaSecurityContextScopeIdClientIdWithoutSessionTest() {
        KapuaSecurityUtils.clearSession();
        for (int i = 0; i < scopeId.length; i++) {
            KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(scopeId[i], clientId);
            assertEquals("Expected and actual values should be the same.", new KapuaEid(BigInteger.valueOf(scopeId[i])), kapuaSecurityContext.getScopeId());
            assertEquals("Expected and actual values should be the same.", clientId, kapuaSecurityContext.getClientId());
            assertNull("NUll expected.", kapuaSecurityContext.getUserName());
            assertEquals("Expected and actual values should be the same.", expectedFullClientId[i], kapuaSecurityContext.getFullClientId());
        }
    }

    @Test(expected = NullPointerException.class)
    public void kapuaSecurityContextNullScopeIdClientIdTest() {
        new KapuaSecurityContext(null, clientId);
    }

    @Test
    public void kapuaSecurityContextScopeIdNullClientIdTest() {
        String[] expectedFullClientId = {"-1,000,000:null", "-10:null", "-1:null", "0:null", "1:null", "10:null", "10,000,000,000:null"};
        for (int i = 0; i < scopeId.length; i++) {
            KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(scopeId[i], null);
            assertEquals("Expected and actual values should be the same.", new KapuaEid(BigInteger.valueOf(scopeId[i])), kapuaSecurityContext.getScopeId());
            assertNull("NUll expected.", kapuaSecurityContext.getClientId());
            assertNull("NUll expected.", kapuaSecurityContext.getUserName());
            assertEquals("Expected and actual values should be the same.", expectedFullClientId[i], kapuaSecurityContext.getFullClientId());
        }
    }

    @Test(expected = NullPointerException.class)
    public void kapuaSecurityContextWithoutSessionTest() {
        KapuaSecurityUtils.clearSession();
        new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", "accountName", connectionInfo, "connectorName");
    }

    @Test
    public void kapuaSecurityContextTest() throws KapuaException {
        KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", "accountName", connectionInfo, "connectorName");

        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getPrincipal());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getKapuaPrincipal());
        assertEquals("Expected and actual values should be the same.", expectedPrincipals, kapuaSecurityContext.getPrincipals());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getMainPrincipal());
        assertEquals("Expected and actual values should be the same.", "brokerId", kapuaSecurityContext.getBrokerId());
        assertEquals("Expected and actual values should be the same.", "brokerIpOrHostName", kapuaSecurityContext.getBrokerIpOrHostName());
        assertEquals("Expected and actual values should be the same.", "accountName", kapuaSecurityContext.getAccountName());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getUserId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getScopeId());
        assertEquals("Expected and actual values should be the same.", 1L, kapuaSecurityContext.getScopeIdAsLong());
        assertEquals("Expected and actual values should be the same.", "client ID", kapuaSecurityContext.getClientId());
        assertEquals("Expected and actual values should be the same.", "client IP", kapuaSecurityContext.getClientIp());
        assertEquals("Expected and actual values should be the same.", "connection id", kapuaSecurityContext.getConnectionId());
        assertNull("Null expected.", kapuaSecurityContext.getClientCertificates());
        assertEquals("Expected and actual values should be the same.", expectedConnectorDescriptor, kapuaSecurityContext.getConnectorDescriptor());
        assertThat("Instance of KapuaSession expected.", kapuaSecurityContext.getKapuaSession(), IsInstanceOf.instanceOf(KapuaSession.class));
        assertEquals("Expected and actual values should be the same.", "1:client ID", kapuaSecurityContext.getFullClientId());
    }

    @Test(expected = NullPointerException.class)
    public void kapuaSecurityContextNullPrincipalTest() {
        new KapuaSecurityContext(null, "brokerId", "brokerIpOrHostName", "accountName", connectionInfo, "connectorName");
    }

    @Test
    public void kapuaSecurityContextNullBrokerIdTest() throws KapuaException {
        KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(kapuaPrincipal, null, "brokerIpOrHostName", "accountName", connectionInfo, "connectorName");

        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getPrincipal());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getKapuaPrincipal());
        assertEquals("Expected and actual values should be the same.", expectedPrincipals, kapuaSecurityContext.getPrincipals());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getMainPrincipal());
        assertNull("Null expected.", kapuaSecurityContext.getBrokerId());
        assertEquals("Expected and actual values should be the same.", "brokerIpOrHostName", kapuaSecurityContext.getBrokerIpOrHostName());
        assertEquals("Expected and actual values should be the same.", "accountName", kapuaSecurityContext.getAccountName());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getUserId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getScopeId());
        assertEquals("Expected and actual values should be the same.", "client ID", kapuaSecurityContext.getClientId());
        assertEquals("Expected and actual values should be the same.", "client IP", kapuaSecurityContext.getClientIp());
        assertEquals("Expected and actual values should be the same.", "connection id", kapuaSecurityContext.getConnectionId());
        assertNull("Null expected.", kapuaSecurityContext.getClientCertificates());
        assertEquals("Expected and actual values should be the same.", expectedConnectorDescriptor, kapuaSecurityContext.getConnectorDescriptor());
        assertThat("Instance of KapuaSession expected.", kapuaSecurityContext.getKapuaSession(), IsInstanceOf.instanceOf(KapuaSession.class));
        assertEquals("Expected and actual values should be the same.", "1:client ID", kapuaSecurityContext.getFullClientId());
    }

    @Test
    public void kapuaSecurityContextNullBrokerIpOrHostNameTest() throws KapuaException {
        KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(kapuaPrincipal, "brokerId", null, "accountName", connectionInfo, "connectorName");

        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getPrincipal());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getKapuaPrincipal());
        assertEquals("Expected and actual values should be the same.", expectedPrincipals, kapuaSecurityContext.getPrincipals());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getMainPrincipal());
        assertEquals("Expected and actual values should be the same.", "brokerId", kapuaSecurityContext.getBrokerId());
        assertNull("Null expected.", kapuaSecurityContext.getBrokerIpOrHostName());
        assertEquals("Expected and actual values should be the same.", "accountName", kapuaSecurityContext.getAccountName());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getUserId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getScopeId());
        assertEquals("Expected and actual values should be the same.", "client ID", kapuaSecurityContext.getClientId());
        assertEquals("Expected and actual values should be the same.", "client IP", kapuaSecurityContext.getClientIp());
        assertEquals("Expected and actual values should be the same.", "connection id", kapuaSecurityContext.getConnectionId());
        assertNull("Null expected.", kapuaSecurityContext.getClientCertificates());
        assertEquals("Expected and actual values should be the same.", expectedConnectorDescriptor, kapuaSecurityContext.getConnectorDescriptor());
        assertThat("Instance of KapuaSession expected.", kapuaSecurityContext.getKapuaSession(), IsInstanceOf.instanceOf(KapuaSession.class));
        assertEquals("Expected and actual values should be the same.", "1:client ID", kapuaSecurityContext.getFullClientId());
    }

    @Test
    public void kapuaSecurityContextNullAccountNameTest() throws KapuaException {
        KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", null, connectionInfo, "connectorName");

        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getPrincipal());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getKapuaPrincipal());
        assertEquals("Expected and actual values should be the same.", expectedPrincipals, kapuaSecurityContext.getPrincipals());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getMainPrincipal());
        assertEquals("Expected and actual values should be the same.", "brokerId", kapuaSecurityContext.getBrokerId());
        assertEquals("Expected and actual values should be the same.", "brokerIpOrHostName", kapuaSecurityContext.getBrokerIpOrHostName());
        assertNull("Null expected.", kapuaSecurityContext.getAccountName());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getUserId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getScopeId());
        assertEquals("Expected and actual values should be the same.", "client ID", kapuaSecurityContext.getClientId());
        assertEquals("Expected and actual values should be the same.", "client IP", kapuaSecurityContext.getClientIp());
        assertEquals("Expected and actual values should be the same.", "connection id", kapuaSecurityContext.getConnectionId());
        assertNull("Null expected.", kapuaSecurityContext.getClientCertificates());
        assertEquals("Expected and actual values should be the same.", expectedConnectorDescriptor, kapuaSecurityContext.getConnectorDescriptor());
        assertThat("Instance of KapuaSession expected.", kapuaSecurityContext.getKapuaSession(), IsInstanceOf.instanceOf(KapuaSession.class));
        assertEquals("Expected and actual values should be the same.", "1:client ID", kapuaSecurityContext.getFullClientId());
    }

    @Test(expected = NullPointerException.class)
    public void kapuaSecurityContextNullConnectionInfoTest() {
        new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", "accountName", null, "connectorName");
    }

    @Test(expected = IllegalStateException.class)
    public void kapuaSecurityContextNullConnectorNameTest() {
        new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", "accountName", connectionInfo, null);
    }

    @Test
    public void kapuaSecurityContextCertificateTest() throws KapuaException {
        connectionInfo.setTransportContext(certificates);
        KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", "accountName", connectionInfo, "connectorName");

        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getPrincipal());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getKapuaPrincipal());
        assertEquals("Expected and actual values should be the same.", expectedPrincipals, kapuaSecurityContext.getPrincipals());
        assertEquals("Expected and actual values should be the same.", kapuaPrincipal, kapuaSecurityContext.getMainPrincipal());
        assertEquals("Expected and actual values should be the same.", "brokerId", kapuaSecurityContext.getBrokerId());
        assertEquals("Expected and actual values should be the same.", "brokerIpOrHostName", kapuaSecurityContext.getBrokerIpOrHostName());
        assertEquals("Expected and actual values should be the same.", "accountName", kapuaSecurityContext.getAccountName());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getUserId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, kapuaSecurityContext.getScopeId());
        assertEquals("Expected and actual values should be the same.", "client ID", kapuaSecurityContext.getClientId());
        assertEquals("Expected and actual values should be the same.", "client IP", kapuaSecurityContext.getClientIp());
        assertEquals("Expected and actual values should be the same.", "connection id", kapuaSecurityContext.getConnectionId());
        assertArrayEquals("Expected and actual values should be the same.", certificates, kapuaSecurityContext.getClientCertificates());
        assertEquals("Expected and actual values should be the same.", expectedConnectorDescriptor, kapuaSecurityContext.getConnectorDescriptor());
        assertThat("Instance of KapuaSession expected.", kapuaSecurityContext.getKapuaSession(), IsInstanceOf.instanceOf(KapuaSession.class));
        assertEquals("Expected and actual values should be the same.", "1:client ID", kapuaSecurityContext.getFullClientId());
    }

    @Test
    public void setAndGetAuthorizationMapTest() {
        AuthorizationMap authorizationMap = Mockito.mock(AuthorizationMap.class);

        assertNull("Null expected.", securityContext1.getAuthorizationMap());
        securityContext1.setAuthorizationMap(authorizationMap);
        assertEquals("Expected and actual values should be the same.", authorizationMap, securityContext1.getAuthorizationMap());

        assertNull("Null expected.", securityContext2.getAuthorizationMap());
        securityContext2.setAuthorizationMap(authorizationMap);
        assertEquals("Expected and actual values should be the same.", authorizationMap, securityContext2.getAuthorizationMap());
    }

    @Test
    public void updateAndGetKapuaConnectionIdTest() {
        DeviceConnection deviceConnection = Mockito.mock(DeviceConnection.class);

        assertNull("Null expected.", securityContext1.getKapuaConnectionId());
        securityContext1.updateKapuaConnectionId(null);
        assertNull("Null expected.", securityContext1.getKapuaConnectionId());
        securityContext1.updateKapuaConnectionId(deviceConnection);
        assertNull("Null expected.", securityContext1.getKapuaConnectionId());
        Mockito.when(deviceConnection.getId()).thenReturn(KapuaId.ONE);
        securityContext1.updateKapuaConnectionId(deviceConnection);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, securityContext1.getKapuaConnectionId());

        assertNull("Null expected.", securityContext2.getKapuaConnectionId());
        securityContext2.updateKapuaConnectionId(null);
        assertNull("Null expected.", securityContext2.getKapuaConnectionId());
        securityContext2.updateKapuaConnectionId(deviceConnection);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, securityContext2.getKapuaConnectionId());
        Mockito.when(deviceConnection.getId()).thenReturn(KapuaId.ONE);
        securityContext2.updateKapuaConnectionId(deviceConnection);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, securityContext2.getKapuaConnectionId());
    }

    @Test
    public void setAndIsMissingTest() {
        assertFalse("False expected.", securityContext1.isMissing());
        assertFalse("False expected.", securityContext1.getProperty(KapuaSecurityContext.PARAM_KEY_STATUS_MISSING, false));
        securityContext1.setMissing();
        assertTrue("True expected.", securityContext1.isMissing());
        assertTrue("True expected.", securityContext1.getProperty(KapuaSecurityContext.PARAM_KEY_STATUS_MISSING, false));

        assertFalse("False expected.", securityContext2.isMissing());
        assertFalse("False expected.", securityContext2.getProperty(KapuaSecurityContext.PARAM_KEY_STATUS_MISSING, false));
        securityContext2.setMissing();
        assertTrue("True expected.", securityContext2.isMissing());
        assertTrue("True expected.", securityContext2.getProperty(KapuaSecurityContext.PARAM_KEY_STATUS_MISSING, false));
    }

    @Test
    public void updateAndHasPermissionsTest() {
        boolean[] hasPermissions = {true, false, false, true, true, false};

        assertNull("Null expected.", securityContext1.getHasPermissions());
        securityContext1.updatePermissions(hasPermissions);
        assertEquals("Expected and actual values should be the same.", hasPermissions, securityContext1.getHasPermissions());
        assertTrue("True expected.", securityContext1.isBrokerConnect());
        assertFalse("False expected.", securityContext1.isDeviceManage());
        assertFalse("False expected.", securityContext1.isDataView());
        assertTrue("True expected.", securityContext1.isDataManage());
        assertTrue("True expected.", securityContext1.isDeviceView());

        assertNull("Null expected.", securityContext2.getHasPermissions());
        securityContext2.updatePermissions(hasPermissions);
        assertEquals("Expected and actual values should be the same.", hasPermissions, securityContext2.getHasPermissions());
        assertTrue("True expected.", securityContext2.isBrokerConnect());
        assertFalse("False expected.", securityContext2.isDeviceManage());
        assertFalse("False expected.", securityContext2.isDataView());
        assertTrue("True expected.", securityContext2.isDataManage());
        assertTrue("True expected.", securityContext2.isDeviceView());
    }

    @Test
    public void updateAndGetOldConnectionIdTest() {
        String[] oldConnectionIds = {"", "old connection id", "1234567890", "!@#$%^&*()_/.,>?"};

        assertNull("Null expected.", securityContext1.getOldConnectionId());
        for (String oldConnectionId : oldConnectionIds) {
            securityContext1.updateOldConnectionId(oldConnectionId);
            assertEquals("Expected and actual values should be the same.", oldConnectionId, securityContext1.getOldConnectionId());
        }
        securityContext1.updateOldConnectionId(null);
        assertNull("Null expected.", securityContext1.getKapuaConnectionId());

        assertNull("Null expected.", securityContext2.getOldConnectionId());
        for (String oldConnectionId : oldConnectionIds) {
            securityContext2.updateOldConnectionId(oldConnectionId);
            assertEquals("Expected and actual values should be the same.", oldConnectionId, securityContext2.getOldConnectionId());
        }
        securityContext2.updateOldConnectionId(null);
        assertNull("Null expected.", securityContext2.getKapuaConnectionId());
    }

    @Test(expected = NullPointerException.class)
    public void getScopeIdAsLongNullScopeIdTest() {
        accessToken.setScopeId(null);
        kapuaPrincipal = new KapuaPrincipalImpl(accessToken, "username", "clientId", "clientIp");
        KapuaSecurityContext kapuaSecurityContext = new KapuaSecurityContext(kapuaPrincipal, "brokerId", "brokerIpOrHostName", "accountName", connectionInfo, "connectorName");
        kapuaSecurityContext.getScopeIdAsLong();
    }

    @Test
    public void setAndIsAdminTest() {
        assertFalse("False expected.", securityContext1.isAdmin());
        assertFalse("False expected.", securityContext1.getProperty(KapuaSecurityContext.PARAM_KEY_PROFILE_ADMIN, false));
        securityContext1.setAdmin(true);
        assertTrue("True expected.", securityContext1.isAdmin());
        assertTrue("True expected.", securityContext1.getProperty(KapuaSecurityContext.PARAM_KEY_PROFILE_ADMIN, false));
        securityContext1.setAdmin(false);
        assertFalse("False expected.", securityContext1.isAdmin());
        assertFalse("False expected.", securityContext1.getProperty(KapuaSecurityContext.PARAM_KEY_PROFILE_ADMIN, false));

        assertFalse("False expected.", securityContext2.isAdmin());
        assertFalse("False expected.", securityContext2.getProperty(KapuaSecurityContext.PARAM_KEY_PROFILE_ADMIN, false));
        securityContext2.setAdmin(true);
        assertTrue("True expected.", securityContext2.isAdmin());
        assertTrue("True expected.", securityContext2.getProperty(KapuaSecurityContext.PARAM_KEY_PROFILE_ADMIN, false));
        securityContext2.setAdmin(false);
        assertFalse("False expected.", securityContext2.isAdmin());
        assertFalse("False expected.", securityContext2.getProperty(KapuaSecurityContext.PARAM_KEY_PROFILE_ADMIN, false));
    }

    @Test
    public void isBrokerConnectNullPermissionsTest() {
        try {
            securityContext1.isBrokerConnect();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }

        try {
            securityContext2.isBrokerConnect();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void isDeviceViewNullPermissionsTest() {
        try {
            securityContext1.isDeviceView();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }

        try {
            securityContext2.isDeviceView();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void isDeviceManageNullPermissionsTest() {
        try {
            securityContext1.isDeviceManage();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }

        try {
            securityContext2.isDeviceManage();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void isDataViewNullPermissionsTest() {
        try {
            securityContext1.isDataView();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }

        try {
            securityContext2.isDataView();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void isDataManageNullPermissionsTest() {
        try {
            securityContext1.isDeviceManage();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }

        try {
            securityContext2.isDeviceManage();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void setAndGetPropertyTest() {
        assertEquals("Expected and actual values should be the same.", defaultValue, securityContext1.getProperty(key, defaultValue));
        securityContext1.setProperty(key, value);
        assertEquals("Expected and actual values should be the same.", value, securityContext1.getProperty(key, defaultValue));
    }

    @Test
    public void setAndGetPropertyNullValueTest() {
        assertEquals("Expected and actual values should be the same.", defaultValue, securityContext1.getProperty(key, defaultValue));
        securityContext1.setProperty(key, null);
        assertEquals("Expected and actual values should be the same.", defaultValue, securityContext1.getProperty(key, defaultValue));
    }

    @Test
    public void setAndGetPropertyNullKeyTest() {
        assertEquals("Expected and actual values should be the same.", defaultValue, securityContext1.getProperty(key, defaultValue));
        securityContext1.setProperty(null, value);
        assertEquals("Expected and actual values should be the same.", value, securityContext1.getProperty(null, defaultValue));
    }

    @Test
    public void setAndGetPropertyNullKeyNullValueTest() {
        assertEquals("Expected and actual values should be the same.", defaultValue, securityContext1.getProperty(key, defaultValue));
        securityContext1.setProperty(null, null);
        assertEquals("Expected and actual values should be the same.", defaultValue, securityContext1.getProperty(null, defaultValue));
    }

    @Test
    public void addAuthDestinationToLogTest() {
        String[] messages = {null, "", "message()", "message_123", "!m3ss@g3", " message me-ssa_ge", "m<123>"};

        for (String message : messages) {
            try {
                securityContext1.addAuthDestinationToLog(message);
            } catch (Exception e) {
                fail("Exception not expected.");
            }

            try {
                securityContext2.addAuthDestinationToLog(message);
            } catch (Exception e) {
                fail("Exception not expected.");
            }
        }
    }

    @Test
    public void logAuthDestinationToLogTest() {
        try {
            securityContext1.logAuthDestinationToLog();
        } catch (Exception e) {
            fail("Exception not expected.");
        }

        try {
            securityContext2.logAuthDestinationToLog();
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }
}