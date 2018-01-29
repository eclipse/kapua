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
package org.eclipse.kapua.broker.core.plugin.authentication;

import com.codahale.metrics.Timer.Context;
import org.apache.shiro.ShiroException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.broker.core.plugin.KapuaConnectionContext;
import org.eclipse.kapua.broker.core.plugin.KapuaDuplicateClientIdException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User profile authentication logic implementation
 *
 * @since 1.0
 */
public class UserAuthenticationLogic extends AuthenticationLogic {

    protected String aclCtrlAccReply;
    protected String aclCtrlAccCliMqttLifeCycle;
    protected String aclCtrlAcc;
    protected String aclCtrlAccCli;
    protected String aclDataAcc;
    protected String aclDataAccCli;
    protected String aclCtrlAccNotify;

    protected static final int BROKER_CONNECT_IDX = 0;
    protected static final int DEVICE_MANAGE_IDX = 1;
    protected static final int DATA_VIEW_IDX = 2;
    protected static final int DATA_MANAGE_IDX = 3;

    /**
     * Default constructor
     *
     * @param options
     */
    public UserAuthenticationLogic(Map<String, Object> options) {
        super((String) options.get(Authenticator.ADDRESS_PREFIX_KEY), (String) options.get(Authenticator.ADDRESS_CLASSIFIER_KEY), (String) options.get(Authenticator.ADDRESS_ADVISORY_PREFIX_KEY));
        String addressPrefix = (String) options.get(Authenticator.ADDRESS_PREFIX_KEY);
        String addressClassifier = (String) options.get(Authenticator.ADDRESS_CLASSIFIER_KEY);
        aclCtrlAccReply = addressPrefix + addressClassifier + ".{0}.*.*.REPLY.>";
        aclCtrlAccCliMqttLifeCycle = addressPrefix + addressClassifier + ".{0}.{1}.MQTT.>";
        aclCtrlAcc = addressPrefix + addressClassifier + ".{0}.>";
        aclCtrlAccCli = addressPrefix + addressClassifier + ".{0}.{1}.>";
        aclDataAcc = addressPrefix + "{0}.>";
        aclDataAccCli = addressPrefix + "{0}.{1}.>";
        aclCtrlAccNotify = addressPrefix + addressClassifier + ".{0}.*.*.NOTIFY.{1}.>";
    }

