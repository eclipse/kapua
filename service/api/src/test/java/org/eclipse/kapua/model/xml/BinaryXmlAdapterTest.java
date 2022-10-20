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
package org.eclipse.kapua.model.xml;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class BinaryXmlAdapterTest {

    BinaryXmlAdapter binaryXmlAdapter;

    @Before
    public void createInstanceOfClass() {
        binaryXmlAdapter = new BinaryXmlAdapter();
    }

    @Test
    public void marshalTest() {
        byte[] byteArray = {-128, -10, 0, 1, 10, 127, 11};
        String expectedString = "gPYAAQp/Cw==";
        Assert.assertEquals("Expected and actual values should be the same.", expectedString, binaryXmlAdapter.marshal(byteArray));
    }

    @Test
    public void marshalNullTest() {
        Assert.assertNull("Null expected", binaryXmlAdapter.marshal(null));
    }

    @Test
    public void unmarshalTest() {
        String stringValue = "gPYAAQp/Cw==";
        Assert.assertThat("Instance of byte[] expected.", binaryXmlAdapter.unmarshal(stringValue), IsInstanceOf.instanceOf(byte[].class));
    }

    @Test
    public void unmarshalNullTest() {
        Assert.assertNull("Null expected", binaryXmlAdapter.unmarshal(null));
    }
}
