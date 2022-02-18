/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;

public class ComponentResolver<I, P extends I> {

    private final Class<I> providedClass;
    private final Class<P> implementationClass;

    private ComponentResolver(Class<I> provided, Class<P> implementation) {
        this.providedClass = provided;
        this.implementationClass = implementation;
    }

    @SuppressWarnings("unchecked")
    public static <M extends ServiceModule, I extends M> ComponentResolver<M, I> newInstance(Class<?> factory, Class<?> implementation)
            throws KapuaLocatorException {
        if (!factory.isAssignableFrom(implementation)) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.FACTORY_PROVIDER_INVALID, implementation, factory);
        }

        return new ComponentResolver<M,I>((Class<M>)factory, (Class<I>)implementation);
    }

    public Class<I> getProvidedClass() {
        return providedClass;
    }

    public Class<P> getImplementationClass() {
        return implementationClass;
    }

}
