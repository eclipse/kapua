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
import org.eclipse.kapua.commons.core.LifecycleComponent;
import org.eclipse.kapua.commons.core.ServiceRegistration;
import org.eclipse.kapua.locator.inject.LocatorConfig;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class ComponentsModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsModule.class);
    
    private ManagedObjectPool managedObjectPool;
    private LocatorConfig locatorConfig;
    private Set<LifecycleComponent> serviceComponents;
    
    public ComponentsModule(ManagedObjectPool managedObjectPool, LocatorConfig locatorConfig, Set<LifecycleComponent> serviceComponents) {
        this.managedObjectPool = managedObjectPool;
        this.locatorConfig = locatorConfig;
        this.serviceComponents = serviceComponents;
    }
    
    @Override
    protected void configure() {

        try {
            
            for(LifecycleComponent serviceComponent:serviceComponents) {
                serviceComponent.onRegisterServices(new ServiceRegistration() {

                    @Override
                    public <I, P extends I> void register(Class<I> clazz, P instance) {
                        bind(clazz).toInstance(instance);
                    }

                    @Override
                    public <P> void register(Class<P> provider) {
                    }

                    @Override
                    public <P> void register(P instance) {
                    }
                    
                });
            }
            
            // When an implementation of a service is created the listener is invoked
            this.bindListener(Matchers.any(), new TypeListener() {
                
                @Override
                public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                    typeEncounter.register(new InjectionListener<I>() {

                        @Override
                        public void afterInjection(Object i) {
                            managedObjectPool.add(i);
                        }
                    });
                }
            });

        } catch (Exception e) {
            logger.error("Exception configuring module", e);
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + locatorConfig.getURL());
        }
    }
}
