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
public class KapuaIllegalStateExceptionTest extends Assert {

    String[] message = {"Message", null};
    Throwable[] throwables = {new Throwable(), null};

    @Before
    public void initialize() {
        message = new String[]{"Message", null};
        throwables = new Throwable[]{new Throwable(), null};
    }

    @Test
    public void kapuaIllegalStateExceptionStringParameterTest() {
        for (String msg : message) {
            KapuaIllegalStateException kapuaIllegalStateExceptionTest = new KapuaIllegalStateException(msg);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ILLEGAL_STATE, kapuaIllegalStateExceptionTest.getCode());
            assertEquals("Expected and actual values should be the same.", "The application is in a illegal state: " + msg + ".", kapuaIllegalStateExceptionTest.getMessage());
            assertNull("Null expected.", kapuaIllegalStateExceptionTest.getCause());
        }
    }

    @Test
    public void kapuaIllegalStateExceptionStringThrowableParametersTest() {
        for (String msg : message) {
            for (Throwable throwable : throwables) {
                KapuaIllegalStateException kapuaIllegalStateExceptionTest = new KapuaIllegalStateException(msg, throwable);
                assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ILLEGAL_STATE, kapuaIllegalStateExceptionTest.getCode());
                assertEquals("Expected and actual values should be the same.", "The application is in a illegal state: " + msg + ".", kapuaIllegalStateExceptionTest.getMessage());
                assertEquals("Expected and actual values should be the same.", throwable, kapuaIllegalStateExceptionTest.getCause());
            }
        }
    }

    @Test(expected = KapuaIllegalStateException.class)
    public void throwingExceptionStringParameterTest() throws KapuaIllegalStateException {
        for (String msg : message) {
            throw new KapuaIllegalStateException(msg);
        }
    }

    @Test(expected = KapuaIllegalStateException.class)
    public void throwingExceptionStringThrowableParametersTest() throws KapuaIllegalStateException {
        for (String msg : message) {
            for (Throwable throwable : throwables) {
                throw new KapuaIllegalStateException(msg, throwable);
            }
        }
    }
}  
