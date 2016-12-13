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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;

/**
 * {@link AccessRole} DAO
 * 
 * @since 1.0
 *
 */
public class AccessRoleDAO extends ServiceDAO {

    /**
     * Creates and return new access permission
     * 
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static AccessRole create(EntityManager em, AccessRoleCreator creator)
            throws KapuaException {
        AccessRole accessRole = new AccessRoleImpl(creator.getScopeId());

        accessRole.setAccessInfoId(creator.getAccessInfoId());
        accessRole.setRoleId(creator.getRoleId());

        return ServiceDAO.create(em, accessRole);
    }

    /**
     * Find the access info by access permission identifier
     * 
     * @param em
     * @param accessPermissionId
     * @return
     */
    public static AccessRole find(EntityManager em, KapuaId accessRoleId) {
        return em.find(AccessRoleImpl.class, accessRoleId);
    }

    /**
     * Delete the access permission by access permission id.
     * 
     * @param em
     * @param accessPermissionId
     */
    public static void delete(EntityManager em, KapuaId accessRoleId) {
        ServiceDAO.delete(em, AccessRoleImpl.class, accessRoleId);
    }

    /**
     * Return the {@link AccessRole}s list matching the provided query
     * 
     * @param em
     * @param accessInfoPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static AccessRoleListResult query(EntityManager em, KapuaQuery<AccessRole> accessInfoPermissionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, AccessRole.class, AccessRoleImpl.class, new AccessRoleListResultImpl(), accessInfoPermissionQuery);
    }

    /**
     * Return the count of {@link AccessRole} matching the provided query
     * 
     * @param em
     * @param accessPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<AccessRole> accessPermissionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, AccessRole.class, AccessRoleImpl.class, accessPermissionQuery);
    }
}
