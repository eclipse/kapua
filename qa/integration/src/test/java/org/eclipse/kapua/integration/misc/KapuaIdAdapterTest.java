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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;


@Category(JUnitTests.class)
public class KapuaIdAdapterTest {

    KapuaIdAdapter kapuaIdAdapter;
    KapuaId[] kapuaId;
    String[] stringValue;

    @Before
    public void initialize() {
        kapuaIdAdapter = new KapuaIdAdapter();
        kapuaId = new KapuaId[]{new KapuaIdImpl(BigInteger.ZERO), new KapuaIdImpl(BigInteger.ONE), new KapuaIdImpl(BigInteger.TEN), null};
        stringValue = new String[]{"AA", "AQ", "Cg", null};
    }

    @Test
    public void marshalTest() throws Exception {
        String[] expectedString = stringValue;
        for (int i = 0; i < kapuaId.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", expectedString[i], kapuaIdAdapter.marshal(kapuaId[i]));
        }
    }

    @Test
    public void unmarshalTest() throws Exception {
        KapuaId[] expectedKapuaId = kapuaId;
        for (int i = 0; i < stringValue.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", expectedKapuaId[i], kapuaIdAdapter.unmarshal(stringValue[i]));
        }
    }
}
