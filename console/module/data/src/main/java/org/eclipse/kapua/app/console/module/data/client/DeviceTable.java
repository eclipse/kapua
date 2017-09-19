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

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
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
import org.eclipse.kapua.app.console.module.api.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

public class DeviceTable extends LayoutContainer {

    // private static final int DEVICE_PAGE_SIZE = 50;
    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);

    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);

    private BaseListLoader<ListLoadResult<GwtDatastoreDevice>> loader;
    private GwtSession currentSession;
    private Grid<GwtDatastoreDevice> deviceGrid;
    private ContentPanel tableContainer;
    private List<SelectionChangedListener<GwtDatastoreDevice>> listeners = new ArrayList<SelectionChangedListener<GwtDatastoreDevice>>();
    // private PagingToolBar pagingToolBar;

    public DeviceTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    public GwtDatastoreDevice getSelectedDevice() {
        if (deviceGrid != null) {
            return deviceGrid.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        initDeviceTable();
        add(tableContainer);

    }

    private void initDeviceTable() {
        initDeviceGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(true);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(MSGS.deviceInfoTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(deviceGrid);
        // tableContainer.setBottomComponent(pagingToolBar);
    }

    private void initDeviceGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("device", MSGS.deviceInfoTableTopicHeader(), 150);
        configs.add(column);

        column = new ColumnConfig("timestamp", MSGS.deviceInfoTableLastPostedHeader(), 150);
        configs.add(column);
        RpcProxy<ListLoadResult<GwtDatastoreDevice>> proxy = new RpcProxy<ListLoadResult<GwtDatastoreDevice>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<ListLoadResult<GwtDatastoreDevice>> callback) {
                dataService.findDevices((LoadConfig) loadConfig, currentSession.getSelectedAccountId(), callback);
            }
        };

        loader = new BaseListLoader<ListLoadResult<GwtDatastoreDevice>>(proxy);
        loader.load();
        //
        SwappableListStore<GwtDatastoreDevice> store = new SwappableListStore<GwtDatastoreDevice>(loader);
        deviceGrid = new Grid<GwtDatastoreDevice>(store, new ColumnModel(configs));
        deviceGrid.setBorders(false);
        deviceGrid.setStateful(false);
        deviceGrid.setLoadMask(true);
        deviceGrid.setStripeRows(true);
        deviceGrid.getView().setAutoFill(true);
        deviceGrid.getView().setEmptyText(MSGS.deviceTableEmptyText());
        deviceGrid.disableTextSelection(false);
        for (SelectionChangedListener<GwtDatastoreDevice> listener : listeners) {
            deviceGrid.getSelectionModel().addSelectionChangedListener(listener);
        }

        // pagingToolBar = new PagingToolBar(DEVICE_PAGE_SIZE);
        // pagingToolBar.bind(loader);
        // pagingToolBar.enable();

    }

    public void addSelectionChangedListener(SelectionChangedListener<GwtDatastoreDevice> listener) {
        listeners.add(listener);
    }

    public void onUnload() {
        super.onUnload();
    }

    public void refresh() {
        deviceGrid.getStore().getLoader().load();
    }

}
