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

import java.util.Set;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;

/**
 * Role service implementation.
 * 
 * @since 1.0
 *
 */
public class RoleServiceImpl implements RoleService
{

    @Override
    public Role create(RoleCreator roleCreator)
        throws KapuaException
    {
        ArgumentValidator.notNull(roleCreator, "roleCreator");
        ArgumentValidator.notEmptyOrNull(roleCreator.getName(), "roleCreator.name");
        ArgumentValidator.notNull(roleCreator.getRoles(), "roleCreator.permissions");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.write, roleCreator.getScopeId()));

        //
        // Do create
        Role role = null;
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            role = RoleDAO.create(em, roleCreator);

            em.commit();
        }
        catch (Exception e) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return role;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId roleId) throws KapuaException {
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.delete, scopeId));

        //
        // Do delete
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            if (RoleDAO.find(em, roleId) == null) {
                throw new KapuaEntityNotFoundException(Role.TYPE, roleId);
            }

            em.beginTransaction();
            RoleDAO.delete(em, roleId);
            em.commit();
        }
        catch (KapuaException e) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }
    }

    @Override
    public Role find(KapuaId scopeId, KapuaId roleId)
        throws KapuaException
    {
        ArgumentValidator.notNull(scopeId, "accountId");
        ArgumentValidator.notNull(roleId, "roleId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.read, scopeId));

        //
        // Do find
        Role role = null;
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            role = RoleDAO.find(em, roleId);
        }
        catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }
        return role;
    }

    @Override
    public RoleListResult query(KapuaQuery<Role> query)
        throws KapuaException
    {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.read, query.getScopeId()));

        //
        // Do query
        RoleListResult result = null;
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            result = RoleDAO.query(em, query);
        }
        catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return result;
    }

    @Override
    public long count(KapuaQuery<Role> query)
        throws KapuaException
    {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.read, query.getScopeId()));

        //
        // Do count
        long count = 0;
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            count = RoleDAO.count(em, query);
        }
        catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return count;
    }

    @Override
    public RoleListResult merge(Set<RoleCreator> newPermissions)
        throws KapuaException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
