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
package org.eclipse.kapua.service.authorization.user.role.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.user.role.UserRoles;
import org.eclipse.kapua.service.authorization.user.role.UserRolesCreator;
import org.eclipse.kapua.service.authorization.user.role.UserRolesListResult;

/**
 * User role DAO
 * 
 * @since 1.0
 *
 */
public class UserRolesDAO extends ServiceDAO
{

    /**
     * Creates and return new user roles
     * 
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static UserRoles create(EntityManager em, UserRolesCreator creator)
        throws KapuaException
    {
        UserRoles userRole = new UserRolesImpl(creator.getScopeId());

        userRole.setUserId(creator.getUserId());
        userRole.setRoles(creator.getRoles());

        return ServiceDAO.create(em, userRole);
    }

    /**
     * Find the user roles by user role identifier
     * 
     * @param em
     * @param userRoleId
     * @return
     */
    public static UserRoles find(EntityManager em, KapuaId userRoleId)
    {
        return em.find(UserRolesImpl.class, userRoleId);
    }

    /**
     * Delete the user roles by user role identifier
     * 
     * @param em
     * @param userRoleId
     */
    public static void delete(EntityManager em, KapuaId userRoleId)
    {
        ServiceDAO.delete(em, UserRolesImpl.class, userRoleId);
    }

    /**
     * Return the user roles list matching the provided query
     * 
     * @param em
     * @param userRoleQuery
     * @return
     * @throws KapuaException
     */
    public static UserRolesListResult query(EntityManager em, KapuaQuery<UserRoles> userRoleQuery)
        throws KapuaException
    {
        return ServiceDAO.query(em, UserRoles.class, UserRolesImpl.class, new UserRolesListResultImpl(), userRoleQuery);
    }

    /**
     * Return the user roles count matching the provided query
     * 
     * @param em
     * @param userRoleQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<UserRoles> userRoleQuery)
        throws KapuaException
    {
        return ServiceDAO.count(em, UserRoles.class, UserRolesImpl.class, userRoleQuery);
    }

}
