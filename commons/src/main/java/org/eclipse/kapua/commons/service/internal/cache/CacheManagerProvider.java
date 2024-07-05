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

import java.net.URI;
import java.net.URISyntaxException;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.setting.KapuaSettingException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class CacheManagerProvider implements Provider<CacheManager> {

    private static final String DEFAULT_CACHING_PROVIDER_CLASS_NAME = "org.eclipse.kapua.commons.service.internal.cache.dummy.CachingProvider";
    private final CommonsMetric commonsMetric;
    private final String cachingProviderClassName;
    private final long ttl;
    private final String expiryPolicy;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    public CacheManagerProvider(CommonsMetric commonsMetric, SystemSetting systemSetting) {
        this.commonsMetric = commonsMetric;
        this.cachingProviderClassName = systemSetting.getString(SystemSettingKey.CACHING_PROVIDER);
        this.ttl = systemSetting.getLong(SystemSettingKey.CACHE_TTL, 60);
        this.expiryPolicy = systemSetting.getString(SystemSettingKey.JCACHE_EXPIRY_POLICY, ExpiryPolicy.MODIFIED.name());
    }

    @Override
    public CacheManager get() {
        CachingProvider cachingProvider;
        final URI cacheConfigUri = getCacheConfig();
        try {
            if (!StringUtils.isEmpty(cachingProviderClassName)) {
                cachingProvider = Caching.getCachingProvider(cachingProviderClassName);
            } else {
                cachingProvider = Caching.getCachingProvider();
            }
            //set the default cache flag
            commonsMetric.setCacheStatus(1);
        } catch (CacheException e) {
            //set the "default cache" flag (already done by initDefualtCacheProvider)
            logger.warn("Error while loading the CachingProvider... Loading the default one ({}).", DEFAULT_CACHING_PROVIDER_CLASS_NAME);
            cachingProvider = initDefaultCacheProvider();
        }
        try {
            return cachingProvider.getCacheManager(cacheConfigUri, null);
        } catch (Exception e) {
            //anyway set the "default cache" flag (already done by initDefualtCacheProvider)
            //second fallback
            logger.warn("Error while loading the CacheManager... Switching to CachingProvider default ({}). Error: {}", DEFAULT_CACHING_PROVIDER_CLASS_NAME, e.getMessage(), e);
            cachingProvider = initDefaultCacheProvider();
            return cachingProvider.getCacheManager(cacheConfigUri, null);
        }
    }

    /**
     * Gets the URI with the cache config file path.
     *
     * @return the URI with the cache config file path
     */
    private URI getCacheConfig() {
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
                .withLogger(logger)
                .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                .withTitle("Cache Configuration")
                .addParameter("Caching provider class name", cachingProviderClassName)
                .addParameter("Default caching provider class name", DEFAULT_CACHING_PROVIDER_CLASS_NAME)
                .addParameter("TTL", ttl)
                .addParameter("Expiry Policy", expiryPolicy)
                .addParameter("Config URI", uri)
                .printLog();

        return uri;
    }

    private CachingProvider initDefaultCacheProvider() {
        //set the default cache flag
        commonsMetric.setCacheStatus(-1);
        return Caching.getCachingProvider(DEFAULT_CACHING_PROVIDER_CLASS_NAME);
    }
}
