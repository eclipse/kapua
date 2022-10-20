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
package org.eclipse.kapua.service.authorization.shiro.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class SubjectUnauthorizedExceptionTest {

    Permission permission, newPermission;
    SubjectUnauthorizedException subjectUnauthorizedException;
    String kapuaErrorMessage;

    @Before
    public void initialize() {
        permission = Mockito.mock(Permission.class);
        newPermission = Mockito.mock(Permission.class);
        subjectUnauthorizedException = new SubjectUnauthorizedException(permission);
        kapuaErrorMessage = "kapua-service-error-messages";
    }

    @Test
    public void subjectUnauthorizedExceptionTest() {
        Assert.assertEquals("Expected and actual values should be the same.", KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED, subjectUnauthorizedException.getCode());
        Assert.assertNull("Null expected.", subjectUnauthorizedException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", "User does not have permission to perform this action. Required permission: " + permission + ".", subjectUnauthorizedException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same.", permission, subjectUnauthorizedException.getPermission());
        Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, subjectUnauthorizedException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void subjectUnauthorizedExceptionNullTest() {
        subjectUnauthorizedException = new SubjectUnauthorizedException(null);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED, subjectUnauthorizedException.getCode());
        Assert.assertNull("Null expected.", subjectUnauthorizedException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", "User does not have permission to perform this action. Required permission: null.", subjectUnauthorizedException.getMessage());
        Assert.assertNull("Null expected.", subjectUnauthorizedException.getPermission());
        Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, subjectUnauthorizedException.getKapuaErrorMessagesBundle());
    }
}