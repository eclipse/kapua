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
package org.eclipse.kapua.locator;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaLocatorExceptionTest {

    String expectedErrorMessage;
    KapuaLocatorErrorCodes[] kapuaLocatorErrorCodes;
    Object argument1, argument2, argument3;
    Throwable[] throwables;
    String[] expectedMessageNullArguments;
    String[] expectedMessage;

    @Before
    public void initialize() {
        expectedErrorMessage = "kapua-locator-service-error-messages";
        kapuaLocatorErrorCodes = new KapuaLocatorErrorCodes[]{KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, KapuaLocatorErrorCodes.SERVICE_PROVIDER_INVALID,
                KapuaLocatorErrorCodes.FACTORY_UNAVAILABLE, KapuaLocatorErrorCodes.FACTORY_PROVIDER_INVALID, KapuaLocatorErrorCodes.COMPONENT_UNAVAILABLE,
                KapuaLocatorErrorCodes.COMPONENT_PROVIDER_INVALID, KapuaLocatorErrorCodes.INVALID_CONFIGURATION};
        argument1 = "argument1";
        argument2 = 42;
        argument3 = 'c';
        throwables = new Throwable[]{new Throwable(), null};
        expectedMessageNullArguments = new String[]{"Service unavailable {0}", "{0} is not a valid service provider for {1}", "Factory unavailable {0}",
                "{0} is not a valid factory provider for {1}", "Error: ", "Error: ", "Error: "};
        expectedMessage = new String[]{"Service unavailable " + argument1, argument1 + " is not a valid service provider for " + argument2, "Factory unavailable " + argument1,
                argument1 + " is not a valid factory provider for " + argument2, "Error: " + argument1 + ", " + argument2 + ", " + argument3, "Error: " + argument1 + ", " + argument2 + ", " + argument3,
                "Error: " + argument1 + ", " + argument2 + ", " + argument3};
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorErrorCodesParameterTest() {
        for (int i = 0; i < kapuaLocatorErrorCodes.length; i++) {
            KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(kapuaLocatorErrorCodes[i]);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaLocatorErrorCodes[i], kapuaLocatorException.getCode());
            Assert.assertNull("Null expected.", kapuaLocatorException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageNullArguments[i], kapuaLocatorException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorNullErrorCodesParameterTest() {
        KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(null);
        Assert.assertNull("Null expected.", kapuaLocatorException.getCode());
        Assert.assertNull("Null expected.", kapuaLocatorException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
        Assert.assertEquals("Error: ", kapuaLocatorException.getMessage());
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorErrorCodesObjectParametersTest() {
        for (int i = 0; i < kapuaLocatorErrorCodes.length; i++) {
            KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(kapuaLocatorErrorCodes[i], argument1, argument2, argument3);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaLocatorErrorCodes[i], kapuaLocatorException.getCode());
            Assert.assertNull("Null expected.", kapuaLocatorException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessage[i], kapuaLocatorException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorErrorCodesNullObjectParametersTest() {
        for (int i = 0; i < kapuaLocatorErrorCodes.length; i++) {
            KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(kapuaLocatorErrorCodes[i], null);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaLocatorErrorCodes[i], kapuaLocatorException.getCode());
            Assert.assertNull("Null expected.", kapuaLocatorException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageNullArguments[i], kapuaLocatorException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaLocatorExceptionNullKapuaLocatorErrorCodesObjectParametersTest() {
        KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(null, argument1, argument2, argument3);
        Assert.assertNull("Null expected.", kapuaLocatorException.getCode());
        Assert.assertNull("Null expected.", kapuaLocatorException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
        Assert.assertEquals("Error: argument1, 42, c", kapuaLocatorException.getMessage());
    }

    @Test
    public void kapuaLocatorExceptionNullKapuaLocatorErrorCodesNullObjectParametersTest() {
        KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(null, null);
        Assert.assertNull("Null expected.", kapuaLocatorException.getCode());
        Assert.assertNull("Null expected.", kapuaLocatorException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
        Assert.assertEquals("Error: ", kapuaLocatorException.getMessage());
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorErrorCodesThrowableObjectParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < kapuaLocatorErrorCodes.length; i++) {
                KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(kapuaLocatorErrorCodes[i], throwable, argument1, argument2, argument3);
                Assert.assertEquals("Expected and actual values should be the same.", kapuaLocatorErrorCodes[i], kapuaLocatorException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaLocatorException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", expectedMessage[i], kapuaLocatorException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorErrorCodesThrowableNullObjectParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < kapuaLocatorErrorCodes.length; i++) {
                KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(kapuaLocatorErrorCodes[i], throwable, null);
                Assert.assertEquals("Expected and actual values should be the same.", kapuaLocatorErrorCodes[i], kapuaLocatorException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaLocatorException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", expectedMessageNullArguments[i], kapuaLocatorException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorNullErrorCodesThrowableObjectParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(null, throwable, argument1, argument2, argument3);
            Assert.assertNull("Null expected.", kapuaLocatorException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaLocatorException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
            Assert.assertEquals("Error: argument1, 42, c", kapuaLocatorException.getMessage());
        }
    }

    @Test
    public void kapuaLocatorExceptionKapuaLocatorNullErrorCodesThrowableNullObjectParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaLocatorException kapuaLocatorException = new KapuaLocatorException(null, throwable, null);
            Assert.assertNull("Null expected", kapuaLocatorException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaLocatorException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaLocatorException.getKapuaErrorMessagesBundle());
            Assert.assertEquals("Error: ", kapuaLocatorException.getMessage());
        }
    }
}
