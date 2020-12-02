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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDErrorCodes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class OpenIDJwtUriExceptionTest extends Assert {

    @Test
    public void openIDJwtUriExceptionCauseTest() {
        Throwable throwable = new Throwable();
        OpenIDJwtUriException openIDJwtUriException = new OpenIDJwtUriException(throwable);
        assertEquals("Expected and actual values should be the same!", OpenIDErrorCodes.JWT_URI_ERROR, openIDJwtUriException.getCode());
        assertEquals("Expected and actual values should be the same!", throwable, openIDJwtUriException.getCause());
        assertEquals("Expected and actual values should be the same!", "An error occurred while retrieving the OpenID Connect Jwt URI", openIDJwtUriException.getMessage());
    }

    @Test
    public void openIDJwtUriExceptionNullCauseTest() {
        OpenIDJwtUriException openIDJwtUriException = new OpenIDJwtUriException(null);
        assertEquals("Expected and actual values should be the same!", OpenIDErrorCodes.JWT_URI_ERROR, openIDJwtUriException.getCode());
        assertNull("Null expected.", openIDJwtUriException.getCause());
        assertEquals("Expected and actual values should be the same!", "An error occurred while retrieving the OpenID Connect Jwt URI", openIDJwtUriException.getMessage());
    }
}