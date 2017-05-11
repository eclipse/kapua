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

public class ApplicationConfiguration {

    private List<Object> registeredObjects;

    public ApplicationConfiguration() {
        registeredObjects = new ArrayList<Object>();
    }

    public void register(LifecyleListener listener) {
        registeredObjects.add(listener);
    }

    public <T> List<T> get(Class<T> clazz) {
        ArrayList<T> objects = new ArrayList<T>();
        for (Object object : registeredObjects) {
            if (object.getClass().equals(clazz)) {
                objects.add((T) object);
            }
        }

        return objects;
    }
}
