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
package org.eclipse.kapua.app.console.shared.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfoCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermissionCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleQuery;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserQuery;
import org.eclipse.kapua.broker.core.BrokerDomain;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDomain;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenDomain;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoDomain;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainDomain;
import org.eclipse.kapua.service.authorization.group.shiro.GroupDomain;
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
import org.eclipse.kapua.service.authorization.role.shiro.RolePredicates;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventDomain;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifecycleDomain;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.internal.UserDomain;
import org.eclipse.kapua.service.user.internal.UserPredicates;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;

/**
 * Utility class for convert {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 * 
 * @author alberto.codutti
 *
 */
public class GwtKapuaModelConverter {

    /**
     * Converts a {@link GwtRoleQuery} into a {@link Role} object for backend usage
     * 
     * @param loadConfig
     *            the load configuration
     * @param gwtRoleQuery
     *            the {@link GwtRoleQuery} to convert
     * @return the converted {@link RoleQuery}
     * @since 1.0.0
     */
    public static RoleQuery convertRoleQuery(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);

        // Convert query
        RoleQuery roleQuery = roleFactory.newQuery(convert(gwtRoleQuery.getScopeId()));
        if (gwtRoleQuery.getName() != null && gwtRoleQuery.getName() != "") {
            roleQuery.setPredicate(new AttributePredicate<String>(RolePredicates.ROLE_NAME, gwtRoleQuery.getName()));
        }
        roleQuery.setOffset(loadConfig.getOffset());
        roleQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return roleQuery;
    }
    
    /**
     * Converts a {@link GwtRoleQuery} into a {@link Role} object for backend usage
     * 
     * @param loadConfig
     *            the load configuration
     * @param gwtRoleQuery
     *            the {@link GwtRoleQuery} to convert
     * @return the converted {@link RoleQuery}
     * @since 1.0.0
     */
    public static UserQuery convertUserQuery(PagingLoadConfig loadConfig, GwtUserQuery gwtUserQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        UserFactory userFactory = locator.getFactory(UserFactory.class);

        // Convert query
        UserQuery userQuery = userFactory.newQuery(convert(gwtUserQuery.getScopeId()));
        if (gwtUserQuery.getName() != null && gwtUserQuery.getName() != "") {
            userQuery.setPredicate(new AttributePredicate<String>(UserPredicates.USER_NAME, gwtUserQuery.getName(), Operator.LIKE));
        }
        userQuery.setOffset(loadConfig.getOffset());
        userQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return userQuery;
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
        convertEntity(gwtRole, role);

        // Convert name
        role.setName(gwtRole.getName());

        // Convert permission associated with role
        Set<RolePermission> rolePermissions = new HashSet<RolePermission>();
        for (GwtRolePermission gwtRolePermission : gwtRole.getPermissions()) {

            Permission p = convert(new GwtPermission(gwtRolePermission.getDomainEnum(),
                    gwtRolePermission.getActionEnum(),
                    gwtRolePermission.getTargetScopeId(),
                    gwtRolePermission.getGroupId()));

            RolePermission rp = permissionFactory.newRolePermission(//
                    scopeId, //
                    p);
            rp.setId(convert(gwtRolePermission.getId()));
            rp.setRoleId(role.getId());

            rolePermissions.add(rp);
        }

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

        // Convert scopeId
        KapuaId scopeId = convert(gwtRoleCreator.getScopeId());
        RoleCreator roleCreator = roleFactory.newCreator(scopeId);

        // Convert name
        roleCreator.setName(gwtRoleCreator.getName());

        // Convert permission associated with role
        Set<Permission> permissions = new HashSet<Permission>();
        if (gwtRoleCreator.getPermissions() != null) {
            for (GwtPermission gwtPermission : gwtRoleCreator.getPermissions()) {
                permissions.add(convert(gwtPermission));
            }
        }

        roleCreator.setPermissions(permissions);

        //
        // Return converted
        return roleCreator;
    }
    
    /**
     * Converts a {@link GwtAccessRoleCreator} into a {@link AccessRoleCreator} object for backend usage
     * 
     * @param gwtAccessRoleCreator
     *            the {@link GwtAccessRoleCreator} to convert
     * @return the converted {@link AccessRoleCreator}
     * @since 1.0.0
     */
    public static AccessRoleCreator convert(GwtAccessRoleCreator gwtAccessRoleCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtAccessRoleCreator.getScopeId());
        AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(scopeId);

        // Convert accessInfoId
        accessRoleCreator.setAccessInfoId(convert(gwtAccessRoleCreator.getAccessInfoId()));
        
        // Convert roleId
        accessRoleCreator.setRoleId(convert(gwtAccessRoleCreator.getRoleId()));

        //
        // Return converted
        return accessRoleCreator;
    }
    
    /**
     * Converts a {@link GwtAccessPermissionCreator} into a {@link AccessPermissionCreator} object for backend usage
     * 
     * @param gwtAccessPermissionCreator
     *            the {@link GwtAccessPermissionCreator} to convert
     * @return the converted {@link AccessPermissionCreator}
     * @since 1.0.0
     */
    public static AccessPermissionCreator convert(GwtAccessPermissionCreator gwtAccessPermissionCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessPermissionFactory accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtAccessPermissionCreator.getScopeId());
        AccessPermissionCreator accessPermissionCreator = accessPermissionFactory.newCreator(scopeId);

        // Convert accessInfoId
        accessPermissionCreator.setAccessInfoId(convert(gwtAccessPermissionCreator.getAccessInfoId()));
        
        // Convert Permission
        accessPermissionCreator.setPermission(convert(gwtAccessPermissionCreator.getPermission()));

        //
        // Return converted
        return accessPermissionCreator;
    }
    
    public static AccessInfoCreator convert(GwtAccessInfoCreator gwtAccessInfoCreator) {
        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtAccessInfoCreator.getScopeId());
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(scopeId);

        // Convert userId
        accessInfoCreator.setUserId(convert(gwtAccessInfoCreator.getUserId()));

        //
        // Return converted
        return accessInfoCreator;
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
        return permissionFactory.newPermission(convert(gwtPermission.getDomainEnum()),
                convert(gwtPermission.getActionEnum()),
                convert(gwtPermission.getTargetScopeId()),
                convert(gwtPermission.getGroupId()));
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
            case execute:
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
    public static Domain convert(GwtDomain gwtDomain) {
        Domain domain = null;

        if (gwtDomain != null) {
            switch (gwtDomain) {
            case access_info:
                domain = new AccessInfoDomain();
                break;
            case access_token:
                domain = new AccessTokenDomain();
                break;
            case account:
                domain = new AccountDomain();
                break;
            case broker:
                domain = new BrokerDomain();
                break;
            case credential:
                domain = new CredentialDomain();
                break;
            case data:
                domain = new DatastoreDomain();
                break;
            case device:
                domain = new DeviceDomain();
                break;
            case device_connection:
                domain = new DeviceConnectionDomain();
                break;
            case device_event:
                domain = new DeviceEventDomain();
                break;
            case device_lifecycle:
                domain = new DeviceLifecycleDomain();
                break;
            case device_management:
                domain = new DeviceManagementDomain();
                break;
            case domain:
                domain = new DomainDomain();
                break;
            case group:
                domain = new GroupDomain();
                break;
            case role:
                domain = new RoleDomain();
                break;
            case user:
                domain = new UserDomain();
                break;
            }
        }

        return domain;
    }

    /**
     * Utility method to convert commons properties of {@link GwtUpdatableEntityModel} object to the matching {@link KapuaUpdatableEntity} object
     * 
     * @param gwtEntity
     *            The {@link GwtUpdatableEntityModel} from which copy values
     * @param kapuaEntity
     *            The {@link KapuaUpdatableEntity} into which to copy values
     * @since 1.0.0
     */
    private static void convertEntity(GwtUpdatableEntityModel gwtEntity, KapuaUpdatableEntity kapuaEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        convertEntity((GwtEntityModel) gwtEntity, (KapuaEntity) kapuaEntity);

        kapuaEntity.setOptlock(gwtEntity.getOptlock());
    }

    /**
     * Utility method to convert commons properties of {@link GwtEntityModel} object to the matching {@link KapuaEntity} object
     * 
     * @param gwtEntity
     *            The {@link GwtEntityModel} from which copy values
     * @param kapuaEntity
     *            The {@link KapuaEntity} into which to copy values
     * @since 1.0.0
     */
    private static void convertEntity(GwtEntityModel gwtEntity, KapuaEntity kapuaEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        kapuaEntity.setId(convert(gwtEntity.getId()));
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
        if (shortKapuaId == null) {
            return null;
        }
        return KapuaEid.parseCompactId(shortKapuaId);
    }
}