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
package org.eclipse.kapua.app.console.commons.shared.util;

import com.extjs.gxt.ui.client.data.BaseModel;
import org.eclipse.kapua.app.console.commons.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.commons.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.commons.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.commons.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for convertKapuaId {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaModelConverter {

    private GwtKapuaModelConverter() {
    }

//    /**
//     * Converts a {@link GwtRoleQuery} into a {@link Role} object for backend usage
//     *
//     * @param loadConfig
//     *            the load configuration
//     * @param gwtRoleQuery
//     *            the {@link GwtRoleQuery} to convertKapuaId
//     * @return the converted {@link RoleQuery}
//     */
//    public static RoleQuery convertRoleQuery(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
//
//        // Convert query
//        RoleQuery roleQuery = roleFactory.newQuery(convertKapuaId(gwtRoleQuery.getScopeId()));
//        if (gwtRoleQuery.getName() != null && !gwtRoleQuery.getName().trim().isEmpty()) {
//            roleQuery.setPredicate(new AttributePredicate<String>(RolePredicates.NAME, gwtRoleQuery.getName(), Operator.LIKE));
//        }
//        roleQuery.setOffset(loadConfig.getOffset());
//        roleQuery.setLimit(loadConfig.getLimit());
//
//        //
//        // Return converted
//        return roleQuery;
//    }
//
//    public static GroupQuery convertGroupQuery(PagingLoadConfig loadConfig,
//            GwtGroupQuery gwtGroupQuery) {
//        KapuaLocator locator = KapuaLocator.getInstance();
//        GroupFactory groupFactory = locator.getFactory(GroupFactory.class);
//        GroupQuery groupQuery = groupFactory.newQuery(convertKapuaId(gwtGroupQuery.getScopeId()));
//        if (gwtGroupQuery.getName() != null && !gwtGroupQuery.getName().isEmpty()) {
//            groupQuery
//                    .setPredicate(new AttributePredicate<String>("name", gwtGroupQuery.getName(), Operator.LIKE));
//        }
//        groupQuery.setOffset(loadConfig.getOffset());
//        groupQuery.setLimit(loadConfig.getLimit());
//
//        return groupQuery;
//    }
//
//    public static AccessRoleQuery convertAccessRoleQuery(PagingLoadConfig pagingLoadConfig,
//            GwtAccessRoleQuery gwtRoleQuery) {
//
//        KapuaLocator locator = KapuaLocator.getInstance();
//        AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
//        AccessRoleQuery accessRoleQuery = accessRoleFactory
//                .newQuery(convertKapuaId(gwtRoleQuery.getScopeId()));
//        accessRoleQuery.setPredicate(new AttributePredicate<KapuaId>("roleId",
//                KapuaEid.parseCompactId(gwtRoleQuery.getRoleId())));
//        accessRoleQuery.setOffset(pagingLoadConfig.getOffset());
//        accessRoleQuery.setLimit(pagingLoadConfig.getLimit());
//
//        return accessRoleQuery;
//
//    }
//
//    public static DeviceConnectionQuery convertConnectionQuery(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) {
//        KapuaLocator locator = KapuaLocator.getInstance();
//        DeviceConnectionFactory factory = locator.getFactory(DeviceConnectionFactory.class);
//        DeviceConnectionQuery query = factory.newQuery(convertKapuaId(gwtDeviceConnectionQuery.getScopeId()));
//        AndPredicate predicate = new AndPredicate();
//
//        if (gwtDeviceConnectionQuery.getClientId() != null && !gwtDeviceConnectionQuery.getClientId().trim().isEmpty()) {
//            predicate.and(new AttributePredicate<String>("clientId", gwtDeviceConnectionQuery.getClientId(), Operator.LIKE));
//        }
//
//        if (gwtDeviceConnectionQuery.getConnectionStatus() != null && !gwtDeviceConnectionQuery.getConnectionStatus().equals(GwtDeviceConnectionStatus.ANY.toString())) {
//            predicate.and(new AttributePredicate<DeviceConnectionStatus>("status", convertConnectionStatus(gwtDeviceConnectionQuery.getConnectionStatus()), Operator.EQUAL));
//        }
//
//        query.setPredicate(predicate);
//
//        return query;
//    }
//
//    public static DeviceConnectionStatus convertConnectionStatus(String connectionStatus) {
//        return DeviceConnectionStatus.valueOf(connectionStatus);
//    }
//
//    /**
//     * Converts a {@link GwtCredentialQuery} into a {@link CredentialQuery} object for backend usage
//     *
//     * @param loadConfig
//     *            the load configuration
//     * @param gwtCredentialQuery
//     *            the {@link GwtCredentialQuery} to convertKapuaId
//     * @return the converted {@link CredentialQuery}
//     */
//    public static CredentialQuery convertCredentialQuery(PagingLoadConfig loadConfig, GwtCredentialQuery gwtCredentialQuery) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
//
//        // Convert query
//        CredentialQuery credentialQuery = credentialFactory.newQuery(convertKapuaId(gwtCredentialQuery.getScopeId()));
//        AndPredicate andPredicate = new AndPredicate();
//        if (gwtCredentialQuery.getUserId() != null && !gwtCredentialQuery.getUserId().trim().isEmpty()) {
//            andPredicate.and(new AttributePredicate<KapuaId>(CredentialPredicates.USER_ID, convertKapuaId(gwtCredentialQuery.getUserId())));
//        }
//        if (gwtCredentialQuery.getUsername() != null && !gwtCredentialQuery.getUsername().trim().isEmpty()) {
//            // TODO set username predicate
//        }
//        if (gwtCredentialQuery.getType() != null && gwtCredentialQuery.getType() != GwtCredentialType.ALL) {
//            andPredicate.and(new AttributePredicate<CredentialType>(CredentialPredicates.CREDENTIAL_TYPE, GwtKapuaModelConverter.convertKapuaId(gwtCredentialQuery.getType()), Operator.EQUAL));
//        }
//        credentialQuery.setPredicate(andPredicate);
//        credentialQuery.setOffset(loadConfig.getOffset());
//        credentialQuery.setLimit(loadConfig.getLimit());
//
//        //
//        // Return converted
//        return credentialQuery;
//    }
//
//
//    /**
//     * Converts a {@link GwtRole} into a {@link Role} object for backend usage
//     *
//     * @param gwtRole
//     *            the {@link GwtRole} to convertKapuaId
//     * @return the converted {@link Role}
//     */
//    public static Role convertKapuaId(GwtRole gwtRole) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
//        RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);
//
//        // Convert scopeId
//        KapuaId scopeId = convertKapuaId(gwtRole.getScopeId());
//        Role role = roleFactory.newEntity(scopeId);
//        convertUpdatableEntity(gwtRole, role);
//
//        // Convert name
//        role.setName(gwtRole.getName());
//
//        if (gwtRole.getPermissions() != null) {
//            // Convert permission associated with role
//            Set<RolePermission> rolePermissions = new HashSet<RolePermission>();
//            for (GwtRolePermission gwtRolePermission : gwtRole.getPermissions()) {
//
//                Permission p = convertKapuaId(new GwtPermission(gwtRolePermission.getDomainEnum(),
//                        gwtRolePermission.getActionEnum(),
//                        gwtRolePermission.getTargetScopeId(),
//                        gwtRolePermission.getGroupId(),
//                        gwtRolePermission.getForwardable()));
//
//                RolePermission rp = rolePermissionFactory.newEntity(scopeId);
//                rp.setPermission(p);
//                rp.setId(convertKapuaId(gwtRolePermission.getId()));
//                rp.setRoleId(role.getId());
//
//                rolePermissions.add(rp);
//            }
//        }
//
//        //
//        // Return converted
//        return role;
//    }
//
//    /**
//     * Converts a {@link GwtRoleCreator} into a {@link RoleCreator} object for backend usage
//     *
//     * @param gwtRoleCreator
//     *            the {@link GwtRoleCreator} to convertKapuaId
//     * @return the converted {@link RoleCreator}
//     */
//    public static RoleCreator convertKapuaId(GwtRoleCreator gwtRoleCreator) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
//
//        // Convert scopeId
//        KapuaId scopeId = convertKapuaId(gwtRoleCreator.getScopeId());
//        RoleCreator roleCreator = roleFactory.newCreator(scopeId);
//
//        // Convert name
//        roleCreator.setName(gwtRoleCreator.getName());
//
//        // Convert permission associated with role
//        Set<Permission> permissions = new HashSet<Permission>();
//        if (gwtRoleCreator.getPermissions() != null) {
//            for (GwtPermission gwtPermission : gwtRoleCreator.getPermissions()) {
//                permissions.add(convertKapuaId(gwtPermission));
//            }
//        }
//
//        roleCreator.setPermissions(permissions);
//
//        //
//        // Return converted
//        return roleCreator;
//    }
//
//    /**
//     * Converts a {@link GwtCredentialCreator} into a {@link CredentialCreator} object for backend usage
//     *
//     * @param gwtCredentialCreator
//     *            the {@link GwtCredentialCreator} to convertKapuaId
//     * @return the converted {@link CredentialCreator}
//     */
//    public static CredentialCreator convertKapuaId(GwtCredentialCreator gwtCredentialCreator) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
//
//        // Convert scopeId
//        KapuaId scopeId = convertKapuaId(gwtCredentialCreator.getScopeId());
//        CredentialCreator credentialCreator = credentialFactory
//                .newCreator(scopeId,
//                        convertKapuaId(gwtCredentialCreator.getUserId()),
//                        convertKapuaId(gwtCredentialCreator.getCredentialType()),
//                        gwtCredentialCreator.getCredentialPlainKey(),
//                        convertKapuaId(gwtCredentialCreator.getCredentialStatus()),
//                        gwtCredentialCreator.getExpirationDate());
//        //
//        // Return converted
//        return credentialCreator;
//    }
//
//    /**
//     * Converts a {@link GwtCredential} into a {@link Credential} object for backend usage
//     *
//     * @param gwtCredential
//     *            the {@link GwtCredential} to convertKapuaId
//     * @return the converted {@link Credential}
//     */
//    public static Credential convertKapuaId(GwtCredential gwtCredential) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
//
//        // Convert scopeId
//        KapuaId scopeId = convertKapuaId(gwtCredential.getScopeId());
//        Credential credential = credentialFactory.newEntity(scopeId);
//        convertUpdatableEntity(gwtCredential, credential);
//        if (gwtCredential.getId() != null && !gwtCredential.getId().trim().isEmpty()) {
//            credential.setId(convertKapuaId(gwtCredential.getId()));
//        }
//        credential.setUserId(convertKapuaId(gwtCredential.getUserId()));
//        credential.setCredentialType(convertKapuaId(gwtCredential.getCredentialTypeEnum()));
//        credential.setCredentialKey(gwtCredential.getCredentialKey());
//        credential.setExpirationDate(gwtCredential.getExpirationDate());
//        credential.setCredentialStatus(convertKapuaId(gwtCredential.getCredentialStatusEnum()));
//        //
//        // Return converted
//        return credential;
//    }
//
//    /**
//     * Converts a {@link GwtAccessRoleCreator} into a {@link AccessRoleCreator} object for backend usage
//     *
//     * @param gwtAccessRoleCreator
//     *            the {@link GwtAccessRoleCreator} to convertKapuaId
//     * @return the converted {@link AccessRoleCreator}
//     * @since 1.0.0
//     */
//    public static AccessRoleCreator convertKapuaId(GwtAccessRoleCreator gwtAccessRoleCreator) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
//
//        // Convert scopeId
//        KapuaId scopeId = convertKapuaId(gwtAccessRoleCreator.getScopeId());
//        AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(scopeId);
//
//        // Convert accessInfoId
//        accessRoleCreator.setAccessInfoId(convertKapuaId(gwtAccessRoleCreator.getAccessInfoId()));
//
//        // Convert roleId
//        accessRoleCreator.setRoleId(convertKapuaId(gwtAccessRoleCreator.getRoleId()));
//
//        //
//        // Return converted
//        return accessRoleCreator;
//    }
//
//    /**
//     * Converts a {@link GwtAccessPermissionCreator} into a {@link AccessPermissionCreator} object for backend usage
//     *
//     * @param gwtAccessPermissionCreator
//     *            the {@link GwtAccessPermissionCreator} to convertKapuaId
//     * @return the converted {@link AccessPermissionCreator}
//     * @since 1.0.0
//     */
//    public static AccessPermissionCreator convertKapuaId(GwtAccessPermissionCreator gwtAccessPermissionCreator) {
//
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        AccessPermissionFactory accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);
//
//        // Convert scopeId
//        KapuaId scopeId = convertKapuaId(gwtAccessPermissionCreator.getScopeId());
//        AccessPermissionCreator accessPermissionCreator = accessPermissionFactory.newCreator(scopeId);
//
//        // Convert accessInfoId
//        accessPermissionCreator.setAccessInfoId(convertKapuaId(gwtAccessPermissionCreator.getAccessInfoId()));
//
//        // Convert Permission
//        accessPermissionCreator.setPermission(convertKapuaId(gwtAccessPermissionCreator.getPermission()));
//
//        //
//        // Return converted
//        return accessPermissionCreator;
//    }
//
//    public static AccessInfoCreator convertKapuaId(GwtAccessInfoCreator gwtAccessInfoCreator) {
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
//
//        // Convert scopeId
//        KapuaId scopeId = convertKapuaId(gwtAccessInfoCreator.getScopeId());
//        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(scopeId);
//
//        // Convert userId
//        accessInfoCreator.setUserId(convertKapuaId(gwtAccessInfoCreator.getUserId()));
//
//        //
//        // Return converted
//        return accessInfoCreator;
//    }
//
//    /**
//     * Converts a {@link GwtPermission} into a {@link Permission} object for backend usage.
//     *
//     * @param gwtPermission
//     *            The {@link GwtPermission} to convertKapuaId.
//     * @return The converted {@link Permission}.
//     * @since 1.0.0
//     */
//    public static Permission convertKapuaId(GwtPermission gwtPermission) {
//        // Get Services
//        KapuaLocator locator = KapuaLocator.getInstance();
//        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
//
//        //
//        // Return converted
//        return permissionFactory.newPermission(convertKapuaId(gwtPermission.getDomainEnum()),
//                convertKapuaId(gwtPermission.getActionEnum()),
//                convertKapuaId(gwtPermission.getTargetScopeId()),
//                convertKapuaId(gwtPermission.getGroupId()),
//                gwtPermission.getForwardable());
//    }
//
//    /**
//     * Converts a {@link GwtAction} into the related {@link Action}
//     *
//     * @param gwtAction
//     *            the {@link GwtAction} to convertKapuaId
//     * @return the converted {@link Action}
//     *
//     * @since 1.0.0
//     */
//    public static Actions convertKapuaId(GwtAction gwtAction) {
//
//        Actions action = null;
//        if (gwtAction != null) {
//            switch (gwtAction) {
//            case GwtAction.connect:
//                action = Actions.connect;
//                break;
//            case GwtAction.delete:
//                action = Actions.delete;
//                break;
//            case GwtAction.execute:
//                action = Actions.execute;
//                break;
//            case GwtAction.read:
//                action = Actions.read;
//                break;
//            case GwtAction.write:
//                action = Actions.write;
//                break;
//            case GwtAction.ALL:
//                action = null;
//                break;
//            }
//        }
//        return action;
//    }
//
//    /**
//     * Converts a {@link GwtDomain} into the related equivalent domain string
//     *
//     * @param gwtDomain
//     *            the {@link GwtDomain} to convertKapuaId
//     * @return the converted domain {@link String}
//     *
//     * @since 1.0.0
//     */
//    public static Domain convertKapuaId(GwtDomain gwtDomain) {
//        Domain domain = null;
//
//        if (gwtDomain != null) {
//            switch (gwtDomain) {
//            case GwtDomain.access_info:
//                domain = new AccessInfoDomain();
//                break;
//            case GwtDomain.access_token:
//                domain = new AccessTokenDomain();
//                break;
//            case GwtDomain.account:
//                domain = new AccountDomain();
//                break;
//            case GwtDomain.broker:
//                domain = new BrokerDomain();
//                break;
//            case GwtDomain.credential:
//                domain = new CredentialDomain();
//                break;
//            case GwtDomain.datastore:
//                domain = new DatastoreDomain();
//                break;
//            case GwtDomain.device:
//                domain = new DeviceDomain();
//                break;
//            case GwtDomain.device_connection:
//                domain = new DeviceConnectionDomain();
//                break;
//            case GwtDomain.device_event:
//                domain = new DeviceEventDomain();
//                break;
//            case GwtDomain.device_lifecycle:
//                domain = new DeviceLifecycleDomain();
//                break;
//            case GwtDomain.device_management:
//                domain = new DeviceManagementDomain();
//                break;
//            case GwtDomain.domain:
//                domain = new DomainDomain();
//                break;
//            case GwtDomain.group:
//                domain = new GroupDomain();
//                break;
//            case GwtDomain.role:
//                domain = new RoleDomain();
//                break;
//            case GwtDomain.user:
//                domain = new UserDomain();
//                break;
//            case GwtDomain.ALL:
//                domain = null;
//                break;
//            }
//        }
//
//        return domain;
//    }
//
//    public static DeviceAssets convertKapuaId(GwtDeviceAssets deviceAssets) {
//        KapuaLocator locator = KapuaLocator.getInstance();
//        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
//        DeviceAssets assets = assetFactory.newAssetListResult();
//        List<DeviceAsset> assetList = new ArrayList<DeviceAsset>();
//        for (GwtDeviceAsset gwtDeviceAsset : deviceAssets.getAssets()) {
//            assetList.add(convertKapuaId(gwtDeviceAsset));
//        }
//        assets.setAssets(assetList);
//        return assets;
//    }
//
//    public static DeviceAsset convertKapuaId(GwtDeviceAsset gwtDeviceAsset) {
//        KapuaLocator locator = KapuaLocator.getInstance();
//        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
//        DeviceAsset deviceAsset = assetFactory.newDeviceAsset();
//        deviceAsset.setName(gwtDeviceAsset.getName());
//        for (GwtDeviceAssetChannel gwtDeviceAssetChannel : gwtDeviceAsset.getChannels()) {
//            deviceAsset.getChannels().add(convertKapuaId(gwtDeviceAssetChannel));
//        }
//        return deviceAsset;
//    }
//
//    public static DeviceAssetChannel convertKapuaId(GwtDeviceAssetChannel gwtDeviceAssetChannel) {
//        KapuaLocator locator = KapuaLocator.getInstance();
//        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
//        DeviceAssetChannel channel = assetFactory.newDeviceAssetChannel();
//        channel.setName(gwtDeviceAssetChannel.getName());
//        try {
//            channel.setType(ObjectTypeConverter.fromString(gwtDeviceAssetChannel.getType()));
//            channel.setValue(ObjectValueConverter.fromString(gwtDeviceAssetChannel.getValue(), channel.getType()));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        channel.setTimestamp(gwtDeviceAssetChannel.getTimestamp());
//        channel.setMode(convertKapuaId(gwtDeviceAssetChannel.getModeEnum()));
//        channel.setError(gwtDeviceAssetChannel.getError());
//        return channel;
//
//    }
//
//    public static DeviceAssetChannelMode convertKapuaId(GwtDeviceAssetChannelMode gwtMode) {
//        return DeviceAssetChannelMode.valueOf(gwtMode.toString());
//    }

    /**
     * Utility method to convertKapuaId commons properties of {@link GwtUpdatableEntityModel} object to the matching {@link KapuaUpdatableEntity} object
     *
     * @param gwtEntity
     *            The {@link GwtUpdatableEntityModel} from which copy values
     * @param kapuaEntity
     *            The {@link KapuaUpdatableEntity} into which to copy values
     * @since 1.0.0
     */
    public static void convertEntity(GwtUpdatableEntityModel gwtEntity, KapuaUpdatableEntity kapuaEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        convertEntity((GwtEntityModel) gwtEntity, (KapuaEntity) kapuaEntity);

        kapuaEntity.setOptlock(gwtEntity.getOptlock());
    }

    /**
     * Utility method to convertKapuaId commons properties of {@link GwtEntityModel} object to the matching {@link KapuaEntity} object
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

        kapuaEntity.setId(convertKapuaId(gwtEntity.getId()));
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
    public static KapuaId convertKapuaId(String shortKapuaId) {
        if (shortKapuaId == null) {
            return null;
        }
        return KapuaEid.parseCompactId(shortKapuaId);
    }

//    public static CredentialType convertKapuaId(GwtCredentialType gwtCredentialType) {
//        return CredentialType.valueOf(gwtCredentialType.toString());
//    }
//
//    public static CredentialStatus convertKapuaId(GwtCredentialStatus gwtCredentialStatus) {
//        return CredentialStatus.valueOf(gwtCredentialStatus.toString());
//    }
//
    public static Map<String, Object> convertConfigComponent(GwtConfigComponent configComponent) {
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
//
//    public static UserStatus convertUserStatus(GwtUserStatus gwtUserStatus) {
//        return UserStatus.valueOf(gwtUserStatus.toString());
//    }

}
