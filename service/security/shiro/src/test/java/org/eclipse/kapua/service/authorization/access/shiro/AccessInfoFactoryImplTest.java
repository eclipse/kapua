/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import java.util.Date;

@Category(Categories.junitTests.class)
public class AccessInfoFactoryImplTest extends Assert {

    AccessInfoFactoryImpl accessInfoFactoryImpl;
    KapuaId scopeId;
    AccessInfo accessInfo;
    Date createdOn,modifiedOn;


    @Before
    public void initialize() {
        accessInfoFactoryImpl = new AccessInfoFactoryImpl();
        scopeId = KapuaId.ONE;
        accessInfo = Mockito.mock(AccessInfo.class);
        createdOn = new Date();
        modifiedOn = new Date();

        Mockito.when(accessInfo.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessInfo.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessInfo.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(accessInfo.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(accessInfo.getModifiedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(accessInfo.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(accessInfo.getOptlock()).thenReturn(11);
        Mockito.when(accessInfo.getUserId()).thenReturn(KapuaId.ANY);
    }

    @Test
    public void newEntityTest() {
        AccessInfo accessInfo = accessInfoFactoryImpl.newEntity(scopeId);

        assertNull("Null expected.", accessInfo.getScopeId());
    }

    @Test
    public void newEntityNullTest() {
        AccessInfo accessInfo = accessInfoFactoryImpl.newEntity(null);

        assertNull("Null expected.", accessInfo.getScopeId());
    }

    @Test
    public void newCreatorTest() {
        AccessInfoCreator accessInfoCreator = accessInfoFactoryImpl.newCreator(scopeId);

        assertEquals("Expected and actual values should be the same.", scopeId, accessInfoCreator.getScopeId());
    }

    @Test
    public void newCreatorNullTest() {
        AccessInfoCreator accessInfoCreator = accessInfoFactoryImpl.newCreator(null);

        assertNull("Null expected.", accessInfoCreator.getScopeId());
    }

    @Test
    public void newQueryTest() {
        AccessInfoQuery accessInfoQuery = accessInfoFactoryImpl.newQuery(scopeId);

        assertEquals("Expected and actual values should be the same.", scopeId, accessInfoQuery.getScopeId());
    }

    @Test
    public void newQueryNullTest() {
        AccessInfoQuery accessInfoQuery = accessInfoFactoryImpl.newQuery(null);

        assertNull("Null expected.", accessInfoQuery.getScopeId());
    }

    @Test
    public void newListResultTest() {
        AccessInfoListResult accessInfoListResult = accessInfoFactoryImpl.newListResult();

        assertTrue("True expected.", accessInfoListResult.isEmpty());
    }

    @Test
    public void cloneTest() {
        AccessInfo resultAccessInfo = accessInfoFactoryImpl.clone(accessInfo);

        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultAccessInfo.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultAccessInfo.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultAccessInfo.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, resultAccessInfo.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultAccessInfo.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", modifiedOn, resultAccessInfo.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", 11, resultAccessInfo.getOptlock());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultAccessInfo.getUserId());
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void cloneNullTest() {
        accessInfoFactoryImpl.clone(null);
    }
}