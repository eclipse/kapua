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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreChannel;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;

import java.util.ArrayList;
import java.util.List;

public class ChannelTable extends LayoutContainer {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);

    private BaseListLoader<ListLoadResult<GwtDatastoreChannel>> loader;
    private GwtSession currentSession;
    private Grid<GwtDatastoreChannel> channelGrid;
    private ContentPanel tableContainer;
    private List<SelectionChangedListener<GwtDatastoreChannel>> listeners = new ArrayList<SelectionChangedListener<GwtDatastoreChannel>>();
    private GwtDatastoreAsset selectedAsset;
    private GwtDatastoreDevice selectedDevice;

    public ChannelTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    public List<GwtDatastoreChannel> getSelectedChannels() {
        return channelGrid.getSelectionModel().getSelectedItems();
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(true);

        initChannelTable();
        add(tableContainer);
    }

    private void initChannelTable() {
        initChannelGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(MSGS.channelTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(channelGrid);
    }

    private void initChannelGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        CheckBoxSelectionModel<GwtDatastoreChannel> selectionModel = new CheckBoxSelectionModel<GwtDatastoreChannel>();
        configs.add(selectionModel.getColumn());
        ColumnConfig column = new ColumnConfig("channel", MSGS.channelTableChannelHeader(), 100);
        configs.add(column);
        column = new ColumnConfig("type", MSGS.channelTableTypeHeader(), 100);
        configs.add(column);
        RpcProxy<ListLoadResult<GwtDatastoreChannel>> proxy = new RpcProxy<ListLoadResult<GwtDatastoreChannel>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtDatastoreChannel>> callback) {
                if (selectedDevice != null && selectedAsset != null) {
                    // TODO fetch available channels.
                } else {
                    callback.onSuccess(new BaseListLoadResult<GwtDatastoreChannel>(new ArrayList<GwtDatastoreChannel>()));
                }
            }
        };
        loader = new BaseListLoader<ListLoadResult<GwtDatastoreChannel>>(proxy);
        loader.load();
        SwappableListStore<GwtDatastoreChannel> store = new SwappableListStore<GwtDatastoreChannel>(loader);
        channelGrid = new Grid<GwtDatastoreChannel>(store, new ColumnModel(configs));
        channelGrid.setBorders(false);
        channelGrid.setStateful(false);
        channelGrid.setLoadMask(true);
        channelGrid.setStripeRows(true);
        channelGrid.getView().setAutoFill(true);
        channelGrid.getView().setForceFit(true);
        channelGrid.getView().setEmptyText(MSGS.channelTableEmptyText());
        channelGrid.disableTextSelection(false);
        for (SelectionChangedListener<GwtDatastoreChannel> listener : listeners) {
            selectionModel.addSelectionChangedListener(listener);
        }
        channelGrid.addPlugin(selectionModel);
        channelGrid.setSelectionModel(selectionModel);
    }

    private void refresh() {
        channelGrid.getStore().getLoader().load();
    }

    public void refresh(GwtDatastoreDevice selectedDevice, GwtDatastoreAsset selectedAsset) {
        this.selectedDevice = selectedDevice;
        this.selectedAsset = selectedAsset;
        refresh();
    }

    public void addSelectionListener(SelectionChangedListener<GwtDatastoreChannel> listener) {
        listeners.add(listener);
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------
    @Override
    public void onUnload() {
        super.onUnload();
    }
}
