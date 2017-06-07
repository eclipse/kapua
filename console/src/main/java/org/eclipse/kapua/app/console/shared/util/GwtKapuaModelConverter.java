/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.kapua.app.console.client.group.GwtGroupQuery;
import org.eclipse.kapua.app.console.client.tag.GwtTagQuery;
import org.eclipse.kapua.app.console.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates.GwtDeviceConnectionStatus;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountQuery;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialCreator;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialQuery;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialType;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfoCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermissionCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRoleQuery;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleQuery;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.shared.model.data.GwtDataChannelInfoQuery;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAssetChannel.GwtDeviceAssetChannelMode;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserQuery;
import org.eclipse.kapua.broker.core.BrokerDomain;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialPredicates;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDomain;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenDomain;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoDomain;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainDomain;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.shiro.GroupDomain;
import org.eclipse.kapua.service.authorization.group.shiro.GroupPredicates;
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
import org.eclipse.kapua.service.authorization.role.shiro.RoleDomain;
import org.eclipse.kapua.service.authorization.role.shiro.RolePredicates;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventDomain;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifecycleDomain;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.internal.TagPredicates;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.internal.UserDomain;
import org.eclipse.kapua.service.user.internal.UserPredicates;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;

/**
 * Utility class for convert {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaModelConverter {

    private GwtKapuaModelConverter() {
    }

    /**
     * Converts a {@link GwtRoleQuery} into a {@link Role} object for backend usage
     *
     * @param loadConfig
     *            the load configuration
     * @param gwtRoleQuery
     *            the {@link GwtRoleQuery} to convert
     * @return the converted {@link RoleQuery}
     */
    public static RoleQuery convertRoleQuery(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);

        // Convert query
        RoleQuery roleQuery = roleFactory.newQuery(convert(gwtRoleQuery.getScopeId()));
        if (gwtRoleQuery.getName() != null && !gwtRoleQuery.getName().trim().isEmpty()) {
            roleQuery.setPredicate(new AttributePredicate<String>(RolePredicates.NAME, gwtRoleQuery.getName(), Operator.LIKE));
        }
        roleQuery.setOffset(loadConfig.getOffset());
        roleQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return roleQuery;
    }

    public static GroupQuery convertGroupQuery(PagingLoadConfig loadConfig,
            GwtGroupQuery gwtGroupQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        GroupFactory groupFactory = locator.getFactory(GroupFactory.class);
        GroupQuery groupQuery = groupFactory.newQuery(convert(gwtGroupQuery.getScopeId()));
        if (gwtGroupQuery.getName() != null && !gwtGroupQuery.getName().isEmpty()) {
            groupQuery.setPredicate(new AttributePredicate<String>(GroupPredicates.NAME, gwtGroupQuery.getName(), Operator.LIKE));
        }
        groupQuery.setOffset(loadConfig.getOffset());
        groupQuery.setLimit(loadConfig.getLimit());

        return groupQuery;
    }

    public static AccessRoleQuery convertAccessRoleQuery(PagingLoadConfig pagingLoadConfig, GwtAccessRoleQuery gwtRoleQuery) {

        KapuaLocator locator = KapuaLocator.getInstance();
        AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
        AccessRoleQuery accessRoleQuery = accessRoleFactory.newQuery(convert(gwtRoleQuery.getScopeId()));
        accessRoleQuery.setPredicate(new AttributePredicate<KapuaId>("roleId", KapuaEid.parseCompactId(gwtRoleQuery.getRoleId())));
        accessRoleQuery.setOffset(pagingLoadConfig.getOffset());
        accessRoleQuery.setLimit(pagingLoadConfig.getLimit());

        return accessRoleQuery;

    }

    /**
     * Converts a {@link GwtTagQuery} into a {@link TagQuery} object for backend usage
     *
     * @param loadConfig
     *            the load configuration
     * @param gwtTagQuery
     *            the {@link GwtTagQuery} to convert
     * @return the converted {@link TagQuery}
     */
    public static TagQuery convertTagQuery(PagingLoadConfig loadConfig, GwtTagQuery gwtTagQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        TagFactory tagFactory = locator.getFactory(TagFactory.class);
        TagQuery tagQuery = tagFactory.newQuery(convert(gwtTagQuery.getScopeId()));
        if (gwtTagQuery.getName() != null && !gwtTagQuery.getName().isEmpty()) {
            tagQuery.setPredicate(new AttributePredicate<String>(TagPredicates.NAME, gwtTagQuery.getName(), Operator.LIKE));
        }
        tagQuery.setOffset(loadConfig.getOffset());
        tagQuery.setLimit(loadConfig.getLimit());

        return tagQuery;
    }

    /**
     * Converts a {@link GwtUserQuery} into a {@link UserQuery} object for backend usage
     *
     * @param loadConfig
     *            the load configuration
     * @param gwtUserQuery
     *            the {@link GwtUserQuery} to convert
     * @return the converted {@link UserQuery}
     */
    public static UserQuery convertUserQuery(PagingLoadConfig loadConfig, GwtUserQuery gwtUserQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        UserFactory userFactory = locator.getFactory(UserFactory.class);

        // Convert query
        UserQuery userQuery = userFactory.newQuery(convert(gwtUserQuery.getScopeId()));
        if (gwtUserQuery.getName() != null && !gwtUserQuery.getName().isEmpty()) {
            userQuery.setPredicate(new AttributePredicate<String>(UserPredicates.NAME, gwtUserQuery.getName(), Operator.LIKE));
        }
        userQuery.setOffset(loadConfig.getOffset());
        userQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return userQuery;
    }

    public static AccountQuery convertAccountQuery(PagingLoadConfig loadConfig, GwtAccountQuery gwtAccountQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountFactory factory = locator.getFactory(AccountFactory.class);
        AccountQuery query = factory.newQuery(convert(gwtAccountQuery.getScopeId()));
        AndPredicate predicate = new AndPredicate();

        if (gwtAccountQuery.getName() != null && !gwtAccountQuery.getName().trim().isEmpty()) {
            predicate.and(new AttributePredicate<String>("name", gwtAccountQuery.getName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationName() != null && !gwtAccountQuery.getOrganizationName().isEmpty()) {
            predicate.and(new AttributePredicate<String>("organization.name", gwtAccountQuery.getOrganizationName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationEmail() != null && !gwtAccountQuery.getOrganizationEmail().isEmpty()) {
            predicate.and(new AttributePredicate<String>("organization.email", gwtAccountQuery.getOrganizationEmail(), Operator.LIKE));
        }

        query.setPredicate(predicate);

        return query;
    }

    public static DeviceConnectionQuery convertConnectionQuery(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionFactory factory = locator.getFactory(DeviceConnectionFactory.class);
        DeviceConnectionQuery query = factory.newQuery(convert(gwtDeviceConnectionQuery.getScopeId()));
        AndPredicate predicate = new AndPredicate();

        if (gwtDeviceConnectionQuery.getClientId() != null && !gwtDeviceConnectionQuery.getClientId().trim().isEmpty()) {
            predicate.and(new AttributePredicate<String>("clientId", gwtDeviceConnectionQuery.getClientId(), Operator.LIKE));
        }

        if (gwtDeviceConnectionQuery.getConnectionStatus() != null && !gwtDeviceConnectionQuery.getConnectionStatus().equals(GwtDeviceConnectionStatus.ANY.toString())) {
            predicate.and(new AttributePredicate<DeviceConnectionStatus>("status", convertConnectionStatus(gwtDeviceConnectionQuery.getConnectionStatus()), Operator.EQUAL));
        }

        query.setPredicate(predicate);

        return query;
    }

    public static DeviceConnectionStatus convertConnectionStatus(String connectionStatus) {
        return DeviceConnectionStatus.valueOf(connectionStatus);
    }

    /**
     * Converts a {@link GwtCredentialQuery} into a {@link CredentialQuery} object for backend usage
     *
     * @param loadConfig
     *            the load configuration
     * @param gwtCredentialQuery
     *            the {@link GwtCredentialQuery} to convert
     * @return the converted {@link CredentialQuery}
     */
    public static CredentialQuery convertCredentialQuery(PagingLoadConfig loadConfig, GwtCredentialQuery gwtCredentialQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        // Convert query
        CredentialQuery credentialQuery = credentialFactory.newQuery(convert(gwtCredentialQuery.getScopeId()));
        AndPredicate andPredicate = new AndPredicate();
        if (gwtCredentialQuery.getUserId() != null && !gwtCredentialQuery.getUserId().trim().isEmpty()) {
            andPredicate.and(new AttributePredicate<KapuaId>(CredentialPredicates.USER_ID, convert(gwtCredentialQuery.getUserId())));
        }
        if (gwtCredentialQuery.getUsername() != null && !gwtCredentialQuery.getUsername().trim().isEmpty()) {
            // TODO set username predicate
        }
        if (gwtCredentialQuery.getType() != null && gwtCredentialQuery.getType() != GwtCredentialType.ALL) {
            andPredicate.and(new AttributePredicate<CredentialType>(CredentialPredicates.CREDENTIAL_TYPE, GwtKapuaModelConverter.convert(gwtCredentialQuery.getType()), Operator.EQUAL));
        }
        credentialQuery.setPredicate(andPredicate);
        credentialQuery.setOffset(loadConfig.getOffset());
        credentialQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return credentialQuery;
    }

    public static ChannelInfoQuery convertChannelInfoQuery(GwtDataChannelInfoQuery query, PagingLoadConfig pagingLoadConfig) {
        ChannelInfoQueryImpl channelInfoQuery = new ChannelInfoQueryImpl(convert(query.getScopeId()));
        channelInfoQuery.setOffset(pagingLoadConfig.getOffset());
        channelInfoQuery.setLimit(pagingLoadConfig.getLimit());
        return channelInfoQuery;
    }

    /**
     * Converts a {@link GwtRole} into a {@link Role} object for backend usage
     *
     * @param gwtRole
     *            the {@link GwtRole} to convert
     * @return the converted {@link Role}
     */
    public static Role convert(GwtRole gwtRole) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
        RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtRole.getScopeId());
        Role role = roleFactory.newEntity(scopeId);
        convertEntity(gwtRole, role);

        // Convert name
        role.setName(gwtRole.getName());

        if (gwtRole.getPermissions() != null) {
            // Convert permission associated with role
            Set<RolePermission> rolePermissions = new HashSet<RolePermission>();
            for (GwtRolePermission gwtRolePermission : gwtRole.getPermissions()) {

                Permission p = convert(new GwtPermission(gwtRolePermission.getDomainEnum(),
                        gwtRolePermission.getActionEnum(),
                        gwtRolePermission.getTargetScopeId(),
                        gwtRolePermission.getGroupId(),
                        gwtRolePermission.getForwardable()));

                RolePermission rp = rolePermissionFactory.newEntity(scopeId);
                rp.setPermission(p);
                rp.setId(convert(gwtRolePermission.getId()));
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
     *            the {@link GwtRoleCreator} to convert
     * @return the converted {@link RoleCreator}
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
     * Converts a {@link GwtCredentialCreator} into a {@link CredentialCreator} object for backend usage
     *
     * @param gwtCredentialCreator
     *            the {@link GwtCredentialCreator} to convert
     * @return the converted {@link CredentialCreator}
     */
    public static CredentialCreator convert(GwtCredentialCreator gwtCredentialCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtCredentialCreator.getScopeId());
        CredentialCreator credentialCreator = credentialFactory
                .newCreator(scopeId, convert(gwtCredentialCreator.getUserId()), convert(gwtCredentialCreator.getCredentialType()), gwtCredentialCreator.getCredentialPlainKey());

        //
        // Return converted
        return credentialCreator;
    }

    /**
     * Converts a {@link GwtCredential} into a {@link Credential} object for backend usage
     *
     * @param gwtCredential
     *            the {@link GwtCredential} to convert
     * @return the converted {@link Credential}
     */
    public static Credential convert(GwtCredential gwtCredential) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        // Convert scopeId
        KapuaId scopeId = convert(gwtCredential.getScopeId());
        Credential credential = credentialFactory
                .newCredential(scopeId, convert(gwtCredential.getUserId()), convert(gwtCredential.getCredentialTypeEnum()), gwtCredential.getCredentialKey());
        if (gwtCredential.getId() != null && !gwtCredential.getId().trim().isEmpty()) {
            credential.setId(convert(gwtCredential.getId()));
        }
        //
        // Return converted
        return credential;
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
                convert(gwtPermission.getGroupId()),
                gwtPermission.getForwardable());
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
            case datastore:
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
            case ALL:
                domain = null;
                break;
            }
        }

        return domain;
    }

    public static DeviceAssets convert(GwtDeviceAssets deviceAssets) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAssets assets = assetFactory.newAssetListResult();
        List<DeviceAsset> assetList = new ArrayList<DeviceAsset>();
        for (GwtDeviceAsset gwtDeviceAsset : deviceAssets.getAssets()) {
            assetList.add(convert(gwtDeviceAsset));
        }
        assets.setAssets(assetList);
        return assets;
    }

    public static DeviceAsset convert(GwtDeviceAsset gwtDeviceAsset) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAsset deviceAsset = assetFactory.newDeviceAsset();
        deviceAsset.setName(gwtDeviceAsset.getName());
        for (GwtDeviceAssetChannel gwtDeviceAssetChannel : gwtDeviceAsset.getChannels()) {
            deviceAsset.getChannels().add(convert(gwtDeviceAssetChannel));
        }
        return deviceAsset;
    }

    public static DeviceAssetChannel convert(GwtDeviceAssetChannel gwtDeviceAssetChannel) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAssetChannel channel = assetFactory.newDeviceAssetChannel();
        channel.setName(gwtDeviceAssetChannel.getName());
        try {
            channel.setType(ObjectTypeConverter.fromString(gwtDeviceAssetChannel.getType()));
            channel.setValue(ObjectValueConverter.fromString(gwtDeviceAssetChannel.getValue(), channel.getType()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        channel.setTimestamp(gwtDeviceAssetChannel.getTimestamp());
        channel.setMode(convert(gwtDeviceAssetChannel.getModeEnum()));
        channel.setError(gwtDeviceAssetChannel.getError());
        return channel;

    }

    public static DeviceAssetChannelMode convert(GwtDeviceAssetChannelMode gwtMode) {
        return DeviceAssetChannelMode.valueOf(gwtMode.toString());
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
     * Example: AQ =&gt; 1
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

    public static CredentialType convert(GwtCredentialType gwtCredentialType) {
        return CredentialType.valueOf(gwtCredentialType.toString());
    }

    public static Map<String, Object> convert(GwtConfigComponent configComponent) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        for (GwtConfigParameter gwtConfigParameter : configComponent.getParameters()) {
            switch (gwtConfigParameter.getType()) {
            case BOOLEAN:
                parameters.put(gwtConfigParameter.getId(), Boolean.parseBoolean(gwtConfigParameter.getValue()));
                break;
            case BYTE:
                parameters.put(gwtConfigParameter.getId(), Byte.parseByte(gwtConfigParameter.getValue()));
                break;
            case CHAR:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue().toCharArray());
                break;
            case DOUBLE:
                parameters.put(gwtConfigParameter.getId(), Double.parseDouble(gwtConfigParameter.getValue()));
                break;
            case FLOAT:
                parameters.put(gwtConfigParameter.getId(), Float.parseFloat(gwtConfigParameter.getValue()));
                break;
            case INTEGER:
                parameters.put(gwtConfigParameter.getId(), Integer.parseInt(gwtConfigParameter.getValue()));
                break;
            case LONG:
                parameters.put(gwtConfigParameter.getId(), Long.parseLong(gwtConfigParameter.getValue()));
                break;
            case PASSWORD:
            case STRING:
            default:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue());
                break;
            case SHORT:
                parameters.put(gwtConfigParameter.getId(), Short.parseShort(gwtConfigParameter.getValue()));
                break;
            }
        }
        return parameters;
    }

}
