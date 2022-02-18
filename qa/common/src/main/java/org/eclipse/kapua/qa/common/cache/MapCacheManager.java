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
package org.eclipse.kapua.qa.common.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

public class MapCacheManager implements CacheManager {

    private static MapCacheManager instance;

    private MapCacheManager() {

    }

    public static MapCacheManager getInstance() {
        if (instance == null) {
            synchronized (MapCacheManager.class) {
                if (instance == null) {
                    instance = new MapCacheManager();
                }
            }
        }
        return instance;
    }

    @Override
    public CachingProvider getCachingProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getURI() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName, C configuration) throws IllegalArgumentException {
        //Class<K> kClass = configuration.getKeyType();
        //Class<V> vClass = configuration.getValueType();
        return new MapCache<>();
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<String> getCacheNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroyCache(String cacheName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void enableManagement(String cacheName, boolean enabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void enableStatistics(String cacheName, boolean enabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        throw new UnsupportedOperationException();
    }
}
