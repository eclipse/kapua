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
package org.eclipse.kapua.app.api.core.exception.model;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.core.Response;


@Category(JUnitTests.class)
public class MfaRequiredExceptionInfoTest {

    Response.Status[] statusList;
    int[] expectedStatusCodes;
    KapuaAuthenticationErrorCodes[] errorCodes;
    KapuaAuthenticationException kapuaException;
    String[] expectedKapuaErrorCode;

    @Before
    public void initialize() {
        statusList = new Response.Status[]{Response.Status.OK, Response.Status.CREATED, Response.Status.ACCEPTED, Response.Status.NO_CONTENT,
                Response.Status.RESET_CONTENT, Response.Status.PARTIAL_CONTENT, Response.Status.MOVED_PERMANENTLY, Response.Status.FOUND,
                Response.Status.SEE_OTHER, Response.Status.NOT_MODIFIED, Response.Status.USE_PROXY, Response.Status.TEMPORARY_REDIRECT,
                Response.Status.BAD_REQUEST, Response.Status.UNAUTHORIZED, Response.Status.PAYMENT_REQUIRED, Response.Status.FORBIDDEN,
                Response.Status.NOT_FOUND, Response.Status.METHOD_NOT_ALLOWED, Response.Status.NOT_ACCEPTABLE, Response.Status.PROXY_AUTHENTICATION_REQUIRED,
                Response.Status.REQUEST_TIMEOUT, Response.Status.CONFLICT, Response.Status.GONE, Response.Status.LENGTH_REQUIRED,
                Response.Status.PRECONDITION_FAILED, Response.Status.REQUEST_ENTITY_TOO_LARGE, Response.Status.REQUEST_URI_TOO_LONG, Response.Status.UNSUPPORTED_MEDIA_TYPE,
                Response.Status.REQUESTED_RANGE_NOT_SATISFIABLE, Response.Status.EXPECTATION_FAILED, Response.Status.INTERNAL_SERVER_ERROR, Response.Status.NOT_IMPLEMENTED,
                Response.Status.BAD_GATEWAY, Response.Status.SERVICE_UNAVAILABLE, Response.Status.GATEWAY_TIMEOUT, Response.Status.HTTP_VERSION_NOT_SUPPORTED};
        expectedStatusCodes = new int[]{200, 201, 202, 204, 205, 206, 301, 302, 303, 304, 305, 307, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 500, 501, 502, 503, 504, 505};
        errorCodes = new KapuaAuthenticationErrorCodes[]{KapuaAuthenticationErrorCodes.SUBJECT_ALREADY_LOGGED, KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED,
                KapuaAuthenticationErrorCodes.AUTHENTICATION_ERROR, KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS,
                KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS, KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL, KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL,
                KapuaAuthenticationErrorCodes.UNKNOWN_SESSION_CREDENTIAL, KapuaAuthenticationErrorCodes.INVALID_SESSION_CREDENTIALS, KapuaAuthenticationErrorCodes.EXPIRED_SESSION_CREDENTIALS,
                KapuaAuthenticationErrorCodes.LOCKED_SESSION_CREDENTIAL, KapuaAuthenticationErrorCodes.DISABLED_SESSION_CREDENTIAL, KapuaAuthenticationErrorCodes.JWK_FILE_ERROR,
                KapuaAuthenticationErrorCodes.REFRESH_ERROR, KapuaAuthenticationErrorCodes.JWK_GENERATION_ERROR, KapuaAuthenticationErrorCodes.JWT_CERTIFICATE_NOT_FOUND,
                KapuaAuthenticationErrorCodes.PASSWORD_CANNOT_BE_CHANGED, KapuaAuthenticationErrorCodes.REQUIRE_MFA_CREDENTIALS};
        expectedKapuaErrorCode = new String[]{"SUBJECT_ALREADY_LOGGED", "INVALID_CREDENTIALS_TYPE_PROVIDED", "AUTHENTICATION_ERROR", "CREDENTIAL_CRYPT_ERROR", "INVALID_LOGIN_CREDENTIALS",
                "EXPIRED_LOGIN_CREDENTIALS", "LOCKED_LOGIN_CREDENTIAL", "DISABLED_LOGIN_CREDENTIAL", "UNKNOWN_SESSION_CREDENTIAL", "INVALID_SESSION_CREDENTIALS", "EXPIRED_SESSION_CREDENTIALS",
                "LOCKED_SESSION_CREDENTIAL", "DISABLED_SESSION_CREDENTIAL", "JWK_FILE_ERROR", "REFRESH_ERROR", "JWK_GENERATION_ERROR", "JWT_CERTIFICATE_NOT_FOUND",
                "PASSWORD_CANNOT_BE_CHANGED", "REQUIRE_MFA_CREDENTIALS"};
    }

    @Test
    public void mfaRequiredExceptionInfoWithoutParametersTest() {
        MfaRequiredExceptionInfo mfaRequiredExceptionInfo = new MfaRequiredExceptionInfo();

        Assert.assertNull("Null expected.", mfaRequiredExceptionInfo.getKapuaErrorCode());
        Assert.assertEquals("Expected and actual values should be the same.", 0, mfaRequiredExceptionInfo.getHttpErrorCode());
        Assert.assertNull("Null expected.", mfaRequiredExceptionInfo.getMessage());
    }

    @Test
    public void mfaRequiredExceptionInfoStatusExceptionTest() {
        for (int i = 0; i < statusList.length; i++) {
            for (int j = 0; j < errorCodes.length; j++) {
                kapuaException = new KapuaAuthenticationException(errorCodes[j]);

                MfaRequiredExceptionInfo mfaRequiredExceptionInfo = new MfaRequiredExceptionInfo(statusList[i], kapuaException);
                Assert.assertEquals("Expected and actual values should be the same.", expectedKapuaErrorCode[j], mfaRequiredExceptionInfo.getKapuaErrorCode());
                Assert.assertEquals("Expected and actual values should be the same.", expectedStatusCodes[i], mfaRequiredExceptionInfo.getHttpErrorCode());
                Assert.assertEquals("Expected and actual values should be the same.", "Error: ", mfaRequiredExceptionInfo.getMessage());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void mfaRequiredExceptionInfoNullStatusExceptionTest() {
        kapuaException = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.SUBJECT_ALREADY_LOGGED);
        new MfaRequiredExceptionInfo(null, kapuaException);
    }

    @Test(expected = NullPointerException.class)
    public void mfaRequiredExceptionInfoStatusNullExceptionTest() {
        new MfaRequiredExceptionInfo(Response.Status.OK, null);
    }
} 