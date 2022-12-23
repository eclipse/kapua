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
package org.eclipse.kapua.service.authorization.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class KapuaAuthorizationExceptionTest {
    @Test
    public void kapuaAuthorizationErrorCodesTest() {
        for (KapuaAuthorizationErrorCodes errorCode : KapuaAuthorizationErrorCodes.values()) {
            KapuaAuthorizationException authorizationException = new KapuaAuthorizationException(errorCode);

            Assert.assertNotEquals(
                    "If this fails it means that KapuaAuthorizationErrorCodes." + errorCode + " does not have a message configured in the resource bundle: " + authorizationException.getKapuaErrorMessagesBundle(),
                    "Error: ",
                    authorizationException.getMessage()
            );
        }
    }

    @Test
    public void kapuaAuthorizationExceptionTest() {

        KapuaAuthorizationErrorCodes errorCode = KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED;

        Throwable throwable = new Throwable();

        String argument = "argument";

        // Code
        KapuaAuthorizationException exception = new KapuaAuthorizationException(errorCode, throwable, argument);

        Assert.assertEquals(errorCode, exception.getCode());
        Assert.assertEquals(throwable, exception.getCause());
        Assert.assertEquals("User does not have permission to perform this action. Required permission: argument.", exception.getMessage());
    }

    @Test
    public void kapuaAuthorizationExceptionTestNull() {
        // Code
        KapuaAuthorizationException exception = new KapuaAuthorizationException(null);

        Assert.assertNull(exception.getCode());
        Assert.assertEquals("Error: ", exception.getMessage());
        Assert.assertNull(exception.getCause());

        // Code, Throwable
        exception = new KapuaAuthorizationException(null, (Throwable) null);

        Assert.assertNull(exception.getCode());
        Assert.assertEquals("Error: ", exception.getMessage());
        Assert.assertNull(exception.getCause());

        // Code, Throwable, Args
        exception = new KapuaAuthorizationException(null, null, (Object) null);

        Assert.assertNull(exception.getCode());
        Assert.assertEquals("Error: null", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void internalUserOnlyExceptionTest() {
        InternalUserOnlyException exception = new InternalUserOnlyException();

        Assert.assertEquals(KapuaAuthorizationErrorCodes.INTERNAL_USER_ONLY, exception.getCode());
        Assert.assertEquals("This operation is reserved only for Users of type INTERNAL.", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void selfManagedOnlyExceptionTest() {
        SelfManagedOnlyException exception = new SelfManagedOnlyException();

        Assert.assertEquals(KapuaAuthorizationErrorCodes.SELF_MANAGED_ONLY, exception.getCode());
        Assert.assertEquals("This operation can be performed only on the current logged User.", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void subjectUnauthorizedExceptionTest() {
        Permission mockPermission = Mockito.mock(Permission.class);
        Mockito.when(mockPermission.toString()).thenReturn("a:mock:permission");

        SubjectUnauthorizedException exception = new SubjectUnauthorizedException(mockPermission);

        Assert.assertEquals(KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED, exception.getCode());
        Assert.assertEquals("User does not have permission to perform this action. Required permission: " + mockPermission.toString() + ".", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void subjectUnauthorizedExceptionTestNullPermission() {
        SubjectUnauthorizedException exception = new SubjectUnauthorizedException(null);

        Assert.assertEquals(KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED, exception.getCode());
        Assert.assertEquals("User does not have permission to perform this action. Required permission: null.", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }
}