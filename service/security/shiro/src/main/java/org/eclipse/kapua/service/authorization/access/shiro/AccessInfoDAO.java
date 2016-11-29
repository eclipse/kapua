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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.shiro.RoleDAO;

/**
 * Access Info DAO
 * 
 * @since 1.0
 *
 */
public class AccessInfoDAO extends ServiceDAO {

    /**
     * Creates and return new access info
     * 
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static AccessInfo create(EntityManager em, AccessInfoCreator creator)
            throws KapuaException {
        AccessInfo accessInfo = new AccessInfoImpl(creator.getScopeId());

        accessInfo.setUserId(creator.getUserId());

        if (creator.getPermissions() != null) {
            Set<AccessPermission> accessPermissions = new HashSet<>();
            for (Permission p : creator.getPermissions()) {
                AccessPermission accessPermission = new AccessPermissionImpl(creator.getScopeId());
                accessPermission.setPermission(p);
                accessPermissions.add(accessPermission);
            }

            accessInfo.setPermissions(accessPermissions);
        }

        if (creator.getRoleIds() != null) {
            Set<AccessRole> accessRoles = new HashSet<>();

            for (KapuaId rId : creator.getRoleIds()) {
                AccessRole accessRole = new AccessRoleImpl(creator.getScopeId());
                accessRole.setRole(RoleDAO.find(em, rId));
                accessRoles.add(accessRole);
            }

            accessInfo.setRoles(accessRoles);
        }

        return ServiceDAO.create(em, accessInfo);
    }

    /**
     * Find the access info by user access info identifier
     * 
     * @param em
     * @param accessInfoId
     * @return
     */
    public static AccessInfo find(EntityManager em, KapuaId accessInfoId) {
        return em.find(AccessInfoImpl.class, accessInfoId);
    }

    /**
     * Delete the access info by access info identifier
     * 
     * @param em
     * @param accessInfoId
     */
    public static void delete(EntityManager em, KapuaId accessInfoId) {
        ServiceDAO.delete(em, AccessInfoImpl.class, accessInfoId);
    }

    /**
     * Return the access info list matching the provided query
     * 
     * @param em
     * @param accessInfoQuery
     * @return
     * @throws KapuaException
     */
    public static AccessInfoListResult query(EntityManager em, KapuaQuery<AccessInfo> accessInfoQuery)
            throws KapuaException {
        return ServiceDAO.query(em, AccessInfo.class, AccessInfoImpl.class, new AccessInfoListResultImpl(), accessInfoQuery);
    }

    /**
     * Return the access info count matching the provided query
     * 
     * @param em
     * @param accessInfoQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<AccessInfo> accessInfoQuery)
            throws KapuaException {
        return ServiceDAO.count(em, AccessInfo.class, AccessInfoImpl.class, accessInfoQuery);
    }

}
