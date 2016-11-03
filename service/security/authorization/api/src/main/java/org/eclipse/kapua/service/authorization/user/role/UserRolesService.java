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
package org.eclipse.kapua.service.authorization.user.role;

import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;

/**
 * User roles service definition.
 * 
 * @since 1.0
 *
 */
public interface UserRolesService extends KapuaEntityService<UserRoles, UserRolesCreator> {

    /**
     * Create e new user role
     * 
     * @since 1.0
     */
    public UserRoles create(UserRolesCreator creator)
            throws KapuaException;

    /**
     * Find the user role by scope identifier and user identifier
     * 
     * @since 1.0
     */
    public UserRoles find(KapuaId accountId, KapuaId entityId)
            throws KapuaException;

    /**
     * Find the {@link UserRoles} by the given userId
     * 
     * @param scopeId
     * @param userId
     * @return
     * @throws KapuaException
     * 
     * @since 1.0
     */
    public UserRoles findByUserId(KapuaId scopeId, KapuaId userId)
            throws KapuaException;

    /**
     * Return the user role list matching the provided query
     * 
     * @since 1.0
     */
    public UserRolesListResult query(KapuaQuery<UserRoles> query)
            throws KapuaException;

    /**
     * Return the count of the user role matching the provided query
     * 
     * @since 1.0
     */
    public long count(KapuaQuery<UserRoles> query)
            throws KapuaException;

    /**
     * Delete the user role by scope identifier and entity identifier
     * 
     * @since 1.0
     */
    public void delete(KapuaId scopeId, KapuaId entityId)
            throws KapuaException;

    /**
     * Merge the new roles list with the already persisted roles.<br>
     * In other word the newRoles list will replace all the roles for the user in the database.
     * 
     * @param newRoles
     * @return
     * @throws KapuaException
     * 
     * @since 1.0
     */
    public UserRolesListResult merge(Set<UserRolesCreator> newRoles)
            throws KapuaException;
}
