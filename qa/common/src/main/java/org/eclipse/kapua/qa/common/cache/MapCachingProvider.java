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
package org.eclipse.kapua.qa.common.cache;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

public class MapCachingProvider implements CachingProvider {

    private static MapCachingProvider instance;

    public static MapCachingProvider getInstance() {
        if (instance == null) {
            synchronized (MapCachingProvider.class) {
                if (instance == null) {
                    instance = new MapCachingProvider();
                }
            }
        }
        return instance;
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        return getCacheManager();
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getDefaultURI() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getDefaultProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager();
    }

    @Override
    public CacheManager getCacheManager() {
        return MapCacheManager.getInstance();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close(ClassLoader classLoader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        throw new UnsupportedOperationException();
    }
}
