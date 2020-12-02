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
package org.eclipse.kapua.plugin.sso.openid.exception;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class OpenIDExceptionTest extends Assert {

    String openIDErrorMessage;
    KapuaErrorCode[] openIDErrorCodes;
    Object argument1, argument2, argument3;
    String[] messages;
    Throwable[] throwables;
    String[] messagesWithArgument;

    private class ActualOpenIDException extends OpenIDException {

        private static final String KAPUA_ERROR_MESSAGES = "openid-error-messages";

        public ActualOpenIDException(KapuaErrorCode code) {
            super(code);
        }

        public ActualOpenIDException(KapuaErrorCode code, Object... arguments) {
            super(code, arguments);
        }

        public ActualOpenIDException(KapuaErrorCode code, Throwable cause, Object... arguments) {
            super(code, cause, arguments);
        }

        @Override
        protected String getKapuaErrorMessagesBundle() {
            return KAPUA_ERROR_MESSAGES;
        }
    }

    @Before
    public void setUp() {
        openIDErrorMessage = "openid-error-messages";
        openIDErrorCodes = new OpenIDErrorCodes[]{OpenIDErrorCodes.LOGIN_URI_ERROR, OpenIDErrorCodes.LOGOUT_URI_ERROR, OpenIDErrorCodes.TOKEN_ERROR, OpenIDErrorCodes.JWT_EXTRACTION_ERROR,
                OpenIDErrorCodes.JWT_PROCESS_ERROR, OpenIDErrorCodes.JWT_URI_ERROR, OpenIDErrorCodes.ILLEGAL_ARGUMENT, OpenIDErrorCodes.ILLEGAL_URI};
        argument1 = "arg1";
        argument2 = "arg2";
        argument3 = "arg3";
        throwables = new Throwable[]{new Throwable(), null};
        messages = new String[]{"An error occurred while retrieving the OpenID Connect login URI", "An error occurred while retrieving the OpenID Connect logout URI", "An error occurred while getting the access token",
                "An error occurred while extracting the Jwt from the string: {0}", "An error occurred while processing the Jwt: {0}", "An error occurred while retrieving the OpenID Connect Jwt URI",
                "An illegal value was provided for the argument {0}: {1}.", "An illegal value was provided for the URI {0}: {1}."};
        messagesWithArgument = new String[]{"An error occurred while retrieving the OpenID Connect login URI", "An error occurred while retrieving the OpenID Connect logout URI", "An error occurred while getting the access token",
                "An error occurred while extracting the Jwt from the string: " + argument1, "An error occurred while processing the Jwt: " + argument1, "An error occurred while retrieving the OpenID Connect Jwt URI",
                "An illegal value was provided for the argument " + argument1 + ": " + argument2 + ".", "An illegal value was provided for the URI " + argument1 + ": " + argument2 + "."};
    }

    @Test
    public void openIDExceptionCodeParameterTest() {
        for (int i = 0; i < openIDErrorCodes.length; i++) {
            OpenIDException openIDException = new ActualOpenIDException(openIDErrorCodes[i]);
            assertNull("Null expected.", openIDException.getCause());
            assertEquals("Expected and actual values should be the same.", openIDErrorCodes[i], openIDException.getCode());
            assertEquals("Expected and actual values should be the same.", openIDErrorMessage, openIDException.getKapuaErrorMessagesBundle());
//            assertEquals("Expected and actual values should be the same.", messages[i], openIDException.getMessage());
//            assertEquals("Expected and actual values should be the same.", messages[i], openIDException.getLocalizedMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDExceptionNullCodeParameterTest() {
        OpenIDException openIDException = new ActualOpenIDException(null);
        assertNull("Null expected.", openIDException.getCode());
        assertNull("Null expected.", openIDException.getCause());
        assertEquals("Expected and actual values should be the same.", openIDErrorMessage, openIDException.getKapuaErrorMessagesBundle());
        openIDException.getMessage();
    }

    @Test
    public void openIDExceptionCodeArgumentsParametersTest() {
        for (int i = 0; i < openIDErrorCodes.length; i++) {
            OpenIDException openIDException = new ActualOpenIDException(openIDErrorCodes[i], argument1, argument2, argument3);

            assertNull("Null expected.", openIDException.getCause());
            assertEquals("Expected and actual values should be the same.", openIDErrorCodes[i], openIDException.getCode());
            assertEquals("Expected and actual values should be the same.", openIDErrorMessage, openIDException.getKapuaErrorMessagesBundle());

//            assertEquals("Expected and actual values should be the same.", messagesWithArgument[i], openIDException.getMessage());
//            assertEquals("Expected and actual values should be the same.", messagesWithArgument[i], openIDException.getLocalizedMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDExceptionNullCodeArgumentsParametersTest() {
        OpenIDException openIDException = new ActualOpenIDException(null, argument1, argument2, argument3);
        assertNull("Null expected.", openIDException.getCode());
        assertNull("Null expected.", openIDException.getCause());
        openIDException.getMessage();
    }

    @Test
    public void openIDExceptionCodeNullArgumentsParametersTest() {
        OpenIDException openIDException = new ActualOpenIDException(OpenIDErrorCodes.ILLEGAL_ARGUMENT, null);
        assertEquals("Expected and actual values should be the same.", OpenIDErrorCodes.ILLEGAL_ARGUMENT, openIDException.getCode());
        assertNull("Null expected.", openIDException.getCause());
        assertEquals("Expected and actual values should be the same.", "An illegal value was provided for the argument {0}: {1}.", openIDException.getMessage());
        assertEquals("Expected and actual values should be the same.", "An illegal value was provided for the argument {0}: {1}.", openIDException.getLocalizedMessage());
        assertEquals("Expected and actual values should be the same.", openIDErrorMessage, openIDException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void openIDExceptionCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDException openIDException = new ActualOpenIDException(OpenIDErrorCodes.ILLEGAL_ARGUMENT, throwable, argument1, argument2);

            assertEquals("Expected and actual values should be the same.", OpenIDErrorCodes.ILLEGAL_ARGUMENT, openIDException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, openIDException.getCause());
            assertEquals("Expected and actual values should be the same.", openIDErrorMessage, openIDException.getKapuaErrorMessagesBundle());
            assertEquals("Expected and actual values should be the same.", "An illegal value was provided for the argument " + argument1 + ": " + argument2 + ".", openIDException.getMessage());
            assertEquals("Expected and actual values should be the same.", "An illegal value was provided for the argument " + argument1 + ": " + argument2 + ".", openIDException.getLocalizedMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDExceptionNullCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDException openIDException = new ActualOpenIDException(null, throwable, argument1, argument2, argument3);
            assertEquals("Expected and actual values should be the same.", throwable, openIDException.getCause());
            assertNull("Null expected.", openIDException.getCode());
            openIDException.getMessage();
        }
    }

    @Test
    public void openIDExceptionCodeCauseNullArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDException openIDException = new ActualOpenIDException(OpenIDErrorCodes.ILLEGAL_ARGUMENT, throwable, null);

            assertEquals("Expected and actual values should be the same.", OpenIDErrorCodes.ILLEGAL_ARGUMENT, openIDException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, openIDException.getCause());
            assertEquals("Expected and actual values should be the same.", openIDErrorMessage, openIDException.getKapuaErrorMessagesBundle());
            assertEquals("Expected and actual values should be the same.", "An illegal value was provided for the argument {0}: {1}.", openIDException.getMessage());
            assertEquals("Expected and actual values should be the same.", "An illegal value was provided for the argument {0}: {1}.", openIDException.getLocalizedMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDExceptionNullCodeCauseNullArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDException openIDException = new ActualOpenIDException(null, throwable, null);
            assertEquals("Expected and actual values should be the same.", throwable, openIDException.getCause());
            assertNull("Null expected.", openIDException.getCode());
            openIDException.getMessage();
        }
    }

    @Test
    public void getKapuaErrorMessagesBundleTest() {
        OpenIDException openIDException = new ActualOpenIDException(OpenIDErrorCodes.ILLEGAL_ARGUMENT, new Throwable(), argument1, argument2);
        assertEquals("Expected and actual values should be the same.", openIDErrorMessage, openIDException.getKapuaErrorMessagesBundle());
    }
}