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

import com.codahale.metrics.Timer.Context;
import org.apache.shiro.ShiroException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.client.security.bean.AuthContext;
import org.eclipse.kapua.client.security.bean.AuthAcl.Action;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import java.util.ArrayList;
import java.util.List;

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
     */
    public UserAuthenticationLogic() {
        String addressClassifier = "\\" + SystemSetting.getInstance().getMessageClassifier();

        aclCtrlAccReply = addressClassifier + "/{0}/+/+/REPLY/#";
        aclCtrlAccCliMqttLifeCycle = addressClassifier + "/{0}/{1}/MQTT/#";
        aclCtrlAcc = addressClassifier + "/{0}/#";
        aclCtrlAccCli = addressClassifier + "/{0}/{1}/#";
        aclDataAcc = "{0}/#";
        aclDataAccCli = "{0}/{1}/#";
        aclCtrlAccNotify = addressClassifier + "/{0}/+/+/NOTIFY/{1}/#";
    }

    @Override
    public List<AuthAcl> connect(AuthContext authContext) throws KapuaException {
        Context loginNormalUserTimeContext = loginMetric.getNormalUserTime().time();
        Context loginCheckAccessTimeContext = loginMetric.getCheckAccessTime().time();
        UserPermissions userPermissions = updatePermissions(authContext);
        loginCheckAccessTimeContext.stop();

        Context loginFindDeviceConnectionTimeContext = loginMetric.getFindDeviceConnectionTime().time();
        DeviceConnection deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(
            KapuaEid.parseCompactId(authContext.getScopeId()), authContext.getClientId()));
        loginFindDeviceConnectionTimeContext.stop();

        // enforce the user-device bound
        final KapuaId scopeId = KapuaEid.parseCompactId(authContext.getScopeId());
        final KapuaId userId = KapuaEid.parseCompactId(authContext.getUserId());
        enforceDeviceConnectionUserBound(KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.getConfigValues(scopeId)), deviceConnection, scopeId, userId);

        Context loginUpdateDeviceConnectionTimeContext = loginMetric.getUpdateDeviceConnectionTime().time();
        deviceConnection = deviceConnection!=null ? updateDeviceConnection(authContext, deviceConnection) : createDeviceConnection(authContext);
        if (deviceConnection!=null && deviceConnection.getId()!=null) {
            authContext.setKapuaConnectionId(deviceConnection.getId());
        }
        loginUpdateDeviceConnectionTimeContext.stop();

        List<AuthAcl> authorizationEntries = buildAuthorizationMap(userPermissions, authContext);
        loginNormalUserTimeContext.stop();

        return authorizationEntries;
    }

    @Override
    public boolean disconnect(AuthContext authContext) {
        boolean deviceOwnedByTheCurrentNode = true;
        if (!authContext.isStealingLink() && !authContext.isIllegalState()) {
            // update device connection (if the disconnection wasn't caused by a stealing link)
            DeviceConnection deviceConnection = getDeviceConnection(authContext);
            deviceOwnedByTheCurrentNode = isDeviceOwnedByTheCurrentNode(authContext, deviceConnection);
            if(deviceOwnedByTheCurrentNode) {
                //update status only if the old status wasn't missing
                if (DeviceConnectionStatus.MISSING.equals(deviceConnection.getStatus())) {
                    logger.warn("Skipping device status update for device {} since last status was MISSING!", deviceConnection.getClientId());
                }
                else {
                    deviceConnection.setStatus(!authContext.isMissing() ? DeviceConnectionStatus.DISCONNECTED : DeviceConnectionStatus.MISSING);
                    try {
                        KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.update(deviceConnection));
                    } catch (Exception e) {
                        throw new ShiroException("Error while updating the device connection status!", e);
                    }
                }
            }
        }
        return !authContext.isStealingLink() && deviceOwnedByTheCurrentNode && !authContext.isMissing();
    }

    @Override
    protected List<AuthAcl> buildAuthorizationMap(UserPermissions userPermissions, AuthContext authContext) {
        ArrayList<AuthAcl> ael = new ArrayList<>();
        StringBuilder aclDestinationsLog = new StringBuilder();
//        ael.add(createAuthorizationEntry(authResponse, Action.writeAdmin, aclAdvisory, aclDestinations));

        // addConnection checks BROKER_CONNECT_IDX permission before call this method
        // then here user has BROKER_CONNECT_IDX permission and if check isn't needed
        // if (hasPermissions[BROKER_CONNECT_IDX]) {
        if (userPermissions.isDeviceManage()) {
            ael.add(createAuthorizationEntry(Action.all, formatAcl(aclCtrlAcc, authContext), aclDestinationsLog));
        } else {
            ael.add(createAuthorizationEntry(Action.all, formatAclFull(aclCtrlAccCli, authContext), aclDestinationsLog));
        }
        if (userPermissions.isDataManage()) {
            ael.add(createAuthorizationEntry(Action.all, formatAcl(aclDataAcc, authContext), aclDestinationsLog));
        } else if (userPermissions.isDataView()) {
            ael.add(createAuthorizationEntry(Action.readAdmin, formatAcl(aclDataAcc, authContext), aclDestinationsLog));
            ael.add(createAuthorizationEntry(Action.write, formatAclFull(aclDataAccCli, authContext), aclDestinationsLog));
        } else {
            ael.add(createAuthorizationEntry(Action.all, formatAclFull(aclDataAccCli, authContext), aclDestinationsLog));
        }
        ael.add(createAuthorizationEntry(Action.writeAdmin, formatAcl(aclCtrlAccReply, authContext), aclDestinationsLog));

        // Write notify to any client Id and any application and operation
        ael.add(createAuthorizationEntry(Action.write, formatAclFull(aclCtrlAccNotify, authContext), aclDestinationsLog));

        logger.info("{}", aclDestinationsLog);

        return ael;
    }

    protected UserPermissions updatePermissions(AuthContext authContext) throws KapuaException {
        List<Permission> permissions = new ArrayList<>();
        KapuaId scopeId = KapuaEid.parseCompactId(authContext.getScopeId());
        permissions.add(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, scopeId));
        permissions.add(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));
        permissions.add(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));
        permissions.add(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.read, scopeId));
        permissions.add(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.write, scopeId));
        UserPermissions userPermissions = new UserPermissions(authorizationService.isPermitted(permissions));

        if (!userPermissions.isBrokerConnect()) {
            throw new KapuaIllegalAccessException(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, scopeId).toString());
        }
        return userPermissions;
    }

}
