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
package org.eclipse.kapua.service.authorization.permission;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.RolePermission;

/**
 * Permission factory service definition.
 * 
 * @since 1.0
 *
 */
public interface PermissionFactory extends KapuaObjectFactory
{
    /**
     * Creates a new permission for the specified domain, action and target scope identifier
     * 
     * @param domain
     * @param action
     * @param targetScopeId
     * @return
     */
    public Permission newPermission(String domain, Actions action, KapuaId targetScopeId);

    /**
     * Creates a new role permission for the specified scope identifier, domain, action and target scope identifier
     * 
     * @param scopeId
     * @param domain
     * @param action
     * @param targetScopeId
     * @return
     */
    public RolePermission newRolePermission(KapuaId scopeId, String domain, Actions action, KapuaId targetScopeId);

    /**
     * Parse the permission string representation to build a new {@link Permission}
     * 
     * @param stringPermission
     * @return
     * @throws KapuaException
     */
    public Permission parseString(String stringPermission)
        throws KapuaException;
}
