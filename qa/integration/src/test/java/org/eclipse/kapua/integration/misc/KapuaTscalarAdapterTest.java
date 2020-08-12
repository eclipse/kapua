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

import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.model.config.metatype.KapuaTscalarAdapter;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaTscalarAdapterTest extends Assert {

    KapuaTscalarAdapter kapuaTscalarAdapter;
    TscalarImpl[] kapuaTscalar;
    String[] stringValue;
    String[] invalidStringValue;

    @Before
    public void initialize() {
        kapuaTscalarAdapter = new KapuaTscalarAdapter();
        kapuaTscalar = new TscalarImpl[]{TscalarImpl.STRING, TscalarImpl.LONG, TscalarImpl.DOUBLE, TscalarImpl.FLOAT, TscalarImpl.INTEGER,
                TscalarImpl.BYTE, TscalarImpl.CHAR, TscalarImpl.BOOLEAN, TscalarImpl.SHORT, TscalarImpl.PASSWORD};
        stringValue = new String[]{"String", "Long", "Double", "Float", "Integer", "Byte", "Char", "Boolean", "Short", "Password"};
        invalidStringValue = new String[]{null, "Invalid Value"};
    }

    @Test
    public void marshalTest() throws Exception {
        String[] expectedStringValue = stringValue;
        for (int i = 0; i < kapuaTscalar.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedStringValue[i], kapuaTscalarAdapter.marshal(kapuaTscalar[i]));
        }
    }

    @Test(expected = NullPointerException.class)
    public void marshalNullParameterTest() throws Exception {
        kapuaTscalarAdapter.marshal(null);
    }

    @Test
    public void unmarshalTest() throws Exception {
        TscalarImpl[] expectedTscalars = kapuaTscalar;
        for (int i = 0; i < stringValue.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedTscalars[i], kapuaTscalarAdapter.unmarshal(stringValue[i]));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void unmarshalInvalidParameterValueTest() throws Exception {
        for (String invalidValue : invalidStringValue) {
            kapuaTscalarAdapter.unmarshal(invalidValue);
        }
    }
}