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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.LifecycleComponent;
import org.eclipse.kapua.commons.locator.ComponentLocator;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.inject.InjectorRegistry;
import org.eclipse.kapua.locator.inject.LocatorConfig;
import org.eclipse.kapua.locator.inject.LocatorConfigurationException;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;


public class ComponentLocatorImpl extends ComponentLocator {

    private static final Logger logger = LoggerFactory.getLogger(ComponentLocatorImpl.class);

    private static final String ROOT_INJECTOR_NAME = "rootLocatorInjector";
    private static final String COMMONS_INJECTOR_NAME = "commonsLocatorInjector";
    private static final String SERVICE_RESOURCE = "locator.xml";
    
    private Set<Class<? extends LifecycleComponent>> serviceClasses;
    private Set<LifecycleComponent> serviceComponents;
    
    public ComponentLocatorImpl() {
        
        URL locatorConfigURL = null;
        try {
            
            Injector rootInjector = Guice.createInjector(new AbstractModule() {
                
                private ManagedObjectPoolImpl managedObjectPool;

                @Override
                protected void configure() {
                    
                    managedObjectPool = new ManagedObjectPoolImpl(); 
                    bind(ManagedObjectPool.class).toInstance(managedObjectPool);           
                }
                
            });
            
            InjectorRegistry.add(ROOT_INJECTOR_NAME, rootInjector);
            
            // Find locator configuration file
            List<URL> locatorConfigurations = Arrays.asList(ResourceUtils.getResource(SERVICE_RESOURCE));
            if (locatorConfigurations.isEmpty()) {
                return;
            }
    
            // Read configurations from resource files
            locatorConfigURL = locatorConfigurations.get(0);
            LocatorConfig locatorConfig;
            locatorConfig = LocatorConfig.fromURL(locatorConfigURL);
            
            serviceClasses = locatorConfig.getAssignableTo(LifecycleComponent.class);
            serviceComponents = initServices(serviceClasses);
            
            ManagedObjectPool managedObjectPool = rootInjector.getInstance(ManagedObjectPool.class);
            Injector injector = rootInjector.createChildInjector(new ComponentsModule(managedObjectPool, locatorConfig, serviceComponents));
            InjectorRegistry.add(COMMONS_INJECTOR_NAME, injector);
            logger.info("Created injector {}", COMMONS_INJECTOR_NAME);
        } catch (LocatorConfigurationException | ClassNotFoundException | IOException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + locatorConfigURL);
        }
    }

    @Override
    public Set<LifecycleComponent> getServiceComponents() {
        return serviceComponents;
    }

    @Override
    public ManagedObjectPool getManagedObjectPool() {
        Injector injector = InjectorRegistry.get(COMMONS_INJECTOR_NAME);
        return injector.getInstance(ManagedObjectPool.class);
    }
    
    @Override
    public <T> T getProvider(Class<T> superOrImplClass) {
        
        Injector injector = InjectorRegistry.get(COMMONS_INJECTOR_NAME);
        
        Binding<T> binding = injector.getExistingBinding(Key.get(superOrImplClass));
        if (binding == null) {
            throw new RuntimeException(superOrImplClass.getName() + " has no binding.");
        }
        
        T t = injector.getInstance(superOrImplClass);
        Annotation providerAnnotation = t.getClass().getAnnotation(KapuaProvider.class);
        if (providerAnnotation != null) {
            return injector.getInstance(superOrImplClass);
        }
        
        throw new RuntimeException(t.getClass().getName() + " is not a provider.");
    }
    
    @Override
    public <T> List<Class<T>> getProviders(Class<T> superOrImplClasss) {
        
        Injector injector = InjectorRegistry.get(COMMONS_INJECTOR_NAME);

        ArrayList<Class<T>> providers = new ArrayList<Class<T>>();
        Map<Key<?>, Binding<?>> explicitBindings = injector.getBindings();
        for (Key<?> k:explicitBindings.keySet()) {
            Class<?> c = k.getTypeLiteral().getRawType();
            if (superOrImplClasss.isAssignableFrom(c) && !providers.contains(c)) {
                providers.add((Class<T>) c);
            }
        }
        
        return providers;
    }
    
    private Set<LifecycleComponent> initServices(Set<Class<? extends LifecycleComponent>> serviceClasses) {
 
        Injector rootInjector = InjectorRegistry.get(ROOT_INJECTOR_NAME);

        Set<LifecycleComponent> serviceComponents = new HashSet<LifecycleComponent>();
        for(Class<? extends LifecycleComponent> serviceClass:serviceClasses) {
            serviceComponents.add(rootInjector.getInstance(serviceClass));
        }
        return serviceComponents;
    }
}
