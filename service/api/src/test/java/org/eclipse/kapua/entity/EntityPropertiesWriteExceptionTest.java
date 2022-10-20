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
package org.eclipse.kapua.entity;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Properties;


@Category(JUnitTests.class)
public class EntityPropertiesWriteExceptionTest {

    @Test
    public void entityPropertiesWriteExceptionTest() {
        Exception[] exception = {new Exception(), null};
        String[] attributes = {"Attribute", null};
        Properties[] properties = {new Properties(), null};

        for (Exception ex : exception) {
            for (String attribute : attributes) {
                for (Properties property : properties) {
                    EntityPropertiesWriteException entityPropertiesWriteException = new EntityPropertiesWriteException(ex, attribute, property);

                    Assert.assertEquals("Expected and actual values should be the same.", attribute, entityPropertiesWriteException.getAttribute());
                    Assert.assertEquals("Expected and actual values should be the same.", property, entityPropertiesWriteException.getProperties());
                    Assert.assertEquals("Expected and actual values should be the same.", KapuaRuntimeErrorCodes.ENTITY_PROPERTIES_WRITE_ERROR, entityPropertiesWriteException.getCode());
                    Assert.assertEquals("Expected and actual values should be the same.", ex, entityPropertiesWriteException.getCause());
                }
            }
        }
    }
}
