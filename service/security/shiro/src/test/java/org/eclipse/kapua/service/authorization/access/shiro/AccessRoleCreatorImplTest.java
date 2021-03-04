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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class AccessRoleCreatorImplTest extends Assert {

    AccessRoleCreatorImpl accessRoleCreatorImpl;

    @Before
    public void initialize() {
        accessRoleCreatorImpl = new AccessRoleCreatorImpl(KapuaId.ONE);
    }

    @Test
    public void accessRoleCreatorImplTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRoleCreatorImpl.getScopeId());
        assertNull("Null expected.", accessRoleCreatorImpl.getAccessInfoId());
        assertNull("Null expected.", accessRoleCreatorImpl.getRoleId());
    }

    @Test
    public void accessRoleCreatorImplNullTest() {
        AccessRoleCreatorImpl accessRoleCreatorImplNullId = new AccessRoleCreatorImpl(null);
        assertNull("Null expected.", accessRoleCreatorImplNullId.getScopeId());
        assertNull("Null expected.", accessRoleCreatorImplNullId.getAccessInfoId());
        assertNull("Null expected.", accessRoleCreatorImplNullId.getRoleId());
    }

    @Test
    public void setAndGetAccessInfoIdTest() {
        accessRoleCreatorImpl.setAccessInfoId(KapuaId.ANY);
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleCreatorImpl.getAccessInfoId());

        accessRoleCreatorImpl.setAccessInfoId(null);
        assertNull("Null expected.", accessRoleCreatorImpl.getAccessInfoId());
    }

    @Test
    public void setAndGetRoleIdTest() {
        accessRoleCreatorImpl.setRoleId(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRoleCreatorImpl.getRoleId());

        accessRoleCreatorImpl.setRoleId(null);
        assertNull("Null expected.", accessRoleCreatorImpl.getRoleId());
    }
}