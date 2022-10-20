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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;


@Category(JUnitTests.class)
public class AccessInfoImplTest {

    AccessInfo accessInfo;
    Date modifiedOn, createdOn;

    @Before
    public void initialize() {
        accessInfo = Mockito.mock(AccessInfo.class);
        modifiedOn = new Date();
        createdOn = new Date();

        Mockito.when(accessInfo.getUserId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessInfo.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(accessInfo.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(accessInfo.getOptlock()).thenReturn(10);
        Mockito.when(accessInfo.getId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessInfo.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessInfo.getCreatedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(accessInfo.getCreatedOn()).thenReturn(createdOn);
    }

    @Test
    public void accessInfoImplWithoutParametersTest() {
        AccessInfoImpl accessInfoImpl = new AccessInfoImpl();

        Assert.assertNull("Null expected.", accessInfoImpl.getScopeId());
        Assert.assertNull("Null expected.", accessInfoImpl.getUserId());
    }

    @Test
    public void accessInfoImplScopeIdParameterTest() {
        AccessInfoImpl accessInfoImpl = new AccessInfoImpl(KapuaId.ONE);

        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessInfoImpl.getScopeId());
        Assert.assertNull("Null expected.", accessInfoImpl.getUserId());
    }

    @Test
    public void accessInfoImplNullScopeIdParameterTest() {
        AccessInfoImpl accessInfoImpl = new AccessInfoImpl((KapuaId) null);

        Assert.assertNull("Null expected.", accessInfoImpl.getScopeId());
        Assert.assertNull("Null expected.", accessInfoImpl.getUserId());
    }

    @Test
    public void accessInfoImplAccessInfoParameterTest() throws KapuaException {
        AccessInfoImpl accessInfoImpl = new AccessInfoImpl(accessInfo);

        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessInfoImpl.getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessInfoImpl.getUserId());
        Assert.assertEquals("Expected and actual values should be the same.", modifiedOn, accessInfoImpl.getModifiedOn());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessInfoImpl.getModifiedBy());
        Assert.assertEquals("Expected and actual values should be the same.", 10, accessInfoImpl.getOptlock());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessInfoImpl.getId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessInfoImpl.getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, accessInfoImpl.getCreatedOn());
    }

    @Test(expected = NullPointerException.class)
    public void accessInfoImplNullAccessInfoParameterTest() throws KapuaException {
        new AccessInfoImpl((AccessInfo) null);
    }

    @Test
    public void setAndGetUserId() throws KapuaException {
        KapuaId[] newUserIds = {null, KapuaId.ONE};
        AccessInfoImpl accessInfoImpl1 = new AccessInfoImpl();
        AccessInfoImpl accessInfoImpl2 = new AccessInfoImpl(KapuaId.ONE);
        AccessInfoImpl accessInfoImpl3 = new AccessInfoImpl(accessInfo);

        for (KapuaId newUserId : newUserIds) {
            accessInfoImpl1.setUserId(newUserId);
            accessInfoImpl2.setUserId(newUserId);
            accessInfoImpl3.setUserId(newUserId);

            Assert.assertEquals("Expected and actual values should be the same.", newUserId, accessInfoImpl1.getUserId());
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, accessInfoImpl2.getUserId());
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, accessInfoImpl3.getUserId());
        }
    }
}