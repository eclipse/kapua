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
import org.eclipse.kapua.service.authorization.role.shiro.RoleDomain;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.user.role.UserRoles;
import org.eclipse.kapua.service.authorization.user.role.UserRolesCreator;
import org.eclipse.kapua.service.authorization.user.role.UserRolesListResult;
import org.eclipse.kapua.service.authorization.user.role.UserRolesService;

/**
 * User roles service implementation.
 * 
 * @since 1.0
 *
 */
public class UserRolesServiceImpl implements UserRolesService {

    @Override
    public UserRoles create(UserRolesCreator userRoleCreator)
        throws KapuaException
    {
        ArgumentValidator.notNull(userRoleCreator, "userRoleCreator");
        ArgumentValidator.notNull(userRoleCreator.getUserId(), "userRoleCreator.userId");
        ArgumentValidator.notEmptyOrNull(userRoleCreator.getRoles(), "userRoleCreator.roles");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.write, userRoleCreator.getScopeId()));

        //
        // Do create
        UserRoles userRole = null;
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            userRole = UserRolesDAO.create(em, userRoleCreator);

            em.commit();
        }
        catch (Exception e) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return userRole;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId userRoleId) throws KapuaException {
        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.delete, scopeId));

        //
        // Do delete
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            if (UserRolesDAO.find(em, userRoleId) == null) {
                throw new KapuaEntityNotFoundException(Role.TYPE, userRoleId);
            }

            em.beginTransaction();
            UserRolesDAO.delete(em, userRoleId);
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
    public UserRoles find(KapuaId scopeId, KapuaId userRoleId)
        throws KapuaException
    {
        ArgumentValidator.notNull(scopeId, "accountId");
        ArgumentValidator.notNull(userRoleId, "userRoleId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(RoleDomain.ROLE, Actions.read, scopeId));

        //
        // Do find
        UserRoles userRole = null;
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            userRole = UserRolesDAO.find(em, userRoleId);
        }
        catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }
        return userRole;
    }

    @Override
    public UserRolesListResult query(KapuaQuery<UserRoles> query)
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
        UserRolesListResult result = null;
        EntityManager em = AuthorizationEntityManagerFactory.getEntityManager();
        try {
            result = UserRolesDAO.query(em, query);
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
    public long count(KapuaQuery<UserRoles> query)
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
            count = UserRolesDAO.count(em, query);
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
    public UserRolesListResult merge(Set<UserRolesCreator> newPermissions)
        throws KapuaException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
