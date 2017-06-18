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
import java.util.ServiceLoader;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 0.3.0
 *
 */
public abstract class ComponentLocator {

    private final static Logger logger = LoggerFactory.getLogger(ComponentLocator.class);

    private static ComponentLocator instance;
    
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
    
    public abstract <T> T getComponent(Class<T> clazz);
}