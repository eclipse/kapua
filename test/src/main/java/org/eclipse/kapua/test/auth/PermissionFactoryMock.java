/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.test.auth;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermission;

@TestService
public class PermissionFactoryMock implements PermissionFactory
{

    @Override
    public Permission newPermission(String domain, Actions action, KapuaId targetScopeId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermission newRolePermission(KapuaId scopeId, Permission p) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Permission parseString(String stringPermission)
        throws KapuaException
    {
        // TODO Auto-generated method stub
        return null;
    }

}
