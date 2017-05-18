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
package org.eclipse.kapua.commons.locator.guice;

import java.util.Set;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.DisposableResource;
import org.eclipse.kapua.commons.core.LifecycleHandler;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.inject.LocatorConfig;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class CommonsModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(CommonsModule.class);
    
    private LocatorConfig locatorConfig;
    
    public CommonsModule(LocatorConfig locatorConfig) {
        this.locatorConfig = locatorConfig;
    }
    
    @Override
    protected void configure() {

        try {
            bind(LifecycleHandler.class).to(LifecycleHandlerImpl.class).in(Singleton.class);           
            
            bind(ManagedObjectPool.class).to(ManagedObjectPoolImpl.class).in(Singleton.class);           
            
            Set<Class<?>> extendedClassInfo = locatorConfig.getAnnotatedWith(KapuaProvider.class);
            for (Class<?> clazz : extendedClassInfo) {
                if (DisposableResource.class.isAssignableFrom(clazz)) {
                    bind(clazz).in(Singleton.class);
                    logger.info("Bind Kapua provider {}", clazz);
                }
            }

        } catch (Exception e) {
            logger.error("Exeption configuring module", e);
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + locatorConfig.getURL());
        }
    }
}
