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

import javax.cache.configuration.OptionalFeature;
import java.net.URI;
import java.util.Properties;

/**
 * Dummy caching provider needed to instantiate {@link Cache}
 */
public class CachingProvider implements javax.cache.spi.CachingProvider {
    @Override
    public javax.cache.CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        return getCacheManager();
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return null;
    }

    @Override
    public URI getDefaultURI() {
        return null;
    }

    @Override
    public Properties getDefaultProperties() {
        return null;
    }

    @Override
    public javax.cache.CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager();
    }

    @Override
    public javax.cache.CacheManager getCacheManager() {
        return CacheManager.getInstance();
    }

    @Override
    public void close() {
    }

    @Override
    public void close(ClassLoader classLoader) {
    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }
}
