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
package org.eclipse.kapua.app.console.module.authorization.shared.util;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessInfoCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermissionCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRoleCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRoleQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtDomain;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleQuery;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.permission.Action;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.shiro.RolePredicates;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for convertKapuaId {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaAuthorizationModelConverter {

    private GwtKapuaAuthorizationModelConverter(){
    }

    public static GroupQuery convertGroupQuery(PagingLoadConfig loadConfig,
            GwtGroupQuery gwtGroupQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        GroupFactory groupFactory = locator.getFactory(GroupFactory.class);
        GroupQuery groupQuery = groupFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtGroupQuery.getScopeId()));
        if (gwtGroupQuery.getName() != null && !gwtGroupQuery.getName().isEmpty()) {
            groupQuery
                    .setPredicate(new AttributePredicate<String>("name", gwtGroupQuery.getName(), Operator.LIKE));
        }
        groupQuery.setOffset(loadConfig.getOffset());
        groupQuery.setLimit(loadConfig.getLimit());

        return groupQuery;
    }

    /**
     * Converts a {@link GwtRoleQuery} into a {@link Role} object for backend usage
     *
     * @param loadConfig
     *            the load configuration
     * @param gwtRoleQuery
     *            the {@link GwtRoleQuery} to convertKapuaId
     * @return the converted {@link RoleQuery}
     */
    public static RoleQuery convertRoleQuery(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);

        // Convert query
        RoleQuery roleQuery = roleFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtRoleQuery.getScopeId()));
        if (gwtRoleQuery.getName() != null && !gwtRoleQuery.getName().trim().isEmpty()) {
            roleQuery.setPredicate(new AttributePredicate<String>(RolePredicates.NAME, gwtRoleQuery.getName(), Operator.LIKE));
        }
        roleQuery.setOffset(loadConfig.getOffset());
        roleQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return roleQuery;
    }

    public static AccessRoleQuery convertAccessRoleQuery(PagingLoadConfig pagingLoadConfig,
            GwtAccessRoleQuery gwtRoleQuery) {

        KapuaLocator locator = KapuaLocator.getInstance();
        AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
        AccessRoleQuery accessRoleQuery = accessRoleFactory
                .newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtRoleQuery.getScopeId()));
        accessRoleQuery.setPredicate(new AttributePredicate<KapuaId>("roleId",
                KapuaEid.parseCompactId(gwtRoleQuery.getRoleId())));
        accessRoleQuery.setOffset(pagingLoadConfig.getOffset());
        accessRoleQuery.setLimit(pagingLoadConfig.getLimit());

        return accessRoleQuery;

    }

    /**
     * Converts a {@link GwtRole} into a {@link Role} object for backend usage
     *
     * @param gwtRole
     *            the {@link GwtRole} to convertKapuaId
     * @return the converted {@link Role}
     */
    public static Role convertRole(GwtRole gwtRole) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
        RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtRole.getScopeId());
        Role role = roleFactory.newEntity(scopeId);
        GwtKapuaCommonsModelConverter.convertUpdatableEntity(gwtRole, role);

        // Convert name
        role.setName(gwtRole.getName());

        if (gwtRole.getPermissions() != null) {
            // Convert permission associated with role
            Set<RolePermission> rolePermissions = new HashSet<RolePermission>();
            for (GwtRolePermission gwtRolePermission : gwtRole.getPermissions()) {

                Permission p = convertPermission(new GwtPermission(
                        gwtRolePermission.getDomain(),
                        gwtRolePermission.getActionEnum(),
                        gwtRolePermission.getTargetScopeId(),
                        gwtRolePermission.getGroupId(),
                        gwtRolePermission.getForwardable()));

                RolePermission rp = rolePermissionFactory.newEntity(scopeId);
                rp.setPermission(p);
                rp.setId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtRolePermission.getId()));
                rp.setRoleId(role.getId());

                rolePermissions.add(rp);
            }
        }

        //
        // Return converted
        return role;
    }

    /**
     * Converts a {@link GwtRoleCreator} into a {@link RoleCreator} object for backend usage
     *
     * @param gwtRoleCreator
     *            the {@link GwtRoleCreator} to convertKapuaId
     * @return the converted {@link RoleCreator}
     */
    public static RoleCreator convertRoleCreator(GwtRoleCreator gwtRoleCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtRoleCreator.getScopeId());
        RoleCreator roleCreator = roleFactory.newCreator(scopeId);

        // Convert name
        roleCreator.setName(gwtRoleCreator.getName());

        // Convert permission associated with role
        Set<Permission> permissions = new HashSet<Permission>();
        if (gwtRoleCreator.getPermissions() != null) {
            for (GwtPermission gwtPermission : gwtRoleCreator.getPermissions()) {
                permissions.add(convertPermission(gwtPermission));
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
     *            the {@link GwtAccessRoleCreator} to convertKapuaId
     * @return the converted {@link AccessRoleCreator}
     * @since 1.0.0
     */
    public static AccessRoleCreator convertAccessRoleCreator(GwtAccessRoleCreator gwtAccessRoleCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccessRoleCreator.getScopeId());
        AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(scopeId);

        // Convert accessInfoId
        accessRoleCreator.setAccessInfoId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccessRoleCreator.getAccessInfoId()));

        // Convert roleId
        accessRoleCreator.setRoleId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccessRoleCreator.getRoleId()));

        //
        // Return converted
        return accessRoleCreator;
    }

    /**
     * Converts a {@link GwtAccessPermissionCreator} into a {@link AccessPermissionCreator} object for backend usage
     *
     * @param gwtAccessPermissionCreator
     *            the {@link GwtAccessPermissionCreator} to convertKapuaId
     * @return the converted {@link AccessPermissionCreator}
     * @since 1.0.0
     */
    public static AccessPermissionCreator convertAccessPermissionCreator(GwtAccessPermissionCreator gwtAccessPermissionCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessPermissionFactory accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccessPermissionCreator.getScopeId());
        AccessPermissionCreator accessPermissionCreator = accessPermissionFactory.newCreator(scopeId);

        // Convert accessInfoId
        accessPermissionCreator.setAccessInfoId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccessPermissionCreator.getAccessInfoId()));

        // Convert Permission
        accessPermissionCreator.setPermission(convertPermission(gwtAccessPermissionCreator.getPermission()));

        //
        // Return converted
        return accessPermissionCreator;
    }

    public static AccessInfoCreator convertAccessInfoCreator(GwtAccessInfoCreator gwtAccessInfoCreator) {
        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccessInfoCreator.getScopeId());
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(scopeId);

        // Convert userId
        accessInfoCreator.setUserId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccessInfoCreator.getUserId()));

        //
        // Return converted
        return accessInfoCreator;
    }

    /**
     * Converts a {@link GwtPermission} into a {@link Permission} object for backend usage.
     *
     * @param gwtPermission
     *            The {@link GwtPermission} to convertKapuaId.
     * @return The converted {@link Permission}.
     * @since 1.0.0
     */
    public static Permission convertPermission(GwtPermission gwtPermission) {
        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        //
        // Return converted
        return permissionFactory.newPermission(convertDomain(new GwtDomain(gwtPermission.getDomain())),
                convertAction(gwtPermission.getActionEnum()),
                GwtKapuaCommonsModelConverter.convertKapuaId(gwtPermission.getTargetScopeId()),
                GwtKapuaCommonsModelConverter.convertKapuaId(gwtPermission.getGroupId()),
                gwtPermission.getForwardable());
    }

    /**
     * Converts a {@link GwtAction} into the related {@link Action}
     *
     * @param gwtAction
     *            the {@link GwtAction} to convertKapuaId
     * @return the converted {@link Action}
     * 
     * @since 1.0.0
     */
    public static Actions convertAction(GwtAction gwtAction) {

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
            case ALL:
                action = null;
                break;
            }
        }
        return action;
    }

    /**
     * Converts a {@link GwtDomain} into the related equivalent domain string
     *
     * @param gwtDomain
     *            the {@link GwtDomain} to convertKapuaId
     * @return the converted domain {@link String}
     * 
     * @since 1.0.0
     */
    public static Domain convertDomain(GwtDomain gwtDomain) {

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainService domainService = locator.getService(DomainService.class);
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery(null);
            DomainListResult list = domainService.query(query);

            for (Domain domain : list.getItems()) {
                if (domain.getName().equals(gwtDomain.getDomainName())) {
                    return domain;
                }
            }
        } catch (KapuaException ex) { }

        return null;
    }

}
