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

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.CacheConfig;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.setting.TranslatorKapuaKuraSettingKeys;
import org.eclipse.kapua.translator.setting.TranslatorKapuaKuraSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Resolves {@link Account} ids and {@link Device} ids for Kura {@link org.eclipse.kapua.translator.Translator}s, caching them locally.
 * <p>
 * Only ids are cached, since the mapping from an {@link Account} name to its id and from a {@link Device} client id to its id never changes,
 * unless the {@link Account} or the {@link Device} is deleted and recreated.
 * Entries are not invalidated in that case: the old id can be returned until the entry expires.
 * <p>
 * Not found {@link Account}s and {@link Device}s are never cached, so that newly created ones are resolved on the next lookup.
 * <p>
 * The services are only invoked when the id is not cached, so their permission checks are performed only in that case.
 *
 * @since 2.1.0
 */
public class TranslatorKuraKapuaIdsCache {

    private static final Logger LOG = LoggerFactory.getLogger(TranslatorKuraKapuaIdsCache.class);

    private final AccountService accountService;
    private final DeviceRegistryService deviceRegistryService;

    private final LocalCache<String, KapuaId> accountIdsByName;
    private final LocalCache<Pair<KapuaId, String>, KapuaId> deviceIdsByClientId;

    /**
     * Constructor.
     *
     * @param accountService              The {@link AccountService}.
     * @param deviceRegistryService       The {@link DeviceRegistryService}.
     * @param translatorKapuaKuraSettings The {@link TranslatorKapuaKuraSettings}.
     * @since 2.1.0
     */
    @Inject
    public TranslatorKuraKapuaIdsCache(AccountService accountService, DeviceRegistryService deviceRegistryService, TranslatorKapuaKuraSettings translatorKapuaKuraSettings) {
        this.accountService = accountService;
        this.deviceRegistryService = deviceRegistryService;

        this.accountIdsByName = newLocalCache("account ids",
                translatorKapuaKuraSettings.getCacheConfig(
                        TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_ACCOUNT_LOCAL_SIZE_MAXIMUM,
                        TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_ACCOUNT_LOCAL_EXPIRE_AFTER,
                        TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_ACCOUNT_LOCAL_EXPIRE_STRATEGY));
        this.deviceIdsByClientId = newLocalCache("device ids",
                translatorKapuaKuraSettings.getCacheConfig(
                        TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_DEVICE_LOCAL_SIZE_MAXIMUM,
                        TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_DEVICE_LOCAL_EXPIRE_AFTER,
                        TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_DEVICE_LOCAL_EXPIRE_STRATEGY));
    }

    /**
     * Gets the id of the {@link Account} with the given name.
     *
     * @param accountName The {@link Account#getName()}.
     * @return The {@link Account#getId()}, or {@code null} if no {@link Account} with the given name exists.
     * @throws KapuaException If {@link AccountService#findByName(String)} fails.
     * @since 2.1.0
     */
    public KapuaId findAccountId(String accountName) throws KapuaException {
        KapuaId accountId = accountIdsByName != null ? accountIdsByName.get(accountName) : null;
        if (accountId == null) {
            Account account = accountService.findByName(accountName);
            if (account == null) {
                return null;
            }

            accountId = account.getId();
            if (accountIdsByName != null) {
                accountIdsByName.put(accountName, accountId);
            }
        }
        return accountId;
    }

    /**
     * Gets the id of the {@link Device} with the given client id in the given scope.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param clientId The {@link Device#getClientId()}.
     * @return The {@link Device#getId()}, or {@code null} if no {@link Device} with the given client id exists in the given scope.
     * @throws KapuaException If {@link DeviceRegistryService#findByClientId(KapuaId, String)} fails.
     * @since 2.1.0
     */
    public KapuaId findDeviceId(KapuaId scopeId, String clientId) throws KapuaException {
        Pair<KapuaId, String> deviceKey = deviceIdsByClientId != null ? Pair.of(scopeId, clientId) : null;
        KapuaId deviceId = deviceKey != null ? deviceIdsByClientId.get(deviceKey) : null;
        if (deviceId == null) {
            Device device = deviceRegistryService.findByClientId(scopeId, clientId);
            if (device == null) {
                return null;
            }

            deviceId = device.getId();
            if (deviceKey != null) {
                deviceIdsByClientId.put(deviceKey, deviceId);
            }
        }
        return deviceId;
    }

    //
    // Private methods
    //

    private static <K> LocalCache<K, KapuaId> newLocalCache(String cacheName, Optional<CacheConfig> cacheConfig) {
        if (cacheConfig.isPresent()) {
            LOG.info("Config for {} cache: max size {}, expire time {}s with policy {}",
                    cacheName, cacheConfig.get().maxSize, cacheConfig.get().expirationTimeoutSeconds, cacheConfig.get().expirationStrategy);
            return new LocalCache<>(cacheConfig.get(), null);
        }

        LOG.info("Config for {} cache: disabled", cacheName);
        return null;
    }
}
