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
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.util;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.shared.model.GwtMessage;
import org.eclipse.kapua.app.console.shared.model.GwtOrganization;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtTag;
import org.eclipse.kapua.app.console.shared.model.GwtTopic;
import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtSubjectType;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfo;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnectionOption;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.broker.core.BrokerDomain;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.Organization;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDomain;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenDomain;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoDomain;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainDomain;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.shiro.GroupDomain;
import org.eclipse.kapua.service.authorization.permission.Action;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.shiro.RoleDomain;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventDomain;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifecycleDomain;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.internal.UserDomain;

public class KapuaGwtModelConverter {

    private KapuaGwtModelConverter() {
    }

    /**
     * Converts a {@link Role} into a {@link GwtRole} object for GWT usage.
     *
     * @param role The {@link Role} to convert.
     * @return The converted {@link GwtRole}.
     * @since 1.0.0
     */
    public static GwtRole convert(Role role) {
        GwtRole gwtRole = new GwtRole();

        //
        // Covert commons attributes
        convertEntity(role, gwtRole);

        //
        // Convert other attributes
        gwtRole.setName(role.getName());

        //
        // Return converted entity
        return gwtRole;
    }

    /**
     * Merges a {@link Role} and a {@link AccessRole} into a {@link GwtAccessRole} object for GWT usage.
     *
     * @param role       The {@link Role} to merge.
     * @param accessRole The {@link AccessRole} to merge.
     * @return The converted {@link GwtAccessRole}.
     * @since 1.0.0
     */
    public static GwtAccessRole convert(Role role, AccessRole accessRole) {
        GwtAccessRole gwtAccessRole = new GwtAccessRole();

        //
        // Covert commons attributes
        convertEntity(accessRole, gwtAccessRole);

        //
        // Convert other attributes
        gwtAccessRole.setRoleName(role.getName());
        gwtAccessRole.setRoleId(role.getId().toCompactId());
        gwtAccessRole.setAccessInfoId(accessRole.getAccessInfoId().toCompactId());
        //
        // Return converted entity
        return gwtAccessRole;
    }

    /**
     * Converts a {@link AccessRole} into a {@link GwtAccessRole} object for GWT usage.
     *
     * @param accessRole The {@link AccessRole} to convert.
     * @return The converted {@link GwtAccessRole}.
     * @since 1.0.0
     */
    public static GwtAccessRole convert(AccessRole accessRole) {
        GwtAccessRole gwtAccessRole = new GwtAccessRole();

        //
        // Covert commons attributes
        convertEntity(accessRole, gwtAccessRole);

        gwtAccessRole.setRoleId(accessRole.getRoleId().toCompactId());
        gwtAccessRole.setAccessInfoId(accessRole.getAccessInfoId().toCompactId());

        //
        // Return converted entity
        return gwtAccessRole;
    }

    /**
     * Converts a {@link AccessPermission} into a {@link GwtAccessPermission} object for GWT usage.
     *
     * @param accessPermission The {@link AccessPermission} to convert.
     * @return The converted {@link GwtAccessPermission}.
     * @since 1.0.0
     */
    public static GwtAccessPermission convert(AccessPermission accessPermission) {
        GwtAccessPermission gwtAccessPermission = new GwtAccessPermission();

        //
        // Covert commons attributes
        convertEntity(accessPermission, gwtAccessPermission);

        gwtAccessPermission.setAccessInfoId(accessPermission.getAccessInfoId().toCompactId());
        gwtAccessPermission.setPermissionDomain(accessPermission.getPermission().getDomain());

        if (accessPermission.getPermission().getAction() != null) {
            gwtAccessPermission.setPermissionAction(accessPermission.getPermission().getAction().toString());
        } else {
            gwtAccessPermission.setPermissionAction("ALL");
        }

        if (accessPermission.getPermission().getTargetScopeId() != null) {
            gwtAccessPermission.setPermissionTargetScopeId(accessPermission.getPermission().getTargetScopeId().toCompactId());
        } else {
            gwtAccessPermission.setPermissionTargetScopeId("ALL");
        }

        if (accessPermission.getPermission().getGroupId() != null) {
            gwtAccessPermission.setPermissionGroupId(accessPermission.getPermission().getGroupId().toCompactId());
        } else {
            gwtAccessPermission.setPermissionGroupId("ALL");
        }

        gwtAccessPermission.setPermissionForwardable(accessPermission.getPermission().getForwardable());

        //
        // Return converted entity
        return gwtAccessPermission;
    }

