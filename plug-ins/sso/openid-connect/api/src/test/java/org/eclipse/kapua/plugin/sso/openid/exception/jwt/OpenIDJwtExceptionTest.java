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
package org.eclipse.kapua.plugin.sso.openid.exception.jwt;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDErrorCodes;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class OpenIDJwtExceptionTest extends Assert {

    Throwable[] throwables;
    String argument1, argument2, argument3;
    OpenIDErrorCodes[] openIDErrorCodes;
    String kapuaErrorMessage;
    String[] messages, messagesWithArgument;

    private class ActualOpenIDJwtException extends OpenIDJwtException {

        public ActualOpenIDJwtException(KapuaErrorCode code) {
            super(code);
        }

        public ActualOpenIDJwtException(KapuaErrorCode code, Throwable cause, Object... arguments) {
            super(code, cause, arguments);
        }
    }

    @Before
    public void setUp() {
        throwables = new Throwable[]{null, new Throwable()};
        argument1 = "arg1";
        argument2 = "arg2";
        argument3 = "arg3";
        kapuaErrorMessage = "sso-error-messages";
        openIDErrorCodes = new OpenIDErrorCodes[]{OpenIDErrorCodes.LOGIN_URI_ERROR, OpenIDErrorCodes.LOGOUT_URI_ERROR, OpenIDErrorCodes.TOKEN_ERROR, OpenIDErrorCodes.JWT_EXTRACTION_ERROR,
                OpenIDErrorCodes.JWT_PROCESS_ERROR, OpenIDErrorCodes.JWT_URI_ERROR, OpenIDErrorCodes.ILLEGAL_ARGUMENT, OpenIDErrorCodes.ILLEGAL_URI};
        messages = new String[]{"An error occurred while retrieving the OpenID Connect login URI", "An error occurred while retrieving the OpenID Connect logout URI", "An error occurred while getting the access token",
                "An error occurred while extracting the Jwt from the string: {0}", "An error occurred while processing the Jwt: {0}", "An error occurred while retrieving the OpenID Connect Jwt URI",
                "An illegal value was provided for the argument {0}: {1}.", "An illegal value was provided for the URI {0}: {1}."};
        messagesWithArgument = new String[]{"An error occurred while retrieving the OpenID Connect login URI", "An error occurred while retrieving the OpenID Connect logout URI", "An error occurred while getting the access token",
                "An error occurred while extracting the Jwt from the string: " + argument1, "An error occurred while processing the Jwt: " + argument1, "An error occurred while retrieving the OpenID Connect Jwt URI",
                "An illegal value was provided for the argument " + argument1 + ": " + argument2 + ".", "An illegal value was provided for the URI " + argument1 + ": " + argument2 + "."};
    }

    @Test
    public void openIDJwtExceptionCodeParameterTest() {
        for (int i = 0; i < openIDErrorCodes.length; i++) {
            OpenIDJwtException openIDJwtException = new ActualOpenIDJwtException(openIDErrorCodes[i]);
            assertNull("Null expected.", openIDJwtException.getCause());
            assertEquals("Expected and actual values should be the same.", openIDErrorCodes[i], openIDJwtException.getCode());
//            assertEquals("Expected and actual values should be the same.", messages[i], openIDJwtException.getMessage());
//            assertEquals("Expected and actual v/alues should be the same.", messages[i], openIDJwtException.getLocalizedMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDExceptionNullCodeParameterTest() {
        OpenIDJwtException openIDJwtException = new ActualOpenIDJwtException(null);
        assertNull("Null expected.", openIDJwtException.getCode());
        assertNull("Null expected.", openIDJwtException.getCause());
        openIDJwtException.getMessage();
    }

    @Test
    public void openIDJwtExtractionExceptionCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < openIDErrorCodes.length; i++) {
                OpenIDJwtException openIDJwtExtractionException = new ActualOpenIDJwtException(openIDErrorCodes[i], throwable, argument1, argument2);
                assertEquals("Expected and actual values should be the same.", openIDErrorCodes[i], openIDJwtExtractionException.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, openIDJwtExtractionException.getCause());
//                assertEquals("Expected and actual values should be the same.", messagesWithArgument[i], openIDJwtExtractionException.getMessage());
//                assertEquals("Expected and actual values should be the same.", messagesWithArgument[i], openIDJwtExtractionException.getLocalizedMessage());
            }
        }
    }

    @Test
    public void openIDJwtExtractionExceptionCodeCauseNullArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < openIDErrorCodes.length; i++) {
                OpenIDJwtException openIDJwtExtractionException = new ActualOpenIDJwtException(openIDErrorCodes[i], throwable, null);
                assertEquals("Expected and actual values should be the same.", openIDErrorCodes[i], openIDJwtExtractionException.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, openIDJwtExtractionException.getCause());
//                assertEquals("Expected and actual values should be the same.", messages[i], openIDJwtExtractionException.getMessage());
//                assertEquals("Expected and actual values should be the same.", messages[i], openIDJwtExtractionException.getLocalizedMessage());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDJwtExceptionNullCodeCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDJwtException openIdJwtException = new ActualOpenIDJwtException(null, throwable, argument1, argument2, argument3);
            assertEquals("Expected and actual values should be the same.", throwable, openIdJwtException.getCause());
            assertNull("Null expected.", openIdJwtException.getCode());
            openIdJwtException.getLocalizedMessage();
        }
    }
}