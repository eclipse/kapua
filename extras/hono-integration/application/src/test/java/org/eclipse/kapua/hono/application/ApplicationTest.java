/*******************************************************************************
 * Copyright (c) 2016, 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.kapua.hono.application;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.eclipse.hono.client.HonoClient;
import org.eclipse.hono.client.impl.HonoClientImpl;
import org.eclipse.hono.config.ClientConfigProperties;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.vertx.core.Vertx.vertx;

@RunWith(VertxUnitRunner.class)
public class ApplicationTest {

    static HonoClient honoClient;

    @Test
    public void testTenantAPI(final TestContext ctx) {
        final ClientConfigProperties config = new ClientConfigProperties();
        config.setPort(25671);
        config.setUsername("hono-client@HONO");
        config.setPassword("secret");
        config.setRequestTimeout(2000);
        honoClient = new HonoClientImpl(vertx(), config);

        final Async testComplete = ctx.async();

        honoClient.connect().compose(
                connected -> connected.getOrCreateTenantClient()
                        .compose(client -> client.get("kapua-sys"))
                        .map(tenantObject -> {
                            System.out.println("Tenant " + tenantObject.getTenantId());
                            ctx.assertEquals("kapua-sys", tenantObject.getTenantId());
                            testComplete.complete();
                            return tenantObject;
                        })

        );

        testComplete.await();
    }

    @AfterClass
    public static void shutdown(final TestContext ctx) {
       if (honoClient != null) {
           honoClient.shutdown();
       }
    }

}
