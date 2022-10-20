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
public class KapuaIllegalAccessExceptionTest {

    String[] operationName;

    @Before
    public void initialize() {
        operationName = new String[]{"Operation Name", null};
    }

    @Test
    public void kapuaIllegalAccessExceptionTest() {
        for (String name : operationName) {
            KapuaIllegalAccessException kapuaIllegalAccessException = new KapuaIllegalAccessException(name);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ILLEGAL_ACCESS, kapuaIllegalAccessException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", "The current subject is not authorized for " + name + ".", kapuaIllegalAccessException.getMessage());
            Assert.assertNull("Null expected.", kapuaIllegalAccessException.getCause());
        }
    }

    @Test(expected = KapuaIllegalAccessException.class)
    public void throwingExceptionTest() throws KapuaIllegalAccessException {
        for (String name : operationName) {
            throw new KapuaIllegalAccessException(name);
        }
    }
}  
