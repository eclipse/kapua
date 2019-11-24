/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.kapua.service.commons.PropertyMapper;
import org.eclipse.kapua.service.commons.Service;
import org.eclipse.kapua.service.commons.ServiceBuilder;
import org.eclipse.kapua.service.commons.ServiceBuilderFactory;
import org.eclipse.kapua.service.commons.ServiceBuilderFactoryRegistry;
import org.eclipse.kapua.service.commons.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;

import io.vertx.core.Vertx;

public class ServiceBuilderManager implements ServiceBuilderFactoryRegistry {

    private Map<String, ServiceBuilderFactory<?, ? extends Service>> registry = new HashMap<>();
    private PropertyMapper mapper;

    @Autowired
    public void setPropertyMapper(PropertyMapper aMapper) {
        Objects.requireNonNull(aMapper, "param: aMapper");
        mapper = aMapper;
    }

    @Override
    public void registerFactory(String aName, ServiceBuilderFactory<?, ? extends Service> aFactory) {
        Objects.requireNonNull(aName, "param: aName");
        Objects.requireNonNull(aFactory, "param: aFactory");
        if (!registry.containsKey(aName)) {
            registry.put(aName, aFactory);
            return;
        }
        throw new IllegalArgumentException(String.format("A factory with name \"%s\" is already registered.", aName));
    }

    /**
     * Returns the factory to which the name was associated or null if the registry contained no mapping for the name
     */
    @Override
    public ServiceBuilderFactory<?, ? extends Service> deregisterFactory(String aName) {
        Objects.requireNonNull(aName, "param: aName");
        ServiceBuilderFactory<?, ? extends Service> factory = registry.get(aName);
        registry.remove(aName);
        return factory;
    }

    public ServiceBuilder<?, ? extends Service> create(Vertx aVertx, ServiceConfig aConfig) {
        return registry.get(aConfig.getType()).create(aVertx, aConfig, mapper);
    }
}
