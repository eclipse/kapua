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
package org.eclipse.kapua.app.console.module.device.shared.service;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deviceConnection")
public interface GwtDeviceConnectionService extends RemoteService {

    /**
     * Returns a GwtDeviceConnection by its Id or null if a device connection with such Id does not exist.
     * 
     * @param connectionId
     * @return
     */
    public GwtDeviceConnection find(String scopeId, String connectionId)
        throws GwtKapuaException;

    /**
     * Get connection info ad name values pairs
     * 
     * @param gwtDeviceConnectionId
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtGroupedNVPair> getConnectionInfo(String scopeIdString, String gwtDeviceConnectionId)
        throws GwtKapuaException;

    /**
     * Returns the list of all DeviceConnection matching the query.
     * 
     * @param gwtDeviceConnectionQuery
     * @return
     * @throws GwtKapuaException
     * 
     */
    PagingLoadResult<GwtDeviceConnection> query(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) 
            throws GwtKapuaException;
}
