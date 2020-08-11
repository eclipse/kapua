/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class TransportTimeoutExceptionTest extends Assert {

    @Test
    public void getTimeoutTest() {
        long[] values = {1, 3, -1000, -645361239, 1235563423, 0};
        for (long value : values) {
            TransportTimeoutException exception = new TransportTimeoutException(value);
            assertEquals(value, exception.getTimeout().longValue());
        }
    }

    @Test
    public void getTimeoutIfNullTest() {
        TransportTimeoutException exception = new TransportTimeoutException(null);
        assertNull(exception.getTimeout());
    }
}
