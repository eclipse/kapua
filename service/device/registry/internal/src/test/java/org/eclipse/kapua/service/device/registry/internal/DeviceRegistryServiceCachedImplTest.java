/*******************************************************************************
 * Copyright (c) 2026 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.cache.CacheConfig;
import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;
import org.eclipse.kapua.storage.TxContext;
import org.eclipse.kapua.storage.TxManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiConsumer;

@Category(JUnitTests.class)
public class DeviceRegistryServiceCachedImplTest {

    private static final String CLIENT_ID = "client";
    private static final KapuaId SCOPE_ID = new KapuaEid(BigInteger.valueOf(10));
    private static final KapuaId OTHER_SCOPE_ID = new KapuaEid(BigInteger.valueOf(20));

    private DeviceRepository deviceRepository;
    private DeviceValidation deviceValidation;
    private Device device;
    private Device otherDevice;

    @Before
    public void setUp() {
        deviceRepository = Mockito.mock(DeviceRepository.class);
        deviceValidation = Mockito.mock(DeviceValidation.class);
        device = Mockito.mock(Device.class);
        otherDevice = Mockito.mock(Device.class);
    }

    @Test
    public void findByClientIdCachedTest() throws Exception {
        Mockito.when(deviceRepository.findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID))).thenReturn(Optional.of(device));
        DeviceRegistryServiceCachedImpl deviceRegistryService = newDeviceRegistryService(true);

        Assert.assertSame(device, deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));
        // Equal, but not the same, scope id instance
        Assert.assertSame(device, deviceRegistryService.findByClientId(new KapuaEid(BigInteger.valueOf(10)), CLIENT_ID));

        // Repository and permission check are hit only once
        Mockito.verify(deviceRepository, Mockito.times(1)).findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID));
        Mockito.verify(deviceValidation, Mockito.times(1)).validateFindByClientIdPreconditions(SCOPE_ID, CLIENT_ID);
    }

    @Test
    public void findByClientIdSameClientIdOtherScopeTest() throws Exception {
        Mockito.when(deviceRepository.findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID))).thenReturn(Optional.of(device));
        Mockito.when(deviceRepository.findByClientId(Mockito.any(TxContext.class), Mockito.eq(OTHER_SCOPE_ID), Mockito.eq(CLIENT_ID))).thenReturn(Optional.of(otherDevice));
        DeviceRegistryServiceCachedImpl deviceRegistryService = newDeviceRegistryService(true);

        Assert.assertSame(device, deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));
        Assert.assertSame(otherDevice, deviceRegistryService.findByClientId(OTHER_SCOPE_ID, CLIENT_ID));
        Assert.assertSame(device, deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));
        Assert.assertSame(otherDevice, deviceRegistryService.findByClientId(OTHER_SCOPE_ID, CLIENT_ID));

        Mockito.verify(deviceRepository, Mockito.times(1)).findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID));
        Mockito.verify(deviceRepository, Mockito.times(1)).findByClientId(Mockito.any(TxContext.class), Mockito.eq(OTHER_SCOPE_ID), Mockito.eq(CLIENT_ID));
    }

    @Test
    public void findByClientIdNotFoundNotCachedTest() throws Exception {
        Mockito.when(deviceRepository.findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID))).thenReturn(Optional.empty(), Optional.of(device));
        DeviceRegistryServiceCachedImpl deviceRegistryService = newDeviceRegistryService(true);

        Assert.assertNull(deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));
        // Registered after the first lookup: must be found
        Assert.assertSame(device, deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));
        Assert.assertSame(device, deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));

        Mockito.verify(deviceRepository, Mockito.times(2)).findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID));
    }

    @Test
    public void findByClientIdCacheDisabledTest() throws Exception {
        Mockito.when(deviceRepository.findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID))).thenReturn(Optional.of(device));
        DeviceRegistryServiceCachedImpl deviceRegistryService = newDeviceRegistryService(false);

        Assert.assertSame(device, deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));
        Assert.assertSame(device, deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID));

        Mockito.verify(deviceRepository, Mockito.times(2)).findByClientId(Mockito.any(TxContext.class), Mockito.eq(SCOPE_ID), Mockito.eq(CLIENT_ID));
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void findByClientIdNullScopeIdTest() throws Exception {
        newDeviceRegistryService(true).findByClientId(null, CLIENT_ID);
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void findByClientIdNullClientIdTest() throws Exception {
        newDeviceRegistryService(true).findByClientId(SCOPE_ID, null);
    }

    //
    // Private methods
    //

    private DeviceRegistryServiceCachedImpl newDeviceRegistryService(boolean cacheEnabled) {
        CacheConfig cacheConfig = new CacheConfig(cacheEnabled ? 100 : 0, 3600, ExpiryPolicy.MODIFIED);
        return new DeviceRegistryServiceCachedImpl(
                new PassThroughTxManager(),
                null,
                null,
                null,
                null,
                deviceValidation,
                deviceRepository,
                null,
                null,
                new LocalCache<>(cacheConfig, null));
    }

    private static class PassThroughTxManager implements TxManager {

        @Override
        @SafeVarargs
        public final <R> R execute(TxConsumer<R> transactionConsumer, BiConsumer<TxContext, R>... justBeforeCommitAdditionalTxConsumers) throws KapuaException {
            return transactionConsumer.execute(Mockito.mock(TxContext.class));
        }

        @Override
        public TxContext getTxContext() {
            return Mockito.mock(TxContext.class);
        }
    }
}