    /**
     * Converts a {@link AccessInfo} into a {@link GwtAccessInfo} object for GWT usage.
     *
     * @param accessInfo The {@link AccessInfo} to convert.
     * @return The converted {@link GwtAccessInfo}.
     * @since 1.0.0
     */
    public static GwtAccessInfo convert(AccessInfo accessInfo) {
        GwtAccessInfo gwtAccessInfo = new GwtAccessInfo();
        //
        // Covert commons attributes
        convertEntity(accessInfo, gwtAccessInfo);

        gwtAccessInfo.setUserId(accessInfo.getUserId().toCompactId());

        //
        // Return converted entity
        return gwtAccessInfo;
    }

    /**
     * Converts a {@link RolePermission} into a {@link GwtRolePermission} object for GWT usage.
     *
     * @param rolePermission The {@link RolePermission} to convert
     * @return The converted {@link GwtRolePermission}
     * @since 1.0.0
     */
    public static GwtRolePermission convert(RolePermission rolePermission) {
        GwtRolePermission gwtRolePermission = new GwtRolePermission();

        //
        // Covert commons attributes
        convertEntity(rolePermission, gwtRolePermission);

        //
        // Convert other attributes
        GwtPermission gwtPermission = convert((Permission) rolePermission.getPermission());

        gwtRolePermission.setRoleId(convert(rolePermission.getRoleId()));
        gwtRolePermission.setDomain(gwtPermission.getDomain());
        gwtRolePermission.setAction(gwtPermission.getAction());
        gwtRolePermission.setGroupId(gwtPermission.getGroupId());
        gwtRolePermission.setTargetScopeId(gwtPermission.getTargetScopeId());
        gwtRolePermission.setForwardable(gwtPermission.getForwardable());

        //
        // Return converted entity
        return gwtRolePermission;
    }

    /**
     * Converts a {@link Permission} into a {@link GwtPermission} object for GWT usage.
     *
     * @param permission The {@link Permission} to convert.
     * @return The converted {@link GwtPermission}.
     * @since 1.0.0
     */
    public static GwtPermission convert(Permission permission) {
        return new GwtPermission(convertDomain(permission.getDomain()),
                convert(permission.getAction()),
                convert(permission.getTargetScopeId()),
                convert(permission.getGroupId()),
                permission.getForwardable());
    }

    /**
     * Converts a {@link Action} into a {@link GwtAction}
     *
     * @param action The {@link Action} to convert
     * @return The converted {@link GwtAction}
     * @since 1.0.0
     */
    public static GwtAction convert(Actions action) {

        GwtAction gwtAction = null;
        if (action != null) {
            switch (action) {
            case connect:
                gwtAction = GwtAction.connect;
                break;
            case delete:
                gwtAction = GwtAction.delete;
                break;
            case execute:
                gwtAction = GwtAction.execute;
                break;
            case read:
                gwtAction = GwtAction.read;
                break;
            case write:
                gwtAction = GwtAction.write;
                break;
            }
        }
        return gwtAction;
    }

    /**
     * Converts a {@link Group} into a {@link GwtGroup}
     *
     * @param group The {@link Group} to convert
     * @return The converted {@link GwtGroup}
     * @since 1.0.0
     */
    public static GwtGroup convert(Group group) {

        GwtGroup gwtGroup = new GwtGroup();
        //
        // Covert commons attributes
        convertEntity(group, gwtGroup);

        //
        // Convert other attributes
        gwtGroup.setGroupName(group.getName());

        return gwtGroup;
    }

