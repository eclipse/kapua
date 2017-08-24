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

import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssets;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("asset")
public interface GwtDeviceAssetService extends RemoteService {

    public List<GwtDeviceAsset> read(String scopeId, String deviceId, GwtDeviceAssets deviceAssets)
            throws GwtKapuaException;

    public void write(GwtXSRFToken xsrfToken, String scopeIdString, String deviceIdString, GwtDeviceAsset deviceAsset)
            throws GwtKapuaException;

    public List<GwtDeviceAsset> get(PagingLoadConfig pagingLoadConfig, String scopeIdString, String deviceIdString, GwtDeviceAssets deviceAssets)
            throws GwtKapuaException;

    public GwtDeviceAssetChannel trickGWT(GwtDeviceAssetChannel channel);

    // PagingLoadResult<GwtUser> query(PagingLoadConfig pagingLoadConfig, GwtAccessRoleQuery query) throws GwtKapuaException;
}
