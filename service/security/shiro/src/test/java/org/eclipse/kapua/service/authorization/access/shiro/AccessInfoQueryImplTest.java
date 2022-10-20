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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class AccessInfoQueryImplTest {

    @Test
    public void accessInfoQueryImplWithoutParameterTest() {
        AccessInfoQueryImpl accessInfoQueryImpl = new AccessInfoQueryImpl();

        Assert.assertNull("Null expected.", accessInfoQueryImpl.getScopeId());
    }

    @Test
    public void accessInfoQueryImplScopeIdParameterTest() {
        AccessInfoQueryImpl accessInfoQueryImpl = new AccessInfoQueryImpl(KapuaId.ONE);

        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessInfoQueryImpl.getScopeId());
    }

    @Test
    public void accessInfoQueryImplNullScopeIdParameterTest() {
        AccessInfoQueryImpl accessInfoQueryImpl = new AccessInfoQueryImpl(null);

        Assert.assertNull("Null expected.", accessInfoQueryImpl.getScopeId());
    }
}