    /**
     * Converts a {@link String} domain into a {@link GwtDomain}
     *
     * @param domain The {@link String} domain to convert
     * @return The converted {@link GwtDomain}
     * @since 1.0.0
     */
    public static GwtDomain convertDomain(String domain) {
        GwtDomain gwtDomain = null;

        if (new AccessInfoDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.access_info;
        } else if (new AccessTokenDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.access_token;
        } else if (new AccountDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.account;
        } else if (new BrokerDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.broker;
        } else if (new CredentialDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.credential;
        } else if (new DatastoreDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.datastore;
        } else if (new DeviceDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.device;
        } else if (new DeviceConnectionDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.device_connection;
        } else if (new DeviceEventDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.device_event;
        } else if (new DeviceLifecycleDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.device_lifecycle;
        } else if (new DeviceManagementDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.device_management;
        } else if (new DomainDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.domain;
        } else if (new GroupDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.group;
        } else if (new RoleDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.role;
        } else if (new UserDomain().getName().equals(domain)) {
            gwtDomain = GwtDomain.user;
        }

        return gwtDomain;
    }

    /**
     * Converts a {@link String} action into a {@link GwtAction}
     *
     * @param action The {@link String} action to convert
     * @return The converted {@link GwtAction}
     * @since 1.0.0
     */
    public static GwtAction convertAction(String action) {
        return GwtAction.valueOf(action);
    }

    /**
     * Converts a {@link Action} action into a {@link GwtAction}
     *
     * @param action The {@link Action} action to convert
     * @return The converted {@link GwtAction}
     * @since 1.0.0
     */
    public static GwtAction convertAction(Action action) {
        return GwtAction.valueOf(action.toString());
    }

    /**
     * Converts a {@link KapuaId} into its {@link String} short id representation.
     * <p>
     * Example: 1 =&gt; AQ
     * </p>
     *
     * @param kapuaId The {@link KapuaId} to convert
     * @return The short id representation of the {@link KapuaId}
     * @since 1.0.0
     */
    public static String convert(KapuaId kapuaId) {
        if (kapuaId == null) {
            return null;
        }

        //
        // Return converted entity
        return kapuaId.toCompactId();
    }

    /**
     * Converts a {@link Tag} into a {@link GwtTag}
     *
     * @param tag
     *            The {@link Tag} to convert
     * @return The converted {@link GwtTag}
     * @since 1.0.0
     */
    public static GwtTag convert(Tag tag) {

        GwtTag gwtTag = new GwtTag();
        //
        // Covert commons attributes
        convertEntity(tag, gwtTag);

        //
        // Convert other attributes
        gwtTag.setTagName(tag.getName());

        return gwtTag;
    }

    /**
     * Converts a {@link Account} into a {@link GwtAccount} for GWT usage.
     *
     * @param account The {@link Account} to convert.
     * @return The converted {@link GwtAccount}
     * @since 1.0.0
     */
    public static GwtAccount convert(Account account) {
        GwtAccount gwtAccount = new GwtAccount();

        //
        // Convert commons attributes
        convertEntity(account, gwtAccount);

        //
        // Convert other attributes
        gwtAccount.setName(account.getName());
        gwtAccount.setGwtOrganization(convert(account.getOrganization()));
        gwtAccount.setParentAccountId(account.getScopeId() != null ? account.getScopeId().toCompactId() : null);
        gwtAccount.setOptlock(account.getOptlock());
        gwtAccount.set("orgName", account.getOrganization().getName());
        gwtAccount.set("orgEmail", account.getOrganization().getEmail());

        try {
            gwtAccount.setBrokerURL(SystemUtils.getBrokerURI().toString());
        } catch (URISyntaxException use) {
            gwtAccount.setBrokerURL("");
        }

        //
        // Return converted entity
        return gwtAccount;
    }

    /**
     * Converts a {@link Organization} into a {@link GwtOrganization} for GWT usage.
     *
     * @param organization The {@link Organization} to convert.
     * @return The converted {@link GwtOrganization}.
     * @since 1.0.0
     */
    public static GwtOrganization convert(Organization organization) {
        GwtOrganization gwtOrganization = new GwtOrganization();

        gwtOrganization.setName(organization.getName());
        gwtOrganization.setPersonName(organization.getPersonName());
        gwtOrganization.setEmailAddress(organization.getEmail());
        gwtOrganization.setPhoneNumber(organization.getPhoneNumber());
        gwtOrganization.setAddressLine1(organization.getAddressLine1());
        gwtOrganization.setAddressLine2(organization.getAddressLine2());
        gwtOrganization.setZipPostCode(organization.getZipPostCode());
        gwtOrganization.setCity(organization.getCity());
        gwtOrganization.setStateProvinceCounty(organization.getStateProvinceCounty());
        gwtOrganization.setCountry(organization.getCountry());

        //
        // Return converted entity
        return gwtOrganization;
    }

