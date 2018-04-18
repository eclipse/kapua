/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionService;
import org.eclipse.kapua.app.console.module.device.shared.util.GwtKapuaDeviceModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.util.KapuaGwtDeviceModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * The server side implementation of the RPC service.
 */
public class GwtDeviceConnectionServiceImpl extends KapuaRemoteServiceServlet implements GwtDeviceConnectionService {

    private static final long serialVersionUID = 3314502846487119577L;

    @Override
    public PagingLoadResult<GwtDeviceConnection> query(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) throws GwtKapuaException {
        KapuaListResult<DeviceConnection> deviceConnections;
        List<GwtDeviceConnection> gwtDeviceConnections = new ArrayList<GwtDeviceConnection>();
        int totalLength = 0;
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionService deviceConnectionService = locator.getService(DeviceConnectionService.class);
        DeviceConnectionQuery query = GwtKapuaDeviceModelConverter.convertConnectionQuery(loadConfig, gwtDeviceConnectionQuery);
        final UserService userService = locator.getService(UserService.class);
        final UserFactory userFactory = locator.getFactory(UserFactory.class);
        Map<String, String> users = new HashMap<String, String>();
        final UserQuery userQuery = userFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceConnectionQuery.getScopeId()));
        try {
            UserListResult userList = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                @Override
                public UserListResult call() throws Exception {
                    return userService.query(userQuery);
                }
            });
            for (User user : userList.getItems()) {
                users.put(user.getId().toCompactId(), user.getName());
            }
            deviceConnections = deviceConnectionService.query(query);
            totalLength = Long.valueOf(deviceConnectionService.count(query)).intValue();
            for (DeviceConnection dc : deviceConnections.getItems()) {
                GwtDeviceConnection gwtDeviceConnection = KapuaGwtDeviceModelConverter.convertDeviceConnection(dc);
                if (dc.getUserId() != null) {
                    gwtDeviceConnection.setUserName(users.get(dc.getUserId().toCompactId()));
                }
                if (dc.getReservedUserId() != null) {
                    gwtDeviceConnection.setReservedUserName(users.get(dc.getReservedUserId().toCompactId()));
                }
                gwtDeviceConnections.add(gwtDeviceConnection);
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtDeviceConnection>(gwtDeviceConnections, loadConfig.getOffset(), totalLength);
    }

    @Override
    public GwtDeviceConnection find(String scopeIdString, String deviceConnectionIdString) throws GwtKapuaException {
        KapuaId deviceConnectionId = KapuaEid.parseCompactId(deviceConnectionIdString);
        KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);

        GwtDeviceConnection gwtDeviceConnection = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceConnectionService deviceConnectionService = locator.getService(DeviceConnectionService.class);
            gwtDeviceConnection = KapuaGwtDeviceModelConverter.convertDeviceConnection(deviceConnectionService.find(scopeId, deviceConnectionId));
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtDeviceConnection;
    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getConnectionInfo(String scopeIdString, String gwtDeviceConnectionId) throws GwtKapuaException {
        KapuaId deviceConnectionId = KapuaEid.parseCompactId(gwtDeviceConnectionId);
        final KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionService deviceConnectionService = locator.getService(DeviceConnectionService.class);
        final UserService userService = locator.getService(UserService.class);

        List<GwtGroupedNVPair> deviceConnectionPropertiesPairs = new ArrayList<GwtGroupedNVPair>();
        try {
            final DeviceConnection deviceConnection = deviceConnectionService.find(scopeId, deviceConnectionId);
            User connectionUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, deviceConnection.getUserId());
                }
            });
            User createdUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, deviceConnection.getCreatedBy());
                }
            });
            User modifiedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, deviceConnection.getModifiedBy());
                }
            });
            User reservedUser;
            if (deviceConnection.getReservedUserId() != null) {
                reservedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                    @Override
                    public User call() throws Exception {
                        return userService.find(scopeId, deviceConnection.getReservedUserId());
                    }
                });
            } else {
                reservedUser = null;
            }
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionStatus", deviceConnection.getStatus().toString()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionModifiedOn", KapuaDateUtils.formatDateTime(deviceConnection.getModifiedOn())));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionModifiedBy", modifiedUser != null ? modifiedUser.getName() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionProtocol", deviceConnection.getProtocol()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionClientId", deviceConnection.getClientId()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionUser", connectionUser != null ? connectionUser.getName() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionClientIp", deviceConnection.getClientIp()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionServerIp", deviceConnection.getServerIp()));
            GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = null;
            if (deviceConnection.getUserCouplingMode() != null) {
                gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.valueOf(deviceConnection.getUserCouplingMode().name());
            }
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionUserCouplingModeInfo", "connectionUserCouplingMode", gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionUserCouplingModeInfo", "connectionReservedUser", reservedUser != null ? reservedUser.getName() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionUserCouplingModeInfo", "allowUserChange", deviceConnection.getAllowUserChange()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionFirstEstablishedOn", KapuaDateUtils.formatDateTime(deviceConnection.getCreatedOn())));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionFirstEstablishedBy", createdUser != null ? createdUser.getName() : null));

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(deviceConnectionPropertiesPairs);
    }

}
