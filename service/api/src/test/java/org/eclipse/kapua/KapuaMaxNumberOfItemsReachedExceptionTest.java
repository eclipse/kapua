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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaMaxNumberOfItemsReachedExceptionTest extends Assert {

    String[] argValue;

    @Before
    public void initialize() {
        argValue = new String[]{"Argument Value", null};
    }

    @Test
    public void kapuaMaxNumberOfItemsReachedExceptionTest() {
        for (String value : argValue) {
            KapuaMaxNumberOfItemsReachedException kapuaMaxNumberOfItemsReachedException = new KapuaMaxNumberOfItemsReachedException(value);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.MAX_NUMBER_OF_ITEMS_REACHED, kapuaMaxNumberOfItemsReachedException.getCode());
            assertEquals("Expected and actual values should be the same.", value, kapuaMaxNumberOfItemsReachedException.getArgValue());
            assertEquals("Expected and actual values should be the same.", "Max number of " + value + " reached. Please increase the number or set InfiniteChild" + value + " parameter to True.", kapuaMaxNumberOfItemsReachedException.getMessage());
            assertNull("Null expected.", kapuaMaxNumberOfItemsReachedException.getCause());
        }
    }

    @Test(expected = KapuaMaxNumberOfItemsReachedException.class)
    public void throwingExceptionTest() throws KapuaMaxNumberOfItemsReachedException {
        for (String value : argValue) {
            throw new KapuaMaxNumberOfItemsReachedException(value);
        }
    }
}