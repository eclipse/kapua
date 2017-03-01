/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *
 *******************************************************************************/
package org.eclipse.kapua.test;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Locator service implementation used for mocking Kapua services and Kapua object
 * factories. Mocking framework such as Mockito can be used to create mocked service
 * which is than injected into this locator using setters.
 * Mocked services can be set multiple times, for each test case individually. This
 * locator implementation should be used only for testing purposes.
 * 
 * Locator can be configured in maven pom file or command line as system parameter:
 * 
 * -Dlocator.class.impl=org.eclipse.kapua.test.MockedLocator
 *
 */
public class MockedLocator extends KapuaLocator {

    private static Logger logger = LoggerFactory.getLogger(MockedLocator.class);

    private Map<Class<?>, KapuaService> serviceMap = new HashMap<>();

    private Map<Class<?>, KapuaObjectFactory> factoryMap = new HashMap<>();

    public void setMockedService(Class<?> clazz, KapuaService service) {

        serviceMap.put(clazz, service);
    }

    public void setMockedFactory(Class<?> clazz, KapuaObjectFactory factory) {

        factoryMap.put(clazz, factory);
    }

    @SuppressWarnings("unchecked")
    private <S extends KapuaService> S getMockedService(Class<S> clazz) {

        return (S) serviceMap.get(clazz);
    }

    @SuppressWarnings("unchecked")
    private <F extends KapuaObjectFactory> F getMockedFactory(Class<F> clazz) {

        return (F) factoryMap.get(clazz);
    }

    @Override
    public <S extends KapuaService> S getService(Class<S> serviceClass) {

        logger.info("Geting mocked service {} from MockedLocator", serviceClass.getName());

        return (S) getMockedService(serviceClass);
    }

    @Override
    public <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass) {

        logger.info("Geting mocked factory {} from MockedLocator", factoryClass.getName());

        return (F) getMockedFactory(factoryClass);
    }

    @Override public List<KapuaService> getServices() {
        return new ArrayList<>(serviceMap.values());
    }

}
