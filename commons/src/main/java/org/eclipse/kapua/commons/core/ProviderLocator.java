/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProviderLocator {

    private final static Logger logger = LoggerFactory.getLogger(ProviderLocator.class);

    protected static ProviderLocator instance;
    
    static {
        instance = createInstance();
    }

    private static ProviderLocator createInstance() {

        logger.info("initialize Servicelocator with the default instance... ");
        ServiceLoader<ProviderLocator> providerLocatorLoaders = ServiceLoader.load(ProviderLocator.class);
        ProviderLocator providerLocator = null;
        Iterator<ProviderLocator> providerLocatorLoaderIterator = providerLocatorLoaders.iterator();
        while (providerLocatorLoaderIterator.hasNext()) {
            providerLocator = providerLocatorLoaderIterator.next();
            break;
        }
        if (providerLocator == null) {
            throw new KapuaRuntimeException(KapuaRuntimeErrorCodes.SERVICE_LOCATOR_UNAVAILABLE);
        }
        logger.info("initialize Servicelocator with the default instance... DONE");
        return providerLocator;
    }
    
    public static ProviderLocator getInstance() {
        return instance;
    }

    public abstract LifecycleHandler getLifecycleHandler();
    
    public abstract <T> T getProvider(Class<T> superOrImplClass);

    public abstract <T> List<Class<T>> getProviders(Class<T> superOrImplClasss);
}