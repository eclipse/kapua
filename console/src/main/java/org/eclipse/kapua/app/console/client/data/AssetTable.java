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
package org.eclipse.kapua.app.console.client.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.shared.service.GwtDataServiceAsync;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssetTable extends LayoutContainer {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);

    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);

    private BaseListLoader<ListLoadResult<GwtDatastoreAsset>> loader;
    private GwtSession currentSession;
    private Grid<GwtDatastoreAsset> assetGrid;
    private ContentPanel tableContainer;
    private List<SelectionChangedListener<GwtDatastoreAsset>> listeners = new ArrayList<SelectionChangedListener<GwtDatastoreAsset>>();

    private GwtDatastoreDevice selectedDevice;

    public AssetTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    public GwtDatastoreAsset getSelectedAsset() {
        if (assetGrid != null) {
            return assetGrid.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(true);

        initAssetTable();
        add(tableContainer);

    }

    private void initAssetTable() {
        initAssetGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(MSGS.assetTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(assetGrid);
    }

    private void initAssetGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("asset", MSGS.assetTableAssetHeader(), 100);
        configs.add(column);
        column = new ColumnConfig("driver", MSGS.assetTableDriverHeader(), 100);
        configs.add(column);
        RpcProxy<ListLoadResult<GwtDatastoreAsset>> proxy = new RpcProxy<ListLoadResult<GwtDatastoreAsset>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtDatastoreAsset>> callback) {
                if (selectedDevice != null) {
                    //TODO Fetch available assets.
                } else {
                    callback.onSuccess(new BaseListLoadResult<GwtDatastoreAsset>(new ArrayList<GwtDatastoreAsset>()));
                }
            }
        };

        loader = new BaseListLoader<ListLoadResult<GwtDatastoreAsset>>(proxy);
        loader.load();
        SwappableListStore<GwtDatastoreAsset> store = new SwappableListStore<GwtDatastoreAsset>(loader);
        assetGrid = new Grid<GwtDatastoreAsset>(store, new ColumnModel(configs));
        assetGrid.setBorders(false);
        assetGrid.setStateful(false);
        assetGrid.setLoadMask(true);
        assetGrid.setStripeRows(true);
        assetGrid.getView().setAutoFill(true);
        assetGrid.getView().setEmptyText(MSGS.assetTableEmptyText());
        assetGrid.disableTextSelection(false);
        for (SelectionChangedListener<GwtDatastoreAsset> listener : listeners) {
            assetGrid.getSelectionModel().addSelectionChangedListener(listener);
        }
    }
    
    private void refresh(){
        assetGrid.getStore().getLoader().load();
    }
    
    public void refresh(GwtDatastoreDevice selectedDevice){
        this.selectedDevice = selectedDevice;
        refresh();
    }
    
    public void addSelectionChangedListener(SelectionChangedListener<GwtDatastoreAsset> listener) {
        listeners.add(listener);
    }

    public void onUnload() {
        super.onUnload();
    }
}
