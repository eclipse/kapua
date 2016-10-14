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
 *
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import java.util.List;
import java.util.ServiceLoader;

import com.google.common.collect.ImmutableList;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.service.KapuaService;

import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua Guice service {@link Provider} customization.<br>
 * It provides a custom provider to be used as a loader provider for a specific service.
 *
 * @param <S> service type handled by the {@link Provider}
 * 
 * @since 1.0
 * 
 */
public class KapuaServiceLoaderProvider<S extends KapuaService> implements Provider<S> {

	private final static Logger LOG = LoggerFactory.getLogger(KapuaServiceLoaderProvider.class);

	private final Class<S> serviceClass;

	private final S service;

    /**
     * Constructors
     * 
     * @param serviceClass service class
     * @param service service instance
     */
	public KapuaServiceLoaderProvider(Class<S> serviceClass, S service) {
		this.serviceClass = serviceClass;
		this.service = service;
	}

    /**
     * Constructor
     * 
     * @param serviceClass service class
     */
	public KapuaServiceLoaderProvider(Class<S> serviceClass) {
		this(serviceClass, null);
	}

    /**
     * Constructor
     * 
     * @param service service instance
     */
	public KapuaServiceLoaderProvider(S service) {
		this(null, service);
	}

    /**
     * Return the requested service instance
     */
    @Override
	public synchronized S get()
	{		
		if (service != null) {
			LOG.debug("Provider has been configured to return service instance: {}", service);
			return service;
		}

		List<S> serviceCandidates = ImmutableList.copyOf(ServiceLoader.load(serviceClass).iterator());

		if(serviceCandidates.isEmpty()) {
			throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, serviceClass);
		}  else if(serviceCandidates.size() > 1) {
			for (S service : serviceCandidates) {
				if (service.getClass().isAnnotationPresent(TestService.class)) {
					LOG.debug("Found test service implementtation: {}", service);
					return service;
				}
			}
		}

		return serviceCandidates.get(0);
	}

}
