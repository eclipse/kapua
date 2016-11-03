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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;

/**
 * Role DAO
 * 
 * @since 1.0
 *
 */
public class RoleDAO extends ServiceDAO {

    /**
     * Creates and return new role
     * 
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static Role create(EntityManager em, RoleCreator creator)
            throws KapuaException {
        Role role = new RoleImpl(creator.getScopeId());

        role.setName(creator.getName());
        role.setPermissions(creator.getRoles());

        return ServiceDAO.create(em, role);
    }

    public static Role update(EntityManager em, Role role) {
        //
        // Update Role
        RoleImpl roleImpl = (RoleImpl) role;

        return ServiceDAO.update(em, RoleImpl.class, roleImpl);
    }

    /**
     * Find the role by role identifier
     * 
     * @param em
     * @param roleId
     * @return
     */
    public static Role find(EntityManager em, KapuaId roleId) {
        return em.find(RoleImpl.class, roleId);
    }

    /**
     * Delete the role by role identifier
     * 
     * @param em
     * @param roleId
     */
    public static void delete(EntityManager em, KapuaId roleId) {
        ServiceDAO.delete(em, RoleImpl.class, roleId);
    }

    /**
     * Return the role list matching the provided query
     * 
     * @param em
     * @param roleQuery
     * @return
     * @throws KapuaException
     */
    public static RoleListResult query(EntityManager em, KapuaQuery<Role> roleQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Role.class, RoleImpl.class, new RoleListResultImpl(), roleQuery);
    }

    /**
     * Return the role count matching the provided query
     * 
     * @param em
     * @param roleQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<Role> roleQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Role.class, RoleImpl.class, roleQuery);
    }

}
