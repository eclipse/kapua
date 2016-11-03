/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
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

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtOrganization;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtUser;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.predicate.KapuaAndPredicate;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.Organization;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDomain;
import org.eclipse.kapua.service.authorization.permission.Action;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.shiro.RoleDomain;
import org.eclipse.kapua.service.authorization.user.permission.shiro.UserPermissionDomain;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionPredicates;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventDomain;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.internal.UserDomain;

public class KapuaGwtModelConverter {

    /**
     * Converts a {@link Role} into a {@link GwtRole} object for GWT usage.
     * 
     * @param role
     *            The {@link Role} to convert.
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

        // Convert list of Role permissions
        Set<GwtRolePermission> gwtRolePermissions = new HashSet<GwtRolePermission>();
        for (RolePermission rolePermission : role.getPermissions()) {
            GwtRolePermission gwtRolePermission = convert(rolePermission);
            gwtRolePermissions.add(gwtRolePermission);
        }

        gwtRole.setPermissions(gwtRolePermissions);

        //
        // Return converted entity
        return gwtRole;
    }

    /**
     * Converts a {@link RolePermission} into a {@link GwtRolePermission} object for GWT usage.
     * 
     * @param rolePermission
     *            The {@link RolePermission} to convert
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
        GwtPermission gwtPermission = convert(rolePermission.getPermission());

        gwtRolePermission.setRoleId(convert(rolePermission.getRoleId()));
        gwtRolePermission.setDomain(gwtPermission.getDomain());
        gwtRolePermission.setAction(gwtPermission.getAction());
        gwtRolePermission.setTargetScopeId(gwtPermission.getTargetScopeId());

        //
        // Return converted entity
        return gwtRolePermission;
    }

    /**
     * Converts a {@link Permission} into a {@link GwtPermission} object for GWT usage.
     * 
     * @param permission
     *            The {@link Permission} to convert.
     * @return The converted {@link GwtPermission}.
     * @since 1.0.0
     */
    public static GwtPermission convert(Permission permission) {
        return new GwtPermission(convert(permission.getDomain()),
                convert(permission.getAction()),
                convert(permission.getTargetScopeId()));
    }

    /**
     * Converts a {@link Action} into a {@link GwtAction}
     * 
     * @param action
     *            The {@link Action} to convert
     * @return The converted {@link GwtAction}
     * 
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
                gwtAction = GwtAction.exec;
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
     * Converts a {@link String} domain into a {@link GwtDomain}
     * 
     * @param domain
     *            The {@link String} domain to convert
     * @return The converted {@link GwtDomain}
     * 
     * @since 1.0.0
     */
    public static GwtDomain convert(String domain) {
        GwtDomain gwtDomain = null;

        if (AccountDomain.ACCOUNT.equals(domain)) {
            gwtDomain = GwtDomain.account;
        } else if (CredentialDomain.CREDENTIAL.equals(domain)) {
            gwtDomain = GwtDomain.credential;
        } else if (DatastoreDomain.DATA_STORE.equals(domain)) {
            gwtDomain = GwtDomain.datastore;
        } else if (DeviceDomain.DEVICE.equals(domain)) {
            gwtDomain = GwtDomain.device;
        } else if (DeviceConnectionDomain.DEVICE_CONNECTION.equals(domain)) {
            gwtDomain = GwtDomain.device_connection;
        } else if (DeviceEventDomain.DEVICE_EVENT.equals(domain)) {
            gwtDomain = GwtDomain.device_event;
        } else if (DeviceManagementDomain.DEVICE_MANAGEMENT.equals(domain)) {
            gwtDomain = GwtDomain.device_management;
        } else if (RoleDomain.ROLE.equals(domain)) {
            gwtDomain = GwtDomain.role;
        } else if (UserDomain.USER.equals(domain)) {
            gwtDomain = GwtDomain.user;
        } else if (UserPermissionDomain.USER_PERMISSION.equals(domain)) {
            gwtDomain = GwtDomain.user_permission;
        }

        return gwtDomain;
    }

    /**
     * Converts a {@link KapuaId} into its {@link String} short id representation.
     * <p>
     * Example: 1 => AQ
     * </p>
     * 
     * @param kapuaId
     *            The {@link KapuaId} to convert
     * @return The short id representation of the {@link KapuaId}
     * @since 1.0.0
     */
    public static String convert(KapuaId kapuaId) {
        if (kapuaId == null) {
            return null;
        }

        //
        // Return converted entity
        return kapuaId.getShortId();
    }

