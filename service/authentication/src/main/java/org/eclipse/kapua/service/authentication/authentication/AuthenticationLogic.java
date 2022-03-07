/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.authentication;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.BrokerDomains;
import org.eclipse.kapua.client.security.AuthErrorCodes;
import org.eclipse.kapua.client.security.KapuaIllegalDeviceStateException;
import org.eclipse.kapua.client.security.bean.AclUtils;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.client.security.bean.AuthAcl.Action;
import org.eclipse.kapua.client.security.bean.AuthContext;
import org.eclipse.kapua.client.security.metric.LoginMetric;
import org.eclipse.kapua.client.security.metric.PublishMetric;
import org.eclipse.kapua.client.security.metric.SubscribeMetric;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.device.management.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionAttributes;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionFactory;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionQuery;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Authentication logic definition
 *
 * @since 1.0
 */
public abstract class AuthenticationLogic {

    protected static final Logger logger = LoggerFactory.getLogger(AuthenticationLogic.class);

    protected static final String PERMISSION_LOG = "{0}/{1}/{2} - {3}";

    protected String aclHash;

    protected LoginMetric loginMetric = LoginMetric.getInstance();
    protected PublishMetric publishMetric = PublishMetric.getInstance();
    protected SubscribeMetric subscribeMetric = SubscribeMetric.getInstance();

    protected static final Domain BROKER_DOMAIN = BrokerDomains.BROKER_DOMAIN;
    //TODO remove domain object. I cannot import one module just to get a constant
    protected static final Domain DATASTORE_DOMAIN = new DatastoreDomain();
    protected static final Domain DEVICE_MANAGEMENT_DOMAIN = new DeviceManagementDomain();

