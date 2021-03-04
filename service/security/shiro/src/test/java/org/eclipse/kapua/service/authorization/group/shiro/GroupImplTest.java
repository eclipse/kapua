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
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.eclipse.kapua.service.authorization.group.Group;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import java.util.Date;

@Category(Categories.junitTests.class)
public class GroupImplTest extends Assert {

    @Test
    public void groupImplWithoutParametersTest() {
        GroupImpl groupImpl = new GroupImpl();
        assertNull("Null expected.", groupImpl.getScopeId());
    }

    @Test
    public void groupImplScopeIdParameterTest() {
        GroupImpl groupImpl = new GroupImpl(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, groupImpl.getScopeId());
    }

    @Test
    public void groupImplNullScopeIdParameterTest() {
        GroupImpl groupImpl = new GroupImpl((KapuaId) null);
        assertNull("Null expected.", groupImpl.getScopeId());
    }

    @Test
    public void groupImplGroupParameterTest() throws KapuaException {
        Group group = Mockito.mock(Group.class);
        Date createdOn = new Date();
        Date modifiedOn = new Date();

        Mockito.when(group.getName()).thenReturn("group name");
        Mockito.when(group.getDescription()).thenReturn("description");
        Mockito.when(group.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(group.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(group.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(group.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(group.getModifiedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(group.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(group.getOptlock()).thenReturn(11);

        GroupImpl groupImpl = new GroupImpl(group);
        assertEquals("Expected and actual values should be the same.", "group name", groupImpl.getName());
        assertEquals("Expected and actual values should be the same.", "description", groupImpl.getDescription());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, groupImpl.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, groupImpl.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, groupImpl.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, groupImpl.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, groupImpl.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", modifiedOn, groupImpl.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", 11, groupImpl.getOptlock());
    }

    @Test(expected = NullPointerException.class)
    public void groupImplNullGroupParameterTest() throws KapuaException {
        new GroupImpl((Group) null);
    }
}