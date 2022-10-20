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
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class GroupQueryImplTest {

    @Test
    public void groupQueryImplTest() {
        GroupQueryImpl groupQueryImpl = new GroupQueryImpl(KapuaId.ONE);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, groupQueryImpl.getScopeId());
        Assert.assertNull("groupQueryImpl.sortCriteria", groupQueryImpl.getSortCriteria());
        Assert.assertNotNull("groupQueryImpl.defaultSortCriteria", groupQueryImpl.getDefaultSortCriteria());
    }

    @Test
    public void groupQueryImplNullTest() {
        GroupQueryImpl groupQueryImpl = new GroupQueryImpl(null);
        Assert.assertNull("Null expected.", groupQueryImpl.getScopeId());
        Assert.assertNull("groupQueryImpl.sortCriteria", groupQueryImpl.getSortCriteria());
        Assert.assertNotNull("groupQueryImpl.defaultSortCriteria", groupQueryImpl.getDefaultSortCriteria());
    }
}