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
package org.eclipse.kapua.service.authorization.user.permission;

import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;

/**
 * User permission service definition.
 * 
 * @since 1.0
 *
 */
public interface UserPermissionService extends KapuaEntityService<UserPermission, UserPermissionCreator>
{
    /**
     * Create e new user permission
     */
    public UserPermission create(UserPermissionCreator creator)
        throws KapuaException;

    /**
     * Find the user permission by scope identifier and user identifier
     */
    public UserPermission find(KapuaId accountId, KapuaId entityId)
        throws KapuaException;

    /**
     * Return the user permission list matching the provided query
     */
    public UserPermissionListResult query(KapuaQuery<UserPermission> query)
        throws KapuaException;

    /**
     * Return the count of the user permission matching the provided query
     */
    public long count(KapuaQuery<UserPermission> query)
        throws KapuaException;

    /**
     * Delete the user permission by scope identifier and entity identifier
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
    public UserPermissionListResult merge(Set<UserPermissionCreator> newPermissions)
        throws KapuaException;
}
