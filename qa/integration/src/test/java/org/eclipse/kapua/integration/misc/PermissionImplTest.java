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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class PermissionImplTest {

    @Test
    public void permissionImplWithoutParametersTest() throws Exception {
        Constructor<PermissionImpl> permissionImpl = PermissionImpl.class.getDeclaredConstructor();
        Assert.assertTrue("True expected.", Modifier.isProtected(permissionImpl.getModifiers()));
        permissionImpl.setAccessible(true);
        permissionImpl.newInstance();
    }

    @Test
    public void permissionImplScopeIdParameterTest() {
        Permission permission = Mockito.mock(Permission.class);

        Mockito.when(permission.getDomain()).thenReturn("domain");
        Mockito.when(permission.getAction()).thenReturn(Actions.connect);
        Mockito.when(permission.getTargetScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(permission.getGroupId()).thenReturn(KapuaId.ANY);
        Mockito.when(permission.getForwardable()).thenReturn(true);

        PermissionImpl permissionImpl = new PermissionImpl(permission);
        Assert.assertEquals("Expected and actual values should be the same.", "domain", permissionImpl.getDomain());
        Assert.assertEquals("Expected and actual values should be the same.", Actions.connect, permissionImpl.getAction());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, permissionImpl.getTargetScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, permissionImpl.getGroupId());
        Assert.assertTrue("True expected.", permissionImpl.getForwardable());
    }

    @Test
    public void permissionDomainActionScopeIdGroupIdTest() {
        String[] domains = {"domain 1 - 333  d>o,,,main.^%$      ", "do-ma)in 12^%$ ,. ///", "@#domain 123*&^,,  11", "-d)oM&^    ain", "D0O-ma  )in 123", "domain  domain 65%^8*7<>"};
        Actions[] actions = {Actions.read, Actions.write, Actions.delete, Actions.connect, Actions.execute};
        KapuaId targetScopeId = KapuaId.ANY;
        KapuaId groupId = KapuaId.ONE;

        for (String domain : domains) {
            for (Actions action : actions) {
                PermissionImpl permissionImpl = new PermissionImpl(domain, action, targetScopeId, groupId);
                Assert.assertEquals("Expected and actual values should be the same.", domain, permissionImpl.getDomain());
                Assert.assertEquals("Expected and actual values should be the same.", action, permissionImpl.getAction());
                Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, permissionImpl.getTargetScopeId());
                Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, permissionImpl.getGroupId());
                Assert.assertFalse("False expected.", permissionImpl.getForwardable());
            }
        }
    }

    @Test
    public void permissionDomainActionScopeIdGroupIdForwardableTest() {
        String[] domains = {"domain 1 - 333  d>o,,,main.^%$      ", "do-ma)in 12^%$ ,. ///", "@#domain 123*&^,,  11", "-d)oM&^    ain", "D0O-ma  )in 123", "domain  domain 65%^8*7<>"};
        Actions[] actions = {Actions.read, Actions.write, Actions.delete, Actions.connect, Actions.execute};
        KapuaId targetScopeId = KapuaId.ANY;
        KapuaId groupId = KapuaId.ONE;
        boolean[] forwardables = {true, false};

        for (String domain : domains) {
            for (Actions action : actions) {
                for (boolean forwardable : forwardables) {
                    PermissionImpl permissionImpl = new PermissionImpl(domain, action, targetScopeId, groupId,forwardable);
                    Assert.assertEquals("Expected and actual values should be the same.", domain, permissionImpl.getDomain());
                    Assert.assertEquals("Expected and actual values should be the same.", action, permissionImpl.getAction());
                    Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, permissionImpl.getTargetScopeId());
                    Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, permissionImpl.getGroupId());
                    Assert.assertEquals("Expected and actual values should be the same.", forwardable, permissionImpl.getForwardable());
                }
            }
        }
    }
}