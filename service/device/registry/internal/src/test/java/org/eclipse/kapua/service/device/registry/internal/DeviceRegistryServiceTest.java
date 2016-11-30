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
package org.eclipse.kapua.service.device.registry.internal;

import org.assertj.core.api.Assertions;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.registry.*;
import org.eclipse.kapua.test.KapuaTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;

import static java.util.UUID.randomUUID;
import static org.eclipse.kapua.commons.model.query.predicate.AttributePredicate.attributeIsEqualTo;
import static org.eclipse.kapua.commons.security.KapuaSecurityUtils.doPriviledge;
import static org.eclipse.kapua.service.device.registry.DeviceCredentialsMode.LOOSE;

public class DeviceRegistryServiceTest extends KapuaTest {

    DeviceRegistryService deviceRegistryService = KapuaLocator.getInstance().getService(DeviceRegistryService.class);

    public static String DEFAULT_FILTER = "dvc_*.sql";
    public static String DROP_FILTER = "dvc_*_drop.sql";

    // Data fixtures

    KapuaEid scope = new KapuaEid(BigInteger.valueOf(random.nextLong()));

    String clientId = randomUUID().toString();

    DeviceCreator deviceCreator;

    @Before
    public void before() {
        deviceCreator = new DeviceFactoryImpl().newCreator(scope, clientId);
        deviceCreator.setCredentialsMode(DeviceCredentialsMode.INHERITED);
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
            Device device = deviceRegistryService.create(deviceCreator);
            Assertions.assertThat(device.getId()).isNotNull();
            return null;
        });
    }

    @Test
    public void shouldFindDeviceByID() throws Exception {
        doPriviledge(() -> {
            // Given
            Device device = deviceRegistryService.create(deviceCreator);

            // When
            Device deviceFound = deviceRegistryService.find(scope, device.getId());

            // Then
            Assertions.assertThat(deviceFound).isNotNull();
            return null;
        });
    }

    @Test
    public void shouldFindDeviceByClientID() throws Exception {
        doPriviledge(() -> {
            // Given
            deviceRegistryService.create(deviceCreator);

            // When
            Device deviceFound = deviceRegistryService.findByClientId(scope, clientId);

            // Then
            Assertions.assertThat(deviceFound).isNotNull();
            return null;
        });
    }

    @Test
    public void shouldUpdateDevice() throws Exception {
        doPriviledge(() -> {
            // Given
            Device device = deviceRegistryService.create(deviceCreator);
            device.setBiosVersion("foo");

            // When
            deviceRegistryService.update(device);

            // Then
            Device deviceFound = deviceRegistryService.find(scope, device.getId());
            Assertions.assertThat(deviceFound.getBiosVersion()).isEqualTo("foo");
            return null;
        });
    }

    @Test
    public void shouldUpdateDeviceCredentialsMode() throws Exception {
        doPriviledge(() -> {
            // Given
            Device device = deviceRegistryService.create(deviceCreator);
            device.setCredentialsMode(LOOSE);

            // When
            deviceRegistryService.update(device);

            // Then
            Device deviceFound = deviceRegistryService.find(scope, device.getId());
            Assertions.assertThat(deviceFound.getCredentialsMode()).isEqualTo(LOOSE);
            return null;
        });
    }

    @Test
    public void shouldDeleteDevice() throws Exception {
        doPriviledge(() -> {
            // Given
            Device device = deviceRegistryService.create(deviceCreator);

            // When
            deviceRegistryService.delete(device.getScopeId(), device.getId());

            // Then
            Device deviceFound = deviceRegistryService.find(scope, device.getId());
            Assertions.assertThat(deviceFound).isNull();
            return null;
        });
    }

    @Test
    public void shouldFindByBiosVersion() throws Exception {
        doPriviledge(() -> {
            // Given
            deviceCreator.setBiosVersion("foo");
            deviceRegistryService.create(deviceCreator);
            DeviceQuery query = new DeviceQueryImpl(scope);
            query.setPredicate(new AttributePredicate<>("biosVersion", "foo"));

            // When
            DeviceListResult result = (DeviceListResult) deviceRegistryService.query(query);

            // Then
            Assertions.assertThat(result.getItem(0).getBiosVersion()).isEqualTo("foo");
            return null;
        });
    }

    @Test
    public void shouldCountByBiosVersion() throws Exception {
        doPriviledge(() -> {
            // Given
            deviceCreator.setBiosVersion(randomUUID().toString());
            Device device = deviceRegistryService.create(deviceCreator);
            DeviceQuery query = new DeviceQueryImpl(scope);
            query.setPredicate(attributeIsEqualTo("biosVersion", device.getBiosVersion()));

            // When
            long count = deviceRegistryService.count(query);

            // Then
            Assertions.assertThat(count).isEqualTo(1L);
            return null;
        });
    }

}
