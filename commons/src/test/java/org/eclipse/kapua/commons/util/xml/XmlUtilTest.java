/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util.xml;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.xml.bind.JAXBException;
import java.lang.reflect.Constructor;

@Category(JUnitTests.class)
public class XmlUtilTest extends Assert {

    @Test
    public void testConstructor() throws Exception {
        Constructor<XmlUtil> xmlUtilConstruct = XmlUtil.class.getDeclaredConstructor();
        xmlUtilConstruct.setAccessible(true);
        xmlUtilConstruct.newInstance();
    }

    @Test
    public void testSetContextProvider() {
        JAXBContextProvider provider = null;
        XmlUtil.setContextProvider(provider);
    }

    @Test
    public void testMarshal() throws Exception {
        Object[] jaxbElements = new Object[] { 123, "string", "s" };
        int sizeOfjaxbElements = jaxbElements.length;
        Object[] jaxbElementsFalse = new Object[] { null };
        int sizeOfjaxbElementsFalse = jaxbElementsFalse.length;
        // JAXBException
        for (int i = 0; i < sizeOfjaxbElements; i++) {
            try {
                Assert.assertNotNull(XmlUtil.marshal(jaxbElements[i]));
                fail("Exception expected for: " + jaxbElements[i]);
            } catch (JAXBException ex) {
                // Expected
            }
        }
        // Exception
        for (int i = 0; i < sizeOfjaxbElementsFalse; i++) {
            try {
                XmlUtil.marshal(jaxbElementsFalse[i]);
                fail("Exception expected for: " + jaxbElementsFalse[i]);
            } catch (Exception ex) {
                // expected
            }
        }
    }

    @Test
    public void testUnmarshal() throws JAXBException {
        String[] listOfStrings = new String[] { "string", null };
        int sizeOfList = listOfStrings.length;
        for (int i = 0; i < sizeOfList; i++) {
            try {
                Assert.assertNotNull(XmlUtil.unmarshal(listOfStrings[i], Integer.class));
                fail("Exception expected for: " + listOfStrings[i]);
            } catch (Exception ex) {
                // Expected
            }
        }
    }
}