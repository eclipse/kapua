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
package org.eclipse.kapua.service.authentication.shiro.exceptions;

import org.eclipse.kapua.qa.markers.Categories;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class JwtCertificateNotFoundExceptionTest extends Assert {

    @Test
    public void jwtCertificateNotFoundExceptionTest() {
        JwtCertificateNotFoundException jwtCertificateNotFoundException = new JwtCertificateNotFoundException();
        assertEquals("Expected and actual values should be the same.", KapuaAuthenticationErrorCodes.JWT_CERTIFICATE_NOT_FOUND, jwtCertificateNotFoundException.getCode());
        assertEquals("Expected and actual values should be the same.", "Error: ", jwtCertificateNotFoundException.getMessage());
        assertNull("Null expected.", jwtCertificateNotFoundException.getCause());
    }
}