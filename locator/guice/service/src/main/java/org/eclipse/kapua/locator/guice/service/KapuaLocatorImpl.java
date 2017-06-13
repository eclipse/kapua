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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.guice.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.guice.inject.InjectorRegistry;
import org.eclipse.kapua.locator.inject.LocatorConfig;
import org.eclipse.kapua.locator.inject.LocatorConfigurationException;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.eclipse.kapua.locator.inject.PoolListener;
import org.eclipse.kapua.locator.inject.ResourceUtils;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;

/**
 * Kapua locator implementation bases on Guice framework
 *
 * @since 1.0
 */
public class KapuaLocatorImpl extends KapuaLocator {

    private static final Logger logger = LoggerFactory.getLogger(KapuaLocatorImpl.class);
    
    private static final String COMMONS_INJECTOR = "commonsLocatorInjector";
    private static final String KAPUA_INJECTOR = "kapuaLocatorInjector";
    private static final String INJECTOR_NOT_FOUND = "Injector named %s not found or null";
    private static final String SERVICE_RESOURCE = "locator.xml";

    public KapuaLocatorImpl() {
        this(SERVICE_RESOURCE);
    }
    
    public KapuaLocatorImpl(String resource) {
        try {
            Injector parentInj = InjectorRegistry.get(COMMONS_INJECTOR);
            if ( parentInj == null) {
                throw new Exception("Injector not found: " + COMMONS_INJECTOR);
            }
            // Find locator configuration file
            List<URL> locatorConfigurations = Arrays.asList(ResourceUtils.getResource(resource));
            if (!locatorConfigurations.isEmpty()) {

                // Read configurations from resource files
                URL locatorConfigURL = locatorConfigurations.get(0);
                LocatorConfig locatorConfig;
                try {
                    locatorConfig = LocatorConfig.fromURL(locatorConfigURL);
                } catch (LocatorConfigurationException e) {
                    throw new KapuaRuntimeException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, e, "Cannot load from URL " + locatorConfigURL);
                }
                
                // Add a listener to be notified when the injector creates a new object instance
                // The listener will be notified when a new service instance get created
                final ManagedObjectPool serviceInstances = parentInj.getInstance(ManagedObjectPool.class);
                KapuaModule module = new KapuaModule(locatorConfig);
                module.setInjectorListener(new PoolListener() {

                    @Override
                    public void onObjectAdded(Object object) {
                        // Add the new object to the instance handler
                        serviceInstances.add(object);
                    }
                    
                });
                
                Injector childInj = parentInj.createChildInjector(module);
                InjectorRegistry.add(KAPUA_INJECTOR, childInj);
            }
        }
        catch (Throwable e) {
            logger.error("Cannot instantiate {} {}", KapuaLocatorImpl.class.getName(), e.getMessage(), e);
        }
    }

    @Override
    public <S extends KapuaService> S getService(Class<S> serviceClass) {
        try {
            Injector injector = InjectorRegistry.get(KAPUA_INJECTOR);
            if (injector == null) {
                String reason = String.format(INJECTOR_NOT_FOUND, KAPUA_INJECTOR);
                throw new KapuaRuntimeException(KapuaLocatorErrorCodes.INVALID_CONTEXT, reason);
            }
            return injector.getInstance(serviceClass);
        } catch (ConfigurationException | ProvisionException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, e, serviceClass);
        }
    }

    @Override
    public <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass) {
        try {
            Injector injector = InjectorRegistry.get(KAPUA_INJECTOR);
            if (injector == null) {
                String reason = String.format(INJECTOR_NOT_FOUND, KAPUA_INJECTOR);
                throw new KapuaRuntimeException(KapuaLocatorErrorCodes.INVALID_CONTEXT, reason);
            }
            return injector.getInstance(factoryClass);
        } catch (ConfigurationException | ProvisionException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.FACTORY_UNAVAILABLE, e, factoryClass);
        }
    }

    @Override
    public List<KapuaService> getServices() {
        Injector injector = InjectorRegistry.get(KAPUA_INJECTOR);
        if (injector == null) {
            String reason = String.format(INJECTOR_NOT_FOUND, KAPUA_INJECTOR);
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.INVALID_CONTEXT, reason);
        }
        final List<KapuaService> servicesList = new ArrayList<>();
        final Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Binding<?> binding : bindings.values()) {
            final Class<?> clazz = binding.getKey().getTypeLiteral().getRawType();
            if (KapuaService.class.isAssignableFrom(clazz)) {
                KapuaService serviceInstance = injector.getInstance(clazz.asSubclass(KapuaService.class));
                if (!servicesList.contains(serviceInstance)) {
                    servicesList.add(serviceInstance);
                }
            }
        }
        return servicesList;
    }

}
