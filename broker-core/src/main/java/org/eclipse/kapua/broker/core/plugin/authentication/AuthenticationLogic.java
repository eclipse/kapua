/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.authentication;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.BrokerDomains;
import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.broker.core.plugin.KapuaConnectionContext;
import org.eclipse.kapua.broker.core.plugin.KapuaDuplicateClientIdException;
import org.eclipse.kapua.broker.core.plugin.metric.ClientMetric;
import org.eclipse.kapua.broker.core.plugin.metric.LoginMetric;
import org.eclipse.kapua.broker.core.plugin.metric.PublishMetric;
import org.eclipse.kapua.broker.core.plugin.metric.SubscribeMetric;
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
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
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
    protected String aclAdvisory;

    protected ClientMetric clientMetric = ClientMetric.getInstance();
    protected LoginMetric loginMetric = LoginMetric.getInstance();
    protected PublishMetric publishMetric = PublishMetric.getInstance();
    protected SubscribeMetric subscribeMetric = SubscribeMetric.getInstance();

    protected static final Domain BROKER_DOMAIN = BrokerDomains.BROKER_DOMAIN;
    protected static final Domain DATASTORE_DOMAIN = new DatastoreDomain();
    protected static final Domain DEVICE_MANAGEMENT_DOMAIN = new DeviceManagementDomain();

    protected DeviceConnectionOptionFactory deviceConnectionOptionFactory = KapuaLocator.getInstance().getFactory(DeviceConnectionOptionFactory.class);
    protected DeviceConnectionOptionService deviceConnectionOptionService = KapuaLocator.getInstance().getService(DeviceConnectionOptionService.class);
    protected AuthorizationService authorizationService = KapuaLocator.getInstance().getService(AuthorizationService.class);
    protected DeviceConnectionFactory deviceConnectionFactory = KapuaLocator.getInstance().getFactory(DeviceConnectionFactory.class);
    protected PermissionFactory permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);
    protected DeviceConnectionService deviceConnectionService = KapuaLocator.getInstance().getService(DeviceConnectionService.class);

    /**
     * Default constructor
     *
     * @param addressPrefix     prefix address to prepend to all the addressed when building the ACL list
     * @param addressClassifier address classifier used by the platform messages (not telemetry messages) (if defined by the platform)
     * @param advisoryPrefix    address prefix for the advisory messages (if defined by the platform)
     */
    protected AuthenticationLogic(String addressPrefix, String addressClassifier, String advisoryPrefix) {
        aclHash = addressPrefix + ">";
        aclAdvisory = addressPrefix + advisoryPrefix;
    }

    /**
     * Execute the connect logic returning the authorization list (ACL)
     *
     * @param kcc
     * @return
     * @throws KapuaException
     */
    public abstract List<org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry> connect(KapuaConnectionContext kcc)
            throws KapuaException;

    /**
     * Execute the disconnection logic
     *
     * @param kcc
     * @param error
     * @return true send disconnect message (if the disconnection is a clean disconnection)
     * false don't send disconnect message (the disconnection is caused by a stealing link or the device is currently connected to another node)
     */
    public abstract boolean disconnect(KapuaConnectionContext kcc, Throwable error);

    /**
     * @param kcc
     * @return
     */
    protected abstract List<AuthorizationEntry> buildAuthorizationMap(KapuaConnectionContext kcc);

    /**
     * Format the ACL resource based on the pattern and the account name looking into the connection context property
     *
     * @param pattern
     * @param kcc
     * @return
     */
    protected String formatAcl(String pattern, KapuaConnectionContext kcc) {
        return MessageFormat.format(pattern, kcc.getAccountName());
    }

    /**
     * Format the ACL resource based on the pattern and the account name and client id looking into the connection context property
     *
     * @param pattern
     * @param kcc
     * @return
     */
    protected String formatAclFull(String pattern, KapuaConnectionContext kcc) {
        return MessageFormat.format(pattern, kcc.getAccountName(), kcc.getClientId());
    }

    /**
     * Create the authorization entry base on the ACL and the resource address
     *
     * @param kcc
     * @param acl
     * @param address
     * @return
     */
    protected AuthorizationEntry createAuthorizationEntry(KapuaConnectionContext kcc, Acl acl, String address) {
        AuthorizationEntry entry = new AuthorizationEntry(address, acl);
        kcc.addAuthDestinationToLog(MessageFormat.format(PERMISSION_LOG,
                acl.isRead() ? "r" : "_",
                acl.isWrite() ? "w" : "_",
                acl.isAdmin() ? "a" : "_",
                address));
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
                connectionUserCouplingMode = loadConnectionUserCouplingModeFromConfig(scopeId, options);
            }
            enforceDeviceUserBound(connectionUserCouplingMode, deviceConnection, scopeId, userId);
        } else {
            logger.debug("Enforce Device-User bound - no device connection found so user account settings for enforcing the bound (user id - '{}')", userId);
            enforceDeviceUserBound(loadConnectionUserCouplingModeFromConfig(scopeId, options), deviceConnection, scopeId, userId);
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
                        throw new SecurityException("User not authorized!");
                        // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
                    }
                } else {
                    checkConnectionCountByReservedUserId(scopeId, deviceConnection.getReservedUserId(), 1);
                    if (!userId.equals(deviceConnection.getReservedUserId())) {
                        throw new SecurityException("User not authorized!");
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
            throw new SecurityException("User not authorized!");
            // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
        }
    }

    /**
     * Load the device connection/user coupling mode<br>
     * <b>Utility method used by the connection logic</b>
     *
     * @param scopeId
     * @param options
     * @return
     * @throws KapuaException
     */
    protected ConnectionUserCouplingMode loadConnectionUserCouplingModeFromConfig(KapuaId scopeId, Map<String, Object> options) throws KapuaException {
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

    protected boolean isStealingLink(KapuaConnectionContext kcc, Throwable error) {
        boolean stealingLinkDetected = false;
        if (kcc.getOldConnectionId() != null) {
            stealingLinkDetected = !kcc.getOldConnectionId().equals(kcc.getConnectionId());
        }
        else {
            logger.error("Cannot find connection id for client id {} on connection map. Correct connection id is {} - IP: {}",
                    kcc.getClientId(),
                    kcc.getConnectionId(),
                    kcc.getClientIp());
        }
        if (!stealingLinkDetected && (error instanceof KapuaDuplicateClientIdException || (error!=null && error.getCause() instanceof KapuaDuplicateClientIdException))) {
            stealingLinkDetected = true;
            logger.warn("Detected Stealing link for cliend id {} - account id {} - last connection id was {} - current connection id is {} - IP: {} - No disconnection info will be added!",
                    kcc.getClientId(),
                    kcc.getScopeId(),
                    kcc.getOldConnectionId(),
                    kcc.getConnectionId(),
                    kcc.getClientIp());
        }
        return stealingLinkDetected;
    }

}
