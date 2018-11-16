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
package org.eclipse.kapua.hono.services;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.eclipse.hono.client.RegistrationClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class RegistrationTest extends TestBase {

    @Test
    public void testHonoRegistrationClient(final TestContext ctx) throws Throwable {
        RegistrationClient registrationClient = new KapuaRegistrationClient("test-tenant");

        final Async assertion = ctx.async();
        registrationClient.assertRegistration("test-device").map(
                result -> {
                    Assert.assertNotNull(result.getString("assertion"));
                    return result;
                }
        ).otherwise(error -> {
            Assert.fail(error.getMessage());
            return null;
        }).setHandler(ctx.asyncAssertSuccess(result -> assertion.complete()));
    }

}
