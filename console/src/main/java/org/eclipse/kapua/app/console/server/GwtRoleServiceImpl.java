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
package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermissionCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleQuery;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
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
            RoleCreator roleCreator = GwtKapuaModelConverter.convert(gwtRoleCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            Role role = roleService.create(roleCreator);

            // Convert
            gwtRole = KapuaGwtModelConverter.convert(role);

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
            Role role = GwtKapuaModelConverter.convert(gwtRole);

            // Update
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            Role roleUpdated = roleService.update(role);

            // Convert
            gwtRoleUpdated = KapuaGwtModelConverter.convert(roleUpdated);

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
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId roleId = GwtKapuaModelConverter.convert(roleShortId);

            // Delete
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);
            Role role = roleService.find(scopeId, roleId);
            gwtRole = KapuaGwtModelConverter.convert(role);
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

            // Convert from GWT entity
            RoleQuery roleQuery = GwtKapuaModelConverter.convertRoleQuery(loadConfig, gwtRoleQuery);

            // query
            RoleListResult roles = roleService.query(roleQuery);

            // If there are results
            if (!roles.isEmpty()) {
                // count
                if (roles.getSize() >= loadConfig.getLimit()) {
                    totalLength = new Long(roleService.count(roleQuery)).intValue();
                } else {
                    totalLength = roles.getSize();
                }

                // Converto to GWT entity
                for (Role r : roles.getItems()) {
                    gwtRoles.add(KapuaGwtModelConverter.convert(r));
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

            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId roleId = GwtKapuaModelConverter.convert(roleShortId);

            // Find
            Role role = roleService.find(scopeId, roleId);

            // If there are results
            if (role != null) {
                gwtRoleDescription.add(new GwtGroupedNVPair("Entity", "Scope Id", KapuaGwtModelConverter.convert(role.getScopeId())));
                gwtRoleDescription.add(new GwtGroupedNVPair("Entity", "Id", KapuaGwtModelConverter.convert(role.getId())));
                gwtRoleDescription.add(new GwtGroupedNVPair("Entity", "Created On", role.getCreatedOn()));
                gwtRoleDescription.add(new GwtGroupedNVPair("Entity", "Created By", KapuaGwtModelConverter.convert(role.getCreatedBy())));
                gwtRoleDescription.add(new GwtGroupedNVPair("Entity", "Modified On", role.getModifiedOn()));
                gwtRoleDescription.add(new GwtGroupedNVPair("Entity", "Modified By", KapuaGwtModelConverter.convert(role.getModifiedBy())));
                gwtRoleDescription.add(new GwtGroupedNVPair("Role", "Name", role.getName()));
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
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId roleId = GwtKapuaModelConverter.convert(roleShortId);

            // Get permissions assigned to the Role
            RolePermissionListResult list = rolePermissionService.findByRoleId(scopeId, roleId);

            if (list != null) {
                for (RolePermission permission : list.getItems()) {
                    gwtRolePermissions.add(KapuaGwtModelConverter.convert(permission));
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
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId roleId = GwtKapuaModelConverter.convert(roleShortId);

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

        KapuaId scopeId = GwtKapuaModelConverter.convert(gwtRolePermissionCreator.getScopeId());

        RolePermissionCreator rolePermissionCreator = rolePermissionFactory.newCreator(scopeId);
        rolePermissionCreator.setRoleId(GwtKapuaModelConverter.convert(gwtRolePermissionCreator.getRoleId()));
        rolePermissionCreator.setScopeId(scopeId);
        rolePermissionCreator.setPermission(GwtKapuaModelConverter.convert(gwtPermission));

        RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
        GwtRolePermission newGwtRolePermission = null;
        RolePermission newRolePermission = null;
        try {
            newRolePermission = rolePermissionService.create(rolePermissionCreator);
            newGwtRolePermission = KapuaGwtModelConverter.convert(newRolePermission);
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
        KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
        KapuaId roleId = GwtKapuaModelConverter.convert(roleShortId);
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
                gwtRoleList.add(KapuaGwtModelConverter.convert(role));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtRole>(gwtRoleList);
    }
}