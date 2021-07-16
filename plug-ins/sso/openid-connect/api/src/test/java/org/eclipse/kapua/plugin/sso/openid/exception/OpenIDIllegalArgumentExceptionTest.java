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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class OpenIDIllegalArgumentExceptionTest extends Assert {

    String[] argumentName, argumentValue, messagesWithArgument;
    OpenIDErrorCodes[] openIDErrorCodes;

    @Before
    public void setUp() {
        argumentName = new String[]{"Argument Name", "", null};
        argumentValue = new String[]{"Argument Value", "", null};
        openIDErrorCodes = new OpenIDErrorCodes[]{OpenIDErrorCodes.LOGIN_URI_ERROR, OpenIDErrorCodes.LOGOUT_URI_ERROR, OpenIDErrorCodes.TOKEN_ERROR, OpenIDErrorCodes.JWT_EXTRACTION_ERROR,
                OpenIDErrorCodes.JWT_PROCESS_ERROR, OpenIDErrorCodes.JWT_URI_ERROR, OpenIDErrorCodes.ILLEGAL_ARGUMENT, OpenIDErrorCodes.ILLEGAL_URI};
    }

    @Test
    public void openIDIllegalArgumentExceptionArgumentNameAndValueParameterTest() {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                OpenIDIllegalArgumentException openIDIllegalArgumentException = new OpenIDIllegalArgumentException(name, value);
                assertEquals("Expected and actual values should be the same!", OpenIDErrorCodes.ILLEGAL_ARGUMENT, openIDIllegalArgumentException.getCode());
                assertEquals("Expected and actual values should be the same!", name, openIDIllegalArgumentException.getArgumentName());
                assertEquals("Expected and actual values should be the same!", value, openIDIllegalArgumentException.getArgumentValue());
                assertEquals("Expected and actual values should be the same!", "An illegal value was provided for the argument " + name + ": " + value + ".", openIDIllegalArgumentException.getMessage());
                assertNull(openIDIllegalArgumentException.getCause());
            }
        }
    }

    @Test
    public void openIDIllegalArgumentExceptionCodeArgumentNameAndValueParameterTest() {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                for (int i = 0; i < openIDErrorCodes.length; i++) {
                    OpenIDIllegalArgumentException openIDIllegalArgumentException = new OpenIDIllegalArgumentException(openIDErrorCodes[i], name, value);
                    messagesWithArgument = new String[]{"An error occurred while retrieving the OpenID Connect login URI", "An error occurred while retrieving the OpenID Connect logout URI", "An error occurred while getting the access token",
                            "An error occurred while extracting the Jwt from the string: " + name, "An error occurred while processing the Jwt: " + name, "An error occurred while retrieving the OpenID Connect Jwt URI",
                            "An illegal value was provided for the argument " + name + ": " + value + ".", "An illegal value was provided for the URI " + name + ": " + value + "."};
                    assertEquals("Expected and actual values should be the same!", openIDErrorCodes[i], openIDIllegalArgumentException.getCode());
                    assertEquals("Expected and actual values should be the same!", name, openIDIllegalArgumentException.getArgumentName());
                    assertEquals("Expected and actual values should be the same!", value, openIDIllegalArgumentException.getArgumentValue());
//                    assertEquals("Expected and actual values should be the same!", messagesWithArgument[i], openIDIllegalArgumentException.getMessage());
                    assertNull(openIDIllegalArgumentException.getCause());
                }
            }
        }
    }
}