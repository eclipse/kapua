/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.service.internal.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.cache.expiry.TouchedExpiryPolicy;

import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Class responsible for managing the various caches that are instantiated. All the caches are stored in a Map, where the keys are the cache names and the value are the caches themselves.
 */
public class KapuaCacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(KapuaCacheManager.class);

    private final long ttl;
    private final String expiryPolicy;
    private final Map<String, Cache<Serializable, Serializable>> cacheMap = new ConcurrentHashMap<>();

    private final CacheManager cacheManager;
    private final CommonsMetric commonsMetric;

    @Inject
    public KapuaCacheManager(CacheManager cacheManager, CommonsMetric commonsMetric, SystemSetting systemSetting) {
        this.cacheManager = cacheManager;
        this.commonsMetric = commonsMetric;
        this.ttl = systemSetting.getLong(SystemSettingKey.CACHE_TTL, 60);
        this.expiryPolicy = systemSetting.getString(SystemSettingKey.JCACHE_EXPIRY_POLICY, ExpiryPolicy.MODIFIED.name());
    }

    /**
     * Method responsible for getting an existing cache, or instantiating a new cache if the searched one does not exists yet.
     *
     * @param cacheName
     *         the name of the cache.
     * @return the Cache object containing the desired cache.
     */
    public Cache<Serializable, Serializable> getCache(String cacheName) {
        Cache<Serializable, Serializable> cache = cacheMap.get(cacheName);
        if (cache == null) {
            synchronized (cacheMap) {
                cache = cacheMap.get(cacheName);
                if (cache == null) {
                    final Cache<Serializable, Serializable> fromManager = cacheManager.getCache(cacheName);
                    if (fromManager != null) {
                        cache = fromManager;
                        LOGGER.info("Retrived cache from manager: {}", cache);
                    } else {
                        cache = cacheManager.createCache(cacheName, initConfig());
                        commonsMetric.getRegisteredCache().inc();
                        LOGGER.info("Created cache: {} - Expiry Policy: {} - TTL: {}", cacheName, expiryPolicy, ttl);
                    }
                    cacheMap.put(cacheName, cache);
                }
            }
        }
        return cache;
    }

    private MutableConfiguration<Serializable, Serializable> initConfig() {
        Factory expiryPolicyFactory;
        if (ExpiryPolicy.TOUCHED.name().equals(expiryPolicy)) {
            expiryPolicyFactory = TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, ttl));
        } else {
            expiryPolicyFactory = ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, ttl));
        }
        MutableConfiguration<Serializable, Serializable> config = new MutableConfiguration<>();
        config.setExpiryPolicyFactory(expiryPolicyFactory);
        return config;
    }

    /**
     * Utility method to cleanup the whole cache.
     */
    public void invalidateAll() {
        cacheMap.forEach((cacheKey, cache) -> {
            cache.clear();
            commonsMetric.getRegisteredCache().dec();
        });
    }
}
