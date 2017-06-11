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
package org.eclipse.kapua.commons.locator;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.LifecycleComponent;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ComponentLocator {

    private final static Logger logger = LoggerFactory.getLogger(ComponentLocator.class);

    protected static ComponentLocator instance;
    
    static {
        instance = createInstance();
    }

    private static ComponentLocator createInstance() {

        logger.info("initialize Servicelocator with the default instance... ");
        ServiceLoader<ComponentLocator> commonsLocatorLoaders = ServiceLoader.load(ComponentLocator.class);
        ComponentLocator commonsLocator = null;
        Iterator<ComponentLocator> commonsLocatorLoaderIterator = commonsLocatorLoaders.iterator();
        while (commonsLocatorLoaderIterator.hasNext()) {
            commonsLocator = commonsLocatorLoaderIterator.next();
            break;
        }
        if (commonsLocator == null) {
            throw new KapuaRuntimeException(KapuaRuntimeErrorCodes.SERVICE_LOCATOR_UNAVAILABLE);
        }
        logger.info("Initialize Servicelocator with the default instance... DONE");
        return commonsLocator;
    }
    
    public static ComponentLocator getInstance() {
        return instance;
    }

    public abstract Set<LifecycleComponent> getServiceComponents();
    
    public abstract ManagedObjectPool getManagedObjectPool();
    
    public abstract <T> T getProvider(Class<T> superOrImplClass);

    public abstract <T> List<Class<T>> getProviders(Class<T> superOrImplClasss);
}