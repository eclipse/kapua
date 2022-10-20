/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class TransportTimeoutExceptionTest {

    Long[] values;
    String[] expectedMessage;

    @Before
    public void initialize() {
        values = new Long[]{1L, 3L, -1000L, -645361239L, 1235563423L, 0L, null};
        expectedMessage = new String[]{"The request has not received a response within the timeout of: 1ms", "The request has not received a response within the timeout of: 3ms",
                "The request has not received a response within the timeout of: -1,000ms", "The request has not received a response within the timeout of: -645,361,239ms",
                "The request has not received a response within the timeout of: 1,235,563,423ms", "The request has not received a response within the timeout of: 0ms",
                "The request has not received a response within the timeout of: nullms"};
    }

    @Test
    public void transportTimeoutExceptionTest() {
        for (int i = 0; i < values.length; i++) {
            TransportTimeoutException transportTimeoutException = new TransportTimeoutException(values[i]);
            Assert.assertEquals("Expected and actual values should be the same.", TransportErrorCodes.TIMEOUT, transportTimeoutException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", values[i], transportTimeoutException.getTimeout());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessage[i], transportTimeoutException.getMessage());
            Assert.assertNull("Null expected.", transportTimeoutException.getCause());
        }
    }

    @Test(expected = TransportTimeoutException.class)
    public void throwingTransportTimeoutExceptionTest() throws TransportTimeoutException {
        for (Long value : values) {
            throw new TransportTimeoutException(value);
        }
    }
}
