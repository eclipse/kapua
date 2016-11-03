package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.role.GwtRole;
import org.eclipse.kapua.app.console.shared.model.role.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.role.GwtRoleQuery;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;

public class GwtRoleServiceImpl extends KapuaRemoteServiceServlet implements GwtRoleService {

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
    public ListLoadResult<GwtRole> query(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery) throws GwtKapuaException {
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