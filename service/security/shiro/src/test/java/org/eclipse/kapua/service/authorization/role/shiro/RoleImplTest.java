/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.role.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class RoleImplTest extends Assert {

    RoleImpl role1, role2;

    @Before
    public void initialize() {
        role1 = new RoleImpl();
        role2 = new RoleImpl();
    }

    @Test
    public void roleImplWithoutParametersTest() {
        RoleImpl roleImpl = new RoleImpl();
        assertNull("Null expected.", roleImpl.getScopeId());
        assertNull("Null expected.", roleImpl.getName());
        assertNull("Null expected.", roleImpl.getDescription());
    }

    @Test
    public void roleImplScopeIdTest() {
        KapuaId[] scopeIds = {null, KapuaId.ONE};

        for (KapuaId scopeId : scopeIds) {
            RoleImpl roleImpl = new RoleImpl(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, roleImpl.getScopeId());
            assertNull("Null expected.", roleImpl.getName());
            assertNull("Null expected.", roleImpl.getDescription());
        }
    }

    @Test
    public void roleImplRoleTest() throws KapuaException {
        Role role = Mockito.mock(Role.class);
        Date createdOn = new Date();
        Date modifiedOn = new Date();

        Mockito.when(role.getName()).thenReturn("role name");
        Mockito.when(role.getDescription()).thenReturn("role description");
        Mockito.when(role.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(role.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(role.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(role.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(role.getModifiedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(role.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(role.getOptlock()).thenReturn(11);

        RoleImpl roleImpl = new RoleImpl(role);
        assertEquals("Expected and actual values should be the same.", "role name", roleImpl.getName());
        assertEquals("Expected and actual values should be the same.", "role description", roleImpl.getDescription());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, roleImpl.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, roleImpl.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, roleImpl.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, roleImpl.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, roleImpl.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", modifiedOn, roleImpl.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", 11, roleImpl.getOptlock());
    }

    @Test(expected = NullPointerException.class)
    public void roleImplNullRoleTest() throws KapuaException {
        new RoleImpl((Role) null);
    }

    @Test
    public void hashCodeNullNameTest() {
        RoleImpl role = new RoleImpl();
        assertEquals("Expected and actual values should be the same.", 31, role.hashCode());
    }

    @Test
    public void hashCodeWithNameTest() {
        RoleImpl role = new RoleImpl();
        String[] names = {"", "  na123)(&*^&NAME  <>", "Na-,,..,,Me name ---", "-&^454536 na___,,12 NAME name    ", "! 2#@ na     meNEMA 2323", "12&^%4   ,,,. '|<>*(", "       ,,123name;;'", "12#name--765   ,.aaa!!#$%^<> "};
        int[] expectedResult = {31, 467704822, -586547515, -576050570, 589188417, -894904038, -1874261591, 12362811};
        for (int i = 0; i < names.length; i++) {
            role.setName(names[i]);
            assertEquals("Expected and actual values should be the same.", expectedResult[i], role.hashCode());
        }
    }

    @Test
    public void equalsSameObjectTest() {
        assertTrue("True expected.", role1.equals(role1));
    }

    @Test
    public void equalsNullObjectTest() {
        assertFalse("False expected.", role1.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        assertFalse("False expected.", role1.equals(new Object()));
    }

    @Test
    public void equalsNullBothNamesTest() {
        assertTrue("True expected.", role1.equals(role2));
    }

    @Test
    public void equalsNullNameTest() {
        role2.setName("name2");
        assertFalse("False expected.", role1.equals(role2));
    }

    @Test
    public void equalsDifferentNamesTest() {
        role1.setName("name1");
        role2.setName("name2");
        assertFalse("False expected.", role1.equals(role2));
    }

    @Test
    public void equalsSameNamesTest() {
        role1.setName("name");
        role2.setName("name");
        assertTrue("True expected.", role1.equals(role2));
    }
}