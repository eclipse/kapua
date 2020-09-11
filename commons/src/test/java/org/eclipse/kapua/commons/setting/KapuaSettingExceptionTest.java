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
package org.eclipse.kapua.commons.setting;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaSettingExceptionTest extends Assert {

    Throwable throwable = new Throwable("throwable_error_message");

    @Test
    public void constructorNullTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(null);
        assertNull("Null expected!", kapuaSettingException.getCode());
    }

    @Test
    public void constructorInvalidResourceNameTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME);
        assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", kapuaSettingException.getCode().toString());
    }

    @Test
    public void constructorInvalidResourceFileTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_FILE);
        assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_FILE", kapuaSettingException.getCode().toString());
    }

    @Test
    public void constructorResourceNotFoundTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND);
        assertEquals("Expected and actual values should be the same!", "RESOURCE_NOT_FOUND", kapuaSettingException.getCode().toString());
    }

    @Test
    public void extendedConstructorWithObjectArgTest() {
        Object[] arguments = new Object[]{"invalid_resource_name", 1, 10L, 10.34, 5.99f, 'D', "", true, false, -128, 127, -32768, 32767, -2147483648, 2147483647, -9223372036854775808L, 9223372036854775807L};
        for (Object value : arguments) {
            KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, value);
            assertEquals("Expected and actual values should be the same!", kapuaSettingException.getCode().toString(), "INVALID_RESOURCE_NAME");
            assertEquals("Expected and actual values should be the same!", kapuaSettingException.getMessage(), "Error: " + value);
        }
    }

    @Test
    public void extendedConstructorWithNullObjArgTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, (Object) null);
        assertEquals("Expected and actual values should be the same!", kapuaSettingException.getCode().toString(), "INVALID_RESOURCE_NAME");
        assertEquals("Expected and actual values should be the same!", kapuaSettingException.getMessage(), "Error: " + null);
    }

    @Test
    public void extendedConstructorWithThrowableAndNullObjArgTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, throwable, (Object) null);
        assertEquals("Expected and actual values should be the same!", kapuaSettingException.getCode().toString(), "INVALID_RESOURCE_NAME");
        assertEquals("Expected and actual values should be the same!", kapuaSettingException.getCause(), throwable);
        assertEquals("Expected and actual values should be the same!", kapuaSettingException.getMessage(), "Error: " + null);
    }

    @Test
    public void extendedConstructorWithNullThrowableAndNullObjArgTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, null, (Object) null);
        assertEquals("Expected and actual values should be the same!", kapuaSettingException.getCode().toString(), "INVALID_RESOURCE_NAME");
        assertNull("Expected and actual values should be the same!", kapuaSettingException.getCause());
        assertEquals("Expected and actual values should be the same!", kapuaSettingException.getMessage(), "Error: " + null);
    }

    @Test
    public void extendedConstructorWithThrowableAndObjectArgTest() {
        Object[] arguments = new Object[]{"invalid_resource_name", 1, 10L, 10.34, 5.99f, 'D', "", true, false, -128, 127, -32768, 32767, -2147483648, 2147483647, -9223372036854775808L, 9223372036854775807L};
        for (Object value : arguments) {
            KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, throwable, value);
            assertEquals("Expected and actual values should be the same!", kapuaSettingException.getCode().toString(), "INVALID_RESOURCE_NAME");
            assertEquals("Expected and actual values should be the same!", kapuaSettingException.getCause(), throwable);
            assertEquals("Expected and actual values should be the same!", kapuaSettingException.getMessage(), "Error: " + value);
        }
    }
}
