/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermissionCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.util.GwtKapuaAuthorizationModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.util.KapuaGwtAuthorizationModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;

public class GwtRoleServiceImpl extends KapuaRemoteServiceServlet implements GwtRoleService {

    private static final long serialVersionUID = 3606053200278262228L;

    @Override
    public GwtRole create(GwtXSRFToken xsrfToken, GwtRoleCreator gwtRoleCreator) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(xsrfToken);

        //
        // Do create
        GwtRole gwtRole = null;
        try {
            // Convert from GWT Entity
            RoleCreator roleCreator = GwtKapuaAuthorizationModelConverter.convertRoleCreator(gwtRoleCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            Role role = roleService.create(roleCreator);

            // Convert
            gwtRole = KapuaGwtAuthorizationModelConverter.convertRole(role);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtRole;
    }

    @Override
    public GwtRole update(GwtXSRFToken gwtXsrfToken, GwtRole gwtRole) throws GwtKapuaException {
        //
        // Checking XSRF token
        checkXSRFToken(gwtXsrfToken);

        //
        // Do update
        GwtRole gwtRoleUpdated = null;
        try {
            // Convert from GWT Entity
            Role role = GwtKapuaAuthorizationModelConverter.convertRole(gwtRole);

            // Update
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            Role roleUpdated = roleService.update(role);

            // Convert
            gwtRoleUpdated = KapuaGwtAuthorizationModelConverter.convertRole(roleUpdated);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtRoleUpdated;
    }

    @Override
    public GwtRole find(String scopeShortId, String roleShortId) throws GwtKapuaException {

        //
        // Do find
        GwtRole gwtRole = null;
        try {
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId roleId = GwtKapuaCommonsModelConverter.convertKapuaId(roleShortId);

            // Delete
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            Role role = roleService.find(scopeId, roleId);
            gwtRole = KapuaGwtAuthorizationModelConverter.convertRole(role);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtRole;
    }

    @Override
    public PagingLoadResult<GwtRole> query(PagingLoadConfig loadConfig, final GwtRoleQuery gwtRoleQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtRole> gwtRoles = new ArrayList<GwtRole>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            final UserService userService = locator.getService(UserService.class);
            final UserFactory userFactory = locator.getFactory(UserFactory.class);

            // Convert from GWT entity
            RoleQuery roleQuery = GwtKapuaAuthorizationModelConverter.convertRoleQuery(loadConfig, gwtRoleQuery);

            // query
            RoleListResult roles = roleService.query(roleQuery);

            UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                @Override
                public UserListResult call() throws Exception {
                    return userService.query(userFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtRoleQuery.getScopeId())));
                }
            });

            Map<String, String> usernameMap = new HashMap<String, String>();
            for (User user : userListResult.getItems()) {
                usernameMap.put(user.getId().toCompactId(), user.getName());
            }

