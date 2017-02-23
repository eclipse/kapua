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

import static java.math.BigInteger.ONE;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.internal.DeviceCreatorImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

@Ignore
public class DeviceValidationTest {

    AuthorizationService authorizationService = mock(AuthorizationService.class);
    PermissionFactory permissionFactory = mock(PermissionFactory.class);

    @Test
    public void shouldValidateNullCreator() throws KapuaException {
        try {
            DeviceValidation.validateCreatePreconditions(null);
        } catch (KapuaIllegalNullArgumentException e) {
            if (e.getMessage().contains("deviceCreator")) {
                return;
            }
        }
        fail();
    }

    @Test
    public void shouldValidateNullScopeIdInCreator() throws KapuaException {
        DeviceCreator deviceCreator = new TestDeviceCreator(null);
        try {
            DeviceValidation.validateCreatePreconditions(deviceCreator);
        } catch (KapuaIllegalNullArgumentException e) {
            if (e.getMessage().contains("scopeId")) {
                return;
            }
        }
        fail();
    }

    @Test
    public void shouldValidateNullClientIdInCreator() throws KapuaException {
        DeviceCreator deviceCreator = new TestDeviceCreator(new KapuaEid(ONE));
        try {
            DeviceValidation.validateCreatePreconditions(deviceCreator);
        } catch (KapuaIllegalNullArgumentException e) {
            if (e.getMessage().contains("clientId")) {
                return;
            }
        }
        fail();
    }

    @Test
    public void shouldPassCreatorValidation() throws KapuaException {
        DeviceCreator deviceCreator = new TestDeviceCreator(new KapuaEid(ONE));
        deviceCreator.setClientId("foo");

        DeviceValidation.validateCreatePreconditions(deviceCreator);
    }

    @Test
    public void shouldCheckCreatorPermissions() throws KapuaException {
        // Given
        DeviceCreator deviceCreator = new TestDeviceCreator(new KapuaEid(ONE));
        deviceCreator.setClientId("foo");

        // When
        DeviceValidation.validateCreatePreconditions(deviceCreator);

        // Then
        Mockito.verify(authorizationService).checkPermission(any(Permission.class));
    }

    //
    // Test classes
    static class TestDeviceCreator extends DeviceCreatorImpl {

        private static final long serialVersionUID = 7599460517092018699L;

        protected TestDeviceCreator(KapuaId scopeId) {
            super(scopeId);
        }
    }

}
