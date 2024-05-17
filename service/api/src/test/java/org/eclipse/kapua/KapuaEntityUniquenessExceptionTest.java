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

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Category(JUnitTests.class)
public class KapuaEntityUniquenessExceptionTest {

    String[] entityType;
    List<Map.Entry<String, Object>> uniquesFieldValues;

    @Before
    public void initialize() {
        entityType = new String[] { "AEntity", "AnotherEntity" };
        uniquesFieldValues = new LinkedList<>();

        uniquesFieldValues.add(new AbstractMap.SimpleEntry<>("aField", "aValue"));
        uniquesFieldValues.add(new AbstractMap.SimpleEntry<>("anotherField", "anotherValue"));
    }

    @Test
    public void kapuaEntityUniquenessExceptionTest() {
        for (String type : entityType) {
            KapuaEntityUniquenessException kapuaEntityUniquenessException = new KapuaEntityUniquenessException(type, uniquesFieldValues);
            Assert.assertEquals(type, kapuaEntityUniquenessException.getEntityType());
            Assert.assertEquals(uniquesFieldValues, kapuaEntityUniquenessException.getUniquesFieldValues());
            Assert.assertEquals(KapuaErrorCodes.ENTITY_UNIQUENESS, kapuaEntityUniquenessException.getCode());
            Assert.assertEquals("Duplicate " + type + " entry for values: " + uniquesFieldValues, kapuaEntityUniquenessException.getMessage());
            Assert.assertNull("Null expected.", kapuaEntityUniquenessException.getCause());
        }
    }
}  
