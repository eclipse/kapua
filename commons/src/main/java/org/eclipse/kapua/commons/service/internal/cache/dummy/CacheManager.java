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
package org.eclipse.kapua.commons.service.internal.cache.dummy;

import javax.cache.configuration.Configuration;
import java.net.URI;
import java.util.Properties;

/**
 * Dummy cache manager needed to instantiate {@link Cache}
 */
public class CacheManager implements javax.cache.CacheManager {

    private static CacheManager instance;

    private CacheManager() {
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    @Override
    public CachingProvider getCachingProvider() {
        return null;
    }

    @Override
    public URI getURI() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public <K, V, C extends Configuration<K, V>> javax.cache.Cache createCache(String cacheName, C configuration) throws IllegalArgumentException {
        return new Cache<>();
    }

    @Override
    public <K, V> javax.cache.Cache getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        return null;
    }

    @Override
    public <K, V> javax.cache.Cache getCache(String cacheName) {
        return null;
    }

    @Override
    public Iterable<String> getCacheNames() {
        return null;
    }

    @Override
    public void destroyCache(String cacheName) {
    }

    @Override
    public void enableManagement(String cacheName, boolean enabled) {
    }

    @Override
    public void enableStatistics(String cacheName, boolean enabled) {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return null;
    }
}
