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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Date;

@Category(Categories.junitTests.class)
public class AccessPermissionImplTest extends Assert {

    AccessPermissionImpl accessPermissionImpl1, accessPermissionImpl2, accessPermissionImpl;
    AccessPermission accessPermission;
    PermissionImpl permission1, permission2;
    Permission newPermission;
    Date createdOn;

    @Before
    public void initialize() {
        accessPermissionImpl1 = new AccessPermissionImpl(KapuaId.ONE);
        accessPermissionImpl2 = new AccessPermissionImpl(KapuaId.ONE);
        accessPermission = Mockito.mock(AccessPermission.class);
        permission1 = Mockito.mock(PermissionImpl.class);
        permission2 = Mockito.mock(PermissionImpl.class);
        newPermission = Mockito.mock(Permission.class);
        createdOn = new Date();

        Mockito.when(accessPermission.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessPermission.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessPermission.getCreatedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(accessPermission.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(accessPermission.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessPermission.getPermission()).thenReturn(permission1);
        Mockito.when(permission1.getDomain()).thenReturn("domain");
        Mockito.when(permission1.getAction()).thenReturn(Actions.connect);
        Mockito.when(permission1.getTargetScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(permission1.getGroupId()).thenReturn(KapuaId.ANY);

        accessPermissionImpl = new AccessPermissionImpl(accessPermission);
    }

    @Test
    public void accessPermissionImplWithoutParametersTest() throws Exception {
        Constructor<AccessPermissionImpl> accessPermissionImplWithoutParameters = AccessPermissionImpl.class.getDeclaredConstructor();
        accessPermissionImplWithoutParameters.setAccessible(true);
        accessPermissionImplWithoutParameters.newInstance();
        assertTrue("True expected.", Modifier.isProtected(accessPermissionImplWithoutParameters.getModifiers()));
    }

    @Test
    public void accessPermissionImplScopeIdParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessPermissionImpl1.getScopeId());
        assertNull("Null expected", accessPermissionImpl1.getAccessInfoId());
        assertEquals("Expected and actual values should be the same.", "*:*:*:*", accessPermissionImpl1.getPermission().toString());
        assertNull("Null expected", accessPermissionImpl1.getPermission().getDomain());
        assertNull("Null expected", accessPermissionImpl1.getPermission().getAction());
        assertNull("Null expected", accessPermissionImpl1.getPermission().getTargetScopeId());
        assertNull("Null expected", accessPermissionImpl1.getPermission().getGroupId());
    }

    @Test
    public void accessPermissionImplNullScopeIdParameterTest() {
        AccessPermissionImpl accessPermission = new AccessPermissionImpl((KapuaId) null);

        assertNull("Null expected", accessPermission.getScopeId());
        assertNull("Null expected", accessPermission.getAccessInfoId());
        assertEquals("Expected and actual values should be the same.", "*:*:*:*", accessPermission.getPermission().toString());
        assertNull("Null expected", accessPermission.getPermission().getDomain());
        assertNull("Null expected", accessPermission.getPermission().getAction());
        assertNull("Null expected", accessPermission.getPermission().getTargetScopeId());
        assertNull("Null expected", accessPermission.getPermission().getGroupId());
    }

    @Test
    public void accessPermissionImplAccessPermissionParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessPermissionImpl.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessPermissionImpl.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessPermissionImpl.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, accessPermissionImpl.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessPermissionImpl.getAccessInfoId());
        assertEquals("Expected and actual values should be the same.", permission1, accessPermissionImpl.getPermission());
        assertEquals("Expected and actual values should be the same.", "domain", accessPermissionImpl.getPermission().getDomain());
        assertEquals("Expected and actual values should be the same.", Actions.connect, accessPermissionImpl.getPermission().getAction());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessPermissionImpl.getPermission().getTargetScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessPermissionImpl.getPermission().getGroupId());
    }

    @Test(expected = NullPointerException.class)
    public void accessPermissionImplNullAccessPermissionParameterTest() {
        new AccessPermissionImpl((AccessPermission) null);
    }

    @Test
    public void setAndGetAccessInfoIdTest() {
        accessPermissionImpl1.setAccessInfoId(KapuaId.ANY);
        assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ANY), accessPermissionImpl1.getAccessInfoId());
        accessPermissionImpl1.setAccessInfoId(new KapuaEid(KapuaId.ANY));
        assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ANY), accessPermissionImpl1.getAccessInfoId());
        accessPermissionImpl1.setAccessInfoId(null);
        assertNull("Null expected.", accessPermissionImpl1.getAccessInfoId());

        accessPermissionImpl.setAccessInfoId(KapuaId.ANY);
        assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ANY), accessPermissionImpl.getAccessInfoId());
        accessPermissionImpl.setAccessInfoId(new KapuaEid(KapuaId.ANY));
        assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ANY), accessPermissionImpl.getAccessInfoId());
        accessPermissionImpl.setAccessInfoId(null);
        assertNull("Null expected.", accessPermissionImpl.getAccessInfoId());

    }

    @Test
    public void setAndGetPermissionTest() {
        accessPermissionImpl1.setPermission(permission1);
        assertEquals("Expected and actual values should be the same.", permission1, accessPermissionImpl1.getPermission());
        accessPermissionImpl1.setPermission(null);
        assertEquals("Expected and actual values should be the same.", "*:*:*:*", accessPermissionImpl1.getPermission().toString());
        accessPermissionImpl1.setPermission(newPermission);
        assertEquals("Expected and actual values should be the same.", "*:*:*:*", accessPermissionImpl1.getPermission().toString());

        accessPermissionImpl.setPermission(permission2);
        assertEquals("Expected and actual values should be the same.", permission2, accessPermissionImpl.getPermission());
        accessPermissionImpl.setPermission(null);
        assertEquals("Expected and actual values should be the same.", "*:*:*:*", accessPermissionImpl.getPermission().toString());
        accessPermissionImpl.setPermission(newPermission);
        assertEquals("Expected and actual values should be the same.", "*:*:*:*", accessPermissionImpl.getPermission().toString());
    }

    @Test
    public void hashCodeNullAccessInfoIdNullPermissionTest() {
        assertEquals("Expected and actual values should be the same.", 961, accessPermissionImpl1.hashCode());
    }

    @Test
    public void hashCodeNullPermissionTest() {
        accessPermissionImpl1.setAccessInfoId(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", 1953, accessPermissionImpl1.hashCode());
    }

    @Test
    public void hashCodeNullAccessInfoIdTest() {
        Permission permission = Mockito.mock(Permission.class);
        Mockito.when(permission.getDomain()).thenReturn(null);
        Mockito.when(permission.getAction()).thenReturn(null);
        Mockito.when(permission.getTargetScopeId()).thenReturn(null);
        Mockito.when(permission.getGroupId()).thenReturn(null);
        PermissionImpl permissionImpl = new PermissionImpl(permission);
        accessPermissionImpl1.setPermission(permissionImpl);
        assertEquals("Expected and actual values should be the same.", 924482, accessPermissionImpl1.hashCode());
    }

    @Test
    public void hashCodeTest() {
        accessPermissionImpl1.setAccessInfoId(KapuaId.ONE);
        Permission permission = Mockito.mock(Permission.class);
        Mockito.when(permission.getDomain()).thenReturn(null);
        Mockito.when(permission.getAction()).thenReturn(null);
        Mockito.when(permission.getTargetScopeId()).thenReturn(null);
        Mockito.when(permission.getGroupId()).thenReturn(null);
        PermissionImpl permissionImpl = new PermissionImpl(permission);
        accessPermissionImpl1.setPermission(permissionImpl);
        assertEquals("Expected and actual values should be the same.", 925474, accessPermissionImpl1.hashCode());
    }

    @Test
    public void equalsSameObjectTest() {
        assertTrue("True expected.", accessPermissionImpl1.equals(accessPermissionImpl1));
    }

    @Test
    public void equalsNullObjectTest() {
        assertFalse("False expected.", accessPermissionImpl1.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        assertFalse("False expected.", accessPermissionImpl1.equals(new Object()));
    }

    @Test
    public void equalsNullBothAccessInfoIdsNullPermissionsTest() {
        assertTrue("True expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }

    @Test
    public void equalsNullBothAccessInfoIdsEqualPermissionsTest() {
        accessPermissionImpl1.setPermission(permission2);
        accessPermissionImpl2.setPermission(permission2);
        assertTrue("True expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }

    @Test
    public void equalsNullBothAccessInfoIdsDifferentPermissionsTest() {
        accessPermissionImpl1.setPermission(permission2);
        assertFalse("False expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }

    @Test
    public void equalsNullAccessInfoIdTest() {
        accessPermissionImpl2.setAccessInfoId(KapuaId.ANY);
        assertFalse("False expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }

    @Test
    public void equalsDifferentAccessInfoIdsTest() {
        accessPermissionImpl1.setAccessInfoId(KapuaId.ONE);
        accessPermissionImpl2.setAccessInfoId(KapuaId.ANY);
        assertFalse("False expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }

    @Test
    public void equalsEqualAccessInfoIdsNullPermissionsTest() {
        accessPermissionImpl1.setAccessInfoId(KapuaId.ONE);
        accessPermissionImpl2.setAccessInfoId(KapuaId.ONE);
        assertTrue("True expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }

    @Test
    public void equalsEqualAccessInfoIdsEqualPermissionsTest() {
        accessPermissionImpl1.setAccessInfoId(KapuaId.ONE);
        accessPermissionImpl2.setAccessInfoId(KapuaId.ONE);
        accessPermissionImpl1.setPermission(permission2);
        accessPermissionImpl2.setPermission(permission2);
        assertTrue("True expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }

    @Test
    public void equalsEqualAccessInfoIdsDifferentPermissionsTest() {
        accessPermissionImpl1.setAccessInfoId(KapuaId.ONE);
        accessPermissionImpl2.setAccessInfoId(KapuaId.ONE);
        accessPermissionImpl1.setPermission(Mockito.mock(PermissionImpl.class));
        assertFalse("False expected.", accessPermissionImpl1.equals(accessPermissionImpl2));
    }
}