    /**
     * Converts a {@link User} into a {@link GwtUser} for GWT usage.
     *
     * @param user The {@link User} to convert.
     * @return The converted {@link GwtUser}
     * @since 1.0.0
     */
    public static GwtUser convert(User user)
            throws KapuaException {

        GwtUser gwtUser = new GwtUser();

        //
        // Convert commons attributes
        convertEntity(user, gwtUser);

        //
        // Convert other attributes
        gwtUser.setUsername(user.getName());
        gwtUser.setDisplayName(user.getDisplayName());
        gwtUser.setEmail(user.getEmail());
        gwtUser.setPhoneNumber(user.getPhoneNumber());
        gwtUser.setStatus(user.getStatus().name());
        gwtUser.setExpirationDate(user.getExpirationDate());

        //
        // Return converted entity
        return gwtUser;
    }

    public static GwtDevice convert(Device device)
            throws KapuaException {

        GwtDevice gwtDevice = new GwtDevice();
        gwtDevice.setId(convert(device.getId()));
        gwtDevice.setScopeId(convert(device.getScopeId()));
        gwtDevice.setGwtDeviceStatus(device.getStatus().toString());
        gwtDevice.setClientId(device.getClientId());
        gwtDevice.setDisplayName(device.getDisplayName());
        gwtDevice.setModelId(device.getModelId());
        gwtDevice.setSerialNumber(device.getSerialNumber());
        gwtDevice.setGroupId(convert(device.getGroupId()));
        gwtDevice.setFirmwareVersion(device.getFirmwareVersion());
        gwtDevice.setBiosVersion(device.getBiosVersion());
        gwtDevice.setOsVersion(device.getOsVersion());
        gwtDevice.setJvmVersion(device.getJvmVersion());
        gwtDevice.setOsgiVersion(device.getOsgiFrameworkVersion());
        gwtDevice.setAcceptEncoding(device.getAcceptEncoding());
        gwtDevice.setApplicationIdentifiers(device.getApplicationIdentifiers());
        gwtDevice.setIotFrameworkVersion(device.getApplicationFrameworkVersion());
        gwtDevice.setIccid(device.getIccid());
        gwtDevice.setImei(device.getImei());
        gwtDevice.setImsi(device.getImsi());
        gwtDevice.setCustomAttribute1(device.getCustomAttribute1());
        gwtDevice.setCustomAttribute2(device.getCustomAttribute2());
        gwtDevice.setCustomAttribute3(device.getCustomAttribute3());
        gwtDevice.setCustomAttribute4(device.getCustomAttribute4());
        gwtDevice.setCustomAttribute5(device.getCustomAttribute5());
        gwtDevice.setOptlock(device.getOptlock());

        // Last device event
        if (device.getLastEvent() != null) {
            DeviceEvent lastEvent = device.getLastEvent();

            gwtDevice.setLastEventType(lastEvent.getType());
            gwtDevice.setLastEventOn(lastEvent.getReceivedOn());

        }

        // Device connection
        gwtDevice.setConnectionIp(device.getConnectionIp());
        gwtDevice.setConnectionInterface(device.getConnectionInterface());
        if (device.getConnection() != null) {
            DeviceConnection connection = device.getConnection();
            gwtDevice.setClientIp(connection.getClientIp());
            gwtDevice.setGwtDeviceConnectionStatus(connection.getStatus().toString());
            gwtDevice.setDeviceUserId(connection.getUserId().toCompactId());
        }
        return gwtDevice;
    }

    public static GwtDeviceEvent convert(DeviceEvent deviceEvent) {
        GwtDeviceEvent gwtDeviceEvent = new GwtDeviceEvent();
        gwtDeviceEvent.setDeviceId(deviceEvent.getDeviceId().toCompactId());
        gwtDeviceEvent.setSentOn(deviceEvent.getSentOn());
        gwtDeviceEvent.setReceivedOn(deviceEvent.getReceivedOn());
        gwtDeviceEvent.setEventType(deviceEvent.getResource());
        gwtDeviceEvent.setGwtActionType(deviceEvent.getAction().toString());
        gwtDeviceEvent.setGwtResponseCode(deviceEvent.getResponseCode().toString());
        String escapedMessage = KapuaSafeHtmlUtils.htmlEscape(deviceEvent.getEventMessage());
        gwtDeviceEvent.setEventMessage(escapedMessage);

        return gwtDeviceEvent;
    }

