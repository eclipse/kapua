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

import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * @since 0.3.0
 *
 */
public class ComponentResolver<I, P extends I> {

    private final Class<I> providedClass;
    private final Class<P> implementationClass;

    private ComponentResolver(Class<I> provided, Class<P> implementation) {
        this.providedClass = provided;
        this.implementationClass = implementation;
    }

    @SuppressWarnings("unchecked")
    public static <F extends KapuaObjectFactory, I extends F> ComponentResolver<F, I> newInstance(Class<?> factory, Class<?> implementation)
            throws KapuaLocatorException {
        if (!factory.isAssignableFrom(implementation)) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.FACTORY_PROVIDER_INVALID, implementation, factory);
        }

        return new ComponentResolver<F,I>((Class<F>)factory, (Class<I>)implementation);
    }

    public Class<I> getProvidedClass() {
        return providedClass;
    }

    public Class<P> getImplementationClass() {
        return implementationClass;
    }

}
