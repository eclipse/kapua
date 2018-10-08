/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.hono;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.eclipse.hono.client.CredentialsClient;
import org.eclipse.hono.config.ServiceConfigProperties;
import org.eclipse.hono.service.auth.device.UsernamePasswordAuthProvider;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;

@RunWith(VertxUnitRunner.class)
public class AuthenticationTest extends TestBase {

    @Test
    public void testHonoAuthenticationProvider(final TestContext ctx) {
        UsernamePasswordAuthProvider authProvider = new UsernamePasswordAuthProvider(new KapuaHonoClient(), new ServiceConfigProperties());

        final Async authenticate = ctx.async();
        authProvider.authenticate(KapuaCredentials.create("kapua-broker@kapua-sys", "kapua-password", true), ctx.asyncAssertSuccess(authenticatedDevice -> {
                    Assert.assertNotNull(authenticatedDevice);
                    Assert.assertEquals("kapua-broker", authenticatedDevice.getDeviceId());
                    authenticate.complete();
                }
        ));
    }

    @Test
    public void testHonoCredentialsClient(final TestContext ctx) {
        CredentialsClient credentialsClient = new KapuaCredentialsClient("kapua-sys");
        final Async get = ctx.async();
        credentialsClient.get("password", "kapua-broker")
                .setHandler(ctx.asyncAssertSuccess(result -> {
                    Assert.assertNotNull(result);
                    Assert.assertEquals("kapua-broker", result.getDeviceId());
                    Assert.assertEquals(1, result.getSecrets().size());
                    get.complete();
                }));
    }

    @Test
    public void testHonoCredentialsService(final TestContext ctx) {
        KapuaCredentialsService credentialsService = new KapuaCredentialsService();
        credentialsService.setConfig(null);

        credentialsService.get("kapua-sys", "password", "kapua-broker", new JsonObject().put("client-id", "my-device"), ctx.asyncAssertSuccess(response -> {
            Assert.assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
            Assert.assertEquals("my-device", response.getPayload().getString("device-id"));
        }));
    }

    @Test
    public void testKapuaServices() throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        JAXBContextProvider brokerProvider = new BrokerJAXBContextProvider();
        XmlUtil.setContextProvider(brokerProvider);

        UserService userService = locator.getService(UserService.class);
        CredentialService credentialService = locator.getService(CredentialService.class);
        User user = KapuaSecurityUtils.doPrivileged(() -> userService.findByName("kapua-broker"));

        Assert.assertNotNull(user);

        AccountService accountService = locator.getService(AccountService.class);
        Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.find(user.getScopeId()));

        Assert.assertNotNull(account);

        CredentialListResult credentials = KapuaSecurityUtils.doPrivileged(() -> credentialService.findByUserId(user.getScopeId(), user.getId()));
        Assert.assertNotNull(credentials.getFirstItem());
        Assert.assertNotNull(credentials.getFirstItem().getCredentialKey());
    }

    @Test
    public void testKapuaAAuthentication() {
        KapuaLocator locator = KapuaLocator.getInstance();
        JAXBContextProvider brokerProvider = new BrokerJAXBContextProvider();
        XmlUtil.setContextProvider(brokerProvider);

        org.eclipse.kapua.service.authentication.AuthenticationService authenticationService = locator.getService(org.eclipse.kapua.service.authentication.AuthenticationService.class);
        CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);
        LoginCredentials credentials = credentialsFactory.newUsernamePasswordCredentials("kapua-broker", "kapua-password");
        try {
            AccessToken accessToken = authenticationService.login(credentials);
            Assert.assertNotNull(accessToken);
        } catch (KapuaException ke) {
           Assert.fail("Failed to authenticate " + ke.getMessage());
        }
    }

}
