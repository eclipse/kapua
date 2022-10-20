/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class JwtCertificateNotFoundExceptionTest {

    @Test
    public void jwtCertificateNotFoundExceptionTest() {
        JwtCertificateNotFoundException jwtCertificateNotFoundException = new JwtCertificateNotFoundException();
        Assert.assertEquals("Expected and actual values should be the same.", KapuaAuthenticationErrorCodes.JWT_CERTIFICATE_NOT_FOUND, jwtCertificateNotFoundException.getCode());
        Assert.assertEquals("Expected and actual values should be the same.", "Error: ", jwtCertificateNotFoundException.getMessage());
        Assert.assertNull("Null expected.", jwtCertificateNotFoundException.getCause());
    }
}