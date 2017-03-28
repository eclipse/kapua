/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.shared.service;

import org.eclipse.kapua.app.console.client.device.deviceGroup.GwtDeviceGroupCreator;
import org.eclipse.kapua.app.console.client.device.deviceGroup.GwtDeviceGroupQuery;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceGroup;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("devicegroup")
public interface GwtDeviceGroupService extends RemoteService {

    public GwtDeviceGroup create(GwtDeviceGroupCreator gwtDeviceGroupCreator)
            throws GwtKapuaException;

    public void delete(String id, String deviceGroupId) throws GwtKapuaException;

    public GwtDeviceGroup find(String id, String devGroupId) throws GwtKapuaException;

    public PagingLoadResult<GwtDeviceGroup> query(PagingLoadConfig loadConfig,
            GwtDeviceGroupQuery gwtDeviceGroupQuery) throws GwtKapuaException;

    public PagingLoadResult<GwtDeviceGroup> findByDeviceId(PagingLoadConfig loadConfig,
            String scopeShortId, String deviceShortId) throws GwtKapuaException;

}
