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

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceCreator;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("device")
public interface GwtDeviceService extends RemoteService {

    /**
     * Finds device given its clientId
     * 
     * @param scopeIdString
     * @param clientId
     * @return
     */
    public GwtDevice findDevice(String scopeIdString, String clientId)
            throws GwtKapuaException;

    /**
     * Finds devices in an account with query
     * 
     * @param loadConfig
     * @param gwtDeviceQuery
     * @return
     */
    public PagingLoadResult<GwtDevice> query(PagingLoadConfig loadConfig, GwtDeviceQuery gwtDeviceQuery)
            throws GwtKapuaException;

    /**
     * Finds devices in an account with query
     *
     * @param gwtDeviceQuery
     * @return
     */
    public List<GwtDevice> query(GwtDeviceQuery gwtDeviceQuery)
            throws GwtKapuaException;

    /**
     * Creates a device entry for the account
     * 
     * @param xsrfToken
     * @param gwtDeviceCreator
     * @return
     * @throws GwtKapuaException
     */
    public GwtDevice createDevice(GwtXSRFToken xsrfToken, GwtDeviceCreator gwtDeviceCreator)
            throws GwtKapuaException;

    /**
     * Updates a device entity with the given gwtDevice new values for custom attributes, display name and associated tag
     * 
     * @param gwtDevice
     * @return
     * @throws GwtKapuaException
     */
    public GwtDevice updateAttributes(GwtXSRFToken xsfrToken, GwtDevice gwtDevice)
            throws GwtKapuaException;

    /**
     * Returns a list of device history events for a specified device within a specified date range.
     * 
     * @param loadConfig
     * @param gwtDevice
     *            the device to return the history of
     * @param startDate
     *            the start of the date range in milliseconds since epoch (Date.getTime())
     * @param endDate
     *            the end of the date range in milliseconds since epoch (Date.getTime())
     * @return
     * @throws GwtKapuaException
     */
    public PagingLoadResult<GwtDeviceEvent> findDeviceEvents(PagingLoadConfig loadConfig, GwtDevice gwtDevice, Date startDate, Date endDate)
            throws GwtKapuaException;

    public ListLoadResult<GwtGroupedNVPair> findDeviceProfile(String scopeIdString, String clientId)
            throws GwtKapuaException;

    /**
     * Deletes a device
     * 
     * @param xsfrToken
     * @param scopeIdString
     * @param clientId
     */
    public void deleteDevice(GwtXSRFToken xsfrToken, String scopeIdString, String clientId)
            throws GwtKapuaException;

    /**
     * Return Maps Tile Endpoint
     */
    public String getTileEndpoint()
            throws GwtKapuaException;

    /**
     * Returns whether the Map tab is enabled or not
     * 
     * @return whether the Map tab is enabled or not
     */
    public boolean isMapEnabled()
            throws GwtKapuaException;
}
