/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;

@Category(Categories.junitTests.class)
public class RandomUtilsTest {

    @Test
    public void constructorTest() throws Exception {
        Constructor<RandomUtils> randomUtils = RandomUtils.class.getDeclaredConstructor();
        randomUtils.setAccessible(true);
        randomUtils.newInstance();
    }
} 
