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
package org.eclipse.kapua.locator.guice;

import java.util.Set;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.DisposableResource;
import org.eclipse.kapua.commons.core.LifecycleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ProviderModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(ProviderModule.class);
    
    private LocatorConfig locatorConfig;
    
    public ProviderModule(LocatorConfig locatorConfig) {
        this.locatorConfig = locatorConfig;
    }
    
    @Override
    protected void configure() {

        try {
            this.bind(LifecycleHandler.class).to(LifecycleHandlerImpl.class).in(Singleton.class);           
  
            Set<Class<?>> extendedClassInfo = locatorConfig.getProvidersInfo();
            for (Class<?> clazz : extendedClassInfo) {
                if (DisposableResource.class.isAssignableFrom(clazz)) {
                    this.bind(clazz).in(Singleton.class);
                    logger.info("Bind Kapua provider {}", clazz);
                }
            }

        } catch (Exception e) {
            logger.error("Exeption configuring module", e);
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + locatorConfig.getURL());
        }
    }
}
