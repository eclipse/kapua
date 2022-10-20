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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ServiceEntryTest {

    @Test
    public void constructorRegularTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry("serviceName", "serviceAddress");
        Assert.assertEquals("Expected and actual values are not equal!", "serviceName", serviceEntry.getServiceName());
        Assert.assertEquals("Expected and actual values are not equal!", "serviceAddress", serviceEntry.getAddress());
    }

    @Test
    public void constructorNullTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry(null, null);
        Assert.assertNull("Null expected!", serviceEntry.getServiceName());
        Assert.assertNull("Null expected!", serviceEntry.getAddress());
    }

    @Test
    public void constructorNameNullTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry(null, "serviceAddress");
        Assert.assertNull("Null expected!", serviceEntry.getServiceName());
        Assert.assertEquals("Expected and actual values are not equal!", "serviceAddress", serviceEntry.getAddress());
    }

    @Test
    public void constructorAddressNullTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry("serviceName", null);
        Assert.assertEquals("Expected and actual values are not equal!", "serviceName", serviceEntry.getServiceName());
        Assert.assertNull("Null expected!", serviceEntry.getAddress());
    }
}
