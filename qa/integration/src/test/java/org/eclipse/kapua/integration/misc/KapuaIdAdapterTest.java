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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.id.KapuaIdStatic;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;

@Category(JUnitTests.class)
public class KapuaIdAdapterTest extends Assert {

    KapuaIdAdapter kapuaIdAdapter;
    KapuaId[] kapuaId;
    String[] stringValue;

    @Before
    public void initialize() {
        kapuaIdAdapter = new KapuaIdAdapter();
        kapuaId = new KapuaId[]{new KapuaIdStatic(BigInteger.ZERO), new KapuaIdStatic(BigInteger.ONE), new KapuaIdStatic(BigInteger.TEN), null};
        stringValue = new String[]{"AA", "AQ", "Cg", null};
    }

    @Test
    public void marshalTest() throws Exception {
        String[] expectedString = stringValue;
        for (int i = 0; i < kapuaId.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedString[i], kapuaIdAdapter.marshal(kapuaId[i]));
        }
    }

    @Test
    public void unmarshalTest() throws Exception {
        KapuaId[] expectedKapuaId = kapuaId;
        for (int i = 0; i < stringValue.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedKapuaId[i], kapuaIdAdapter.unmarshal(stringValue[i]));
        }
    }
}