    public static GwtDeviceConnection convert(DeviceConnection deviceConnection) {
        GwtDeviceConnection gwtDeviceConnection = new GwtDeviceConnection();

        //
        // Convert commons attributes
        convertEntity(deviceConnection, gwtDeviceConnection);

        //
        // Convert other attributes
        gwtDeviceConnection.setClientId(deviceConnection.getClientId());
        gwtDeviceConnection.setUserId(convert(deviceConnection.getUserId()));
        gwtDeviceConnection.setClientIp(deviceConnection.getClientIp());
        gwtDeviceConnection.setServerIp(deviceConnection.getServerIp());
        gwtDeviceConnection.setProtocol(deviceConnection.getProtocol());
        gwtDeviceConnection.setConnectionStatus(convert(deviceConnection.getStatus()));
        gwtDeviceConnection.setOptlock(deviceConnection.getOptlock());

        // convert user coupling attributes
        gwtDeviceConnection.setReservedUserId(convert(deviceConnection.getReservedUserId()));
        if (deviceConnection.getUserCouplingMode() != null) {
            GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.valueOf(deviceConnection.getUserCouplingMode().name());
            gwtDeviceConnection.setConnectionUserCouplingMode(gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : "");
        }
        gwtDeviceConnection.setAllowUserChange(deviceConnection.getAllowUserChange());

        //
        // Return converted entity
        return gwtDeviceConnection;
    }

    public static GwtDeviceConnectionOption convert(DeviceConnectionOption deviceConnectionOption) {
        GwtDeviceConnectionOption gwtDeviceConnectionOption = new GwtDeviceConnectionOption();

        //
        // Convert commons attributes
        convertEntity(deviceConnectionOption, gwtDeviceConnectionOption);

        // convert user coupling attributes
        gwtDeviceConnectionOption.setReservedUserId(convert(deviceConnectionOption.getReservedUserId()));
        if (deviceConnectionOption.getUserCouplingMode() != null) {
            GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.getEnumFromLabel(deviceConnectionOption.getUserCouplingMode().name());
            gwtDeviceConnectionOption.setConnectionUserCouplingMode(gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : "");
        }
        gwtDeviceConnectionOption.setAllowUserChange(deviceConnectionOption.getAllowUserChange());

        //
        // Return converted entity
        return gwtDeviceConnectionOption;
    }

    private static String convert(DeviceConnectionStatus status) {
        return status.toString();
    }

    public static GwtCredential convert(Credential credential, User user) {
        GwtCredential gwtCredential = new GwtCredential();
        convertEntity(credential, gwtCredential);
        gwtCredential.setUserId(credential.getUserId().toCompactId());
        gwtCredential.setCredentialType(credential.getCredentialType().toString());
        gwtCredential.setCredentialKey(credential.getCredentialKey());
        gwtCredential.setCredentialStatus(credential.getStatus().toString());
        gwtCredential.setExpirationDate(credential.getExpirationDate());
        if (user != null) {
            gwtCredential.setUsername(user.getName());
        }
        gwtCredential.setSubjectType(GwtSubjectType.USER.toString());
        return gwtCredential;
    }

    public static GwtTopic convertToTopic(ChannelInfo channelInfo) {
        return new GwtTopic(channelInfo.getName(), channelInfo.getName(), channelInfo.getName(), channelInfo.getLastMessageOn());
    }

    public static GwtDatastoreAsset convertToAssets(ChannelInfo channelInfo) {
        return new GwtDatastoreAsset(channelInfo.getName().substring(6), channelInfo.getName(), channelInfo.getName(), channelInfo.getLastMessageOn());
    }

    public static GwtHeader convertToHeader(MetricInfo metric) {
        GwtHeader header = new GwtHeader();
        header.setName(metric.getName());
        header.setType(metric.getMetricType().getSimpleName());
        return header;
    }

