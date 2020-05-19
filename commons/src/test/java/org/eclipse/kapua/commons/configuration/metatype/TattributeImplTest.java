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
public class TattributeImplTest extends Assert {

    @Before
    public void createInstanceOfClass() {

        tattributeImpl = new TattributeImpl();
    }

    TattributeImpl tattributeImpl;

    @Test
    public void getValueTest() {
        assertTrue(tattributeImpl.getValue().isEmpty());
    }

    @Test
    public void getAnyTest() {
        assertTrue(tattributeImpl.getAny().isEmpty());
    }

    @Test
    public void setAndGetAdrefToNullTest() {
        tattributeImpl.setAdref(null);
        assertNull(tattributeImpl.getAdref());
    }

    @Test
    public void setAndGetAdrefTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularAdref", "regular Adref", "49", "regularAdref49", "ADREF", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tattributeImpl.setAdref(value);
            assertTrue(tattributeImpl.getAdref().contains(value));
        }
    }

    @Test
    public void setAndGetContentToNullTest() {
        tattributeImpl.setContent(null);
        assertNull(tattributeImpl.getContent());
    }

    @Test
    public void setAndGetContentTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularContent", "regular Content", "49", "regularContent49", "CONTENT", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tattributeImpl.setContent(value);
            assertTrue(tattributeImpl.getContent().contains(value));
        }
    }

    @Test
    public void getOtherAttributesTest() {
        assertTrue(tattributeImpl.getOtherAttributes().isEmpty());
    }
}