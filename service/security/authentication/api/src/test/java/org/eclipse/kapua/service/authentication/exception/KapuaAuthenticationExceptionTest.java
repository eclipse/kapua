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
package org.eclipse.kapua.service.authentication.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaAuthenticationExceptionTest {
    @Test
    public void kapuaKapuaAuthenticationErrorCodesTest() {
        for (KapuaAuthenticationErrorCodes errorCode : KapuaAuthenticationErrorCodes.values()) {
            KapuaAuthenticationException authenticationException = new KapuaAuthenticationException(errorCode);

            Assert.assertNotEquals(
                    "If this fails it means that KapuaKapuaAuthenticationErrorCodes." + errorCode + " does not have a message configured in the resource bundle: " + authenticationException.getKapuaErrorMessagesBundle(),
                    "Error: ",
                    authenticationException.getMessage()
            );
        }
    }

    @Test
    public void kapuaAuthenticationExceptionTest() {

        KapuaAuthenticationErrorCodes errorCode = KapuaAuthenticationErrorCodes.PASSWORD_INVALID_LENGTH;

        Throwable throwable = new Throwable();

        String argument1 = "argument";
        String argument2 = "argument";

        // Code
        KapuaAuthenticationException exception = new KapuaAuthenticationException(errorCode, throwable, argument1, argument2);

        Assert.assertEquals(errorCode, exception.getCode());
        Assert.assertEquals(throwable, exception.getCause());
        Assert.assertEquals("Password length must be between argument and argument characters long (inclusive).", exception.getMessage());
    }

    @Test
    public void kapuaAuthenticationExceptionTestNull() {
        // Code
        KapuaAuthenticationException exception = new KapuaAuthenticationException(null);

        Assert.assertNull(exception.getCode());
        Assert.assertEquals("Error: ", exception.getMessage());
        Assert.assertNull(exception.getCause());

        // Code, Throwable
        exception = new KapuaAuthenticationException(null, (Throwable) null);

        Assert.assertNull(exception.getCode());
        Assert.assertEquals("Error: ", exception.getMessage());
        Assert.assertNull(exception.getCause());

        // Code, Throwable, Args
        exception = new KapuaAuthenticationException(null, null, (Object) null);

        Assert.assertNull(exception.getCode());
        Assert.assertEquals("Error: null", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void duplicatedPasswordCredentialExceptionTest() {
        DuplicatedPasswordCredentialException exception = new DuplicatedPasswordCredentialException();

        Assert.assertEquals(KapuaAuthenticationErrorCodes.DUPLICATED_PASSWORD_CREDENTIAL, exception.getCode());
        Assert.assertEquals("The user already has a Credential of type PASSWORD.", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void subjectUnauthorizedExceptionTest() {
        PasswordLengthException exception = new PasswordLengthException(5, 10);

        Assert.assertEquals(KapuaAuthenticationErrorCodes.PASSWORD_INVALID_LENGTH, exception.getCode());
        Assert.assertEquals("Password length must be between 5 and 10 characters long (inclusive).", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }
}