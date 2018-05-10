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
package org.eclipse.kapua.app.console.module.data.shared.service;

import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.data.client.GwtTopic;
import org.eclipse.kapua.app.console.module.data.client.util.GwtMessage;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDataChannelInfoQuery;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;

@RemoteServiceRelativePath("data")
public interface GwtDataService extends RemoteService {

    /**
     * Return the Topics for a given account; the returned structure is ready to
     * be fed into the TreeGrid UI widget, so it contains all the topic children.
     *
     * @param scopeId
     * @return
     * @throws GwtKapuaException
     */
    List<GwtTopic> findTopicsTree(String scopeId) throws GwtKapuaException;

    List<GwtTopic> updateTopicTimestamps(String gwtScopeId, List<ModelData> topics)
            throws GwtKapuaException;

    List<GwtDatastoreDevice> updateDeviceTimestamps(String gwtScopeId, List<GwtDatastoreDevice> device)
            throws GwtKapuaException;

    PagingLoadResult<GwtTopic> findTopicsList(PagingLoadConfig config, GwtDataChannelInfoQuery query) throws GwtKapuaException;

    /**
     * @param config
     * @param scopeId
     * @return
     * @throws GwtKapuaException
     */
    PagingLoadResult<GwtDatastoreDevice> findDevices(PagingLoadConfig config, String scopeId, String filter) throws GwtKapuaException;

    ListLoadResult<GwtDatastoreAsset> findAssets(LoadConfig config, String scopeId, GwtDatastoreDevice selectedDevice) throws GwtKapuaException;

    /**
     * Return the Headers for a given account/topic pair.
     * The returned structure is ready to be fed into the Grid UI widget through a loader.
     *
     * @param config
     * @param scopeId
     * @param topic
     * @return
     * @throws GwtKapuaException
     */
    ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, GwtTopic topic) throws GwtKapuaException;

    /**
     * Return the number Headers (int, long, float, double and so on. Not String, Boolean, Byte) for a given account/topic pair.
     * The returned structure is ready to be fed into the Grid UI widget through a loader.
     *
     * @param config
     * @param scopeId
     * @param topic
     * @return
     * @throws GwtKapuaException
     */
    ListLoadResult<GwtHeader> findNumberHeaders(LoadConfig config, String scopeId, GwtTopic topic) throws GwtKapuaException;

    /**
     * Return the Headers for a given account/topic pair.
     * The returned structure is ready to be fed into the Grid UI widget through a loader.
     *
     * @param config
     * @param scopeId
     * @param device
     * @return
     * @throws GwtKapuaException
     */
    ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, GwtDatastoreDevice device) throws GwtKapuaException;

    PagingLoadResult<GwtMessage> findMessagesByTopic(PagingLoadConfig loadConfig, String scopeId, GwtTopic topic, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException;

    List<GwtMessage> findLastMessageByTopic(String scopeId, int limit) throws GwtKapuaException;

    PagingLoadResult<GwtMessage> findMessagesByDevice(PagingLoadConfig loadConfig, String scopeId, GwtDatastoreDevice device, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException;

    PagingLoadResult<GwtMessage> findMessagesByAssets(PagingLoadConfig loadConfig, String scopeId, GwtDatastoreAsset asset, List<GwtHeader> headers, Date startDate, Date endDate) throws GwtKapuaException;

    ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, GwtDatastoreAsset gwtDatastoreAsset) throws GwtKapuaException;

}
