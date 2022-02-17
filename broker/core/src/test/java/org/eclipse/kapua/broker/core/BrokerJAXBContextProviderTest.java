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
package org.eclipse.kapua.broker.core;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.xml.bind.JAXBContext;

@Category(JUnitTests.class)
public class BrokerJAXBContextProviderTest extends Assert {

    BrokerJAXBContextProvider brokerJAXBContextProvider;

    @Before
    public void initialize() {
        brokerJAXBContextProvider = new BrokerJAXBContextProvider();
    }

    @Test
    public void getJAXBContextNullContextTest() throws KapuaException {
        assertTrue("True expected.", brokerJAXBContextProvider.getJAXBContext() instanceof JAXBContext);
        assertNotNull("Null not expected.", brokerJAXBContextProvider.getJAXBContext());
    }

    @Test
    public void getJAXBContextTest() throws KapuaException {
        brokerJAXBContextProvider.getJAXBContext();
        assertTrue("True expected.", brokerJAXBContextProvider.getJAXBContext() instanceof JAXBContext);
        assertNotNull("Null not expected.", brokerJAXBContextProvider.getJAXBContext());
    }
}