/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaMaxNumberOfItemsReachedExceptionTest {

    String[] argValue;

    @Before
    public void initialize() {
        argValue = new String[]{"Argument Value", null};
    }

    @Test
    public void kapuaMaxNumberOfItemsReachedExceptionTest() {
        for (String value : argValue) {
            KapuaMaxNumberOfItemsReachedException kapuaMaxNumberOfItemsReachedException = new KapuaMaxNumberOfItemsReachedException(value);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.MAX_NUMBER_OF_ITEMS_REACHED, kapuaMaxNumberOfItemsReachedException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", value, kapuaMaxNumberOfItemsReachedException.getEntityType());
            Assert.assertEquals("Expected and actual values should be the same.", "Max number of " + value + " reached. Please increase the number or set InfiniteChild" + value + " parameter to True.", kapuaMaxNumberOfItemsReachedException.getMessage());
            Assert.assertNull("Null expected.", kapuaMaxNumberOfItemsReachedException.getCause());
        }
    }

    @Test(expected = KapuaMaxNumberOfItemsReachedException.class)
    public void throwingExceptionTest() throws KapuaMaxNumberOfItemsReachedException {
        for (String value : argValue) {
            throw new KapuaMaxNumberOfItemsReachedException(value);
        }
    }
}
