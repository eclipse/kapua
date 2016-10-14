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
package org.eclipse.kapua.service.authorization.role;

import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;

/**
 * Role service definition.
 * 
 * @since 1.0
 *
 */
public interface RoleService extends KapuaEntityService<Role, RoleCreator>
{
    /**
     * Creates a new {@link Role} based on the parameters provided by the creator
     */
    public Role create(RoleCreator creator)
        throws KapuaException;

    /**
     * Find the {@link Role} specified by the provided account identifier and entity identifier
     */
    public Role find(KapuaId accountId, KapuaId entityId)
        throws KapuaException;

    /**
     * Return the role list for the specific role query
     */
    public RoleListResult query(KapuaQuery<Role> query)
        throws KapuaException;

    /**
     * Return the role count for the specific role query
     */
    public long count(KapuaQuery<Role> query)
        throws KapuaException;

    /**
     * Delete the role identified by the provided entity
     */
    public void delete(KapuaId scopeId, KapuaId entityId)
        throws KapuaException;

    /**
     * Merge the new permissions list with the already persisted permissions.<br>
     * In other word the newPermission list will replace all the roles for the user in the database.
     * 
     * @param newPermissions
     * @return
     * @throws KapuaException
     */
    public RoleListResult merge(Set<RoleCreator> newPermissions)
        throws KapuaException;
}
