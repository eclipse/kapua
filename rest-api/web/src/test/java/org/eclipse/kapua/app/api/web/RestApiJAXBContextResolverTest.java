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
package org.eclipse.kapua.app.api.web;

import org.eclipse.kapua.app.api.core.exception.model.EntityNotFoundExceptionInfo;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackages;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class RestApiJAXBContextResolverTest {

    RestApiJAXBContextResolver jaxbContextResolver;

    @Before
    public void initialize() {
        jaxbContextResolver = new RestApiJAXBContextResolver();
    }

    @Test
    public void getContextTest() {
        Class<?>[] classes = {
                AccessToken.class,
                Account.class,
                ChannelInfo.class,
                Credential.class,
                Device.class,
                DeviceManagementOperation.class,
                EntityNotFoundExceptionInfo.class,
                Exception.class,
                Group.class,
                Job.class,
                KapuaRequestMessage.class,
                KapuaResponseMessage.class,
                KapuaTocd.class,
                KuraDeploymentPackages.class,
                Role.class,
                String.class,
                User.class};

        for (Class<?> clazz : classes) {
            Assert.assertNotNull(jaxbContextResolver.getContext(clazz));
        }
    }

    @Test
    public void getContextObjectTest() {
        Assert.assertNotNull(jaxbContextResolver.getContext(Object.class));
    }

    @Test(expected = NullPointerException.class)
    public void getContextNullTest() {
        jaxbContextResolver.getContext(null);
    }
}