/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
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
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Properties;

@Category(JUnitTests.class)
public class EntityPropertiesWriteExceptionTest extends Assert {

    @Test
    public void entityPropertiesWriteExceptionTest() {
        Exception[] exception = {new Exception(), null};
        String[] attributes = {"Attribute", null};
        Properties[] properties = {new Properties(),null};

        for (Exception ex : exception) {
            for (String attribute : attributes) {
                for (Properties property : properties) {
                    EntityPropertiesWriteException entityPropertiesWriteException = new EntityPropertiesWriteException(ex, attribute, property);

                    assertEquals("Expected and actual values should be the same.", attribute, entityPropertiesWriteException.getAttribute());
                    assertEquals("Expected and actual values should be the same.", property, entityPropertiesWriteException.getProperties());
                    assertEquals("Expected and actual values should be the same.", KapuaRuntimeErrorCodes.ENTITY_PROPERTIES_READ_ERROR, entityPropertiesWriteException.getCode());
                    assertEquals("Expected and actual values should be the same.", ex, entityPropertiesWriteException.getCause());
                }
            }
        }
    }
}
