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
 *
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import com.google.inject.*;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Kapua locator implementation bases on Guice framework
 * 
 * @since 1.0
 * 
 */
public class GuiceLocatorImpl extends KapuaLocator {

    private static final Logger logger = LoggerFactory.getLogger(GuiceLocatorImpl.class);

    private static final Injector injector;

    static {
        try {
            injector = Guice.createInjector(new KapuaModule());
        } catch (Throwable e) {
            logger.error("Cannot instantiate injector {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public <S extends KapuaService> S getService(Class<S> serviceClass) {
        try {
            return injector.getInstance(serviceClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, serviceClass);
        }
    }

    @Override
    public <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass) {
        F kapuaEntityFactory = injector.getInstance(factoryClass);

        if (kapuaEntityFactory == null) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, factoryClass);
        }

        return kapuaEntityFactory;
    }

    @Override
    public List<KapuaService> getServices() {
        List<KapuaService> servicesList = new ArrayList<>();
        Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Binding binding : bindings.values()) {
            if (KapuaService.class.isAssignableFrom(binding.getKey().getTypeLiteral().getRawType())) {
                servicesList.add(injector.getInstance((Class<KapuaService>)binding.getKey().getTypeLiteral().getRawType()));
            }
        }
        return servicesList;
    }

}
