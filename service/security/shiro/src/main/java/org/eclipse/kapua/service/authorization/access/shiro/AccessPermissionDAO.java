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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;

/**
 * {@link AccessPermission} {@link ServiceDAO}
 * 
 * @since 1.0
 *
 */
public class AccessPermissionDAO extends ServiceDAO {

    /**
     * Creates and return new access permission
     * 
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static AccessPermission create(EntityManager em, AccessPermissionCreator creator)
            throws KapuaException {
        AccessPermission accessPermission = new AccessPermissionImpl(creator.getScopeId());

        accessPermission.setAccessInfoId(creator.getAccessInfoId());
        accessPermission.setPermission(creator.getPermission());

        return ServiceDAO.create(em, accessPermission);
    }

    /**
     * Find the access info by access permission identifier
     * 
     * @param em
     * @param scopeId
     * @param accessPermissionId
     * @return
     */
    public static AccessPermission find(EntityManager em, KapuaId scopeId, KapuaId accessPermissionId) {
        return ServiceDAO.find(em, AccessPermissionImpl.class, scopeId, accessPermissionId);
    }

    /**
     * Return the {@link AccessPermission}s list matching the provided query
     *
     * @param em
     * @param accessInfoPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static AccessPermissionListResult query(EntityManager em, KapuaQuery<AccessPermission> accessInfoPermissionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, AccessPermission.class, AccessPermissionImpl.class, new AccessPermissionListResultImpl(), accessInfoPermissionQuery);
    }

    /**
     * Return the count of {@link AccessPermission} matching the provided query
     *
     * @param em
     * @param accessPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<AccessPermission> accessPermissionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, AccessPermission.class, AccessPermissionImpl.class, accessPermissionQuery);
    }

    /**
     * Delete the access permission by access permission id.
     *
     * @param em
     * @param scopeId
     * @param accessPermissionId
     * @throws KapuaEntityNotFoundException
     *             If {@link AccessPermission} is not found.
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId accessPermissionId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, AccessPermissionImpl.class, scopeId, accessPermissionId);
    }
}
