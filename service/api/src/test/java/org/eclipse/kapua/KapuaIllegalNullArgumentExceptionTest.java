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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class KapuaIllegalNullArgumentExceptionTest extends Assert {

    String[] argumentName;

    @Before
    public void initialize() {
        argumentName = new String[]{"Argument Name", null};
    }

    @Test
    public void kapuaIllegalNullArgumentExceptionTest() {
        for (String name : argumentName) {
            KapuaIllegalNullArgumentException kapuaIllegalNullArgumentException = new KapuaIllegalNullArgumentException(name);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ILLEGAL_NULL_ARGUMENT, kapuaIllegalNullArgumentException.getCode());
            assertEquals("Expected and actual values should be the same.", name, kapuaIllegalNullArgumentException.getArgumentName());
            assertNull("Null expected.", kapuaIllegalNullArgumentException.getArgumentValue());
            assertNull("Null expected.", kapuaIllegalNullArgumentException.getCause());
            assertEquals("Expected and actual values should be the same.", "An illegal null value was provided for the argument " + name + ".", kapuaIllegalNullArgumentException.getMessage());
        }
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void throwingExceptionTest() throws KapuaIllegalNullArgumentException {
        for (String name : argumentName) {
            throw new KapuaIllegalNullArgumentException(name);
        }
    }
}  
