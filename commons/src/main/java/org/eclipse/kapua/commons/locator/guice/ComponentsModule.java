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
import org.eclipse.kapua.commons.core.Bundle;
import org.eclipse.kapua.commons.locator.BundleProvider;
import org.eclipse.kapua.commons.locator.ComponentProvider;
import org.eclipse.kapua.locator.inject.LocatorConfig;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * @since 0.3.0
 *
 */
public class ComponentsModule extends PrivateModule {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsModule.class);
    
    private ManagedObjectPool managedObjectPool;
    private LocatorConfig locatorConfig;
    
    public ComponentsModule(ManagedObjectPool managedObjectPool, LocatorConfig locatorConfig) {
        this.managedObjectPool = managedObjectPool;
        this.locatorConfig = locatorConfig;
    }
    
    @Override
    protected void configure() {

        try {
            
            bind(BundleProvider.class).to(BundleProviderImpl.class);
            expose(BundleProvider.class);
            
//            Multibinder<Bundle> bundleBinder = Multibinder.newSetBinder(binder(), Bundle.class);
            Set<Class<?>> componentProviders = locatorConfig.getAnnotatedWith(ComponentProvider.class);
            for(Class<?> componentProvider:componentProviders) {
                if (Bundle.class.isAssignableFrom(componentProvider)) {
//                    ComponentResolver resolver = ComponentResolver.newInstance(Bundle.class, componentProvider);
//                    bundleBinder.addBinding().to(resolver.getImplementationClass());
                    continue;
                }
                
                ComponentProvider providerAnnotation = componentProvider.getAnnotation(ComponentProvider.class);
                Class<?> providedComponent  = providerAnnotation.provides();
                ComponentResolver resolver = ComponentResolver.newInstance(providedComponent, componentProvider);
                bind(resolver.getImplementationClass()).in(Singleton.class);
                bind(resolver.getProvidedClass()).to(resolver.getImplementationClass());
                expose(resolver.getProvidedClass());
            }

            // When a new insance object is created by the injector the  
            // following listener is invoked
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
