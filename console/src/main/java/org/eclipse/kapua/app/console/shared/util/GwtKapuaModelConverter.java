package org.eclipse.kapua.app.console.shared.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.role.GwtRole;
import org.eclipse.kapua.app.console.shared.model.role.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.role.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.role.GwtRoleQuery;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDomain;
import org.eclipse.kapua.service.authorization.permission.Action;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.shiro.RoleDomain;
import org.eclipse.kapua.service.authorization.user.permission.shiro.UserPermissionDomain;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventDomain;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.user.internal.UserDomain;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;

/**
 * Utility class for convert {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 * 
 * @author alberto.codutti
 *
 */
public class GwtKapuaModelConverter {

    public static RoleQuery convertQuery(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);

        // Convert query
        RoleQuery roleQuery = roleFactory.newQuery(convert(gwtRoleQuery.getScopeId()));
        roleQuery.setOffset(loadConfig.getOffset());
        roleQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return roleQuery;
    }

    /**
     * Converts a {@link GwtRole} into a {@link Role} object for backend usage
     * 
     * @param gwtRole
     *            the {@link GwtRole} to convert
     * @return the converted {@link Role}
     * @since 1.0.0
     */
    public static Role convert(GwtRole gwtRole) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtRole.getScopeId());
        Role role = roleFactory.newRole(scopeId);

        // Convert name
        role.setName(gwtRole.getName());

        // Convert permission associated with role
        Set<RolePermission> rolePermissions = new HashSet<RolePermission>();
        for (GwtRolePermission gwtRolePermission : gwtRole.getPermissions()) {
            Permission p = convert(gwtRolePermission.getPermission());

            RolePermission rp = permissionFactory.newRolePermission(//
                    scopeId, //
                    p.getDomain(), //
                    p.getAction(), //
                    p.getTargetScopeId());

            rolePermissions.add(rp);
        }

        role.setPermissions(rolePermissions);

        //
        // Return converted
        return role;
    }

    /**
     * Converts a {@link GwtRoleCreator} into a {@link RoleCreator} object for backend usage
     * 
     * @param gwtRoleCreator
     *            the {@link GwtRoleCreator} to convert
     * @return the converted {@link RoleCreator}
     * @since 1.0.0
     */
    public static RoleCreator convert(GwtRoleCreator gwtRoleCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtRoleCreator.getScopeId());
        RoleCreator roleCreator = roleFactory.newCreator(scopeId);

        // Convert name
        roleCreator.setName(gwtRoleCreator.getName());

        // Convert permission associated with role
        Set<RolePermission> rolePermissions = new HashSet<RolePermission>();
        for (GwtPermission gwtPermission : gwtRoleCreator.getPermissions()) {
            Permission p = convert(gwtPermission);

            RolePermission rp = permissionFactory.newRolePermission(//
                    scopeId, //
                    p.getDomain(), //
                    p.getAction(), //
                    p.getTargetScopeId());

            rolePermissions.add(rp);
        }

        roleCreator.setRoles(rolePermissions);

        //
        // Return converted
        return roleCreator;
    }

    /**
     * Converts a {@link GwtPermission} into a {@link Permission} object for backend usage.
     * 
     * @param gwtPermission
     *            The {@link GwtPermission} to convert.
     * @return The converted {@link Permission}.
     * @since 1.0.0
     */
    public static Permission convert(GwtPermission gwtPermission) {
        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        //
        // Return converted
        return permissionFactory.newPermission(convert(gwtPermission.getDomain()),
                convert(gwtPermission.getAction()),
                convert(gwtPermission.getTargetScopeId()));
    }

    /**
     * Converts a {@link GwtAction} into the related {@link Action}
     * 
     * @param gwtAction
     *            the {@link GwtAction} to convert
     * @return the converted {@link Action}
     * 
     * @since 1.0.0
     */
    public static Actions convert(GwtAction gwtAction) {

        Actions action = null;
        if (gwtAction != null) {
            switch (gwtAction) {
            case connect:
                action = Actions.connect;
                break;
            case delete:
                action = Actions.delete;
                break;
            case exec:
                action = Actions.execute;
                break;
            case read:
                action = Actions.read;
                break;
            case write:
                action = Actions.write;
                break;
            }
        }
        return action;
    }

    /**
     * Converts a {@link GwtDomain} into the related equivalent domain string
     * 
     * @param gwtDomain
     *            the {@link GwtDomain} to convert
     * @return the converted domain {@link String}
     * 
     * @since 1.0.0
     */
    public static String convert(GwtDomain gwtDomain) {
        String domain = null;

        if (gwtDomain != null) {
            switch (gwtDomain) {
            case account:
                domain = AccountDomain.ACCOUNT;
                break;
            case credential:
                domain = CredentialDomain.CREDENTIAL;
                break;
            case datastore:
                domain = DatastoreDomain.DATA_STORE;
                break;
            case device:
                domain = DeviceDomain.DEVICE;
                break;
            case device_connection:
                domain = DeviceConnectionDomain.DEVICE_CONNECTION;
                break;
            case device_event:
                domain = DeviceEventDomain.DEVICE_EVENT;
                break;
            case device_management:
                domain = DeviceManagementDomain.DEVICE_MANAGEMENT;
                break;
            case role:
                domain = RoleDomain.ROLE;
                break;
            case user:
                domain = UserDomain.USER;
                break;
            case user_permission:
                domain = UserPermissionDomain.USER_PERMISSION;
                break;
            }
        }

        return domain;
    }

    /**
     * Converts a {@link KapuaId} form the short form to the actual object.
     * <p>
     * Example: AQ => 1
     * </p>
     * 
     * @param shortKapuaId
     *            the {@link KapuaId} in the short form
     * @return The converted {@link KapuaId}
     * @since 1.0.0
     */
    public static KapuaId convert(String shortKapuaId) {
        return KapuaEid.parseShortId(shortKapuaId);
    }
}