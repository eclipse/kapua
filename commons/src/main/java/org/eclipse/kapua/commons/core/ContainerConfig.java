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
package org.eclipse.kapua.commons.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.3.0
 */
public class ContainerConfig {

    private List<Object> registeredObjects;

    public ContainerConfig() {
        registeredObjects = new ArrayList<Object>();
    }

    public void register(LifecyleListener listener) {
        registeredObjects.add(listener);
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> getImplementationsOf(Class<T> superClass) {
        List<T> implementations = new ArrayList<>();
        for (Object object:registeredObjects) {
            if (superClass.isAssignableFrom(object.getClass())) {
                implementations.add((T)object);
            }
        }
        return implementations;
    }
}
