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

import java.lang.reflect.Method;

import org.eclipse.kapua.locator.inject.ObjectInspector;

public class GuiceObjectInspector implements ObjectInspector {

    private static final String GUICE_ENHANCER_TAG = "$$EnhancerByGuice$$";
    
    @Override
    public boolean isEnhancedClass(Object obj) {
        String canonicalName = obj.getClass().getCanonicalName();
        boolean isEnhanced = canonicalName.contains(GUICE_ENHANCER_TAG);
        return isEnhanced;
    }

    @Override
    public Method[] getSuperMethods(Object obj) {
        Class<?> superclass = obj.getClass().getSuperclass();
        Method[] superMethod = superclass.getMethods();
        return superMethod;
    }

    @Override
    public Method[] getMethods(Object obj) {
        Method[] superMethod = obj.getClass().getMethods();
        return superMethod;
    }

}
