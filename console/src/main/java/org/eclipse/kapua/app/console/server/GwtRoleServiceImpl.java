package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleQuery;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
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
            RoleQuery roleQuery = GwtKapuaModelConverter.convertQuery(loadConfig, gwtRoleQuery);

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

                for (RolePermission rolePermission : role.getPermissions()) {
                    GwtRolePermission gwtRolePermission = KapuaGwtModelConverter.convert(rolePermission);

                    gwtRoleDescription.add(new GwtGroupedNVPair("Permission - " + rolePermission.toString(), "Domain", gwtRolePermission.getDomain()));
                    gwtRoleDescription.add(new GwtGroupedNVPair("Permission - " + rolePermission.toString(), "Action", gwtRolePermission.getAction()));
                    gwtRoleDescription.add(new GwtGroupedNVPair("Permission - " + rolePermission.toString(), "Target Scope Id", gwtRolePermission.getTargetScopeId()));
                    gwtRoleDescription.add(new GwtGroupedNVPair("Permission - " + rolePermission.toString(), "Added On", gwtRolePermission.getCreatedOn()));
                }
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
            RoleService roleService = locator.getService(RoleService.class);

            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId roleId = GwtKapuaModelConverter.convert(roleShortId);

            // Find
            Role role = roleService.find(scopeId, roleId);

            // If there are results
            if (role != null) {
                for (RolePermission rolePermission : role.getPermissions()) {
                    gwtRolePermissions.add(KapuaGwtModelConverter.convert(rolePermission));
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
}