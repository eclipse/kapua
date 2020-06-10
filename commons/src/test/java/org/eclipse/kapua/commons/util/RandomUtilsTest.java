/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;

@Category(JUnitTests.class)
public class RandomUtilsTest {

    @Test
    public void constructorTest() throws Exception {
        Constructor<RandomUtils> randomUtils = RandomUtils.class.getDeclaredConstructor();
        randomUtils.setAccessible(true);
        randomUtils.newInstance();
    }
} 