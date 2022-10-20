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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;



@Category(JUnitTests.class)
public class KapuaEntityNotFoundExceptionTest {
    String[] entityType;
    String[] entityName;
    KapuaId entityId;

    @Before
    public void initialize() {
        entityType = new String[]{"Entity Type", "1234", "!@#$%^&*(", null};
        entityName = new String[]{"Entity Name", "1234", "!@#$%^&*(", null};
        entityId = new KapuaIdImpl(BigInteger.ONE);
    }

    @Test
    public void kapuaEntityNotFoundExceptionStringParametersTest() {
        for (String type : entityType) {
            for (String name : entityName) {
                KapuaEntityNotFoundException kapuaEntityNotFoundException = new KapuaEntityNotFoundException(type, name);
                Assert.assertEquals("Expected and actual values should be the same.", type, kapuaEntityNotFoundException.getEntityType());
                Assert.assertEquals("Expected and actual values should be the same.", name, kapuaEntityNotFoundException.getEntityName());
                Assert.assertNull("Null expected.", kapuaEntityNotFoundException.getEntityId());
                Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_NOT_FOUND, kapuaEntityNotFoundException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", "The entity of type " + type + " with id/name " + name + " was not found.", kapuaEntityNotFoundException.getMessage());
                Assert.assertNull("Null expected.", kapuaEntityNotFoundException.getCause());
            }
        }
    }

    @Test
    public void kapuaEntityNotFoundExceptionStringKapuaIdParametersTest() {
        for (String type : entityType) {
            KapuaEntityNotFoundException kapuaEntityNotFoundException = new KapuaEntityNotFoundException(type, entityId);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_NOT_FOUND, kapuaEntityNotFoundException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", type, kapuaEntityNotFoundException.getEntityType());
            Assert.assertEquals("Expected and actual values should be the same.", entityId, kapuaEntityNotFoundException.getEntityId());
            Assert.assertEquals("Expected and actual values should be the same.", "The entity of type " + type + " with id/name " + entityId.getId() + " was not found.", kapuaEntityNotFoundException.getMessage());
            Assert.assertNull("Null expected.", kapuaEntityNotFoundException.getCause());
        }
    }

    @Test(expected = NullPointerException.class)
    public void kapuaEntityNotFoundExceptionStringNullKapuaIdParametersTest() {
        KapuaId nullEntityId = null;
        for (String type : entityType) {
            KapuaEntityNotFoundException kapuaEntityNotFoundException = new KapuaEntityNotFoundException(type, nullEntityId);
        }
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void throwingExceptionStringParametersTest() throws KapuaEntityNotFoundException {
        for (String type : entityType) {
            for (String name : entityName) {
                throw new KapuaEntityNotFoundException(type, name);
            }
        }
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void throwingExceptionTest() throws KapuaEntityNotFoundException {
        for (String type : entityType) {
            throw new KapuaEntityNotFoundException(type, entityId);
        }
    }
}
