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
package org.eclipse.kapua.commons.jpa;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.locator.inject.Service;

import com.google.inject.Injector;

@ComponentProvider
@Service(provides = { EntityManagerFactoryRegistry.class })
public class EntityManagerFactoryRegistryImpl implements EntityManagerFactoryRegistry {

    @Inject Injector injector;
    
    @Override
    public <T extends EntityManagerFactory> T getFactory(Class<T> clazz) {
        
        // Throws a ConfigurationException if the binding does not exist
        injector.getBinding(clazz);
        // The previous prevents the following to create an un-binded object
        return injector.getInstance(clazz);
    }

}
