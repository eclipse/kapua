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
package org.eclipse.kapua.app.console.module.device.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionService;
import org.eclipse.kapua.app.console.module.device.shared.util.GwtKapuaDeviceModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.util.KapuaGwtDeviceModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.user.User;
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
        UserService userService = locator.getService(UserService.class);

        try {
            deviceConnections = deviceConnectionService.query(query);
            if (deviceConnections.getSize() >= loadConfig.getLimit()) {
                totalLength = Long.valueOf(deviceConnectionService.count(query)).intValue();
            } else {
                totalLength = deviceConnections.getSize();
            }

            for (DeviceConnection dc : deviceConnections.getItems()) {
                gwtDeviceConnections.add(KapuaGwtDeviceModelConverter.convertDeviceConnection(dc));
                for (GwtDeviceConnection gwtDeviceConnection : gwtDeviceConnections) {
                    User user = userService.find(dc.getScopeId(), dc.getUserId());
                    if (user != null) {
                        gwtDeviceConnection.setUserName(user.getName());
                    }
                }
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
        KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionService deviceConnectionService = locator.getService(DeviceConnectionService.class);
        UserService userService = locator.getService(UserService.class);

        List<GwtGroupedNVPair> deviceConnectionPropertiesPairs = new ArrayList<GwtGroupedNVPair>();
        try {
            DeviceConnection deviceConnection = deviceConnectionService.find(scopeId, deviceConnectionId);
            User user = userService.find(scopeId, deviceConnection.getUserId());

            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Information", "Connection Status", deviceConnection.getStatus().toString()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Information", "Modified On", deviceConnection.getModifiedOn().toString()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Information", "Modified By", deviceConnection.getModifiedBy().toCompactId()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Information", "Protocol", deviceConnection.getProtocol()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Information", "Client ID", deviceConnection.getClientId()));

            if (user != null) {
                deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Information", "User", user.getName()));
            } else {
                deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Information", "User", "N/A"));
            }

            GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = null;
            if (deviceConnection.getUserCouplingMode() != null) {
                gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.valueOf(deviceConnection.getUserCouplingMode().name());
            }
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection-user bound Informations", "Connection-user bound", gwtConnectionUserCouplingMode.getLabel()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection-user bound Informations", "Reserved User Id",
            deviceConnection.getReservedUserId() != null ? deviceConnection.getReservedUserId().toCompactId() : "N/A"));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection-user bound Informations", "Allow user change", deviceConnection.getAllowUserChange()));

            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Informations", "Client Id", deviceConnection.getClientId()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Informations", "User Id", deviceConnection.getUserId().toCompactId()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Informations", "Client IP", deviceConnection.getClientIp()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Informations", "Server IP", deviceConnection.getServerIp()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Informations", "Protocol", deviceConnection.getProtocol()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("Connection Informations", "Connection Status", deviceConnection.getStatus().toString()));
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(deviceConnectionPropertiesPairs);
    }

}
