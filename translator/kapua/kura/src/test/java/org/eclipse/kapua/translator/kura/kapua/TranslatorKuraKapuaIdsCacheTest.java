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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.CacheConfig;
import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.setting.TranslatorKapuaKuraSettingKeys;
import org.eclipse.kapua.translator.setting.TranslatorKapuaKuraSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.Optional;

@Category(JUnitTests.class)
public class TranslatorKuraKapuaIdsCacheTest {

    private static final String ACCOUNT_NAME = "account";
    private static final String CLIENT_ID = "client";
    private static final KapuaId SCOPE_ID = new KapuaEid(BigInteger.valueOf(10));
    private static final KapuaId OTHER_SCOPE_ID = new KapuaEid(BigInteger.valueOf(20));
    private static final KapuaId DEVICE_ID = new KapuaEid(BigInteger.valueOf(30));
    private static final KapuaId OTHER_DEVICE_ID = new KapuaEid(BigInteger.valueOf(40));

    private AccountService accountService;
    private DeviceRegistryService deviceRegistryService;

    @Before
    public void setUp() {
        accountService = Mockito.mock(AccountService.class);
        deviceRegistryService = Mockito.mock(DeviceRegistryService.class);
    }

    @Test
    public void findAccountIdCachedTest() throws Exception {
        Account account = newAccount(SCOPE_ID);
        Mockito.when(accountService.findByName(ACCOUNT_NAME)).thenReturn(account);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(true);

        Assert.assertEquals(SCOPE_ID, idsCache.findAccountId(ACCOUNT_NAME));
        Assert.assertEquals(SCOPE_ID, idsCache.findAccountId(ACCOUNT_NAME));

        Mockito.verify(accountService, Mockito.times(1)).findByName(ACCOUNT_NAME);
    }

    @Test
    public void findAccountIdNotFoundNotCachedTest() throws Exception {
        Account account = newAccount(SCOPE_ID);
        Mockito.when(accountService.findByName(ACCOUNT_NAME)).thenReturn(null, account);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(true);

        Assert.assertNull(idsCache.findAccountId(ACCOUNT_NAME));
        // Created after the first lookup: must be found
        Assert.assertEquals(SCOPE_ID, idsCache.findAccountId(ACCOUNT_NAME));

        Mockito.verify(accountService, Mockito.times(2)).findByName(ACCOUNT_NAME);
    }

    @Test
    public void findAccountIdErrorNotCachedTest() throws Exception {
        Account account = newAccount(SCOPE_ID);
        Mockito.when(accountService.findByName(ACCOUNT_NAME)).thenThrow(KapuaException.internalError("test")).thenReturn(account);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(true);

        try {
            idsCache.findAccountId(ACCOUNT_NAME);
            Assert.fail("KapuaException expected");
        } catch (KapuaException e) {
            // expected
        }
        Assert.assertEquals(SCOPE_ID, idsCache.findAccountId(ACCOUNT_NAME));

        Mockito.verify(accountService, Mockito.times(2)).findByName(ACCOUNT_NAME);
    }

    @Test
    public void findAccountIdCacheDisabledTest() throws Exception {
        Account account = newAccount(SCOPE_ID);
        Mockito.when(accountService.findByName(ACCOUNT_NAME)).thenReturn(account);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(false);

        Assert.assertEquals(SCOPE_ID, idsCache.findAccountId(ACCOUNT_NAME));
        Assert.assertEquals(SCOPE_ID, idsCache.findAccountId(ACCOUNT_NAME));

        Mockito.verify(accountService, Mockito.times(2)).findByName(ACCOUNT_NAME);
    }

