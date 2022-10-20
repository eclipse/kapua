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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class DomainQueryImplTest {

    @Test
    public void domainQueryImplWithoutParametersTest() {
        DomainQueryImpl domainQueryImpl = new DomainQueryImpl();
        Assert.assertNull("Null expected.", domainQueryImpl.getScopeId());
    }

    @Test
    public void domainQueryImplScopeIdParameterTest() {
        DomainQueryImpl domainQueryImpl = new DomainQueryImpl(KapuaId.ONE);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainQueryImpl.getScopeId());
    }

    @Test
    public void domainQueryImplNullScopeIdParameterTest() {
        DomainQueryImpl domainQueryImpl = new DomainQueryImpl(null);
        Assert.assertNull("Null expected.", domainQueryImpl.getScopeId());
    }
}