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
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class AccessRoleFactoryImplTest extends Assert {

    AccessRoleFactoryImpl accessRoleFactoryImpl;
    AccessRole accessRole;
    Date createdOn;

    @Before
    public void initialize() {
        accessRoleFactoryImpl = new AccessRoleFactoryImpl();
        accessRole = Mockito.mock(AccessRole.class);
        createdOn = new Date();

        Mockito.when(accessRole.getId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessRole.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRole.getCreatedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(accessRole.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(accessRole.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRole.getRoleId()).thenReturn(KapuaId.ANY);
    }

    @Test
    public void newEntityTest() {
        AccessRole accessRole = accessRoleFactoryImpl.newEntity(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRole.getScopeId());
    }

    @Test
    public void newEntityNullTest() {
        AccessRole accessRole = accessRoleFactoryImpl.newEntity(null);
        assertNull("Null expected.", accessRole.getScopeId());
    }

    @Test
    public void newCreatorTest() {
        AccessRoleCreator accessRoleCreator = accessRoleFactoryImpl.newCreator(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRoleCreator.getScopeId());
    }

    @Test
    public void newCreatorNullTest() {
        AccessRoleCreator accessRoleCreator = accessRoleFactoryImpl.newCreator(null);
        assertNull("Null expected.", accessRoleCreator.getScopeId());
    }

    @Test
    public void newQueryTest() {
        AccessRoleQuery accessRoleQuery = accessRoleFactoryImpl.newQuery(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRoleQuery.getScopeId());
    }

    @Test
    public void newQueryNullTest() {
        AccessRoleQuery accessRoleQuery = accessRoleFactoryImpl.newQuery(null);
        assertNull("Null expected.", accessRoleQuery.getScopeId());
    }

    @Test
    public void newListResultTest() {
        AccessRoleListResult accessRoleListResult = accessRoleFactoryImpl.newListResult();
        assertTrue("True expected.", accessRoleListResult.isEmpty());
    }

    @Test
    public void cloneTest() {
        accessRoleFactoryImpl.clone(accessRole);

        AccessRole resultAccessRole = accessRoleFactoryImpl.clone(accessRole);
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultAccessRole.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultAccessRole.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultAccessRole.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, resultAccessRole.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultAccessRole.getAccessInfoId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultAccessRole.getRoleId());
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void cloneNullTest() {
        accessRoleFactoryImpl.clone(null);
    }
}