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
package org.eclipse.kapua.commons.setting;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaSettingExceptionTest {

    Throwable throwable = new Throwable("throwable_error_message");
    String kapuaErrorMessage = "kapua-setting-service-error-messages";
    KapuaSettingErrorCodes[] kapuaSettingErrorCodes = {KapuaSettingErrorCodes.INVALID_RESOURCE_FILE, KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, KapuaSettingErrorCodes.RESOURCE_NOT_FOUND};

    @Test
    public void kapuaSettingExceptionNullTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(null);
        Assert.assertNull("Null expected!", kapuaSettingException.getCode());
        Assert.assertNull("Null expected!", kapuaSettingException.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "Error: ", kapuaSettingException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaSettingExceptionInvalidResourceNameTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME);
        Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", kapuaSettingException.getCode().toString());
        Assert.assertNull("Null expected!", kapuaSettingException.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "Error: ", kapuaSettingException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaSettingExceptionInvalidResourceFileTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_FILE);
        Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_FILE", kapuaSettingException.getCode().toString());
        Assert.assertNull("Null expected!", kapuaSettingException.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "Error: ", kapuaSettingException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaSettingExceptionResourceNotFoundTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND);
        Assert.assertEquals("Expected and actual values should be the same!", "RESOURCE_NOT_FOUND", kapuaSettingException.getCode().toString());
        Assert.assertNull("Null expected!", kapuaSettingException.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "Error: ", kapuaSettingException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaSettingExceptionWithObjectArgTest() {
        Object[] arguments = new Object[]{"invalid_resource_name", 1, 10L, 10.34, 5.99f, 'D', "", true, false, -128, 127, -32768, 32767, -2147483648, 2147483647, -9223372036854775808L, 9223372036854775807L};
        for (Object value : arguments) {
            KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, value);
            Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", kapuaSettingException.getCode().toString());
            Assert.assertNull("Null expected!", kapuaSettingException.getCause());
            Assert.assertEquals("Expected and actual values should be the same!", "Error: " + value, kapuaSettingException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaSettingExceptionWithNullObjArgTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, (Object) null);
        Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", kapuaSettingException.getCode().toString());
        Assert.assertNull("Null expected!", kapuaSettingException.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "Error: " + null, kapuaSettingException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaSettingExceptionWithThrowableAndNullObjArgTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, throwable, (Object) null);
        Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", kapuaSettingException.getCode().toString());
        Assert.assertEquals("Expected and actual values should be the same!", throwable, kapuaSettingException.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "Error: " + null, kapuaSettingException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaSettingExceptionWithNullThrowableAndNullObjArgTest() {
        KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, null, (Object) null);
        Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", kapuaSettingException.getCode().toString());
        Assert.assertNull("Null expected!", kapuaSettingException.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "Error: " + null, kapuaSettingException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaSettingExceptionWithThrowableAndObjectArgTest() {
        Object[] arguments = new Object[]{"invalid_resource_name", 1, 10L, 10.34, 5.99f, 'D', "", true, false, -128, 127, -32768, 32767, -2147483648, 2147483647, -9223372036854775808L, 9223372036854775807L};
        for (Object value : arguments) {
            KapuaSettingException kapuaSettingException = new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, throwable, value);
            Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", kapuaSettingException.getCode().toString());
            Assert.assertEquals("Null expected!", throwable, kapuaSettingException.getCause());
            Assert.assertEquals("Expected and actual values should be the same!", kapuaSettingException.getMessage(), "Error: " + value);
            Assert.assertEquals("Expected and actual values should be the same!", kapuaErrorMessage, kapuaSettingException.getKapuaErrorMessagesBundle());
        }
    }

    @Test(expected = KapuaSettingException.class)
    public void throwingKapuaSettingErrorCodesTest() throws KapuaSettingException {
        for (KapuaSettingErrorCodes code : kapuaSettingErrorCodes) {
            throw new KapuaSettingException(code);
        }
    }

    @Test(expected = KapuaSettingException.class)
    public void throwingKapuaSettingErrorCodesWithArgumentsTest() throws KapuaSettingException {
        for (KapuaSettingErrorCodes code : kapuaSettingErrorCodes) {
            throw new KapuaSettingException(code, "String", true, 1, 10L);
        }
    }

    @Test(expected = KapuaSettingException.class)
    public void throwingKapuaSettingErrorCodesWithCauseAndArgumentsTest() throws KapuaSettingException {
        for (KapuaSettingErrorCodes code : kapuaSettingErrorCodes) {
            throw new KapuaSettingException(code, new Throwable(), "String", true, 1, 10L);
        }
    }
}
