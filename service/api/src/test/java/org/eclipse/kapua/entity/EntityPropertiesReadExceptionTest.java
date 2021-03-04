/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.entity;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class EntityPropertiesReadExceptionTest extends Assert {

    @Test
    public void entityPropertiesReadExceptionTest() {
        Exception[] exception = {new Exception(), null};
        String[] attributes = {"Attribute", null};
        String[] stringProperties = {"String Properties", null};

        for (Exception ex : exception) {
            for (String attribute : attributes) {
                for (String properties : stringProperties) {
                    EntityPropertiesReadException entityPropertiesReadException = new EntityPropertiesReadException(ex, attribute, properties);

                    assertEquals("Expected and actual values should be the same.", attribute, entityPropertiesReadException.getAttribute());
                    assertEquals("Expected and actual values should be the same.", properties, entityPropertiesReadException.getStringProperties());
                    assertEquals("Expected and actual values should be the same.", KapuaRuntimeErrorCodes.ENTITY_PROPERTIES_READ_ERROR, entityPropertiesReadException.getCode());
                    assertEquals("Expected and actual values should be the same.", ex, entityPropertiesReadException.getCause());
                }
            }
        }
    }
}
