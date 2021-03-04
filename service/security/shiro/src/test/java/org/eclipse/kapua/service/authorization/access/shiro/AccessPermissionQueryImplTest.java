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
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class AccessPermissionQueryImplTest extends Assert {

    @Test
    public void accessPermissionQueryImplWithoutParametersTest() {
        AccessPermissionQueryImpl accessPermissionQueryImpl = new AccessPermissionQueryImpl();
        assertNull("Null expected.", accessPermissionQueryImpl.getScopeId());
        assertNotNull("NotNull expected.", accessPermissionQueryImpl.getSortCriteria());
    }

    @Test
    public void accessPermissionQueryImplScopeIdParameterTest() {
        AccessPermissionQueryImpl accessPermissionQueryImpl = new AccessPermissionQueryImpl(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessPermissionQueryImpl.getScopeId());
        assertNotNull("NotNull expected.", accessPermissionQueryImpl.getSortCriteria());
    }

    @Test
    public void accessPermissionQueryImplNullScopeIdParameterTest() {
        AccessPermissionQueryImpl accessPermissionQueryImpl = new AccessPermissionQueryImpl(null);
        assertNull("Null expected.", accessPermissionQueryImpl.getScopeId());
        assertNotNull("NotNull expected.", accessPermissionQueryImpl.getSortCriteria());
    }
}