    protected DeviceConnectionOptionFactory deviceConnectionOptionFactory = KapuaLocator.getInstance().getFactory(DeviceConnectionOptionFactory.class);
    protected DeviceConnectionOptionService deviceConnectionOptionService = KapuaLocator.getInstance().getService(DeviceConnectionOptionService.class);
    protected AuthorizationService authorizationService = KapuaLocator.getInstance().getService(AuthorizationService.class);
    protected DeviceConnectionFactory deviceConnectionFactory = KapuaLocator.getInstance().getFactory(DeviceConnectionFactory.class);
    protected PermissionFactory permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);
    protected DeviceConnectionService deviceConnectionService = KapuaLocator.getInstance().getService(DeviceConnectionService.class);

    private static final String USER_NOT_AUTHORIZED = "User not authorized!";

    /**
     * Default constructor
     *
     */
    protected AuthenticationLogic() {
        aclHash = "#";
    }

    /**
     * Execute the connect logic returning the authorization list (ACL)
     *
     * @param authContext
     * @return
     * @throws KapuaException
     */
    public abstract List<AuthAcl> connect(AuthContext authContext)
            throws KapuaException;

    /**
     * Execute the disconnection logic
     *
     * @param authContext
     * @return true send disconnect message (if the disconnection is a clean disconnection)
     * false don't send disconnect message (the disconnection is caused by a stealing link or the device is currently connected to another node)
     */
    public abstract boolean disconnect(AuthContext authContext);

    /**
     * @param userPermissions
     * @param authContext
     * @return
     */
    protected abstract List<AuthAcl> buildAuthorizationMap(UserPermissions userPermissions, AuthContext authContext);

    /**
     * Format the ACL resource based on the pattern and the account name looking into the connection context property
     *
     * @param pattern
     * @param authContext
     * @return
     */
    protected String formatAcl(String pattern, AuthContext authContext) {
        return MessageFormat.format(pattern, authContext.getAccountName());
    }

    /**
     * Format the ACL resource based on the pattern and the account name and client id looking into the connection context property
     *
     * @param pattern
     * @param authContext
     * @return
     */
    protected String formatAclFull(String pattern, AuthContext authContext) {
        return MessageFormat.format(pattern, authContext.getAccountName(), authContext.getClientId());
    }

    /**
     * Create the authorization entry base on the ACL and the resource address
     *
     * @param action
     * @param address
     * @param aclDestinationsLog
     * @return
     */
    protected AuthAcl createAuthorizationEntry(Action action, String address, StringBuilder aclDestinationsLog) {
        AuthAcl entry = new AuthAcl(address, action);
        aclDestinationsLog.append(MessageFormat.format(PERMISSION_LOG,
                AclUtils.isRead(action) ? "r" : "_",
                AclUtils.isWrite(action) ? "w" : "_",
                AclUtils.isAdmin(action) ? "a" : "_",
                address));
        aclDestinationsLog.append("\n");
        return entry;
    }

    /**
     * Enforce the device connection/user bound (if enabled)<br>
     * <b>Utility method used by the connection logic</b>
     *
     * @param options
     * @param deviceConnection
     * @param scopeId
     * @param userId
     * @throws KapuaException
     */
    protected void enforceDeviceConnectionUserBound(Map<String, Object> options, DeviceConnection deviceConnection, KapuaId scopeId, KapuaId userId) throws KapuaException {
        if (deviceConnection != null) {
            ConnectionUserCouplingMode connectionUserCouplingMode = deviceConnection.getUserCouplingMode();
            if (ConnectionUserCouplingMode.INHERITED.equals(deviceConnection.getUserCouplingMode())) {
                connectionUserCouplingMode = loadConnectionUserCouplingModeFromConfig(options);
            }
            enforceDeviceUserBound(connectionUserCouplingMode, deviceConnection, scopeId, userId);
        } else {
            logger.debug("Enforce Device-User bound - no device connection found so user account settings for enforcing the bound (user id - '{}')", userId);
            enforceDeviceUserBound(loadConnectionUserCouplingModeFromConfig(options), null, scopeId, userId);
        }
    }

    /**
     * Enforce the device connection/user bound (if enabled)<br>
     * <b>Utility method used by the connection logic</b>
     *
     * @param connectionUserCouplingMode
     * @param deviceConnection
     * @param scopeId
     * @param userId
     * @throws KapuaException
     */
    protected void enforceDeviceUserBound(ConnectionUserCouplingMode connectionUserCouplingMode, DeviceConnection deviceConnection, KapuaId scopeId, KapuaId userId)
            throws KapuaException {
        if (ConnectionUserCouplingMode.STRICT.equals(connectionUserCouplingMode)) {
            if (deviceConnection == null) {
                checkConnectionCountByReservedUserId(scopeId, userId, 0);
            } else {
                if (deviceConnection.getReservedUserId() == null) {
                    checkConnectionCountByReservedUserId(scopeId, userId, 0);
                    if (!deviceConnection.getAllowUserChange() && !userId.equals(deviceConnection.getUserId())) {
                        throw new SecurityException(USER_NOT_AUTHORIZED);
                        // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
                    }
                } else {
                    checkConnectionCountByReservedUserId(scopeId, deviceConnection.getReservedUserId(), 1);
                    if (!userId.equals(deviceConnection.getReservedUserId())) {
                        throw new SecurityException(USER_NOT_AUTHORIZED);
                        // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
                    }
                }
            }
        } else {
            if (deviceConnection != null && deviceConnection.getReservedUserId() != null && userId.equals(deviceConnection.getReservedUserId())) {
                checkConnectionCountByReservedUserId(scopeId, userId, 1);
            } else {
                checkConnectionCountByReservedUserId(scopeId, userId, 0);
            }
        }
    }

    /**
     * Check the connection count for a specific reserved user id<br>
     * <b>Utility method used by the connection logic</b>
     *
     * @param scopeId
     * @param userId
     * @param count
     * @throws KapuaException
     */
    protected void checkConnectionCountByReservedUserId(KapuaId scopeId, KapuaId userId, long count) throws KapuaException {
        // check that no devices have this user as strict user
        DeviceConnectionOptionQuery query = deviceConnectionOptionFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(DeviceConnectionOptionAttributes.RESERVED_USER_ID, userId));
        query.setLimit(1);

        Long connectionCountByReservedUserId = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionOptionService.count(query));
        if (connectionCountByReservedUserId != null && connectionCountByReservedUserId > count) {
            throw new SecurityException(USER_NOT_AUTHORIZED);
            // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
        }
    }

    /**
     * Load the device connection/user coupling mode<br>
     * <b>Utility method used by the connection logic</b>
     *
     * @param options
     * @return
     */
    protected ConnectionUserCouplingMode loadConnectionUserCouplingModeFromConfig(Map<String, Object> options) {
        String tmp = (String) options.get("deviceConnectionUserCouplingDefaultMode");// TODO move to constants
        if (tmp != null) {
            ConnectionUserCouplingMode tmpConnectionUserCouplingMode = ConnectionUserCouplingMode.valueOf(tmp);
            if (tmpConnectionUserCouplingMode == null) {
                throw new SecurityException(String
                        .format("Cannot parse the default Device-User coupling mode in the registry service configuration! (found '%s' - allowed values are 'LOOSE' - 'STRICT')", tmp));
                // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
            } else {
                return tmpConnectionUserCouplingMode;
            }
        } else {
            throw new SecurityException("Cannot find default Device-User coupling mode in the registry service configuration! (deviceConnectionUserCouplingDefaultMode");
            // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
        }
    }

    protected boolean isStealingLink(AuthContext authContext) {
        boolean stealingLink = false;
        if (authContext.getOldConnectionId() != null) {
            stealingLink = !authContext.getOldConnectionId().equals(authContext.getConnectionId());
        }
        else {
            logger.error("Cannot find connection id for client id {} on connection map. Correct connection id is {} - IP: {}",
                    authContext.getClientId(),
                    authContext.getConnectionId(),
                    authContext.getClientIp());
        }
        if (stealingLink) {
            loginMetric.getStealingLinkDisconnect().inc();
            logger.warn("Detected Stealing link for cliend id {} - account id {} - last connection id was {} - current connection id is {} - IP: {}",
                authContext.getClientId(),
                authContext.getScopeId(),
                authContext.getOldConnectionId(),
                authContext.getConnectionId(),
                authContext.getClientIp());
        }
        return stealingLink;
    }

    protected boolean isIllegalState(AuthContext authContext) {
        //TODO make this check based on instanceof
        //something like Class.forName(exceptionClass).. just are we sure we have the exceptionClass implementation available at runtime?
        return KapuaIllegalDeviceStateException.class.getName().equals(authContext.getExceptionClass()) && AuthErrorCodes.DUPLICATE_CLIENT_ID.equals(authContext.getAuthErrorCode());
    }

    /**
     * Create a new {@link DeviceConnection} or updates the existing one using the info provided.
     *
     * @param authContext
     * @param deviceConnection The {@link DeviceConnection} to update, or null if it needs to be created
     * @return The created/updated {@link DeviceConnection}
     * @throws KapuaException
     */
    protected DeviceConnection upsertDeviceConnection(AuthContext authContext, DeviceConnection deviceConnection) throws KapuaException {
        if (deviceConnection == null) {
            DeviceConnectionCreator deviceConnectionCreator = deviceConnectionFactory.newCreator(KapuaEid.parseCompactId(authContext.getScopeId()));
            deviceConnectionCreator.setStatus(DeviceConnectionStatus.CONNECTED);
            deviceConnectionCreator.setClientId(authContext.getClientId());
            deviceConnectionCreator.setClientIp(authContext.getClientIp());
            deviceConnectionCreator.setProtocol(authContext.getTransportProtocol());
            deviceConnectionCreator.setServerIp(authContext.getBrokerHost());
            deviceConnectionCreator.setUserId(KapuaEid.parseCompactId(authContext.getUserId()));
            deviceConnectionCreator.setUserCouplingMode(ConnectionUserCouplingMode.INHERITED);
            deviceConnectionCreator.setAllowUserChange(false);
            deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.create(deviceConnectionCreator));
        } else {
            deviceConnection.setStatus(DeviceConnectionStatus.CONNECTED);
            deviceConnection.setClientIp(authContext.getClientIp());
            deviceConnection.setProtocol(authContext.getTransportProtocol());
            deviceConnection.setServerIp(authContext.getBrokerHost());
            deviceConnection.setUserId(KapuaEid.parseCompactId(authContext.getUserId()));
            deviceConnection.setAllowUserChange(false);
            DeviceConnection deviceConnectionToUpdate = deviceConnection;
            KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.update(deviceConnectionToUpdate));
            // TODO implement the banned status
            // if (DeviceStatus.DISABLED.equals(device.getStatus())) {
            // throw new KapuaIllegalAccessException("clientId - This client ID is disabled and cannot connect");
            // }

            // TODO manage the stealing link event (may be a good idea to use different connect status (connect -stealing)?
            String previousConnectionId = authContext.getOldConnectionId();
            if (previousConnectionId != null) {
                loginMetric.getStealingLinkConnect().inc();

                // stealing link detected, skip info
                logger.warn("Detected Stealing link for cliend id {} - account {} - last connection id was {} - current connection id is {} - IP: {}",
                        authContext.getClientId(),
                        authContext.getAccountName(),
                        previousConnectionId,
                        authContext.getConnectionId(),
                        authContext.getClientIp());
            }
        }
        return deviceConnection;
    }

}
