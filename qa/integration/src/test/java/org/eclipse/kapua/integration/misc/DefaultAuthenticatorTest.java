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

import com.codahale.metrics.Counter;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;
import org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry;
import org.eclipse.kapua.broker.core.plugin.authentication.DefaultAuthenticator;
import org.eclipse.kapua.broker.core.plugin.metric.ClientMetric;
import org.eclipse.kapua.broker.core.plugin.metric.LoginMetric;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Category(JUnitTests.class)
public class DefaultAuthenticatorTest {

    KapuaSecurityContext kapuaSecurityContext;
    Map<String, Object> options;
    DefaultAuthenticator defaultAuthenticator;
    Throwable[] throwables;
    Counter kapuasysTokenAttemptCount, connectedKapuasysCount, disconnectedKapuasysCount, disconnectedClient;

    @Before
    public void initialize() {
        kapuaSecurityContext = Mockito.mock(KapuaSecurityContext.class);
        options = new HashMap<>();
        throwables = new Throwable[]{null, new Throwable(), new Throwable("message"), new Exception(), new Throwable(new Exception())};
        kapuasysTokenAttemptCount = LoginMetric.getInstance().getKapuasysTokenAttempt();
        connectedKapuasysCount = ClientMetric.getInstance().getConnectedKapuasys();
        disconnectedKapuasysCount = ClientMetric.getInstance().getDisconnectedKapuasys();
        disconnectedClient = ClientMetric.getInstance().getDisconnectedClient();
    }

    @Test
    public void defaultAuthenticatorEmptyOptionsTest() {
        try {
            new DefaultAuthenticator(options);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void defaultAuthenticatorTest() {
        options.put("key1", "value");
        options.put("key2", 11);
        options.put("key3", new Object());

        try {
            new DefaultAuthenticator(options);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void defaultAuthenticatorNullOptionsTest() {
        try {
            new DefaultAuthenticator(null);
            Assert.fail("KapuaException expected.");
        } catch (Exception e) {
            Assert.assertEquals("KapuaException expected.", "org.eclipse.kapua.KapuaException: An internal error occurred: Cannot load instance null for class org.eclipse.kapua.broker.core.plugin.authentication.AdminAuthenticationLogic. Please check the configuration file!.", e.toString());
        }
    }

    @Test
    public void connectAdminEmptyOptionsTest() throws KapuaException {
        defaultAuthenticator = new DefaultAuthenticator(options);

        Mockito.when(kapuaSecurityContext.getUserName()).thenReturn("kapua-sys");

        Assert.assertEquals("Expected and actual values should be the same.", 0, kapuasysTokenAttemptCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, connectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedClient.getCount());

        List<AuthorizationEntry> authorizationEntryList = defaultAuthenticator.connect(kapuaSecurityContext);

        Assert.assertEquals("Expected and actual values should be the same.", 2, authorizationEntryList.size());
        Assert.assertEquals("Expected and actual values should be the same.", "null>", authorizationEntryList.get(0).getAddress());
        Assert.assertEquals("Expected and actual values should be the same.", Acl.ALL, authorizationEntryList.get(0).getAcl());
        Assert.assertEquals("Expected and actual values should be the same.", "nullnull", authorizationEntryList.get(1).getAddress());
        Assert.assertEquals("Expected and actual values should be the same.", Acl.WRITE_ADMIN, authorizationEntryList.get(1).getAcl());

        Assert.assertEquals("Expected and actual values should be the same.", 1, kapuasysTokenAttemptCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 1, connectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedClient.getCount());

        kapuasysTokenAttemptCount.dec();
        connectedKapuasysCount.dec();
    }

    @Test
    public void connectAdminTest() throws KapuaException {
        options.put("address_prefix", "prefix");
        options.put("address_advisory_prefix", "advisory_prefix");
        defaultAuthenticator = new DefaultAuthenticator(options);

        Mockito.when(kapuaSecurityContext.getUserName()).thenReturn("kapua-sys");

        Assert.assertEquals("Expected and actual values should be the same.", 0, kapuasysTokenAttemptCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, connectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedClient.getCount());

        List<AuthorizationEntry> authorizationEntryList = defaultAuthenticator.connect(kapuaSecurityContext);

        Assert.assertEquals("Expected and actual values should be the same.", 2, authorizationEntryList.size());
        Assert.assertEquals("Expected and actual values should be the same.", "prefix>", authorizationEntryList.get(0).getAddress());
        Assert.assertEquals("Expected and actual values should be the same.", Acl.ALL, authorizationEntryList.get(0).getAcl());
        Assert.assertEquals("Expected and actual values should be the same.", "prefixadvisory_prefix", authorizationEntryList.get(1).getAddress());
        Assert.assertEquals("Expected and actual values should be the same.", Acl.WRITE_ADMIN, authorizationEntryList.get(1).getAcl());

        Assert.assertEquals("Expected and actual values should be the same.", 1, kapuasysTokenAttemptCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 1, connectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedKapuasysCount.getCount());
        Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedClient.getCount());

        kapuasysTokenAttemptCount.dec();
        connectedKapuasysCount.dec();
    }

    @Test
    public void disconnectAdminTest() throws KapuaException {
        defaultAuthenticator = new DefaultAuthenticator(options);
        Mockito.when(kapuaSecurityContext.getUserName()).thenReturn("kapua-sys");

        for (Throwable throwable : throwables) {
            Assert.assertEquals("Expected and actual values should be the same.", 0, kapuasysTokenAttemptCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, connectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedClient.getCount());
            defaultAuthenticator.disconnect(kapuaSecurityContext, throwable);
            Assert.assertEquals("Expected and actual values should be the same.", 0, kapuasysTokenAttemptCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, connectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 1, disconnectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedClient.getCount());

            disconnectedKapuasysCount.dec();
        }
    }

    @Test
    public void disconnectUserTest() throws KapuaException {
        defaultAuthenticator = new DefaultAuthenticator(options);
        Mockito.when(kapuaSecurityContext.getOldConnectionId()).thenReturn("ConnectionId");
        Mockito.when(kapuaSecurityContext.getConnectionId()).thenReturn("NewConnectionId");
        Mockito.when(kapuaSecurityContext.getClientId()).thenReturn("clientid");

        Mockito.when(kapuaSecurityContext.getUserName()).thenReturn("user");
        for (Throwable throwable : throwables) {
            Assert.assertEquals("Expected and actual values should be the same.", 0, kapuasysTokenAttemptCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, connectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedClient.getCount());
            defaultAuthenticator.disconnect(kapuaSecurityContext, throwable);
            Assert.assertEquals("Expected and actual values should be the same.", 0, kapuasysTokenAttemptCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, connectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 0, disconnectedKapuasysCount.getCount());
            Assert.assertEquals("Expected and actual values should be the same.", 1, disconnectedClient.getCount());

            disconnectedClient.dec();
        }
    }
}