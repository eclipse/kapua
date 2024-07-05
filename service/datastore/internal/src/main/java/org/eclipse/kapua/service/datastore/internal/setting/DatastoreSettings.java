/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal.setting;

import java.util.EnumSet;
import java.util.Optional;

import org.eclipse.kapua.commons.cache.CacheConfig;
import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Datastore {@link AbstractKapuaSetting}.
 *
 * @since 1.0.0
 */
public class DatastoreSettings extends AbstractKapuaSetting<DatastoreSettingsKey> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Resource file from which source properties.
     *
     * @since 1.0.0
     */
    private static final String DATASTORE_CONFIG_RESOURCE = "kapua-datastore-settings.properties";

    /**
     * Singleton instance of this {@link Class}.
     *
     * @since 1.0.0
     */
    private static final DatastoreSettings INSTANCE = new DatastoreSettings();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link DatastoreSettings#DATASTORE_CONFIG_RESOURCE} value.
     *
     * @since 1.0.0
     */
    private DatastoreSettings() {
        super(DATASTORE_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link DatastoreSettings}.
     *
     * @return A singleton instance of {@link DatastoreSettings}.
     * @since 1.0.0
     */
    public static DatastoreSettings getInstance() {
        return INSTANCE;
    }

    private CacheConfig getCacheConfig(
            String cacheName,
            DatastoreSettingsKey specificCacheExpireKey,
            DatastoreSettingsKey specificCacheExpireStrategy,
            DatastoreSettingsKey specificCacheMaxSizeKey) {
        int defaultExpireAfter = this.getInt(DatastoreSettingsKey.CONFIG_CACHE_LOCAL_EXPIRE_AFTER);
        int defaultSizeMax = this.getInt(DatastoreSettingsKey.CONFIG_CACHE_LOCAL_SIZE_MAXIMUM);

        final Optional<Integer> specificCacheExpireAfter = this.getInteger(specificCacheExpireKey);
        final ExpiryPolicy expirationStrategy = Optional.ofNullable(this.getString(specificCacheExpireStrategy))
                .flatMap(v -> EnumSet.allOf(ExpiryPolicy.class)
                        .stream()
                        .filter(e -> e.name().toUpperCase().equals(v.toUpperCase()))
                        .findFirst())
                .orElse(ExpiryPolicy.MODIFIED);
        final Optional<Integer> specificCacheMaxSize = this.getInteger(specificCacheMaxSizeKey);
        final Integer maxSizeFinal = specificCacheMaxSize.orElse(defaultSizeMax);
        final Integer cacheExpireFinal = specificCacheExpireAfter.orElse(defaultExpireAfter);
        logger.info("Config for {} cache: max size {}, expire time {}s with policy {}", cacheName, maxSizeFinal, cacheExpireFinal, expirationStrategy);
        return new CacheConfig(maxSizeFinal, cacheExpireFinal, expirationStrategy);
    }

    public CacheConfig getClientCacheConfig() {
        return getCacheConfig("clients",
                DatastoreSettingsKey.CONFIG_CLIENTS_CACHE_LOCAL_EXPIRE_AFTER,
                DatastoreSettingsKey.CONFIG_CLIENTS_CACHE_LOCAL_EXPIRE_STRATEGY,
                DatastoreSettingsKey.CONFIG_CLIENTS_CACHE_LOCAL_SIZE_MAXIMUM
        );
    }

    public CacheConfig getChannelsCacheConfig() {
        return getCacheConfig("channels",
                DatastoreSettingsKey.CONFIG_CHANNELS_CACHE_LOCAL_EXPIRE_AFTER,
                DatastoreSettingsKey.CONFIG_CHANNELS_CACHE_LOCAL_EXPIRE_STRATEGY,
                DatastoreSettingsKey.CONFIG_CHANNELS_CACHE_LOCAL_SIZE_MAXIMUM
        );
    }

    public CacheConfig getMetricsCacheConfig() {
        return getCacheConfig("metrics",
                DatastoreSettingsKey.CONFIG_METRICS_CACHE_LOCAL_EXPIRE_AFTER,
                DatastoreSettingsKey.CONFIG_METRICS_CACHE_LOCAL_EXPIRE_STRATEGY,
                DatastoreSettingsKey.CONFIG_METRICS_CACHE_LOCAL_SIZE_MAXIMUM
        );
    }
}
