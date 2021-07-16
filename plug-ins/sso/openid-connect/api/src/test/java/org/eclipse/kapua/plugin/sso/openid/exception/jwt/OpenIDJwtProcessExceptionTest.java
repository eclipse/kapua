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

import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDErrorCodes;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class OpenIDJwtProcessExceptionTest extends Assert {

    Throwable[] throwables;
    String argument1, argument2, argument3;

    @Before
    public void setUp() {
        throwables = new Throwable[]{null, new Throwable()};
        argument1 = "arg1";
        argument2 = "arg2";
        argument3 = "arg3";
    }

    @Test
    public void openIDJwtProcessExceptionCauseArgumentParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDJwtProcessException openIDJwtProcessException = new OpenIDJwtProcessException(throwable, argument1, argument2);

            assertEquals("Expected and actual values should be the same.", OpenIDErrorCodes.JWT_PROCESS_ERROR, openIDJwtProcessException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, openIDJwtProcessException.getCause());
            assertEquals("Expected and actual values should be the same.", "An error occurred while processing the Jwt: " + argument1, openIDJwtProcessException.getMessage());
            assertEquals("Expected and actual values should be the same.", "An error occurred while processing the Jwt: " + argument1, openIDJwtProcessException.getLocalizedMessage());
        }
    }

    @Test
    public void openIDJwtProcessExceptionCauseNullArgumentParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDJwtProcessException openIDJwtProcessException = new OpenIDJwtProcessException(throwable, null);
            assertEquals("Expected and actual values should be the same.", OpenIDErrorCodes.JWT_PROCESS_ERROR, openIDJwtProcessException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, openIDJwtProcessException.getCause());
            assertEquals("Expected and actual values should be the same.", "An error occurred while processing the Jwt: {0}", openIDJwtProcessException.getMessage());
            assertEquals("Expected and actual values should be the same.", "An error occurred while processing the Jwt: {0}", openIDJwtProcessException.getLocalizedMessage());
        }
    }
}