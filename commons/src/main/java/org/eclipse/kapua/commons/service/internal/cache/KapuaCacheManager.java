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

import com.codahale.metrics.Counter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.setting.KapuaSettingException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
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

/**
 * Class responsible for managing the various caches that are instantiated.
 * All the caches are stored in a Map, where the keys are the cache names and the value are the caches themselves.
 */
public class KapuaCacheManager {

    enum ExpiryPolicy {
        MODIFIED,
        TOUCHED
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(KapuaCacheManager.class);

    private static final String MODULE_NAME = "cache";
    private static final String COMPONENT_NAME = "manager";

    private static final SystemSetting SYSTEM_SETTING = SystemSetting.getInstance();
    private static final String CACHING_PROVIDER_CLASS_NAME = SYSTEM_SETTING.getString(SystemSettingKey.CACHING_PROVIDER);
    private static final String DEFAULT_CACHING_PROVIDER_CLASS_NAME = "org.eclipse.kapua.commons.service.internal.cache.dummy.CachingProvider";
    private static final long TTL = SYSTEM_SETTING.getLong(SystemSettingKey.CACHE_TTL, 60);
    private static final String EXPIRY_POLICY = SYSTEM_SETTING.getString(SystemSettingKey.JCACHE_EXPIRY_POLICY, ExpiryPolicy.MODIFIED.name());
    private static final Map<String, Cache<Serializable, Serializable>> CACHE_MAP = new ConcurrentHashMap<>();
    private static final URI CACHE_CONFIG_URI = getCacheConfig();

    private static CacheManager cacheManager;
    private static Integer cacheStatus = new Integer(0);
    private static Counter registeredCache;

    static {
        try {
            MetricServiceFactory.getInstance().registerGauge(() -> cacheStatus, MODULE_NAME, COMPONENT_NAME, "cache_status");
            registeredCache = MetricServiceFactory.getInstance().getCounter(MODULE_NAME, COMPONENT_NAME, "available_cache_count");
        } catch (KapuaException e) {
            LOGGER.error("Error registering cache status metrics! Error: {}", e.getMessage(), e);
        }
    }

    private KapuaCacheManager() {
    }

    /**
     * Gets the URI with the cache config file path.
     *
     * @return the URI with the cache config file path
     */
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

        // Print configuration
        ConfigurationPrinter
                .create()
                .withLogger(LOGGER)
                .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                .withTitle("Cache Configuration")
                .addParameter("Caching provider class name", CACHING_PROVIDER_CLASS_NAME)
                .addParameter("Default caching provider class name", DEFAULT_CACHING_PROVIDER_CLASS_NAME)
                .addParameter("TTL", TTL)
                .addParameter("Expiry Policy", EXPIRY_POLICY)
                .addParameter("Config URI", uri)
                .printLog();

        return uri;
    }

    /**
     * Method responsible for getting an existing cache, or instantiating a new cache if the searched one does not exists yet.
     *
     * @param cacheName the name of the cache.
     * @return the Cache object containing the desired cache.
     */
    public static Cache<Serializable, Serializable> getCache(String cacheName) {
        Cache<Serializable, Serializable> cache = CACHE_MAP.get(cacheName);
        if (cache == null) {
            synchronized (CACHE_MAP) {
                cache = CACHE_MAP.get(cacheName);
                if (cache == null) {
                    checkCacheManager();
                    cache = cacheManager.createCache(cacheName, initConfig());
                    CACHE_MAP.put(cacheName, cache);
                    registeredCache.inc();
                    LOGGER.info("Created cache: {} - Expiry Policy: {} - TTL: {}", cacheName, EXPIRY_POLICY, TTL);
                }
            }
        }
        return cache;
    }


    private static void checkCacheManager() {
        //called by synchronized section so no concurrency issues can arise
        if (cacheManager == null) {
            CachingProvider cachingProvider;
            try {
                if (!StringUtils.isEmpty(CACHING_PROVIDER_CLASS_NAME)) {
                    cachingProvider = Caching.getCachingProvider(CACHING_PROVIDER_CLASS_NAME);
                } else {
                    cachingProvider = Caching.getCachingProvider();
                }
                //set the default cache flag
                cacheStatus = 1;
            } catch (CacheException e) {
                //set the "default cache" flag (already done by initDefualtCacheProvider)
                LOGGER.warn("Error while loading the CachingProvider... Loading the default one ({}).", DEFAULT_CACHING_PROVIDER_CLASS_NAME);
                cachingProvider = initDefualtCacheProvider();
            }
            try {
                cacheManager = cachingProvider.getCacheManager(CACHE_CONFIG_URI, null);
            } catch (Exception e) {
                //anyway set the "default cache" flag (already done by initDefualtCacheProvider)
                //second fallback
                LOGGER.warn("Error while loading the CacheManager... Switching to CachingProvider default ({}). Error: {}", DEFAULT_CACHING_PROVIDER_CLASS_NAME, e.getMessage(), e);
                cachingProvider = initDefualtCacheProvider();
                cacheManager = cachingProvider.getCacheManager(CACHE_CONFIG_URI, null);
            }
        }
    }

    private static CachingProvider initDefualtCacheProvider() {
        //set the default cache flag
        cacheStatus = -1;
        return Caching.getCachingProvider(DEFAULT_CACHING_PROVIDER_CLASS_NAME);
    }

    private static MutableConfiguration<Serializable, Serializable> initConfig() {
        Factory expiryPolicyFactory;
        if (ExpiryPolicy.TOUCHED.name().equals(EXPIRY_POLICY)) {
            expiryPolicyFactory = TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, TTL));
        } else {
            expiryPolicyFactory = ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, TTL));
        }
        MutableConfiguration<Serializable, Serializable> config = new MutableConfiguration<>();
        config.setExpiryPolicyFactory(expiryPolicyFactory);
        return config;
    }

    /**
     * Utility method to cleanup the whole cache.
     */
    public static void invalidateAll() {
        CACHE_MAP.forEach((cacheKey, cache) -> {
            cache.clear();
            registeredCache.dec();
        });
    }

}
