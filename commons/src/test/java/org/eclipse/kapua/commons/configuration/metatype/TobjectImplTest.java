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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class TobjectImplTest extends Assert {

    @Before
    public void createInstanceOfClasses() {
        tobjectImpl = new TobjectImpl();
    }

    TobjectImpl tobjectImpl;

    @Test
    public void getAttributeTest() {
        assertTrue(tobjectImpl.getAttribute().isEmpty());
    }

    @Test
    public void getAnyTest() {
        assertTrue(tobjectImpl.getAny().isEmpty());
    }

    @Test
    public void setAndGetOcdrefToNullTest() {
        tobjectImpl.setOcdref(null);
        assertNull(tobjectImpl.getOcdref());
    }

    @Test
    public void setAndGetOcdrefTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularOcdref", "regular Ocdref", "49", "regularOcdref49", "OCDREF", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tobjectImpl.setOcdref(value);
            assertTrue(tobjectImpl.getOcdref().contains(value));
        }
    }

    @Test
    public void testGetOtherAttributes() {
        assertTrue(tobjectImpl.getOtherAttributes().isEmpty());
    }
}
