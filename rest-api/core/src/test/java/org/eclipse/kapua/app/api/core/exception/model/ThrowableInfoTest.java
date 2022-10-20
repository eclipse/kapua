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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.core.Response;


@Category(JUnitTests.class)
public class ThrowableInfoTest {

    Response.Status[] statusList;
    int[] expectedStatusCodes;
    Throwable[] throwables;
    String[] expectedMessage;

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
        throwables = new Throwable[]{new Throwable(), new Throwable("message")};
        expectedMessage = new String[]{null, "message"};
    }

    @Test
    public void throwableInfoWithoutParametersTest() {
        ThrowableInfo throwableInfo = new ThrowableInfo();

        Assert.assertEquals("Expected and actual values should be the same.", 0, throwableInfo.getHttpErrorCode());
        Assert.assertNull("Null expected.", throwableInfo.getMessage());
        Assert.assertNull("Null expected.", throwableInfo.getStackTrace());
    }

    @Test
    public void throwableInfoWithParametersFalseStackTraceTest() {
        for (int i = 0; i < statusList.length; i++) {
            for (int j = 0; j < throwables.length; j++) {
                ThrowableInfo throwableInfo = new ThrowableInfo(statusList[i], throwables[j]);

                Assert.assertEquals("Expected and actual values should be the same.", expectedStatusCodes[i], throwableInfo.getHttpErrorCode());
                Assert.assertEquals("Expected and actual values should be the same.", expectedMessage[j], throwableInfo.getMessage());
                Assert.assertNull("Null expected.", throwableInfo.getStackTrace());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void throwableInfoNullStatusTest() {
        new ThrowableInfo(null, new Throwable());
    }

    @Test
    public void throwableInfoNullThrowableTest() {
        for (int i = 0; i < statusList.length; i++) {
            ThrowableInfo throwableInfo = new ThrowableInfo(statusList[i], null);
            Assert.assertEquals("Expected and actual values should be the same.", expectedStatusCodes[i], throwableInfo.getHttpErrorCode());
            Assert.assertNull("Null expected.", throwableInfo.getMessage());
            Assert.assertNull("Null expected.", throwableInfo.getStackTrace());
        }
    }

    @Test
    public void setAndGetHttpErrorCodeTest() {
        ThrowableInfo throwableInfo1 = new ThrowableInfo();
        ThrowableInfo throwableInfo2 = new ThrowableInfo(Response.Status.OK, new Throwable());

        for (int i = 0; i < statusList.length; i++) {
            throwableInfo1.setHttpErrorCode(statusList[i]);
            Assert.assertEquals("Expected and actual values should be the same.", expectedStatusCodes[i], throwableInfo1.getHttpErrorCode());
        }

        try {
            throwableInfo1.setHttpErrorCode(null);
            Assert.fail("Exception expected.");
        } catch (Exception e) {
            Assert.assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }

        for (int i = 0; i < statusList.length; i++) {
            throwableInfo2.setHttpErrorCode(statusList[i]);
            Assert.assertEquals("Expected and actual values should be the same.", expectedStatusCodes[i], throwableInfo2.getHttpErrorCode());
        }

        try {
            throwableInfo2.setHttpErrorCode(null);
            Assert.fail("Exception expected.");
        } catch (Exception e) {
            Assert.assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void setAndGetMessageTest() {
        ThrowableInfo throwableInfo1 = new ThrowableInfo();
        ThrowableInfo throwableInfo2 = new ThrowableInfo(Response.Status.OK, new Throwable());
        String[] messages = {null, "", "mess_age", "message#123", "@message-333", "message<9>,", "(1*2)m,."};

        for (int i = 0; i < messages.length; i++) {
            throwableInfo1.setMessage(messages[i]);
            Assert.assertEquals("Expected and actual values should be the same.", messages[i], throwableInfo1.getMessage());
        }

        for (int i = 0; i < messages.length; i++) {
            throwableInfo2.setMessage(messages[i]);
            Assert.assertEquals("Expected and actual values should be the same.", messages[i], throwableInfo2.getMessage());
        }
    }
} 