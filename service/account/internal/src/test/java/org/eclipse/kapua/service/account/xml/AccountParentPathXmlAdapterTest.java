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
package org.eclipse.kapua.service.account.xml;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.List;

@Category(JUnitTests.class)
public class AccountParentPathXmlAdapterTest {

    private static final AccountParentPathXmlAdapter XML_ADAPTER = new AccountParentPathXmlAdapter();

    @Test
    public void testMarshal() {
        String parentAccountPath = "/1/2/3";

        String marshalled = XML_ADAPTER.marshal(parentAccountPath);

        Assert.assertNotNull(marshalled);
        Assert.assertEquals("/AQ/Ag/Aw", marshalled);
    }

    @Test
    public void testMarshalNull() {
        String marshalled = XML_ADAPTER.marshal(null);

        Assert.assertNull(marshalled);
    }

    @Test
    public void testUnmarshal() throws KapuaIllegalArgumentException {
        String parentAccountPath = "/AQ/Ag/Aw";

        String unmarshalled = XML_ADAPTER.unmarshal(parentAccountPath);

        Assert.assertNotNull(unmarshalled);
        Assert.assertEquals("/1/2/3", unmarshalled);
    }

    @Test
    public void testUnmarshalNull() throws KapuaIllegalArgumentException {
        String unmarshalled = XML_ADAPTER.unmarshal(null);

        Assert.assertNull(unmarshalled);
    }

    @Test
    public void testUnmarshalInvalid() {
        List<String> parentAccountPaths = Arrays.asList("/AQ/Ag/Awwerty###", "///", "A//B", "!@#$%^&*()_");

        for (String parentAccountPath : parentAccountPaths) {
            try {
                XML_ADAPTER.unmarshal(parentAccountPath);
            } catch (KapuaIllegalArgumentException kapuaIllegalArgumentException) {
                Assert.assertEquals("account.parentAccountPath", kapuaIllegalArgumentException.getArgumentName());
                Assert.assertEquals(parentAccountPath, kapuaIllegalArgumentException.getArgumentValue());
            }
        }
    }
}
