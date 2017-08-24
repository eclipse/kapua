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
     * @param accountName
     * @return
     * @throws GwtKapuaException
     */
    public List<GwtTopic> findTopicsTree(String accountName) throws GwtKapuaException;

    PagingLoadResult<GwtTopic> findTopicsList(PagingLoadConfig config, GwtDataChannelInfoQuery query) throws GwtKapuaException;

    /**
     *
     * @param config
     * @param accountName
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtDatastoreDevice> findDevices(LoadConfig config, String accountName) throws GwtKapuaException;

    public ListLoadResult<GwtDatastoreAsset> findAssets(LoadConfig config, String accountName, GwtDatastoreDevice selectedDevice) throws GwtKapuaException;

    /**
     * Return the Headers for a given account/topic pair.
     * The returned structure is ready to be fed into the Grid UI widget through a loader.
     *
     * @param config
     * @param accountName
     * @param topic
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String accountName, GwtTopic topic) throws GwtKapuaException;

    /**
     * Return the number Headers (int, long, float, double and so on. Not String, Boolean, Byte) for a given account/topic pair.
     * The returned structure is ready to be fed into the Grid UI widget through a loader.
     *
     * @param config
     * @param accountName
     * @param topic
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtHeader> findNumberHeaders(LoadConfig config, String accountName, GwtTopic topic) throws GwtKapuaException;

    /**
     * Return the Headers for a given account/topic pair.
     * The returned structure is ready to be fed into the Grid UI widget through a loader.
     *
     * @param config
     * @param accountName
     * @param device
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String accountName, GwtDatastoreDevice device) throws GwtKapuaException;

    public PagingLoadResult<GwtMessage> findMessagesByTopic(PagingLoadConfig loadConfig, String accountName, GwtTopic topic, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException;

    public List<GwtMessage> findLastMessageByTopic(String accountName, int limit) throws GwtKapuaException;

    public PagingLoadResult<GwtMessage> findMessagesByDevice(PagingLoadConfig loadConfig, String accountName, GwtDatastoreDevice device, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException;

    public PagingLoadResult<GwtMessage> findMessagesByAssets(PagingLoadConfig loadConfig, String accountName, GwtDatastoreAsset asset, List<GwtHeader> headers, Date startDate, Date endDate) throws GwtKapuaException;

    ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String accountName, GwtDatastoreAsset gwtDatastoreAsset) throws GwtKapuaException;

}
