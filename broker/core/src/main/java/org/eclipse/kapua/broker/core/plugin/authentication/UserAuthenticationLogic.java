/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
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
    public List<AuthorizationEntry> connect(KapuaSecurityContext kapuaSecurityContext) throws KapuaException {
        Context loginNormalUserTimeContext = loginMetric.getNormalUserTime().time();

        Context loginCheckAccessTimeContext = loginMetric.getCheckAccessTime().time();
        updatePermissions(kapuaSecurityContext);
        loginCheckAccessTimeContext.stop();

        Context loginFindDeviceConnectionTimeContext = loginMetric.getFindDeviceConnectionTime().time();
        DeviceConnection deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(kapuaSecurityContext.getScopeId(), kapuaSecurityContext.getClientId()));
        loginFindDeviceConnectionTimeContext.stop();

        // enforce the user-device bound
        enforceDeviceConnectionUserBound(KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.getConfigValues(kapuaSecurityContext.getScopeId())), deviceConnection, kapuaSecurityContext.getScopeId(), kapuaSecurityContext.getUserId());

        Context loginUpdateDeviceConnectionTimeContext = loginMetric.getUpdateDeviceConnectionTime().time();
        {
            deviceConnection = upsertDeviceConnection(kapuaSecurityContext, deviceConnection);
            kapuaSecurityContext.updateKapuaConnectionId(deviceConnection);
        }
        loginUpdateDeviceConnectionTimeContext.stop();

        List<AuthorizationEntry> authorizationEntries = buildAuthorizationMap(kapuaSecurityContext);
        loginNormalUserTimeContext.stop();

        return authorizationEntries;
    }

    @Override
    public boolean disconnect(KapuaSecurityContext kapuaSecurityContext, Throwable error) {
        boolean stealingLinkDetected = isStealingLink(kapuaSecurityContext, error);
        boolean deviceOwnedByTheCurrentNode = true;
        logger.debug("Old connection id: {} - new connection id: {} - error: {} - error cause: {}", kapuaSecurityContext.getOldConnectionId(), kapuaSecurityContext.getConnectionId(), error, (error!=null ? error.getCause() : "null"), error);
        if (stealingLinkDetected) {
            loginMetric.getStealingLinkDisconnect().inc();
            logger.debug("Skip device connection status update since is coming from a stealing link condition. Client id: {} - Connection id: {}",
                    kapuaSecurityContext.getClientId(),
                    kapuaSecurityContext.getConnectionId());
        }
        else {
            // update device connection (if the disconnection wasn't caused by a stealing link)
            DeviceConnection deviceConnection;
            try {
                deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(kapuaSecurityContext.getScopeId(), kapuaSecurityContext.getClientId()));
            } catch (Exception e) {
                throw new ShiroException("Error while looking for device connection on updating the device status!", e);
            }
            // the device connection must be not null
            if (deviceConnection != null) {
                if (kapuaSecurityContext.getBrokerIpOrHostName() == null) {
                    logger.warn("Broker Ip or host name is not correctly set! Please check the configuration!");
                }
                else if (!kapuaSecurityContext.getBrokerIpOrHostName().equals(deviceConnection.getServerIp())) {
                    //the device is connected to a different node so skip to update the status!
                    deviceOwnedByTheCurrentNode = false;
                    logger.warn("Detected disconnection for client connected to another node: cliend id {} - account id {} - last connection id was {} - current connection id is {} - IP: {} - No disconnection info will be added!",
                            kapuaSecurityContext.getClientId(),
                            kapuaSecurityContext.getScopeId(),
                            kapuaSecurityContext.getOldConnectionId(),
                            kapuaSecurityContext.getConnectionId(),
                            kapuaSecurityContext.getClientIp());
                }
                if(deviceOwnedByTheCurrentNode) {
                    //update status only if the old status wasn't missing
                    if (DeviceConnectionStatus.MISSING.equals(deviceConnection.getStatus())) {
                        logger.warn("Skipping device status update for device {} since last status was MISSING!", deviceConnection.getClientId());
                    }
                    else {
                        deviceConnection.setStatus(error == null && !kapuaSecurityContext.isMissing() ? DeviceConnectionStatus.DISCONNECTED : DeviceConnectionStatus.MISSING);
                        try {
                            KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.update(deviceConnection));
                        } catch (Exception e) {
                            throw new ShiroException("Error while updating the device connection status!", e);
                        }
                    }
                }
            }
        }
        return !stealingLinkDetected && deviceOwnedByTheCurrentNode && !kapuaSecurityContext.isMissing();
    }

    @Override
    protected List<AuthorizationEntry> buildAuthorizationMap(KapuaSecurityContext kapuaSecurityContext) {
        ArrayList<AuthorizationEntry> ael = new ArrayList<>();
        ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.WRITE_ADMIN, aclAdvisory));

        // addConnection checks BROKER_CONNECT_IDX permission before call this method
        // then here user has BROKER_CONNECT_IDX permission and if check isn't needed
        // if (hasPermissions[BROKER_CONNECT_IDX]) {
        if (kapuaSecurityContext.isDeviceManage()) {
            ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.ALL, formatAcl(aclCtrlAcc, kapuaSecurityContext)));
        } else {
            ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.ALL, formatAclFull(aclCtrlAccCli, kapuaSecurityContext)));
        }
        if (kapuaSecurityContext.isDataManage()) {
            ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.ALL, formatAcl(aclDataAcc, kapuaSecurityContext)));
        } else if (kapuaSecurityContext.isDataView()) {
            ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.READ_ADMIN, formatAcl(aclDataAcc, kapuaSecurityContext)));
            ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.WRITE, formatAclFull(aclDataAccCli, kapuaSecurityContext)));
        } else {
            ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.ALL, formatAclFull(aclDataAccCli, kapuaSecurityContext)));
        }
        ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.WRITE_ADMIN, formatAcl(aclCtrlAccReply, kapuaSecurityContext)));

        // Write notify to any client Id and any application and operation
        ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.WRITE, formatAclFull(aclCtrlAccNotify, kapuaSecurityContext)));

        kapuaSecurityContext.logAuthDestinationToLog();

        return ael;
    }

    protected void updatePermissions(KapuaSecurityContext kapuaSecurityContext) throws KapuaException {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, kapuaSecurityContext.getScopeId()));
        permissions.add(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.write, kapuaSecurityContext.getScopeId()));
        permissions.add(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.read, kapuaSecurityContext.getScopeId()));
        permissions.add(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.write, kapuaSecurityContext.getScopeId()));
        permissions.add(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.read, kapuaSecurityContext.getScopeId()));
        kapuaSecurityContext.updatePermissions(authorizationService.isPermitted(permissions));

        if (!kapuaSecurityContext.isBrokerConnect()) {
            throw new KapuaIllegalAccessException(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, kapuaSecurityContext.getScopeId()).toString());
        }
    }

    /**
     * Create a new {@link DeviceConnection} or updates the existing one using the info provided in the {@link KapuaSecurityContext}.
     *
     * @param kapuaSecurityContext              The {@link KapuaSecurityContext} of the currect connection
     * @param deviceConnection The {@link DeviceConnection} to update, or null if it needs to be created
     * @return The created/updated {@link DeviceConnection}
     * @throws KapuaException
     */
    protected DeviceConnection upsertDeviceConnection(KapuaSecurityContext kapuaSecurityContext, DeviceConnection deviceConnection) throws KapuaException {

        if (deviceConnection == null) {
            DeviceConnectionCreator deviceConnectionCreator = deviceConnectionFactory.newCreator(kapuaSecurityContext.getScopeId());
            deviceConnectionCreator.setStatus(DeviceConnectionStatus.CONNECTED);
            deviceConnectionCreator.setClientId(kapuaSecurityContext.getClientId());
            deviceConnectionCreator.setClientIp(kapuaSecurityContext.getClientIp());
            deviceConnectionCreator.setProtocol(kapuaSecurityContext.getConnectorDescriptor().getTransportProtocol());
            deviceConnectionCreator.setServerIp(kapuaSecurityContext.getBrokerIpOrHostName());
            deviceConnectionCreator.setUserId(kapuaSecurityContext.getUserId());
            deviceConnectionCreator.setUserCouplingMode(ConnectionUserCouplingMode.INHERITED);
            deviceConnectionCreator.setAllowUserChange(false);
            deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.create(deviceConnectionCreator));
        } else {
            deviceConnection.setStatus(DeviceConnectionStatus.CONNECTED);
            deviceConnection.setClientIp(kapuaSecurityContext.getClientIp());
            deviceConnection.setProtocol(kapuaSecurityContext.getConnectorDescriptor().getTransportProtocol());
            deviceConnection.setServerIp(kapuaSecurityContext.getBrokerIpOrHostName());
            deviceConnection.setUserId(kapuaSecurityContext.getUserId());
            deviceConnection.setAllowUserChange(false);
            DeviceConnection deviceConnectionToUpdate = deviceConnection;
            KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.update(deviceConnectionToUpdate));
            // TODO implement the banned status
            // if (DeviceStatus.DISABLED.equals(device.getStatus())) {
            // throw new KapuaIllegalAccessException("clientId - This client ID is disabled and cannot connect");
            // }

            // TODO manage the stealing link event (may be a good idea to use different connect status (connect -stealing)?
            String previousConnectionId = kapuaSecurityContext.getOldConnectionId();
            if (previousConnectionId != null) {
                loginMetric.getStealingLinkConnect().inc();

                // stealing link detected, skip info
                logger.warn("Detected Stealing link for cliend id {} - account - last connection id was {} - current connection id is {} - IP: {} - No connection status changes!",
                        kapuaSecurityContext.getClientId(),
                        kapuaSecurityContext.getAccountName(),
                        previousConnectionId,
                        kapuaSecurityContext.getConnectionId(),
                        kapuaSecurityContext.getClientIp());
            }
        }
        return deviceConnection;
    }

}
