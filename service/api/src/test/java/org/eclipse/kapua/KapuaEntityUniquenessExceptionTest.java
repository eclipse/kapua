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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;



@Category(JUnitTests.class)
public class KapuaEntityUniquenessExceptionTest {

    String[] entityType;
    List<Map.Entry<String, Object>> uniquesFieldValues;
    Map.Entry mapEntry;

    @Before
    public void initialize() {
        entityType = new String[]{"Entity Type", null};
        uniquesFieldValues = new LinkedList<>();
        mapEntry = Mockito.mock(Map.Entry.class);
        uniquesFieldValues.add(mapEntry);
        uniquesFieldValues.add(mapEntry);
        uniquesFieldValues.add(null);
    }

    @Test
    public void kapuaEntityUniquenessExceptionTest() {
        for (String type : entityType) {
            KapuaEntityUniquenessException kapuaEntityUniquenessException1 = new KapuaEntityUniquenessException(type, uniquesFieldValues);
            Assert.assertEquals("Expected and actual values should be the same.", type, kapuaEntityUniquenessException1.getEntityType());
            Assert.assertEquals("Expected and actual values should be the same.", uniquesFieldValues, kapuaEntityUniquenessException1.getUniquesFieldValues());
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_UNIQUENESS, kapuaEntityUniquenessException1.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", "Error: " + uniquesFieldValues, kapuaEntityUniquenessException1.getMessage());
            Assert.assertNull("Null expected.", kapuaEntityUniquenessException1.getCause());
        }
    }

    @Test
    public void kapuaEntityUniquenessExceptionNullFieldValuesTest() {
        for (String type : entityType) {
            KapuaEntityUniquenessException kapuaEntityUniquenessException2 = new KapuaEntityUniquenessException(type, null);
            Assert.assertEquals("Expected and actual values should be the same.", type, kapuaEntityUniquenessException2.getEntityType());
            Assert.assertNull("Null expected.", kapuaEntityUniquenessException2.getUniquesFieldValues());
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_UNIQUENESS, kapuaEntityUniquenessException2.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", "Error: null", kapuaEntityUniquenessException2.getMessage());
            Assert.assertNull("Null expected.", kapuaEntityUniquenessException2.getCause());
        }
    }

    @Test(expected = KapuaEntityUniquenessException.class)
    public void throwingExceptionTest() throws KapuaEntityUniquenessException {
        for (String type : entityType) {
            throw new KapuaEntityUniquenessException(type, uniquesFieldValues);
        }
    }

    @Test(expected = KapuaEntityUniquenessException.class)
    public void throwingExceptionNullFieldValuesTest() throws KapuaEntityUniquenessException {
        for (String type : entityType) {
            throw new KapuaEntityUniquenessException(type, null);
        }
    }
}  
