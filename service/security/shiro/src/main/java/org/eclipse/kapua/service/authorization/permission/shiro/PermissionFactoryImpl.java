/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.permission.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

/**
 * {@link PermissionFactory} implementation.
 */
@KapuaProvider
public class PermissionFactoryImpl implements PermissionFactory {

    @Override
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId, KapuaId groupId, boolean forwardable) {
        return new PermissionImpl(domain != null ? domain.getName() : null, action, targetScopeId, groupId, forwardable);
    }
}
