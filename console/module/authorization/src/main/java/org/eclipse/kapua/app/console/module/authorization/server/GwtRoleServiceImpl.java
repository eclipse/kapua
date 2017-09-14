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
import java.util.List;

import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
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
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

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
    public PagingLoadResult<GwtRole> query(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtRole> gwtRoles = new ArrayList<GwtRole>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            UserService userService = locator.getService(UserService.class);

            // Convert from GWT entity
            RoleQuery roleQuery = GwtKapuaAuthorizationModelConverter.convertRoleQuery(loadConfig, gwtRoleQuery);

            // query
            RoleListResult roles = roleService.query(roleQuery);

            // If there are results
            if (!roles.isEmpty()) {
                // count
                if (roles.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(roleService.count(roleQuery)).intValue();
                } else {
                    totalLength = roles.getSize();
                }

                // Converto to GWT entity
                for (Role r : roles.getItems()) {
                    gwtRoles.add(KapuaGwtAuthorizationModelConverter.convertRole(r));
                    for (GwtRole gwtRole : gwtRoles) {
                        User user = userService.find(r.getScopeId(), r.getCreatedBy());
                        if (user != null) {
                            gwtRole.setUserName(user.getDisplayName());
                        }
                    }
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
            UserService userService = locator.getService(UserService.class);

            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId roleId = GwtKapuaCommonsModelConverter.convertKapuaId(roleShortId);

            // Find
            Role role = roleService.find(scopeId, roleId);
            User user = userService.find(scopeId, role.getCreatedBy());

            // If there are results
            if (role != null) {
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleName", role.getName()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleModifiedOn", role.getModifiedOn()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleModifiedBy", user.getDisplayName()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleCreatedOn", role.getCreatedOn()));
                gwtRoleDescription.add(new GwtGroupedNVPair("roleInfo", "roleCreatedBy", user.getDisplayName()));
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(gwtRoleDescription);
    }

    @Override
    public PagingLoadResult<GwtRolePermission> getRolePermissions(PagingLoadConfig loadConfig, String scopeShortId, String roleShortId) throws GwtKapuaException {
        //
        // Do get
        List<GwtRolePermission> gwtRolePermissions = new ArrayList<GwtRolePermission>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
            UserService userService = locator.getService(UserService.class);
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId roleId = GwtKapuaCommonsModelConverter.convertKapuaId(roleShortId);

            // Get permissions assigned to the Role
            RolePermissionListResult list = rolePermissionService.findByRoleId(scopeId, roleId);

            if (list != null) {
                for (RolePermission rolePermission : list.getItems()) {
                    gwtRolePermissions.add(KapuaGwtAuthorizationModelConverter.convertRolePermission(rolePermission));
                    for (GwtRolePermission gwtRolePermission : gwtRolePermissions) {
                        User user = userService.find(scopeId, rolePermission.getCreatedBy());
                        if (user != null) {
                            gwtRolePermission.setUserName(user.getDisplayName());
                        }
                    }
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtRolePermission>(gwtRolePermissions, 0, gwtRolePermissions.size());
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