    /**
     * Utility method to convert commons properties of {@link KapuaUpdatableEntity} object to the GWT matching {@link GwtUpdatableEntityModel} object
     *
     * @param kapuaEntity The {@link KapuaUpdatableEntity} from which to copy values
     * @param gwtEntity   The {@link GwtUpdatableEntityModel} into which copy values
     * @since 1.0.0
     */
    private static void convertEntity(KapuaUpdatableEntity kapuaEntity, GwtUpdatableEntityModel gwtEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        convertEntity((KapuaEntity) kapuaEntity, (GwtEntityModel) gwtEntity);

        gwtEntity.setModifiedOn(kapuaEntity.getModifiedOn());
        gwtEntity.setModifiedBy(convert(kapuaEntity.getModifiedBy()));
        gwtEntity.setOptlock(kapuaEntity.getOptlock());
    }

    /**
     * Utility method to convert commons properties of {@link KapuaEntity} object to the GWT matching {@link GwtEntityModel} object
     *
     * @param kapuaEntity The {@link KapuaEntity} from which to copy values
     * @param gwtEntity   The {@link GwtEntityModel} into which copy values
     * @since 1.0.0
     */
    private static void convertEntity(KapuaEntity kapuaEntity, GwtEntityModel gwtEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        gwtEntity.setScopeId(convert(kapuaEntity.getScopeId()));
        gwtEntity.setId(convert(kapuaEntity.getId()));
        gwtEntity.setCreatedOn(kapuaEntity.getCreatedOn());
        gwtEntity.setCreatedBy(convert(kapuaEntity.getCreatedBy()));
    }

    /**
     * @param client
     * @return
     */
    public static GwtDatastoreDevice convertToDatastoreDevice(ClientInfo client) {
        return new GwtDatastoreDevice(client.getClientId(), client.getLastMessageOn());
    }

    /**
     * @param message
     * @param headers
     * @return
     */
    public static GwtMessage convertToMessage(DatastoreMessage message, List<GwtHeader> headers) {
        GwtMessage gwtMessage = new GwtMessage();
        List<String> semanticParts = message.getChannel().getSemanticParts();
        StringBuilder semanticTopic = new StringBuilder();
        for (int i = 0; i < semanticParts.size() - 1; i++) {
            semanticTopic.append(semanticParts.get(i));
            semanticTopic.append("/");
        }
        semanticTopic.append(semanticParts.get(semanticParts.size() - 1));
        gwtMessage.setChannel(semanticTopic.toString());
        gwtMessage.setClientId(message.getClientId());
        gwtMessage.setTimestamp(message.getTimestamp());
        for (GwtHeader header : headers) {
            gwtMessage.set(header.getName(), message.getPayload().getMetrics().get(header.getName()));
        }
        return gwtMessage;
    }

    public static GwtDeviceAssets convert(DeviceAssets assets) {
        GwtDeviceAssets gwtAssets = new GwtDeviceAssets();
        List<GwtDeviceAsset> gwtAssetsList = new ArrayList<GwtDeviceAsset>();
        for (DeviceAsset asset : assets.getAssets()) {
            gwtAssetsList.add(convert(asset));
        }
        gwtAssets.setAssets(gwtAssetsList);
        return gwtAssets;
    }

    public static GwtDeviceAsset convert(DeviceAsset asset) {
        GwtDeviceAsset gwtAsset = new GwtDeviceAsset();
        List<GwtDeviceAssetChannel> gwtChannelsList = new ArrayList<GwtDeviceAssetChannel>();
        gwtAsset.setName(asset.getName());
        for (DeviceAssetChannel channel : asset.getChannels()) {
            gwtChannelsList.add(convert(channel));
        }
        gwtAsset.setChannels(gwtChannelsList);
        return gwtAsset;
    }

    public static GwtDeviceAssetChannel convert(DeviceAssetChannel channel) {
        GwtDeviceAssetChannel gwtChannel = new GwtDeviceAssetChannel();
        gwtChannel.setName(channel.getName());
        gwtChannel.setError(channel.getError());
        gwtChannel.setTimestamp(channel.getTimestamp());
        gwtChannel.setMode(channel.getMode().toString());
        gwtChannel.setType(ObjectTypeConverter.toString(channel.getType()));
        gwtChannel.setValue(ObjectValueConverter.toString(channel.getValue()));
        return gwtChannel;
    }

}
