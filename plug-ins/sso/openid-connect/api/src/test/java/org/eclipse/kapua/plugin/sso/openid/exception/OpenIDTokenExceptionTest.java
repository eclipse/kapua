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
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class OpenIDTokenExceptionTest extends Assert {

    @Test
    public void openIDTokenExceptionTest() {
        Throwable throwable = new Throwable();
        OpenIDTokenException openIDTokenException = new OpenIDTokenException(throwable);
        assertEquals("Expected and actual values should be the same!", OpenIDErrorCodes.TOKEN_ERROR, openIDTokenException.getCode());
        assertEquals("Expected and actual values should be the same!", throwable, openIDTokenException.getCause());
//        assertEquals("Expected and actual values should be the same!", "An error occurred while getting the access token", openIDTokenException.getMessage());
    }

    @Test
    public void openIDTokenExceptionNullCauseTest() {
        OpenIDTokenException openIDTokenException = new OpenIDTokenException(null);
        assertEquals("Expected and actual values should be the same!", OpenIDErrorCodes.TOKEN_ERROR, openIDTokenException.getCode());
        assertNull("Null expected.", openIDTokenException.getCause());
//        assertEquals("Expected and actual values should be the same!", "An error occurred while getting the access token", openIDTokenException.getMessage());
    }
}