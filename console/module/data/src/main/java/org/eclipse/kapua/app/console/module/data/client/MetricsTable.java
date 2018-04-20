/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
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
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGridCheckBoxSelectionModel;
import org.eclipse.kapua.app.console.module.api.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class MetricsTable extends LayoutContainer {

    public enum Type {
        TOPIC, DEVICE, ASSET
    }

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);
    private static final GwtDataServiceAsync DATA_SERVICE = GWT.create(GwtDataService.class);

    private BaseListLoader<ListLoadResult<GwtHeader>> loader;
    private GwtSession currentSession;
    private Grid<GwtHeader> metricsInfoGrid;
    private ContentPanel tableContainer;
    private List<SelectionChangedListener<GwtHeader>> listeners = new ArrayList<SelectionChangedListener<GwtHeader>>();
    private GwtTopic selectedTopic;
    private GwtDatastoreDevice selectedDevice;
    private GwtDatastoreAsset selectedAsset;
    private Type type;

    public MetricsTable(GwtSession currentSession, Type type) {
        this.currentSession = currentSession;
        this.type = type;
    }

    public List<GwtHeader> getSelectedMetrics() {
        return metricsInfoGrid.getSelectionModel().getSelectedItems();
    }

    @Override
    protected void onRender(Element parent, int index) {
        initMetricsTable();

        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        add(tableContainer);
    }

    private void initMetricsTable() {
        initChannelInfoGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBodyBorder(true);
        tableContainer.setHeaderVisible(true);
        switch (type) {
        case TOPIC:
            tableContainer.setHeading(MSGS.metricsTableHeaderTopic(""));
            break;
        case DEVICE:
            tableContainer.setHeading(MSGS.metricsTableHeaderDevice(""));
            break;

        case ASSET:
            tableContainer.setHeading(MSGS.metricsTableHeaderAssets());
            break;
        default:
            break;
        }
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(metricsInfoGrid);
    }

    private void initChannelInfoGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        CheckBoxSelectionModel<GwtHeader> selectionModel = new EntityGridCheckBoxSelectionModel<GwtHeader>();
        configs.add(selectionModel.getColumn());
        ColumnConfig column;
        if (type == Type.ASSET) {
            column = new ColumnConfig("name", MSGS.metricsTableMetricHeader(), 100);
        } else {
            column = new ColumnConfig("name", MSGS.metricsTableMetricHeader(), 100);
        }
        configs.add(column);
        if (type == Type.ASSET) {
            column = new ColumnConfig("type", MSGS.metricsTableChannelTypeHeader(), 100);
        } else {
            column = new ColumnConfig("type", MSGS.metricsTableMetricTypeHeader(), 100);
        }
        configs.add(column);

        RpcProxy<ListLoadResult<GwtHeader>> proxy = new RpcProxy<ListLoadResult<GwtHeader>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtHeader>> callback) {
                if (selectedTopic != null) {
                    DATA_SERVICE.findHeaders((LoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedTopic, callback);
                } else if (selectedDevice != null) {
                    DATA_SERVICE.findHeaders((LoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedDevice, callback);
                } else if (selectedAsset != null) {
                    DATA_SERVICE.findHeaders((LoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedAsset, callback);
                } else {
                    callback.onSuccess(new BaseListLoadResult<GwtHeader>(new ArrayList<GwtHeader>()));
                }
            }
        };

        loader = new BaseListLoader<ListLoadResult<GwtHeader>>(proxy);
        loader.load();

        SwappableListStore<GwtHeader> store = new SwappableListStore<GwtHeader>(loader);
        metricsInfoGrid = new Grid<GwtHeader>(store, new ColumnModel(configs));
        metricsInfoGrid.setBorders(false);
        metricsInfoGrid.setStateful(false);
        metricsInfoGrid.setLoadMask(true);
        metricsInfoGrid.setStripeRows(true);
        metricsInfoGrid.getView().setAutoFill(true);
        metricsInfoGrid.getView().setForceFit(true);
        metricsInfoGrid.getView().setEmptyText(MSGS.metricsTableEmptyText());
        metricsInfoGrid.disableTextSelection(false);
        for (SelectionChangedListener<GwtHeader> listener : listeners) {
            selectionModel.addSelectionChangedListener(listener);
        }
        selectionModel.setSelectionMode(Style.SelectionMode.SIMPLE);
        metricsInfoGrid.addPlugin(selectionModel);
        metricsInfoGrid.setSelectionModel(selectionModel);
    }

    public void clearTable() {
        metricsInfoGrid.getStore().removeAll();
    }

    public void refreshChannels() {
        metricsInfoGrid.getStore().removeAll();
    }

    public void refresh() {
        metricsInfoGrid.getStore().getLoader().load();
    }

    public void refresh(GwtTopic selectedTopic) {
        if (selectedTopic != null) {
            tableContainer.setHeading(MSGS.metricsTableHeaderTopic(selectedTopic.getSemanticTopic()));
            this.selectedTopic = selectedTopic;
        } else {
            tableContainer.setHeading(MSGS.metricsTableHeaderTopic(""));
            this.selectedTopic = null;
        }
        refresh();
    }

    public void refresh(GwtDatastoreDevice selectedDevice) {
        if (selectedDevice != null) {
            tableContainer.getHeader().setStyleAttribute("white-space", "nowrap");
            tableContainer.getHeader().setStyleAttribute("text-overflow", "ellipsis");
            tableContainer.getHeader().setStyleAttribute("overflow", "hidden");
            tableContainer.setHeading(MSGS.metricsTableHeaderDevice(selectedDevice.getDevice()));
            if (selectedAsset != null) {
                tableContainer.setHeading(MSGS.metricsTableHeaderAssets());
            }
            this.selectedDevice = selectedDevice;
        } else {
            tableContainer.setHeading(MSGS.metricsTableHeaderDevice(""));
            this.selectedDevice = null;
        }
        refresh();
    }

    public void refresh(GwtDatastoreAsset selectedAsset) {
        if (selectedAsset != null) {
            tableContainer.setHeading(MSGS.metricsTableHeaderAssets());
            this.selectedAsset = selectedAsset;
        } else {
            tableContainer.setHeading(MSGS.metricsTableHeaderAssets());
            this.selectedAsset = null;
        }
        refresh();
    }

    public void addSelectionListener(SelectionChangedListener<GwtHeader> listener) {
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
