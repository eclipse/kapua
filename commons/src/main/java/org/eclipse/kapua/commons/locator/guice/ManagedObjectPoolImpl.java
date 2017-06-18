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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.eclipse.kapua.locator.inject.PoolListener;


/**
 * @since 0.3.0
 *
 */
public class ManagedObjectPoolImpl implements ManagedObjectPool {

    private List<Object> instances = new ArrayList<Object>();
    private List<PoolListener> listeners = new ArrayList<PoolListener>();
    
    @Override
    public void add(Object object) {
        instances.add(object);
        
        for (PoolListener listener:listeners) {
            listener.onObjectAdded(object);
        }
    }

    @Override
    public <T> List<T> getImplementationsOf(Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        for(Object instance:instances) {
            if (clazz.isAssignableFrom(instance.getClass())) {
                result.add((T) instance);
            }
        }
        
        return result;
    }

    @Override
    public void register(PoolListener listener) {
        listeners.add(listener);
    }

    @Override
    public PoolListener deregister(PoolListener listener) {
        PoolListener removedListener = null;
        if (listeners.contains(listener)) {
            removedListener = listener;
            listeners.remove(listener);
        }
        
        return removedListener;
    }

}
