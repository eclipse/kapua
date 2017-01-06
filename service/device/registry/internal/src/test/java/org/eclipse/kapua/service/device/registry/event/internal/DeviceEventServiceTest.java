/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.event.internal;

import static java.math.BigInteger.ONE;
import static org.eclipse.kapua.commons.security.KapuaSecurityUtils.doPriviledge;

import java.math.BigInteger;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.test.KapuaTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DeviceEventServiceTest extends KapuaTest {

    DeviceEventService deviceEventService = KapuaLocator.getInstance().getService(DeviceEventService.class);

    public static String DEFAULT_FILTER = "dvc_*.sql";
    public static String DROP_FILTER = "dvc_*_drop.sql";

    // Data fixtures

    KapuaEid scope = new KapuaEid(BigInteger.valueOf(random.nextLong()));

    DeviceEventCreator deviceEventCreator;

    @Before
    public void before() {
        deviceEventCreator = new DeviceEventFactoryImpl().newCreator(scope, new KapuaEid(ONE), new Date(), "resource");
        deviceEventCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
    }

    // Database fixtures

    @BeforeClass
    public static void beforeClass() throws KapuaException {
        enableH2Connection();
        scriptSession(DeviceEntityManagerFactory.instance(), DEFAULT_FILTER);
    }

    @AfterClass
    public static void afterClass() throws KapuaException {
        scriptSession(DeviceEntityManagerFactory.instance(), DROP_FILTER);
    }

    // Tests

    @Test
    public void shouldAssignIdAfterCreation() throws Exception {
        doPriviledge(() -> {
            DeviceEvent deviceEvent = deviceEventService.create(deviceEventCreator);
            Assertions.assertThat(deviceEvent.getId()).isNotNull();
            return null;
        });
    }

    @Test
    @Ignore
    public void shouldFindDeviceEventByID() throws Exception {
        doPriviledge(() -> {
            // Given
            DeviceEvent device = deviceEventService.create(deviceEventCreator);

            // When
            DeviceEvent deviceEventFound = deviceEventService.find(scope, device.getId());

            // Then
            Assertions.assertThat(deviceEventFound).isNotNull();
            return null;
        });
    }

    @Test
    @Ignore
    public void shouldDeleteDeviceEvent() throws Exception {
        doPriviledge(() -> {
            // Given
            DeviceEvent device = deviceEventService.create(deviceEventCreator);

            // When
            deviceEventService.delete(device.getScopeId(), device.getId());

            // Then
            DeviceEvent deviceEventFound = deviceEventService.find(scope, device.getId());
            Assertions.assertThat(deviceEventFound).isNull();
            return null;
        });
    }

}
