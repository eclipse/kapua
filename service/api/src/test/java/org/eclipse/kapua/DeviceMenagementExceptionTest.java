/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class DeviceMenagementExceptionTest extends Assert {

    DeviceMenagementErrorCodes[] deviceMenagementErrorCodes;

    @Before
    public void initialize() {
        deviceMenagementErrorCodes = new DeviceMenagementErrorCodes[]{DeviceMenagementErrorCodes.DEVICE_NEVER_CONNECTED,
                DeviceMenagementErrorCodes.DEVICE_NOT_CONNECTED, DeviceMenagementErrorCodes.REQUEST_BAD_METHOD};
    }

    @Test
    public void deviceMenagementExceptionTest() {
        for (DeviceMenagementErrorCodes code : deviceMenagementErrorCodes) {
            DeviceMenagementException deviceMenagementException = new DeviceMenagementException(code);
            assertEquals("Expected and actual values should be the same.", code, deviceMenagementException.getCode());
            assertNull("Null expected.", deviceMenagementException.getCause());
            assertEquals("Expected and actual values should be the same.", "Error: ", deviceMenagementException.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void deviceMenagementExceptionNullParameterTest() {
        DeviceMenagementException deviceMenagementException = new DeviceMenagementException(null);
        assertNull("Null expected.", deviceMenagementException.getCode());
        assertNull("Null expected.", deviceMenagementException.getCause());
        deviceMenagementException.getMessage();
    }

    @Test(expected = DeviceMenagementException.class)
    public void throwingExceptionTest() throws DeviceMenagementException {
        for (DeviceMenagementErrorCodes code : deviceMenagementErrorCodes) {
            throw new DeviceMenagementException(code);
        }
    }

    @Test(expected = DeviceMenagementException.class)
    public void throwingExceptionNullParameterTest() throws DeviceMenagementException {
        throw new DeviceMenagementException(null);
    }
} 