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
package org.eclipse.kapua.locator.guice;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Kapua locator implementation bases on Guice framework
 *
 * @since 1.0
 */
public class KapuaLocatorImpl extends KapuaLocator {

    private static final Logger logger = LoggerFactory.getLogger(KapuaLocatorImpl.class);
    
    private static final String KAPUA_INJECTOR = "kapuaInjector";
    private static final String INJECTOR_NOT_FOUND = "Injector named %s not found or null";
    private static final String SERVICE_RESOURCE = "locator.xml";

    static {
        try {
            Injector inj = InjectorRegistry.get("parentInjector");
            if ( inj == null) {
                throw new Exception("Injector not found: parentInjector");
            }
            // Find locator configuration file
            List<URL> locatorConfigurations = Arrays.asList(ResourceUtils.getResource(SERVICE_RESOURCE));
            if (!locatorConfigurations.isEmpty()) {

                // Read configurations from resource files
                URL locatorConfigURL = locatorConfigurations.get(0);
                LocatorConfig locatorConfig;
                try {
                    locatorConfig = LocatorConfig.fromURL(locatorConfigURL);
                } catch (KapuaLocatorException e) {
                    throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + locatorConfigURL);
                }
                
                Injector childInj = inj.createChildInjector(new KapuaModule(locatorConfig));
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
                throw new IllegalStateException(String.format(INJECTOR_NOT_FOUND, KAPUA_INJECTOR));
            }
            
            return injector.getInstance(serviceClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, e, serviceClass);
        }
    }

    @Override
    public <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass) {
        Injector injector = InjectorRegistry.get(KAPUA_INJECTOR);
        if (injector == null) {
            throw new IllegalStateException(String.format(INJECTOR_NOT_FOUND, KAPUA_INJECTOR));
        }
        
        F kapuaEntityFactory = injector.getInstance(factoryClass);

        if (kapuaEntityFactory == null) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, factoryClass);
        }

        return kapuaEntityFactory;
    }

    @Override
    public List<KapuaService> getServices() {
        Injector injector = InjectorRegistry.get(KAPUA_INJECTOR);
        if (injector == null) {
            throw new IllegalStateException(String.format(INJECTOR_NOT_FOUND, KAPUA_INJECTOR));
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
