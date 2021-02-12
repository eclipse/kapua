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
package org.eclipse.kapua.service.authentication.token.shiro;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

@Category(JUnitTests.class)
public class LoginInfoImplTest extends Assert {

    LoginInfoImpl loginInfoImpl;
    Set<RolePermission> rolePermissions;
    Set<AccessPermission> accessPermissions;

    @Before
    public void initialize() {
        loginInfoImpl = new LoginInfoImpl();
        rolePermissions = new HashSet<>();
        accessPermissions = new HashSet<>();
    }

    @Test
    public void setAndGetAccessTokenTest() {
        AccessToken[] accessTokens = {null, Mockito.mock(AccessToken.class)};
        assertNull("Null expected.", loginInfoImpl.getAccessToken());
        for (AccessToken accessToken : accessTokens) {
            loginInfoImpl.setAccessToken(accessToken);
            assertEquals("Expected and actual values should be the same.", accessToken, loginInfoImpl.getAccessToken());
        }
    }

    @Test
    public void setAndGetRolePermissionEmptySetTest() {
        assertNull("Null expected.", loginInfoImpl.getRolePermission());
        loginInfoImpl.setRolePermission(rolePermissions);
        assertEquals("Expected and actual values should be the same.", rolePermissions, loginInfoImpl.getRolePermission());
    }

    @Test
    public void setAndGetRolePermissionTest() {
        rolePermissions.add(Mockito.mock(RolePermission.class));
        rolePermissions.add(Mockito.mock(RolePermission.class));

        assertNull("Null expected.", loginInfoImpl.getRolePermission());
        loginInfoImpl.setRolePermission(rolePermissions);
        assertEquals("Expected and actual values should be the same.", rolePermissions, loginInfoImpl.getRolePermission());
    }

    @Test
    public void setAndGetRolePermissionNullTest() {
        assertNull("Null expected.", loginInfoImpl.getRolePermission());
        loginInfoImpl.setRolePermission(null);
        assertNull("Null expected.", loginInfoImpl.getRolePermission());
    }

    @Test
    public void setAndGetAccessPermissionEmptySetTest() {
        assertNull("Null expected.", loginInfoImpl.getAccessPermission());
        loginInfoImpl.setAccessPermission(accessPermissions);
        assertEquals("Expected and actual values should be the same.", accessPermissions, loginInfoImpl.getAccessPermission());
    }

    @Test
    public void setAndGetAccessPermissionTest() {
        accessPermissions.add(Mockito.mock(AccessPermission.class));
        accessPermissions.add(Mockito.mock(AccessPermission.class));

        assertNull("Null expected.", loginInfoImpl.getAccessPermission());
        loginInfoImpl.setAccessPermission(accessPermissions);
        assertEquals("Expected and actual values should be the same.", accessPermissions, loginInfoImpl.getAccessPermission());
    }

    @Test
    public void setAndGetAccessPermissionNullTest() {
        assertNull("Null expected.", loginInfoImpl.getAccessPermission());
        loginInfoImpl.setAccessPermission(null);
        assertNull("Null expected.", loginInfoImpl.getAccessPermission());
    }
}