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
package org.eclipse.kapua.app.console.module.device.client.device.assets;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.button.ExportButton;
import org.eclipse.kapua.app.console.module.api.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtChannel;

import java.util.ArrayList;
import java.util.List;

public class AssetDetailsTable extends LayoutContainer {

    private GwtSession currentSession;
    private ContentPanel tableContainer;
    private ToolBar toolBar;
    private ExportButton readAll, writeAll;
    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    public AssetDetailsTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    private void initDetailsTable() {
        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(false);
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        initDetailsGrid();
        tableContainer.setTopComponent(toolBar);
    }

    private void initDetailsGrid() {

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("Name", MSGS.assetName(), 150);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);
        ColumnConfig readWrite = new ColumnConfig("Read/Write", MSGS.assetReadWrite(), 150);
        readWrite.setAlignment(HorizontalAlignment.CENTER);
        configs.add(readWrite);
        ColumnConfig value = new ColumnConfig("Value", MSGS.assetValue(), 150);
        value.setAlignment(HorizontalAlignment.CENTER);
        configs.add(value);
        ColumnConfig action = new ColumnConfig("Action", MSGS.assetAction(), 150);
        action.setAlignment(HorizontalAlignment.CENTER);
        configs.add(action);

        RpcProxy<PagingLoadResult<GwtChannel>> proxy = new RpcProxy<PagingLoadResult<GwtChannel>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtChannel>> callback) {
            }
        };
        ListLoader<ListLoadResult<GwtChannel>> loader = new BaseListLoader<ListLoadResult<GwtChannel>>(proxy);
        SwappableListStore<GwtChannel> store = new SwappableListStore<GwtChannel>(loader);
        Grid<GwtChannel> grid = new Grid<GwtChannel>(store, new ColumnModel(configs));
        tableContainer.add(grid);
        toolBar = new ToolBar();
        Label label = new Label(MSGS.assetLabel());
        toolBar.add(label);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(new FillToolItem());
        readAll = new ExportButton();
        readAll.setText(MSGS.assetReadAll());
        toolBar.add(readAll);
        writeAll = new ExportButton();
        writeAll.setText(MSGS.assetWriteAll());
        toolBar.add(writeAll);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);
        initDetailsTable();
        add(tableContainer);
    }

}
