/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ServiceResolver newInstance(Class<?> service, Class<?> implementation)
            throws KapuaLocatorException {
        if (!service.isAssignableFrom(implementation)) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.SERVICE_PROVIDER_INVALID, implementation, service);
        }

        return new ServiceResolver(service, implementation);
    }

    public Class<S> getServiceClass() {
        return serviceClass;
    }

    public Class<I> getImplementationClass() {
        return implementationClass;
    }

}
