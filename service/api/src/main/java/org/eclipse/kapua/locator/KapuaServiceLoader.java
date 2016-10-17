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
package org.eclipse.kapua.locator;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;

/**
 * Kapua service loader definition.<br>
 * Each service loader must provide the proper implementation for these methods.
 * 
 * @since 1.0
 *
 */
public interface KapuaServiceLoader {

	/**
     * Returns an instance of a KapuaService implementing the provided KapuaService class.
     * 
     * @param serviceClass - class of the service whose instance is required.
     * @return
     */
    public <S extends KapuaService> S getService(Class<S> serviceClass);

    /**
     * Returns an instance of a KapuaEntityFactory implementing the provided KapuaFactory class.
     * 
     * @param factoryClass - class of the factory whose instance is required.
     * @return
     */
    public <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass);
    
}
