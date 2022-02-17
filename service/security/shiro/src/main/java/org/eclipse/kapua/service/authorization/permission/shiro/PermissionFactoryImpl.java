/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.permission.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
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