    /**
     * Converts a {@link Account} into a {@link GwtAccount} for GWT usage.
     * 
     * @param account
     *            The {@link Account} to convert.
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
        gwtAccount.setParentAccountId(account.getScopeId() != null ? account.getScopeId().getShortId() : null);
        gwtAccount.setOptlock(account.getOptlock());

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
     * @param organization
     *            The {@link Organization} to convert.
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
     * @param user
     *            The {@link User} to convert.
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

        //
        // Return converted entity
        return gwtUser;
    }

    public static GwtDevice convert(Device device)
            throws KapuaException {
        GwtDevice gwtDevice = new GwtDevice();
        gwtDevice.setId(device.getId().getShortId());
        gwtDevice.setScopeId(device.getScopeId().getShortId());
        gwtDevice.setGwtDeviceStatus(device.getStatus().toString());
        gwtDevice.setClientId(device.getClientId());
        gwtDevice.setDisplayName(device.getDisplayName());
        gwtDevice.setModelId(device.getModelId());
        gwtDevice.setSerialNumber(device.getSerialNumber());
        gwtDevice.setFirmwareVersion(device.getFirmwareVersion());
        gwtDevice.setBiosVersion(device.getBiosVersion());
        gwtDevice.setOsVersion(device.getOsVersion());
        gwtDevice.setJvmVersion(device.getJvmVersion());
        gwtDevice.setOsgiVersion(device.getOsgiFrameworkVersion());
        gwtDevice.setAcceptEncoding(device.getAcceptEncoding());
        gwtDevice.setApplicationIdentifiers(device.getApplicationIdentifiers());
        gwtDevice.setLastEventOn(device.getLastEventOn());

        gwtDevice.setIccid(device.getIccid());
        gwtDevice.setImei(device.getImei());
        gwtDevice.setImsi(device.getImsi());

        String lastEventType = device.getLastEventType() != null ? device.getLastEventType().name() : "";
        gwtDevice.setLastEventType(lastEventType);

        // custom Attributes
        gwtDevice.setCustomAttribute1(device.getCustomAttribute1());
        gwtDevice.setCustomAttribute2(device.getCustomAttribute2());
        gwtDevice.setCustomAttribute3(device.getCustomAttribute3());
        gwtDevice.setCustomAttribute4(device.getCustomAttribute4());
        gwtDevice.setCustomAttribute5(device.getCustomAttribute5());

        gwtDevice.setOptlock(device.getOptlock());

        // Device connection
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionService deviceConnectionService = locator.getService(DeviceConnectionService.class);
        DeviceConnectionFactory deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

        DeviceConnectionQuery query = deviceConnectionFactory.newQuery(device.getScopeId());
        KapuaAndPredicate andPredicate = new AndPredicate();
        andPredicate = andPredicate.and(new AttributePredicate<String>(DeviceConnectionPredicates.CLIENT_ID, device.getClientId()));
        // andPredicate = andPredicate.and(new AttributePredicate<DeviceConnectionStatus[]>(DeviceConnectionPredicates.CONNECTION_STATUS,
        // new DeviceConnectionStatus[] { DeviceConnectionStatus.CONNECTED, DeviceConnectionStatus.MISSING }));

        query.setPredicate(andPredicate);

        KapuaListResult<DeviceConnection> deviceConnections = deviceConnectionService.query(query);

        if (!deviceConnections.isEmpty()) {
            DeviceConnection connection = deviceConnections.getItem(0);

            gwtDevice.setGwtDeviceConnectionStatus(connection.getStatus().toString());
            gwtDevice.setConnectionIp(connection.getClientIp());
            gwtDevice.setDeviceUserId(connection.getUserId().getShortId());
        }
        return gwtDevice;
    }

    public static GwtDeviceEvent convert(DeviceEvent deviceEvent) {
        GwtDeviceEvent gwtDeviceEvent = new GwtDeviceEvent();
        gwtDeviceEvent.setDeviceId(deviceEvent.getDeviceId().getShortId());
        gwtDeviceEvent.setSentOn(deviceEvent.getSentOn());
        gwtDeviceEvent.setReceivedOn(deviceEvent.getReceivedOn());
        gwtDeviceEvent.setEventType(deviceEvent.getResource());

        String escapedMessage = KapuaSafeHtmlUtils.htmlEscape(deviceEvent.getEventMessage());
        gwtDeviceEvent.setEventMessage(escapedMessage);

        return gwtDeviceEvent;
    }

    /**
     * Utility method to convert commons properties of {@link KapuaUpdatableEntity} object to the GWT matching {@link GwtUpdatableEntityModel} object
     * 
     * @param kapuaEntity
     *            The {@link KapuaUpdatableEntity} from which to copy values
     * @param gwtEntity
     *            The {@link GwtUpdatableEntityModel} into which copy values
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
     * @param kapuaEntity
     *            The {@link KapuaEntity} from which to copy values
     * @param gwtEntity
     *            The {@link GwtEntityModel} into which copy values
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
}