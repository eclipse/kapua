/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
