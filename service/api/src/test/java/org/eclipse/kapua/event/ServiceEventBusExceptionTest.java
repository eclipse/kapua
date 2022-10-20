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
package org.eclipse.kapua.event;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ServiceEventBusExceptionTest {

    @Test
    public void serviceEventBusExceptionThrowableCauseTest() {
        Throwable[] throwables = {new Throwable(), null};

        for (Throwable throwable : throwables) {
            ServiceEventBusException serviceEventBusException = new ServiceEventBusException(throwable);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.INTERNAL_ERROR, serviceEventBusException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", throwable, serviceEventBusException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", "An internal error occurred: {0}.", serviceEventBusException.getMessage());
        }
    }

    @Test
    public void serviceEventBusExceptionStringMessageTest() {
        String[] messages = {"message", null};

        for (String message : messages) {
            ServiceEventBusException serviceEventBusException = new ServiceEventBusException(message);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.INTERNAL_ERROR, serviceEventBusException.getCode());
            Assert.assertNull("Null expected.", serviceEventBusException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", "An internal error occurred: " + message + ".", serviceEventBusException.getMessage());
        }
    }
}
