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
package org.eclipse.kapua.locator.guice.inject;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Injector;

public class InjectorRegistry {

    private static Map<String, Injector> injectors;

    static {
        injectors = new HashMap<String, Injector>();
    }

    public static void add(String name, Injector aInjector) {
        injectors.put(name, aInjector);
    }

    public static void remove(String name) {
        injectors.remove(name);
    }

    public static Injector get(String name) {
        return injectors.get(name);
    }
}
