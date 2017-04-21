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
package org.eclipse.kapua.app.console.client.device;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.shared.model.GwtChannel;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

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

public class ConfigurationPanelTable extends LayoutContainer {

    private GwtSession currentSession;
    private ContentPanel tableContainer;
    private ToolBar toolBar;
    private ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public ConfigurationPanelTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    private void initConfigurationPanelTable() {
        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(false);
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        initConfigurationPanelTableGrid();
        tableContainer.setTopComponent(toolBar);
    }

    private void initConfigurationPanelTableGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig id = new ColumnConfig("id", "ID", 150);
        id.setAlignment(HorizontalAlignment.CENTER);
        configs.add(id);
        ColumnConfig name = new ColumnConfig("name", "Name", 150);
        name.setAlignment(HorizontalAlignment.CENTER);
        configs.add(name);
        ColumnConfig type = new ColumnConfig("type", "Type", 150);
        type.setAlignment(HorizontalAlignment.CENTER);
        configs.add(type);
        ColumnConfig valueType = new ColumnConfig("valueType", "Value Type", 150);
        valueType.setAlignment(HorizontalAlignment.CENTER);
        configs.add(valueType);
        ColumnConfig unitId = new ColumnConfig("unitId", "unit_id", 150);
        unitId.setAlignment(HorizontalAlignment.CENTER);
        configs.add(unitId);
        ColumnConfig primaryTable = new ColumnConfig("primaryTable", "primary_table", 150);
        primaryTable.setAlignment(HorizontalAlignment.CENTER);
        configs.add(primaryTable);
        ColumnConfig memoryAddress = new ColumnConfig("memoryAddress", "memmory_address", 150);
        memoryAddress.setAlignment(HorizontalAlignment.CENTER);
        configs.add(memoryAddress);

        RpcProxy<PagingLoadResult<GwtChannel>> proxy = new RpcProxy<PagingLoadResult<GwtChannel>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtChannel>> callback) {
                // TODO Auto-generated method stub

            }
        };
        ListLoader<ListLoadResult<GwtChannel>> loader = new BaseListLoader<ListLoadResult<GwtChannel>>(proxy);
        SwappableListStore<GwtChannel> store = new SwappableListStore<GwtChannel>(loader);
        Grid<GwtChannel> grid = new Grid<GwtChannel>(store, new ColumnModel(configs));
        tableContainer.add(grid);
        toolBar = new ToolBar();
        Label label = new Label("Asset 1(Driver)");
        toolBar.add(label);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(new FillToolItem());

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);
        initConfigurationPanelTable();
        add(tableContainer);
    }

}
