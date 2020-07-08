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