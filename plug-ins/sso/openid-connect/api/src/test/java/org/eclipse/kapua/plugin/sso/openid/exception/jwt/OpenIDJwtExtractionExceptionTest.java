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
public class OpenIDJwtExtractionExceptionTest extends Assert {

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
    public void openIDJwtExtractionExcepCauseArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDJwtExtractionException openIDJwtExtractionException = new OpenIDJwtExtractionException(throwable, argument1, argument2);

            assertEquals("Expected and actual values should be the same.", OpenIDErrorCodes.JWT_EXTRACTION_ERROR, openIDJwtExtractionException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, openIDJwtExtractionException.getCause());
            assertEquals("Expected and actual values should be the same.", "An error occurred while extracting the Jwt from the string: " + argument1, openIDJwtExtractionException.getMessage());
            assertEquals("Expected and actual values should be the same.", "An error occurred while extracting the Jwt from the string: " + argument1, openIDJwtExtractionException.getLocalizedMessage());
        }
    }

    @Test
    public void openIDJwtExtractionExceptionCauseNullArgumentsParametersTest() {
        for (Throwable throwable : throwables) {
            OpenIDJwtExtractionException openIDJwtExtractionException = new OpenIDJwtExtractionException(throwable, null);

            assertEquals("Expected and actual values should be the same.", OpenIDErrorCodes.JWT_EXTRACTION_ERROR, openIDJwtExtractionException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, openIDJwtExtractionException.getCause());
            assertEquals("Expected and actual values should be the same.", "An error occurred while extracting the Jwt from the string: {0}", openIDJwtExtractionException.getMessage());
            assertEquals("Expected and actual values should be the same.", "An error occurred while extracting the Jwt from the string: {0}", openIDJwtExtractionException.getLocalizedMessage());
        }
    }
}