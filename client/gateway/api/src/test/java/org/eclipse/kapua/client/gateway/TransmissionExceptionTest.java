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
package org.eclipse.kapua.client.gateway;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class TransmissionExceptionTest {

    TransmissionException transmissionException;
    Throwable throwable;

    String[] stringValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};

    @Before
    public void createInstancesOfClasses() {
        transmissionException = new TransmissionException("message", throwable);
        throwable = new Throwable();
    }

    @Test
    public void transmissionExceptionDefaultConstructorTest() {
        TransmissionException transmissionException = new TransmissionException();
        Assert.assertThat("Expected and actual values should be the same!", transmissionException, IsInstanceOf.instanceOf(TransmissionException.class));
    }

    @Test
    public void transmissionExceptionTwoParameterConstructorValidTest() {
        for (String value : stringValues) {
            transmissionException = new TransmissionException(value, throwable);
            Assert.assertEquals("Expected and actual values should be the same!", value, transmissionException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same!", throwable, transmissionException.getCause());
        }
    }

    @Test
    public void transmissionExceptionTwoParametersNullMessageTest() {
        transmissionException = new TransmissionException(null, throwable);
        Assert.assertNull("Null expected!", transmissionException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same!", throwable, transmissionException.getCause());
    }

    @Test
    public void transmissionExceptionTwoParametersNullCauseTest() {
        for (String value : stringValues) {
            transmissionException = new TransmissionException(value, null);
            Assert.assertEquals("Expected and actual values should be the same!", value, transmissionException.getMessage());
            Assert.assertNull("Null expected!", transmissionException.getCause());
        }
    }

    @Test
    public void transmissionExceptionTwoParametersNullTest() {
        transmissionException = new TransmissionException(null, null);
        Assert.assertNull("Null expected!", transmissionException.getMessage());
        Assert.assertNull("Null expected!", transmissionException.getCause());
    }

    @Test
    public void transmissionExceptionMessageConstructorValidTest() {
        for (String value : stringValues) {
            transmissionException = new TransmissionException(value);
            Assert.assertEquals("Expected and actual values should be the same!", value, transmissionException.getMessage());
        }
    }

    @Test
    public void transmissionExceptionMessageConstructorNullTest() {
        transmissionException = new TransmissionException((String) null);
        Assert.assertNull("Null expected!", transmissionException.getMessage());
    }

    @Test
    public void transmissionExceptionThrowableConstructorValidTest() {
        transmissionException = new TransmissionException(throwable);
        Assert.assertEquals("Expected and actual values should be the same!", throwable, transmissionException.getCause());
    }

    @Test
    public void transmissionExceptionThrowableConstructorNullTest() {
        transmissionException = new TransmissionException(throwable);
        Assert.assertEquals("Expected and actual values should be the same!", throwable, transmissionException.getCause());
    }

    @Test (expected = TransmissionException.class)
    public void throwingExceptionTest() throws TransmissionException {
        throw transmissionException;
    }
}
