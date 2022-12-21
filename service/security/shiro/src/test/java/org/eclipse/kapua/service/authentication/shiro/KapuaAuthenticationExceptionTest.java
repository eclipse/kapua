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
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaAuthenticationExceptionTest {

    KapuaAuthenticationErrorCodes[] kapuaAuthenticationErrorCodes;
    String kapuaErrorMessageBundle, expectedMessageWithoutArguments, expectedMessageWithArguments;
    Object stringObject, intObject, charObject;
    Throwable[] throwables;

    @Before
    public void initialize() {
        kapuaAuthenticationErrorCodes = new KapuaAuthenticationErrorCodes[]{null, KapuaAuthenticationErrorCodes.SUBJECT_ALREADY_LOGGED, KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED, KapuaAuthenticationErrorCodes.AUTHENTICATION_ERROR,
                KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL, KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS, KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS,
                KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL, KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL, KapuaAuthenticationErrorCodes.UNKNOWN_SESSION_CREDENTIAL, KapuaAuthenticationErrorCodes.INVALID_SESSION_CREDENTIALS,
                KapuaAuthenticationErrorCodes.EXPIRED_SESSION_CREDENTIALS, KapuaAuthenticationErrorCodes.LOCKED_SESSION_CREDENTIAL, KapuaAuthenticationErrorCodes.DISABLED_SESSION_CREDENTIAL, KapuaAuthenticationErrorCodes.JWK_FILE_ERROR,
                KapuaAuthenticationErrorCodes.REFRESH_ERROR, KapuaAuthenticationErrorCodes.JWK_GENERATION_ERROR, KapuaAuthenticationErrorCodes.JWT_CERTIFICATE_NOT_FOUND, KapuaAuthenticationErrorCodes.PASSWORD_CANNOT_BE_CHANGED};
        kapuaErrorMessageBundle = "kapua-authentication-service-error-messages";
        expectedMessageWithoutArguments = "Error: ";
        stringObject = "String Object";
        intObject = 10;
        charObject = 'c';
        expectedMessageWithArguments = "Error: " + stringObject + ", " + intObject + ", " + charObject;
        throwables = new Throwable[]{null, new Throwable(), new Throwable(new Exception()), new Throwable("message")};
    }

    @Test
    public void kapuaAuthenticationExceptionCodeParameterTest() {
        for (KapuaAuthenticationErrorCodes kapuaAuthenticationErrorCode : kapuaAuthenticationErrorCodes) {
            KapuaAuthenticationException kapuaAuthenticationException = new KapuaAuthenticationException(kapuaAuthenticationErrorCode);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthenticationErrorCode, kapuaAuthenticationException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithoutArguments, kapuaAuthenticationException.getMessage());
            Assert.assertNull("Null expected.", kapuaAuthenticationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthenticationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthenticationExceptionCodeArgumentsParametersTest() {
        for (KapuaAuthenticationErrorCodes kapuaAuthenticationErrorCode : kapuaAuthenticationErrorCodes) {
            KapuaAuthenticationException kapuaAuthenticationException = new KapuaAuthenticationException(kapuaAuthenticationErrorCode, stringObject, intObject, charObject);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthenticationErrorCode, kapuaAuthenticationException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithArguments, kapuaAuthenticationException.getMessage());
            Assert.assertNull("Null expected.", kapuaAuthenticationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthenticationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthenticationExceptionCodeNullArgumentsParametersTest() {
        for (KapuaAuthenticationErrorCodes kapuaAuthenticationErrorCode : kapuaAuthenticationErrorCodes) {
            KapuaAuthenticationException kapuaAuthenticationException = new KapuaAuthenticationException(kapuaAuthenticationErrorCode, null);
            Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthenticationErrorCode, kapuaAuthenticationException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithoutArguments, kapuaAuthenticationException.getMessage());
            Assert.assertNull("Null expected.", kapuaAuthenticationException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthenticationException.getKapuaErrorMessagesBundle());
        }
    }

    @Test
    public void kapuaAuthenticationExceptionCodeCauseArgumentsParametersTest() {
        for (KapuaAuthenticationErrorCodes kapuaAuthenticationErrorCode : kapuaAuthenticationErrorCodes) {
            for (Throwable throwable : throwables) {
                KapuaAuthenticationException kapuaAuthenticationException = new KapuaAuthenticationException(kapuaAuthenticationErrorCode, throwable, stringObject, intObject, charObject);
                Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthenticationErrorCode, kapuaAuthenticationException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithArguments, kapuaAuthenticationException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthenticationException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthenticationException.getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test
    public void kapuaAuthenticationExceptionCodeCauseNullArgumentsParametersTest() {
        for (KapuaAuthenticationErrorCodes kapuaAuthenticationErrorCode : kapuaAuthenticationErrorCodes) {
            for (Throwable throwable : throwables) {
                KapuaAuthenticationException kapuaAuthenticationException = new KapuaAuthenticationException(kapuaAuthenticationErrorCode, throwable, null);
                Assert.assertEquals("Expected and actual values should be the same.", kapuaAuthenticationErrorCode, kapuaAuthenticationException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithoutArguments, kapuaAuthenticationException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaAuthenticationException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessageBundle, kapuaAuthenticationException.getKapuaErrorMessagesBundle());
            }
        }
    }
}