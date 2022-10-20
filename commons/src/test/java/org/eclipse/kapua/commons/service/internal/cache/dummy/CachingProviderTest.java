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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.cache.configuration.OptionalFeature;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;


@Category(JUnitTests.class)
public class CachingProviderTest {

    @Test
    public void getCacheManagerWithPropertiesTest() throws URISyntaxException {
        CachingProvider cachingProvider = new CachingProvider();
        Properties properties = new Properties();
        properties.setProperty("Key", "value");
        URI uri = new URI("String");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Assert.assertEquals(cachingProvider.getCacheManager(), cachingProvider.getCacheManager(uri, classloader, properties));
    }

    @Test
    public void getDefaultClassLoaderTest() {
        CachingProvider cachingProvider = new CachingProvider();
        Assert.assertNull("Null expected", cachingProvider.getDefaultClassLoader());
    }

    @Test
    public void getDefaultURITest() {
        CachingProvider cachingProvider = new CachingProvider();
        Assert.assertNull("Null expected", cachingProvider.getDefaultURI());
    }

    @Test
    public void getDefaultPropertiesTest() {
        CachingProvider cachingProvider = new CachingProvider();
        Assert.assertNull("Null expected", cachingProvider.getDefaultProperties());
    }

    @Test
    public void getCacheManagerUriClassLoaderTest() throws URISyntaxException {
        CachingProvider cachingProvider = new CachingProvider();
        URI uri = new URI("String");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Assert.assertEquals(cachingProvider.getCacheManager(), cachingProvider.getCacheManager(uri, classloader));
    }

    @Test
    public void getCacheManagerTest() {
        CachingProvider cachingProvider = new CachingProvider();
        Assert.assertEquals(CacheManager.getInstance(), cachingProvider.getCacheManager());
    }

    @Test
    public void closeTest() {
        CachingProvider cachingProvider = new CachingProvider();
        cachingProvider.close();
    }

    @Test
    public void closeClassLoaderTest() {
        CachingProvider cachingProvider = new CachingProvider();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        cachingProvider.close(classloader);
    }

    @Test
    public void closeURIClassLoaderTest() throws URISyntaxException {
        CachingProvider cachingProvider = new CachingProvider();
        URI uri = new URI("String");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        cachingProvider.close(uri, classloader);
    }

    @Test
    public void isSupportedTest() {
        CachingProvider cachingProvider = new CachingProvider();
        Assert.assertFalse(cachingProvider.isSupported(OptionalFeature.STORE_BY_REFERENCE));
    }
} 
