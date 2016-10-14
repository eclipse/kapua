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

import java.util.Iterator;
import java.util.ServiceLoader;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.model.KapuaObjectFactory;

import com.google.inject.Provider;

/**
 * Kapua Guice factory {@link Provider} customization.<br>
 * It provides a custom provider to be used as a loader provider for a specific factory.
 *
 * @param <F> factory type handled by the {@link Provider}
 * 
 * @since 1.0
 * 
 */
public class KapuaFactoryLoaderProvider<F extends KapuaObjectFactory> implements Provider<F> 
{
	private Class<F> factoryClass;
	private F        factory;
	
    /**
     * Constructor
     * 
     * @param serviceClass factory class
     */
	public KapuaFactoryLoaderProvider(Class<F> serviceClass) {
		this.factoryClass = serviceClass;
	}
	
    /**
     * Return the requested factory instance
     */
    @Override
	public synchronized F get()
	{		
		if (factory != null) { 
			return factory;
		}
		
		ServiceLoader<F> serviceLoaders = ServiceLoader.load(factoryClass);
		Iterator<F> serviceLoaderIterator = serviceLoaders.iterator();
		while (serviceLoaderIterator.hasNext()) {
			factory = serviceLoaderIterator.next();
			break;
		}

		if (factory == null) {
			throw new KapuaRuntimeException(KapuaLocatorErrorCodes.FACTORY_UNAVAILABLE, factoryClass);
		}
		
		return factory;
	}	
}
