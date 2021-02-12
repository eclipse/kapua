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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class RoleFactoryImplTest extends Assert {

    RoleFactoryImpl roleFactoryImpl;
    KapuaId scopeId;
    Role role;
    Date createdOn, modifiedOn;

    @Before
    public void initialize() {
        roleFactoryImpl = new RoleFactoryImpl();
        scopeId = KapuaId.ONE;
        createdOn = new Date();
        modifiedOn = new Date();
        role = Mockito.mock(Role.class);

        Mockito.when(role.getName()).thenReturn("role name");
        Mockito.when(role.getDescription()).thenReturn("role description");
        Mockito.when(role.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(role.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(role.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(role.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(role.getModifiedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(role.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(role.getOptlock()).thenReturn(11);
    }

    @Test
    public void newEntityTest() {
        assertTrue("True expected.", roleFactoryImpl.newEntity(scopeId) instanceof Role);
        assertEquals("Expected and actual values should be the same.", scopeId, roleFactoryImpl.newEntity(scopeId).getScopeId());
    }

    @Test
    public void newEntityNullTest() {
        assertTrue("True expected.", roleFactoryImpl.newEntity(null) instanceof Role);
        assertNull("Null expected.", roleFactoryImpl.newEntity(null).getScopeId());
    }

    @Test
    public void newCreatorTest() {
        assertTrue("True expected.", roleFactoryImpl.newCreator(scopeId) instanceof RoleCreator);
        assertEquals("Expected and actual values should be the same.", scopeId, roleFactoryImpl.newCreator(scopeId).getScopeId());
    }

    @Test
    public void newCreatorNullTest() {
        assertTrue("True expected.", roleFactoryImpl.newCreator(null) instanceof RoleCreator);
        assertNull("Null expected.", roleFactoryImpl.newCreator(null).getScopeId());
    }

    @Test
    public void newQueryTest() {
        assertTrue("True expected.", roleFactoryImpl.newQuery(scopeId) instanceof RoleQuery);
        assertEquals("Expected and actual values should be the same.", scopeId, roleFactoryImpl.newQuery(scopeId).getScopeId());
    }

    @Test
    public void newQueryNullTest() {
        assertTrue("True expected.", roleFactoryImpl.newQuery(null) instanceof RoleQuery);
        assertNull("Null expected.", roleFactoryImpl.newQuery(null).getScopeId());
    }

    @Test
    public void newListResultTest() {
        assertTrue("True expected.", roleFactoryImpl.newListResult() instanceof RoleListResult);
        assertTrue("True expected.", roleFactoryImpl.newListResult().isEmpty());
    }

    @Test
    public void newRolePermissionTest() {
        assertTrue("True expected.", roleFactoryImpl.newRolePermission() instanceof RolePermission);
    }

    @Test
    public void cloneTest() {
        Role resultRole = roleFactoryImpl.clone(role);

        assertEquals("Expected and actual values should be the same.", "role name", resultRole.getName());
        assertEquals("Expected and actual values should be the same.", "role description", resultRole.getDescription());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultRole.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultRole.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultRole.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, resultRole.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultRole.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", modifiedOn, resultRole.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", 11, resultRole.getOptlock());
    }

    @Test
    public void cloneNullTest() {
        try {
            roleFactoryImpl.clone(null);
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same", "org.eclipse.kapua.KapuaEntityCloneException: Severe error while cloning: role", e.toString());
        }
    }
}