    @Override
    public List<org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry> connect(KapuaConnectionContext kcc) throws KapuaException {
        Context loginNormalUserTimeContext = loginMetric.getNormalUserTime().time();
        Context loginCheckAccessTimeContext = loginMetric.getCheckAccessTime().time();
        boolean[] hasPermissions = new boolean[] {
                // TODO check the permissions... move them to a constants class?
                authorizationService.isPermitted(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, kcc.getScopeId())),
                authorizationService.isPermitted(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.write, kcc.getScopeId())),
                authorizationService.isPermitted(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.read, kcc.getScopeId())),
                authorizationService.isPermitted(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.write, kcc.getScopeId()))
        };
        if (!hasPermissions[BROKER_CONNECT_IDX]) {
            throw new KapuaIllegalAccessException(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, kcc.getScopeId()).toString());
        }
        loginCheckAccessTimeContext.stop();

        kcc.updatePermissions(hasPermissions);
        List<org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry> authorizationEntries = buildAuthorizationMap(kcc);

        Context loginFindClientIdTimeContext = loginMetric.getFindClientIdTime().time();
        DeviceConnection deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(kcc.getScopeId(), kcc.getClientId()));
        loginFindClientIdTimeContext.stop();
        // enforce the user-device bound
        enforceDeviceConnectionUserBound(KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.getConfigValues(kcc.getScopeId())), deviceConnection, kcc.getScopeId(), kcc.getUserId());

        Context loginFindDevTimeContext = loginMetric.getFindDevTime().time();

        String previousConnectionId = kcc.getOldConnectionId();
        boolean stealingLinkDetected = (previousConnectionId != null);
        if (deviceConnection == null) {
            DeviceConnectionCreator deviceConnectionCreator = deviceConnectionFactory.newCreator(kcc.getScopeId());
            deviceConnectionCreator.setClientId(kcc.getClientId());
            deviceConnectionCreator.setClientIp(kcc.getClientIp());
            deviceConnectionCreator.setProtocol(kcc.getConnectorDescriptor().getTransportProtocol());
            deviceConnectionCreator.setServerIp(kcc.getBrokerIpOrHostName());
            deviceConnectionCreator.setUserId(kcc.getUserId());
            deviceConnectionCreator.setUserCouplingMode(ConnectionUserCouplingMode.INHERITED);
            deviceConnectionCreator.setAllowUserChange(false);
            deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.create(deviceConnectionCreator));
        } else {
            deviceConnection.setClientIp(kcc.getClientIp());
            deviceConnection.setProtocol(kcc.getConnectorDescriptor().getTransportProtocol());
            deviceConnection.setServerIp(kcc.getBrokerIpOrHostName());
            deviceConnection.setUserId(kcc.getUserId());
            deviceConnection.setStatus(DeviceConnectionStatus.CONNECTED);
            deviceConnection.setAllowUserChange(false);
            final DeviceConnection deviceConnectionToUpdate = deviceConnection;
            KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.update(deviceConnectionToUpdate));
            // TODO implement the banned status
            // if (DeviceStatus.DISABLED.equals(device.getStatus())) {
            // throw new KapuaIllegalAccessException("clientId - This client ID is disabled and cannot connect");
            // }
            // TODO manage the stealing link event (may be a good idea to use different connect status (connect -stealing)?
            if (stealingLinkDetected) {
                loginMetric.getStealingLinkConnect().inc();

                // stealing link detected, skip info
                logger.warn("Detected Stealing link for cliend id {} - account - last connection id was {} - current connection id is {} - IP: {} - No connection status changes!",
                        new Object[] { kcc.getClientId(), kcc.getAccountName(), previousConnectionId, kcc.getConnectionId(), kcc.getClientIp() });
            }
        }
        kcc.updateKapuaConnectionId(deviceConnection);
        loginFindDevTimeContext.stop();
        loginNormalUserTimeContext.stop();
        return authorizationEntries;
    }

    @Override
    public void disconnect(KapuaConnectionContext kcc, Throwable error) {
        boolean stealingLinkDetected = false;
        if (kcc.getOldConnectionId() != null) {
            stealingLinkDetected = !kcc.getOldConnectionId().equals(kcc.getConnectionId());
        } else {
            logger.error("Cannot find connection id for client id {} on connection map. Correct connection id is {} - IP: {}",
                    new Object[] { kcc.getClientId(), kcc.getConnectionId(), kcc.getClientIp() });
        }
        if (stealingLinkDetected) {
            loginMetric.getStealingLinkDisconnect().inc();
            // stealing link detected, skip info
            logger.warn("Detected Stealing link for cliend id {} - account id {} - last connection id was {} - current connection id is {} - IP: {} - No disconnection info will be added!",
                    new Object[] { kcc.getClientId(), kcc.getScopeId(), kcc.getOldConnectionId(), kcc.getConnectionId(), kcc.getClientIp() });
        } else {
            final DeviceConnection deviceConnection;
            try {
                deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(kcc.getScopeId(), kcc.getClientId()));
            } catch (Exception e) {
                throw new ShiroException("Error while looking for device connection on updating the device!", e);
            }
            if (deviceConnection != null) {
                // the device connection must be not null
                // update device connection (if the disconnection wasn't caused by a stealing link)
                if (error instanceof KapuaDuplicateClientIdException) {
                    logger.debug("Skip device connection status update since is coming from a stealing link condition. Client id: {} - Connection id: {}",
                            new Object[] { kcc.getClientId(), kcc.getConnectionId() });
                } else {
                    deviceConnection.setStatus(error == null ? DeviceConnectionStatus.DISCONNECTED : DeviceConnectionStatus.MISSING);
                    try {
                        KapuaSecurityUtils.doPrivileged(() -> {
                            deviceConnectionService.update(deviceConnection);
                            return null;
                        });
                    } catch (Exception e) {
                        throw new ShiroException("Error while updating the device connection status!", e);
                    }
                }
            }
        }
    }

    @Override
    protected List<AuthorizationEntry> buildAuthorizationMap(KapuaConnectionContext kcc) {
        ArrayList<AuthorizationEntry> ael = new ArrayList<AuthorizationEntry>();
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE_ADMIN, aclAdvisory));

        // addConnection checks BROKER_CONNECT_IDX permission before call this method
        // then here user has BROKER_CONNECT_IDX permission and if check isn't needed
        // if (hasPermissions[BROKER_CONNECT_IDX]) {
        if (kcc.getHasPermissions()[DEVICE_MANAGE_IDX]) {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAcl(aclCtrlAcc, kcc)));
        } else {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAclFull(aclCtrlAccCli, kcc)));
        }
        if (kcc.getHasPermissions()[DATA_MANAGE_IDX]) {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAcl(aclDataAcc, kcc)));
        } else if (kcc.getHasPermissions()[DATA_VIEW_IDX]) {
            ael.add(createAuthorizationEntry(kcc, Acl.READ_ADMIN, formatAcl(aclDataAcc, kcc)));
            ael.add(createAuthorizationEntry(kcc, Acl.WRITE, formatAclFull(aclDataAccCli, kcc)));
        } else {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAclFull(aclDataAccCli, kcc)));
        }
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE_ADMIN, formatAcl(aclCtrlAccReply, kcc)));

        // Write notify to any client Id and any application and operation
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE, formatAclFull(aclCtrlAccNotify, kcc)));

        kcc.logAuthDestinationToLog();

        return ael;
    }
}
