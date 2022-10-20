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
package org.eclipse.kapua.model.query;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class SortOrderTest {

    @Test
    public void fromStringTest() throws KapuaIllegalArgumentException {
        String[] stringValue = {"Ascending", "Descending"};
        SortOrder[] expectedValues = {SortOrder.ASCENDING, SortOrder.DESCENDING};

        for (int i = 0; i < stringValue.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", expectedValues[i], SortOrder.fromString(stringValue[i]));
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void fromStringInvalidStringValueTest() throws KapuaIllegalArgumentException {
        String invalidStringValue = "invalidStringValue";
        SortOrder.fromString(invalidStringValue);
    }
}
