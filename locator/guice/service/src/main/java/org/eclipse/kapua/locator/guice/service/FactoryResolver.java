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

import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.model.KapuaObjectFactory;

public class FactoryResolver<F extends KapuaObjectFactory, I extends F> {

    private final Class<F> factoryClass;
    private final Class<I> implementationClass;

    private FactoryResolver(Class<F> factory, Class<I> implementation) {
        this.factoryClass = factory;
        this.implementationClass = implementation;
    }

    @SuppressWarnings("unchecked")
    public static <F extends KapuaObjectFactory, I extends F> FactoryResolver<F, I> newInstance(Class<?> factory, Class<?> implementation)
            throws KapuaLocatorException {
        if (!factory.isAssignableFrom(implementation)) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.FACTORY_PROVIDER_INVALID, implementation, factory);
        }

        return new FactoryResolver<F,I>((Class<F>)factory, (Class<I>)implementation);
    }

    public Class<F> getFactoryClass() {
        return factoryClass;
    }

    public Class<I> getImplementationClass() {
        return implementationClass;
    }

}