    @Test
    public void findDeviceIdCachedTest() throws Exception {
        Device device = newDevice(DEVICE_ID);
        Mockito.when(deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID)).thenReturn(device);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(true);

        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));
        // Equal, but not the same, scope id instance
        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(new KapuaEid(BigInteger.valueOf(10)), CLIENT_ID));

        Mockito.verify(deviceRegistryService, Mockito.times(1)).findByClientId(SCOPE_ID, CLIENT_ID);
    }

    @Test
    public void findDeviceIdSameClientIdOtherScopeTest() throws Exception {
        Device device = newDevice(DEVICE_ID);
        Device otherDevice = newDevice(OTHER_DEVICE_ID);
        Mockito.when(deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID)).thenReturn(device);
        Mockito.when(deviceRegistryService.findByClientId(OTHER_SCOPE_ID, CLIENT_ID)).thenReturn(otherDevice);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(true);

        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));
        Assert.assertEquals(OTHER_DEVICE_ID, idsCache.findDeviceId(OTHER_SCOPE_ID, CLIENT_ID));
        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));
        Assert.assertEquals(OTHER_DEVICE_ID, idsCache.findDeviceId(OTHER_SCOPE_ID, CLIENT_ID));

        Mockito.verify(deviceRegistryService, Mockito.times(1)).findByClientId(SCOPE_ID, CLIENT_ID);
        Mockito.verify(deviceRegistryService, Mockito.times(1)).findByClientId(OTHER_SCOPE_ID, CLIENT_ID);
    }

    @Test
    public void findDeviceIdNotFoundNotCachedTest() throws Exception {
        Device device = newDevice(DEVICE_ID);
        Mockito.when(deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID)).thenReturn(null, device);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(true);

        Assert.assertNull(idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));
        // Registered after the first lookup: must be found
        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));
        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));

        Mockito.verify(deviceRegistryService, Mockito.times(2)).findByClientId(SCOPE_ID, CLIENT_ID);
    }

    @Test
    public void findDeviceIdCacheDisabledTest() throws Exception {
        Device device = newDevice(DEVICE_ID);
        Mockito.when(deviceRegistryService.findByClientId(SCOPE_ID, CLIENT_ID)).thenReturn(device);
        TranslatorKuraKapuaIdsCache idsCache = newIdsCache(false);

        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));
        Assert.assertEquals(DEVICE_ID, idsCache.findDeviceId(SCOPE_ID, CLIENT_ID));

        Mockito.verify(deviceRegistryService, Mockito.times(2)).findByClientId(SCOPE_ID, CLIENT_ID);
    }

    @Test
    public void defaultCacheConfigTest() {
        TranslatorKapuaKuraSettings settings = new TranslatorKapuaKuraSettings();

        CacheConfig accountCacheConfig = settings.getCacheConfig(
                TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_ACCOUNT_LOCAL_SIZE_MAXIMUM,
                TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_ACCOUNT_LOCAL_EXPIRE_AFTER,
                TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_ACCOUNT_LOCAL_EXPIRE_STRATEGY).orElse(null);
        Assert.assertNotNull(accountCacheConfig);
        Assert.assertEquals(1000, accountCacheConfig.maxSize);
        Assert.assertEquals(3600, accountCacheConfig.expirationTimeoutSeconds);
        Assert.assertEquals(ExpiryPolicy.MODIFIED, accountCacheConfig.expirationStrategy);

        CacheConfig deviceCacheConfig = settings.getCacheConfig(
                TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_DEVICE_LOCAL_SIZE_MAXIMUM,
                TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_DEVICE_LOCAL_EXPIRE_AFTER,
                TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_DEVICE_LOCAL_EXPIRE_STRATEGY).orElse(null);
        Assert.assertNotNull(deviceCacheConfig);
        Assert.assertEquals(10000, deviceCacheConfig.maxSize);
        Assert.assertEquals(3600, deviceCacheConfig.expirationTimeoutSeconds);
        Assert.assertEquals(ExpiryPolicy.MODIFIED, deviceCacheConfig.expirationStrategy);
    }

    //
    // Private methods
    //

    private TranslatorKuraKapuaIdsCache newIdsCache(boolean cacheEnabled) {
        TranslatorKapuaKuraSettings settings = Mockito.mock(TranslatorKapuaKuraSettings.class);
        Optional<CacheConfig> cacheConfig = cacheEnabled ? Optional.of(new CacheConfig(100, 3600, ExpiryPolicy.MODIFIED)) : Optional.empty();
        Mockito.when(settings.getCacheConfig(Mockito.any(TranslatorKapuaKuraSettingKeys.class), Mockito.any(TranslatorKapuaKuraSettingKeys.class), Mockito.any(TranslatorKapuaKuraSettingKeys.class)))
                .thenReturn(cacheConfig);
        return new TranslatorKuraKapuaIdsCache(accountService, deviceRegistryService, settings);
    }

    private static Account newAccount(KapuaId accountId) {
        Account account = Mockito.mock(Account.class);
        Mockito.when(account.getId()).thenReturn(accountId);
        return account;
    }

    private static Device newDevice(KapuaId deviceId) {
        Device device = Mockito.mock(Device.class);
        Mockito.when(device.getId()).thenReturn(deviceId);
        return device;
    }
}
