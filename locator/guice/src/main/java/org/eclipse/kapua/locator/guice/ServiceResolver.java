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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.service.KapuaService;

public class ServiceResolver<S extends KapuaService, I extends S> {

    private final Class<S> serviceClass;
    private final Class<I> implementationClass;

    private ServiceResolver(Class<S> service, Class<I> implementation) {
        this.serviceClass = service;
        this.implementationClass = implementation;
    }

    @SuppressWarnings("unchecked")
    public static <S extends KapuaService, I extends S> ServiceResolver<S,I> newInstance(Class<?> service, Class<?> implementation)
            throws KapuaLocatorException {

        if (!service.isAssignableFrom(implementation)) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.SERVICE_PROVIDER_INVALID, implementation, service);
        }

        return new ServiceResolver<S,I>((Class<S>)service, (Class<I>)implementation);
    }

    public Class<S> getServiceClass() {
        return serviceClass;
    }

    public Class<I> getImplementationClass() {
        return implementationClass;
    }

}
