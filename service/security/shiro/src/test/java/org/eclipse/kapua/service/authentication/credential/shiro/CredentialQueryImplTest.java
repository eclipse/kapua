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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class CredentialQueryImplTest {

    @Test
    public void credentialQueryImplWithoutParameterTest() {
        CredentialQueryImpl credentialQueryImpl = new CredentialQueryImpl();
        Assert.assertNull("Null expected.", credentialQueryImpl.getScopeId());
        Assert.assertNull("credentialQueryImpl.sortCriteria", credentialQueryImpl.getSortCriteria());
        Assert.assertNotNull("credentialQueryImpl.defaultSortCriteria", credentialQueryImpl.getDefaultSortCriteria());
    }

    @Test
    public void credentialQueryImplScopeIdTest() {
        KapuaId[] scopeIds = {null, KapuaId.ONE};
        for (KapuaId scopeId : scopeIds) {
            CredentialQueryImpl credentialQueryImpl = new CredentialQueryImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, credentialQueryImpl.getScopeId());
            Assert.assertNull("credentialQueryImpl.sortCriteria", credentialQueryImpl.getSortCriteria());
            Assert.assertNotNull("credentialQueryImpl.defaultSortCriteria", credentialQueryImpl.getDefaultSortCriteria());
        }
    }
}