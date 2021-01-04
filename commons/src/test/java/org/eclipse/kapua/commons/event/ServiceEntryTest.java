/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
public class ServiceEntryTest extends Assert {

    @Test
    public void constructorRegularTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry("serviceName", "serviceAddress");
        assertEquals("Expected and actual values are not equal!", "serviceName", serviceEntry.getServiceName());
        assertEquals("Expected and actual values are not equal!", "serviceAddress", serviceEntry.getAddress());
    }

    @Test
    public void constructorNullTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry(null, null);
        assertNull("Null expected!", serviceEntry.getServiceName());
        assertNull("Null expected!", serviceEntry.getAddress());
    }

    @Test
    public void constructorNameNullTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry(null, "serviceAddress");
        assertNull("Null expected!", serviceEntry.getServiceName());
        assertEquals("Expected and actual values are not equal!", "serviceAddress", serviceEntry.getAddress());
    }

    @Test
    public void constructorAddressNullTest() throws Exception {
        ServiceEntry serviceEntry = new ServiceEntry("serviceName", null);
        assertEquals("Expected and actual values are not equal!", "serviceName", serviceEntry.getServiceName());
        assertNull("Null expected!", serviceEntry.getAddress());
    }
}
