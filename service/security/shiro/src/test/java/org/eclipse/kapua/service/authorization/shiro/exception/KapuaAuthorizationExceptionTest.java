/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.shiro.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaAuthorizationExceptionTest {

    KapuaAuthorizationErrorCodes[] kapuaAuthorizationErrorCodes;
    Object stringArgument, intArgument, booleanArgument;
    Throwable[] throwables;
    String kapuaErrorMessageBundle;
    String[] errorMessagesWithoutArguments, errorMessagesWithArguments;

    @Before
    public void initialize() {
        kapuaAuthorizationErrorCodes = new KapuaAuthorizationErrorCodes[]{KapuaAuthorizationErrorCodes.ENTITY_SCOPE_MISSMATCH, KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED,
                KapuaAuthorizationErrorCodes.INVALID_STRING_PERMISSION};
        stringArgument = "string argument";
        intArgument = 10;
        booleanArgument = true;
        throwables = new Throwable[]{null, new Throwable(), new Throwable(new Exception()), new Throwable("message")};
        kapuaErrorMessageBundle = "kapua-service-error-messages";
        errorMessagesWithoutArguments = new String[]{"Error: ", "User does not have permission to perform this action. Required permission: {0}.", "Error: "};
        errorMessagesWithArguments = new String[]{"Error: " + stringArgument + ", " + intArgument + ", " + booleanArgument, "User does not have permission to perform this action. Required permission: " + stringArgument + ".", "Error: " + stringArgument + ", " + intArgument + ", " + booleanArgument};
    }

    @Test
    public void kapuaAuthorizationExceptionCodeParameterTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i]);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
            Assert.assertNull("Null expected.", kapuaAuthorizationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", errorMessagesWithoutArguments[i], kapuaAuthorizationException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionNullCodeParameterTest() {
        KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(null);
        Assert.assertNull("Null expected.", kapuaAuthorizationException.getCode());
        Assert.assertNull("Null expected.", kapuaAuthorizationException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        Assert.assertEquals("Error: ", kapuaAuthorizationException.getMessage());
    }

    @Test
    public void kapuaAuthorizationExceptionCodeArgumentsParametersTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], stringArgument, intArgument, booleanArgument);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
            Assert.assertNull("Null expected.", kapuaAuthorizationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", errorMessagesWithArguments[i], kapuaAuthorizationException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionNullCodeArgumentsParametersTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(null, stringArgument, intArgument, booleanArgument);
            Assert.assertNull("Null expected.", kapuaAuthorizationException.getCode());
            Assert.assertNull("Null expected.", kapuaAuthorizationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            Assert.assertEquals("Error: string argument, 10, true", kapuaAuthorizationException.getMessage());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionCodeNullArgumentsParametersTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], null);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
            Assert.assertNull("Null expected.", kapuaAuthorizationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", errorMessagesWithoutArguments[i], kapuaAuthorizationException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
                KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], throwable, stringArgument, intArgument, booleanArgument);
                Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthorizationException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", errorMessagesWithArguments[i], kapuaAuthorizationException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test
    public void kapuaAuthorizationExceptionNullCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(null, throwable, stringArgument, intArgument, booleanArgument);
            Assert.assertNull("Null expected.", kapuaAuthorizationException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthorizationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            Assert.assertEquals("Error: string argument, 10, true", kapuaAuthorizationException.getMessage());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionCodeCauseNullArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
                KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], throwable, null);
                Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthorizationException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", errorMessagesWithoutArguments[i], kapuaAuthorizationException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            }
        }
    }
}