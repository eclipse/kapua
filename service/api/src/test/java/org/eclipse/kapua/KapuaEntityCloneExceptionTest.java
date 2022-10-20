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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class KapuaEntityCloneExceptionTest {

    String[] entityType;
    KapuaEntity[] kapuaEntity;
    Throwable[] throwables;

    @Before
    public void initialize() {
        entityType = new String[]{"Entity Type", null};
        kapuaEntity = new KapuaEntity[]{Mockito.mock(KapuaEntity.class), null};
        throwables = new Throwable[]{new Throwable(), null};
    }

    @Test
    public void kapuaEntityCloneExceptionStringKapuaEntityParametersTest() {
        for (String type : entityType) {
            for (KapuaEntity entity : kapuaEntity) {
                KapuaEntityCloneException kapuaEntityCloneException = new KapuaEntityCloneException(type, entity);
                Assert.assertEquals("Expected and actual values should be the same.", KapuaRuntimeErrorCodes.ENTITY_CLONE_ERROR, kapuaEntityCloneException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", type, kapuaEntityCloneException.getEntityType());
                Assert.assertEquals("Expected and actual values should be the same.", entity, kapuaEntityCloneException.getEntity());
                Assert.assertNull("Null expected.", kapuaEntityCloneException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", "Severe error while cloning: " + type, kapuaEntityCloneException.getMessage());
            }
        }
    }

    @Test
    public void kapuaEntityCloneExceptionThrowableStringKapuaEntityParametersTest() {
        for (String type : entityType) {
            for (KapuaEntity entity : kapuaEntity) {
                for (Throwable throwable : throwables) {
                    KapuaEntityCloneException kapuaEntityCloneException = new KapuaEntityCloneException(throwable, type, entity);
                    Assert.assertEquals("Expected and actual values should be the same.", KapuaRuntimeErrorCodes.ENTITY_CLONE_ERROR, kapuaEntityCloneException.getCode());
                    Assert.assertEquals("Expected and actual values should be the same.", type, kapuaEntityCloneException.getEntityType());
                    Assert.assertEquals("Expected and actual values should be the same.", entity, kapuaEntityCloneException.getEntity());
                    Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaEntityCloneException.getCause());
                    Assert.assertEquals("Expected and actual values should be the same.", "Severe error while cloning: " + type, kapuaEntityCloneException.getMessage());
                }
            }
        }
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void throwingExceptionStringKapuaEntityParametersTest() {
        for (String type : entityType) {
            for (KapuaEntity entity : kapuaEntity) {
                throw new KapuaEntityCloneException(type, entity);
            }
        }
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void throwingExceptionThrowableStringKapuaEntityParametersTest() {
        for (String type : entityType) {
            for (KapuaEntity entity : kapuaEntity) {
                for (Throwable throwable : throwables) {
                    throw new KapuaEntityCloneException(throwable, type, entity);
                }
            }
        }
    }
}  
