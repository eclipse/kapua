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
public class OpenIDLoginUriExceptionTest extends Assert {

    @Test
    public void openIDLoginUriExceptionCauseTest() {
        Throwable throwable = new Throwable();
        OpenIDLoginUriException openIDLoginUriException = new OpenIDLoginUriException(throwable);
        assertEquals("Expected and actual values should be the same!", OpenIDErrorCodes.LOGIN_URI_ERROR, openIDLoginUriException.getCode());
        assertEquals("Expected and actual values should be the same!", throwable, openIDLoginUriException.getCause());
        assertEquals("Expected and actual values should be the same!", "An error occurred while retrieving the OpenID Connect login URI", openIDLoginUriException.getMessage());
    }

    @Test
    public void openIDLoginUriExceptionNullCauseTest() {
        OpenIDLoginUriException openIDLoginUriException = new OpenIDLoginUriException(null);
        assertEquals("Expected and actual values should be the same!", OpenIDErrorCodes.LOGIN_URI_ERROR, openIDLoginUriException.getCode());
        assertEquals("Expected and actual values should be the same!", null, openIDLoginUriException.getCause());
        assertEquals("Expected and actual values should be the same!", "An error occurred while retrieving the OpenID Connect login URI", openIDLoginUriException.getMessage());
    }
}