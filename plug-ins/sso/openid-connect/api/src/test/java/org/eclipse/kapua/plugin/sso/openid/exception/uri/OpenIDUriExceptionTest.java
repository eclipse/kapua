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
package org.eclipse.kapua.plugin.sso.openid.exception.uri;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDErrorCodes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class OpenIDUriExceptionTest extends Assert {

    Throwable[] throwables;
    String argument1, argument2, argument3;
    OpenIDErrorCodes[] openIDErrorCodes;
    String[] messages, messagesWithArgument;

    private class ActualOpenIDUriException extends OpenIDUriException {

        public ActualOpenIDUriException(KapuaErrorCode code, Throwable cause, Object... arguments) {
            super(code, cause, arguments);
        }
    }

    @Before
    public void setUp() {
        throwables = new Throwable[]{null, new Throwable()};
        argument1 = "arg1";
        argument2 = "arg2";
        argument3 = "arg3";
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
    public void openIDUriExceptionCodeCauseArgumentParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < openIDErrorCodes.length; i++) {
                OpenIDUriException openIDUriException = new ActualOpenIDUriException(openIDErrorCodes[i], throwable, argument1, argument2);
                assertEquals("Expected and actual values should be the same.", openIDErrorCodes[i], openIDUriException.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, openIDUriException.getCause());
//                assertEquals("Expected and actual values should be the same.", messagesWithArgument[i], openIDUriException.getMessage());
//                assertEquals("Expected and actual values should be the same.", messagesWithArgument[i], openIDUriException.getLocalizedMessage());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDUriExceptionNullCodeCauseArgumentParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDUriException openIDUriException = new ActualOpenIDUriException(null, throwable, argument1, argument2, argument3);
            assertEquals("Expected and actual values should be the same.", throwable, openIDUriException.getCause());
            assertNull("Null expected.", openIDUriException.getCode());
            openIDUriException.getMessage();
        }
    }

    @Test
    public void openIDUriExceptionCodeCauseNullArgumentParametersTest() {
        for (Throwable throwable : throwables) {
            for (int i = 0; i < openIDErrorCodes.length; i++) {
                OpenIDUriException openIDUriException = new ActualOpenIDUriException(openIDErrorCodes[i], throwable, null);

                assertEquals("Expected and actual values should be the same.", openIDErrorCodes[i], openIDUriException.getCode());
                assertEquals("Expected and actual values should be the same.", throwable, openIDUriException.getCause());
//                assertEquals("Expected and actual values should be the same.", messages[i], openIDUriException.getMessage());
//                assertEquals("Expected and actual values should be the same.", messages[i], openIDUriException.getLocalizedMessage());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void openIDUriExceptionNullCodeCauseNullArgumentParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDUriException openIDUriException = new ActualOpenIDUriException(null, throwable, null);
            assertEquals("Expected and actual values should be the same.", throwable, openIDUriException.getCause());
            assertNull("Null expected.", openIDUriException.getCode());
            openIDUriException.getMessage();
        }
    }
}