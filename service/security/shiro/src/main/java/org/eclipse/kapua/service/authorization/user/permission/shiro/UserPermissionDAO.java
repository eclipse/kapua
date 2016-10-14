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
package org.eclipse.kapua.service.authorization.user.permission.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.user.permission.UserPermission;
import org.eclipse.kapua.service.authorization.user.permission.UserPermissionCreator;
import org.eclipse.kapua.service.authorization.user.permission.UserPermissionListResult;

/**
 * User permission DAO
 * 
 * @since 1.0
 *
 */
public class UserPermissionDAO extends ServiceDAO
{

    /**
     * Creates and return new user permission
     * 
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static UserPermission create(EntityManager em, UserPermissionCreator creator)
        throws KapuaException
    {
        UserPermission permission = new UserPermissionImpl(creator.getScopeId());

        permission.setUserId(creator.getUserId());
        permission.setPermission(creator.getPermission());

        return ServiceDAO.create(em, permission);
    }

    /**
     * Find the user permission by user permission identifier
     * 
     * @param em
     * @param permissionId
     * @return
     */
    public static UserPermission find(EntityManager em, KapuaId permissionId)
    {
        return em.find(UserPermissionImpl.class, permissionId);
    }

    /**
     * Delete the user permission by user permission identifier
     * 
     * @param em
     * @param permissionId
     */
    public static void delete(EntityManager em, KapuaId permissionId)
    {
        ServiceDAO.delete(em, UserPermissionImpl.class, permissionId);
    }

    /**
     * Return the user permission list matching the provided query
     * 
     * @param em
     * @param userPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static UserPermissionListResult query(EntityManager em, KapuaQuery<UserPermission> userPermissionQuery)
        throws KapuaException
    {
        return ServiceDAO.query(em, UserPermission.class, UserPermissionImpl.class, new UserPermissionListResultImpl(), userPermissionQuery);
    }

    /**
     * Return the user permission count matching the provided query
     * 
     * @param em
     * @param userPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<UserPermission> userPermissionQuery)
        throws KapuaException
    {
        return ServiceDAO.count(em, UserPermission.class, UserPermissionImpl.class, userPermissionQuery);
    }

}
