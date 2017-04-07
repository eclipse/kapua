/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtAsset;
import org.eclipse.kapua.app.console.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.shared.model.GwtKapuaChartResult;
import org.eclipse.kapua.app.console.shared.model.GwtMessage;
import org.eclipse.kapua.app.console.shared.model.GwtTopic;
import org.eclipse.kapua.app.console.shared.model.KapuaBasePagingCursor;
import org.eclipse.kapua.app.console.shared.model.data.GwtDataChannelInfoQuery;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


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
    public ListLoadResult<GwtAsset> findAssets(LoadConfig config, String accountName) throws GwtKapuaException;


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
     * @param asset
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String accountName, GwtAsset asset) throws GwtKapuaException;


    public PagingLoadResult<GwtMessage> findMessagesByTopic(PagingLoadConfig loadConfig, String accountName, GwtTopic topic, List<GwtHeader> headers, Date startDate, Date endDate) throws GwtKapuaException;

    public List<GwtMessage> findLastMessageByTopic(String accountName, int limit) throws GwtKapuaException;

    public GwtKapuaChartResult findMessagesByTopic(String accountName, GwtTopic topic, List<GwtHeader> metrics, Date startDate, Date endDate) throws GwtKapuaException;

    public GwtKapuaChartResult findMessagesByTopic(String accountName, GwtTopic topic,
            List<GwtHeader> headers, Date startDate, Date endDate, Stack<KapuaBasePagingCursor> cursors, int limit, int lastOffset, Integer indexOffset) throws GwtKapuaException;

    public PagingLoadResult<GwtMessage> findMessagesByAsset(PagingLoadConfig loadConfig, String accountName, GwtAsset asset, List<GwtHeader> headers, Date startDate, Date endDate) throws GwtKapuaException;

    public GwtKapuaChartResult findMessagesByAsset(String accountName, GwtAsset asset,
            List<GwtHeader> headers, Date startDate, Date endDate, Stack<KapuaBasePagingCursor> cursors, int limit, int lastOffset, Integer indexOffset) throws GwtKapuaException;

}
