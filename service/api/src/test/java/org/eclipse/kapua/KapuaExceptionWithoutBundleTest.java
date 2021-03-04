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
public class KapuaExceptionWithoutBundleTest extends Assert {

    String expectedKapuaErrorMessagesBundle;
    Object argument1, argument2, argument3;
    Throwable[] throwables;
    KapuaErrorCode[] kapuaErrorCodes;

    @Before
    public void initialize() {
        expectedKapuaErrorMessagesBundle = "non-existing-file.properties";
        argument1 = "user";
        argument2 = 1;
        argument3 = 'c';
        throwables = new Throwable[]{new Throwable(), null};
        kapuaErrorCodes = new KapuaErrorCode[]{KapuaErrorCodes.ADMIN_ROLE_DELETED_ERROR, null, MissingKapuaErrorCodes.NOT_EXISTING};

    }

    @Test
    public void kapuaExceptionWithoutBundleKapuaErrorCodeParameterTest() {
        for (KapuaErrorCode kapuaErrorCode : kapuaErrorCodes) {
            KapuaExceptionWithoutBundle kapuaExceptionWithoutBundle = new KapuaExceptionWithoutBundle(kapuaErrorCode);
            assertEquals("Expected and actual values should be the same.", kapuaErrorCode, kapuaExceptionWithoutBundle.getCode());
            assertNull("Null expected.", kapuaExceptionWithoutBundle.getCause());
            assertEquals("Expected and actual values should be the same.", "Error: ", kapuaExceptionWithoutBundle.getMessage());
            assertEquals("Expected and actual values should be the same.", "Error: ", kapuaExceptionWithoutBundle.getLocalizedMessage());
            assertEquals("Expected and actual values should be the same.", expectedKapuaErrorMessagesBundle, kapuaExceptionWithoutBundle.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaExceptionWithoutBundleKapuaErrorCodeObjectParameterTest() {
        for (KapuaErrorCode kapuaErrorCode : kapuaErrorCodes) {
            KapuaExceptionWithoutBundle kapuaExceptionWithoutBundle = new KapuaExceptionWithoutBundle(kapuaErrorCode, argument1, argument2, argument3);
            assertEquals("Expected and actual values should be the same.", kapuaErrorCode, kapuaExceptionWithoutBundle.getCode());
            assertNull("Null expected.", kapuaExceptionWithoutBundle.getCause());
            assertEquals("Expected and actual values should be the same.", "Error: " + argument1 + ", " + argument2 + ", " + argument3, kapuaExceptionWithoutBundle.getMessage());
            assertEquals("Expected and actual values should be the same.", "Error: " + argument1 + ", " + argument2 + ", " + argument3, kapuaExceptionWithoutBundle.getLocalizedMessage());
            assertEquals("Expected and actual values should be the same.", expectedKapuaErrorMessagesBundle, kapuaExceptionWithoutBundle.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaExceptionWithoutBundleKapuaErrorCodeNullObjectParameterTest() {
        for (KapuaErrorCode kapuaErrorCode : kapuaErrorCodes) {
            KapuaExceptionWithoutBundle kapuaExceptionWithoutBundle = new KapuaExceptionWithoutBundle(kapuaErrorCode, null);
            assertEquals("Expected and actual values should be the same.", kapuaErrorCode, kapuaExceptionWithoutBundle.getCode());
            assertNull("Null expected.", kapuaExceptionWithoutBundle.getCause());
            assertEquals("Expected and actual values should be the same.", "Error: ", kapuaExceptionWithoutBundle.getMessage());
            assertEquals("Expected and actual values should be the same.", "Error: ", kapuaExceptionWithoutBundle.getLocalizedMessage());
            assertEquals("Expected and actual values should be the same.", expectedKapuaErrorMessagesBundle, kapuaExceptionWithoutBundle.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaExceptionWithoutBundleKapuaErrorCodeThrowableObjectParameterTest() {
        for (KapuaErrorCode kapuaErrorCode : kapuaErrorCodes) {
            for (Throwable throwable : throwables) {
                KapuaExceptionWithoutBundle kapuaExceptionWithoutBundle = new KapuaExceptionWithoutBundle(kapuaErrorCode, throwable, argument1, argument2, argument3);
                assertEquals("Expected and actual values should be the same.", kapuaErrorCode, kapuaExceptionWithoutBundle.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, kapuaExceptionWithoutBundle.getCause());
                assertEquals("Expected and actual values should be the same.", "Error: " + argument1 + ", " + argument2 + ", " + argument3, kapuaExceptionWithoutBundle.getMessage());
                assertEquals("Expected and actual values should be the same.", "Error: " + argument1 + ", " + argument2 + ", " + argument3, kapuaExceptionWithoutBundle.getLocalizedMessage());
                assertEquals("Expected and actual values should be the same.", expectedKapuaErrorMessagesBundle, kapuaExceptionWithoutBundle.getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test
    public void kapuaExceptionWithoutBundleKapuaErrorCodeThrowableNullObjectParameterTest() {
        for (KapuaErrorCode kapuaErrorCode : kapuaErrorCodes) {
            for (Throwable throwable : throwables) {
                KapuaExceptionWithoutBundle kapuaExceptionWithoutBundle = new KapuaExceptionWithoutBundle(kapuaErrorCode, throwable, null);
                assertEquals("Expected and actual values should be the same.", kapuaErrorCode, kapuaExceptionWithoutBundle.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, kapuaExceptionWithoutBundle.getCause());
                assertEquals("Expected and actual values should be the same.", "Error: ", kapuaExceptionWithoutBundle.getMessage());
                assertEquals("Expected and actual values should be the same.", "Error: ", kapuaExceptionWithoutBundle.getLocalizedMessage());
                assertEquals("Expected and actual values should be the same.", expectedKapuaErrorMessagesBundle, kapuaExceptionWithoutBundle.getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test(expected = KapuaExceptionWithoutBundle.class)
    public void throwingExceptionKapuaErrorCodeParameterTest() throws KapuaExceptionWithoutBundle {
        for (KapuaErrorCode kapuaErrorCode : kapuaErrorCodes) {
            throw new KapuaExceptionWithoutBundle(kapuaErrorCode);
        }
    }

    @Test(expected = KapuaExceptionWithoutBundle.class)
    public void throwingExceptionKapuaErrorCodeObjectParameterTest() throws KapuaExceptionWithoutBundle {
        for (KapuaErrorCode kapuaErrorCode : kapuaErrorCodes) {
            throw new KapuaExceptionWithoutBundle(kapuaErrorCode, argument1, argument2, argument3);
        }
    }
}  
