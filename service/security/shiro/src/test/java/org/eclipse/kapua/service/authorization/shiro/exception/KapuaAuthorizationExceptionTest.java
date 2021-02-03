/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
public class KapuaAuthorizationExceptionTest extends Assert {

    KapuaAuthorizationErrorCodes[] kapuaAuthorizationErrorCodes;
    Object stringArgument, intArgument, booleanArgument;
    Throwable[] throwables;
    String kapuaErrorMessage;
    String[] errorMessagesWithoutArguments, errorMessagesWithArguments;

    @Before
    public void initialize() {
        kapuaAuthorizationErrorCodes = new KapuaAuthorizationErrorCodes[]{KapuaAuthorizationErrorCodes.ENTITY_SCOPE_MISSMATCH, KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED,
                KapuaAuthorizationErrorCodes.INVALID_STRING_PERMISSION};
        stringArgument = "string argument";
        intArgument = 10;
        booleanArgument = true;
        throwables = new Throwable[]{null, new Throwable(), new Throwable(new Exception()), new Throwable("message")};
        kapuaErrorMessage = "kapua-service-error-messages";
        errorMessagesWithoutArguments = new String[]{"Error: ", "User does not have permission to perform this action. Missing permission: {0}. Please perform a new login to refresh users permissions.", "Error: "};
        errorMessagesWithArguments = new String[]{"Error: " + stringArgument + ", " + intArgument + ", " + booleanArgument, "User does not have permission to perform this action. Missing permission: " + stringArgument + ". Please perform a new login to refresh users permissions.", "Error: " + stringArgument + ", " + intArgument + ", " + booleanArgument};
    }

    @Test
    public void kapuaAuthorizationExceptionCodeParameterTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i]);
            assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
            assertNull("Null expected.", kapuaAuthorizationException.getCause());
            assertEquals("Expected and actual values should be the same.", errorMessagesWithoutArguments[i], kapuaAuthorizationException.getMessage());
            assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionNullCodeParameterTest() {
        KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(null);
        assertNull("Null expected.", kapuaAuthorizationException.getCode());
        assertNull("Null expected.", kapuaAuthorizationException.getCause());
        assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        try {
            kapuaAuthorizationException.getMessage();
            fail("NullPointerException expected");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionCodeArgumentsParametersTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], stringArgument, intArgument, booleanArgument);
            assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
            assertNull("Null expected.", kapuaAuthorizationException.getCause());
            assertEquals("Expected and actual values should be the same.", errorMessagesWithArguments[i], kapuaAuthorizationException.getMessage());
            assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionNullCodeArgumentsParametersTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(null, stringArgument, intArgument, booleanArgument);
            assertNull("Null expected.", kapuaAuthorizationException.getCode());
            assertNull("Null expected.", kapuaAuthorizationException.getCause());
            assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            try {
                kapuaAuthorizationException.getMessage();
                fail("NullPointerException expected");
            } catch (Exception e) {
                assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
            }
        }
    }

    @Test
    public void kapuaAuthorizationExceptionCodeNullArgumentsParametersTest() {
        for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], null);
            assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
            assertNull("Null expected.", kapuaAuthorizationException.getCause());
            assertEquals("Expected and actual values should be the same.", errorMessagesWithoutArguments[i], kapuaAuthorizationException.getMessage());
            assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthorizationExceptionCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
                KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], throwable, stringArgument, intArgument, booleanArgument);
                assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthorizationException.getCause());
                assertEquals("Expected and actual values should be the same.", errorMessagesWithArguments[i], kapuaAuthorizationException.getMessage());
                assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test
    public void kapuaAuthorizationExceptionNullCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(null, throwable, stringArgument, intArgument, booleanArgument);
            assertNull("Null expected.", kapuaAuthorizationException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthorizationException.getCause());
            assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            try {
                kapuaAuthorizationException.getMessage();
                fail("NullPointerException expected");
            } catch (Exception e) {
                assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
            }
        }
    }

    @Test
    public void kapuaAuthorizationExceptionCodeCauseNullArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < kapuaAuthorizationErrorCodes.length; i++) {
                KapuaAuthorizationException kapuaAuthorizationException = new KapuaAuthorizationException(kapuaAuthorizationErrorCodes[i], throwable, null);
                assertEquals("Expected and actual values should be the same.", kapuaAuthorizationErrorCodes[i], kapuaAuthorizationException.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthorizationException.getCause());
                assertEquals("Expected and actual values should be the same.", errorMessagesWithoutArguments[i], kapuaAuthorizationException.getMessage());
                assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, kapuaAuthorizationException.getKapuaErrorMessagesBundle());
            }
        }
    }
}