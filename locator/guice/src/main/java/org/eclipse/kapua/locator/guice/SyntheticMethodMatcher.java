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

import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;

public class SyntheticMethodMatcher extends AbstractMatcher<Method> {

    public SyntheticMethodMatcher() {
    }

    @Override
    public boolean matches(Method method) {
        return method.isSynthetic();
    }
}
