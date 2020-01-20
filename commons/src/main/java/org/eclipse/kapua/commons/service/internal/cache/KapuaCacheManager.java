/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.internal.cache;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.setting.KapuaSettingException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.Caching;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.cache.expiry.TouchedExpiryPolicy;
import javax.cache.spi.CachingProvider;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class KapuaCacheManager {

    enum ExpiryPolicy {
        MODIFIED,
        TOUCHED
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(KapuaCacheManager.class);

    private static final SystemSetting SYSTEM_SETTING = SystemSetting.getInstance();
    private static final String CACHING_PROVIDER_CLASS_NAME = SYSTEM_SETTING.getString(SystemSettingKey.CACHING_PROVIDER);
    private static final String DEFAULT_CACHING_PROVIDER_CLASS_NAME = "org.eclipse.kapua.commons.service.internal.cache.dummy.CachingProvider";
    private static final long TTL = SYSTEM_SETTING.getLong(SystemSettingKey.CACHE_TTL, 60);
    private static final String EXPIRY_POLICY = SYSTEM_SETTING.getString(SystemSettingKey.JCACHE_EXPIRY_POLICY, ExpiryPolicy.MODIFIED.name());
    private static final Map<String, Cache<Serializable, Serializable>> CACHE_MAP = new ConcurrentHashMap<>();
    private static final URI CACHE_CONFIG_URI = getCacheConfig();

    private KapuaCacheManager() {
    }

    private static URI getCacheConfig() {
        String configurationFileName = SystemSetting.getInstance().getString(SystemSettingKey.CACHE_CONFIG_URL);
        URI uri = null;
        if (configurationFileName != null) {
            try {
                uri = KapuaFileUtils.getAsURL(configurationFileName).toURI();
            } catch (KapuaSettingException | URISyntaxException e) {
                throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, String.format("Unable to load cache config file (%s)", configurationFileName));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cache configuration:\n\tCaching provider class name: ").append(CACHING_PROVIDER_CLASS_NAME);
        stringBuilder.append("\n\tDefault caching provider class name: ").append(DEFAULT_CACHING_PROVIDER_CLASS_NAME);
        stringBuilder.append("\n\tTTL: ").append(TTL);
        stringBuilder.append("\n\tExpiry Policy: ").append(EXPIRY_POLICY);
        stringBuilder.append("\n\tCache config URI: ").append(uri);
        LOGGER.info("{}", stringBuilder);
        return uri;
    }

    public static Cache<Serializable, Serializable> getCache(String cacheName) {
        Cache<Serializable, Serializable> cache = CACHE_MAP.get(cacheName);
        if (cache == null) {
            synchronized (CACHE_MAP) {
                cache = CACHE_MAP.get(cacheName);
                if (cache == null) {
                    Factory expiryPolicyFactory;
                    if (ExpiryPolicy.TOUCHED.name().equals(EXPIRY_POLICY)) {
                        expiryPolicyFactory = TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, TTL));
                    } else {
                        expiryPolicyFactory = ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, TTL));
                    }
                    MutableConfiguration<Serializable, Serializable> config = new MutableConfiguration<>();
                    config.setExpiryPolicyFactory(expiryPolicyFactory);
                    CachingProvider cachingProvider;
                    if (CACHING_PROVIDER_CLASS_NAME != null && CACHING_PROVIDER_CLASS_NAME.trim().length() > 0) {
                        cachingProvider = Caching.getCachingProvider(CACHING_PROVIDER_CLASS_NAME);
                    } else {
                        try {
                            cachingProvider = Caching.getCachingProvider();
                        } catch (CacheException e) {
                            LOGGER.warn("Error while loading the CachingProvider... Loading the default one ({}).", DEFAULT_CACHING_PROVIDER_CLASS_NAME);
                            cachingProvider = Caching.getCachingProvider(DEFAULT_CACHING_PROVIDER_CLASS_NAME);
                        }
                    }
                    cache = cachingProvider.getCacheManager(CACHE_CONFIG_URI, null).createCache(cacheName, config);
                    CACHE_MAP.put(cacheName, cache);
                    LOGGER.info("Created cache: {} - Expiry Policy: {} - TTL: {}", cacheName, EXPIRY_POLICY, TTL);
                }
            }
        }
        return cache;
    }

    /**
     * Utility method to cleanup the whole cache.
     */
    public static void invalidateAll() {
        CACHE_MAP.forEach((cacheKey, cache) -> cache.clear());
    }

    // TODO: create an invalidateByAccount?

}
