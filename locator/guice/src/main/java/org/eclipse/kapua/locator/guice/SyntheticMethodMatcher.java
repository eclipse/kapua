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
package org.eclipse.kapua.locator.guice;

import java.lang.reflect.Method;

import com.google.inject.matcher.AbstractMatcher;

public class SyntheticMethodMatcher extends AbstractMatcher<Method> {

    private static SyntheticMethodMatcher instance = new SyntheticMethodMatcher();

    private SyntheticMethodMatcher() {
    }

    public static SyntheticMethodMatcher getInstance() {
        return instance;
    }

    @Override
    public boolean matches(Method method) {
        return method.isSynthetic();
    }
}