            // If there are results
            if (!roles.isEmpty()) {
                // count
                totalLength = Long.valueOf(roleService.count(roleQuery)).intValue();

                // Converto to GWT entity
                for (Role r : roles.getItems()) {
                    GwtRole gwtRole = KapuaGwtAuthorizationModelConverter.convertRole(r);
                    gwtRole.setCreatedByName(usernameMap.get(r.getCreatedBy().toCompactId()));
                    gwtRole.setModifiedByName(usernameMap.get(r.getModifiedBy().toCompactId()));
                    gwtRoles.add(gwtRole);
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtRole>(gwtRoles, loadConfig.getOffset(), totalLength);
    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getRoleDescription(String scopeShortId, String roleShortId) throws GwtKapuaException {
        //
        // Do get
        List<GwtGroupedNVPair> gwtRoleDescription = new ArrayList<GwtGroupedNVPair>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            final UserService userService = locator.getService(UserService.class);

            // Convert from GWT Entity
            final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId roleId = GwtKapuaCommonsModelConverter.convertKapuaId(roleShortId);

            // Find
            final Role role = roleService.find(scopeId, roleId);
            User createdUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, role.getCreatedBy());
                }
            });

            User modifiedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, role.getModifiedBy());
                }
            });

            // If there are results
            if (role != null) {
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleName", role.getName()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleModifiedOn", role.getModifiedOn()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleModifiedBy", modifiedUser.getName()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleCreatedOn", role.getCreatedOn()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleCreatedBy", createdUser.getName()));
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(gwtRoleDescription);
    }

    @Override
    public PagingLoadResult<GwtRolePermission> getRolePermissions(PagingLoadConfig loadConfig, final String scopeShortId, String roleShortId) throws GwtKapuaException {
        //
        // Do get
        int totalLength = 0;
        List<GwtRolePermission> gwtRolePermissions = new ArrayList<GwtRolePermission>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
            RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);
            final UserService userService = locator.getService(UserService.class);
            final UserFactory userFactory = locator.getFactory(UserFactory.class);
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId roleId = GwtKapuaCommonsModelConverter.convertKapuaId(roleShortId);

            // Get permissions assigned to the Role
            RolePermissionQuery query = rolePermissionFactory.newQuery(scopeId);
            query.setPredicate(new AttributePredicate<KapuaId>("roleId", roleId));
            query.setLimit(loadConfig.getLimit());
            query.setOffset(loadConfig.getOffset());
            String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? "createdOnFormatted" : loadConfig.getSortField();
            if (sortField.equals("createdOnFormatted")) {
                sortField = "createdOn";
            }
            SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
            FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
            query.setSortCriteria(sortCriteria);
            RolePermissionListResult list = rolePermissionService.query(query);

            if (list != null) {
                UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return userService.query(userFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId)));
                    }
                });
                Map<String, String> usernameMap = new HashMap<String, String>();
                for (User user : userListResult.getItems()) {
                    usernameMap.put(user.getId().toCompactId(), user.getName());
                }
                totalLength = Long.valueOf(rolePermissionService.count(query)).intValue();
                for (RolePermission rolePermission : list.getItems()) {
                    GwtRolePermission gwtRolePermission = KapuaGwtAuthorizationModelConverter.convertRolePermission(rolePermission);
                    gwtRolePermission.setCreatedByName(usernameMap.get(rolePermission.getCreatedBy().toCompactId()));
                    gwtRolePermissions.add(gwtRolePermission);
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtRolePermission>(gwtRolePermissions, loadConfig.getOffset(), totalLength);
    }

    @Override
    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String roleShortId) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(gwtXsrfToken);

        //
        // Do delete
        try {
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId roleId = GwtKapuaCommonsModelConverter.convertKapuaId(roleShortId);

            // Delete
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            roleService.delete(scopeId, roleId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtRolePermission addRolePermission(GwtXSRFToken gwtXsrfToken, GwtRolePermissionCreator gwtRolePermissionCreator, GwtPermission gwtPermission) throws GwtKapuaException {
        checkXSRFToken(gwtXsrfToken);

        KapuaLocator locator = KapuaLocator.getInstance();
        RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtRolePermissionCreator.getScopeId());

        RolePermissionCreator rolePermissionCreator = rolePermissionFactory.newCreator(scopeId);
        rolePermissionCreator.setRoleId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtRolePermissionCreator.getRoleId()));
        rolePermissionCreator.setScopeId(scopeId);
        rolePermissionCreator.setPermission(GwtKapuaAuthorizationModelConverter.convertPermission(gwtPermission));

        RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
        GwtRolePermission newGwtRolePermission = null;
        RolePermission newRolePermission = null;
        try {
            newRolePermission = rolePermissionService.create(rolePermissionCreator);
            newGwtRolePermission = KapuaGwtAuthorizationModelConverter.convertRolePermission(newRolePermission);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return newGwtRolePermission;
    }

    @Override
    public void deleteRolePermission(GwtXSRFToken gwtXsrfToken, String scopeShortId, String roleShortId) throws GwtKapuaException {
        checkXSRFToken(gwtXsrfToken);
        KapuaLocator locator = KapuaLocator.getInstance();
        RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
        KapuaId roleId = GwtKapuaCommonsModelConverter.convertKapuaId(roleShortId);
        try {
            rolePermissionService.delete(scopeId, roleId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public ListLoadResult<GwtRole> findAll(String scopeIdString) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
        List<GwtRole> gwtRoleList = new ArrayList<GwtRole>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
            RoleQuery query = roleFactory.newQuery(scopeId);
            RoleListResult list = roleService.query(query);

            for (Role role : list.getItems()) {
                gwtRoleList.add(KapuaGwtAuthorizationModelConverter.convertRole(role));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtRole>(gwtRoleList);
    }
}
