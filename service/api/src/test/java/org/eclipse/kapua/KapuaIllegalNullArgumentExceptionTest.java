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
public class KapuaIllegalNullArgumentExceptionTest {

    String[] argumentName;

    @Before
    public void initialize() {
        argumentName = new String[]{"Argument Name", null};
    }

    @Test
    public void kapuaIllegalNullArgumentExceptionTest() {
        for (String name : argumentName) {
            KapuaIllegalNullArgumentException kapuaIllegalNullArgumentException = new KapuaIllegalNullArgumentException(name);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ILLEGAL_NULL_ARGUMENT, kapuaIllegalNullArgumentException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", name, kapuaIllegalNullArgumentException.getArgumentName());
            Assert.assertNull("Null expected.", kapuaIllegalNullArgumentException.getArgumentValue());
            Assert.assertNull("Null expected.", kapuaIllegalNullArgumentException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", "An illegal null value was provided for the argument " + name + ".", kapuaIllegalNullArgumentException.getMessage());
        }
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void throwingExceptionTest() throws KapuaIllegalNullArgumentException {
        for (String name : argumentName) {
            throw new KapuaIllegalNullArgumentException(name);
        }
    }
}  
