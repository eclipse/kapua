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
 *******************************************************************************/
package org.eclipse.kapua.test.authorization;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

@TestService
@KapuaProvider
public class PermissionFactoryMock implements PermissionFactory {

    @Override
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId) {
        return newPermission(domain, action, targetScopeId, null);
    }

    @Override
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId, KapuaId groupId) {
        return newPermission(domain, action, targetScopeId, groupId, true);
    }

    @Override
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId, KapuaId groupId, boolean forwardable) {
        return null;
    }

    @Override
    public Permission parseString(String stringPermission)
            throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

}
