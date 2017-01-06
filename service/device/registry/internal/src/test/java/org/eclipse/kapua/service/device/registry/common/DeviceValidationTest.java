/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.common;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.internal.DeviceCreatorImpl;
import org.junit.Test;
import org.junit.Ignore;
import org.mockito.Mockito;

import static java.math.BigInteger.ONE;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@Ignore
public class DeviceValidationTest {

    AuthorizationService authorizationService = mock(AuthorizationService.class);

    DeviceValidation deviceValidation = new DeviceValidation(mock(PermissionFactory.class), authorizationService);

    @Test
    public void shouldValidateNullCreator() throws KapuaException {
        try {
            deviceValidation.validateCreatePreconditions(null);
        } catch (KapuaIllegalNullArgumentException e) {
            if(e.getMessage().contains("deviceCreator")) {
                return;
            }
        }
        fail();
    }

    @Test
    public void shouldValidateNullScopeIdInCreator() throws KapuaException {
        DeviceCreator deviceCreator = new TestDeviceCreator(null);
        try {
            deviceValidation.validateCreatePreconditions(deviceCreator);
        } catch (KapuaIllegalNullArgumentException e) {
            if(e.getMessage().contains("scopeId")) {
                return;
            }
        }
        fail();
    }

    @Test
    public void shouldValidateNullClientIdInCreator() throws KapuaException {
        DeviceCreator deviceCreator = new TestDeviceCreator(new KapuaEid(ONE));
        try {
            deviceValidation.validateCreatePreconditions(deviceCreator);
        } catch (KapuaIllegalNullArgumentException e) {
            if(e.getMessage().contains("clientId")) {
                return;
            }
        }
        fail();
    }

    @Test
    public void shouldPassCreatorValidation() throws KapuaException {
        DeviceCreator deviceCreator = new TestDeviceCreator(new KapuaEid(ONE));
        deviceCreator.setClientId("foo");

        deviceValidation.validateCreatePreconditions(deviceCreator);
    }

    @Test
    public void shouldCheckCreatorPermissions() throws KapuaException {
        // Given
        DeviceCreator deviceCreator = new TestDeviceCreator(new KapuaEid(ONE));
        deviceCreator.setClientId("foo");

        // When
        deviceValidation.validateCreatePreconditions(deviceCreator);

        // Then
        Mockito.verify(authorizationService).checkPermission(any(Permission.class));
    }

    // Test classes

    static class TestDeviceCreator extends DeviceCreatorImpl{
        protected TestDeviceCreator(KapuaId scopeId) {
            super(scopeId);
        }
    }

}
