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
import org.eclipse.kapua.app.console.client.ui.button.ExportButton;
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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssetTable extends LayoutContainer {

    private GwtSession currentSession;
    private ContentPanel tableContainer;
    private ToolBar toolBar;
    private ExportButton assetsButton, channelsButton;
    private SplitButton addAssets;
    private Button deleteAsset, writeAsset, readAsset, addChannel, deleteChannel, readChannel, writeChannel;
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public AssetTable(GwtSession currentSession) {
        this.currentSession = currentSession;
        toolBar = new ToolBar();
    }

    private void initAssetTable() {

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setHeaderVisible(false);
        tableContainer.setLayout(new FitLayout());
        initAssetGrid();
        tableContainer.setTopComponent(toolBar);
    }

    private void initAssetGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("Assets", MSGS.assetName(), 275);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);
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

        assetsButton = new ExportButton();
        assetsButton.setText(MSGS.assetButton());
        addAssets = new SplitButton("New Asset");
        addAssets.setSize(85, 18);
        deleteAsset = new Button("DeleteAsset");
        deleteAsset.setSize(100, 18);
        readAsset = new Button("Read Asset");
        readAsset.setSize(100, 18);
        writeAsset = new Button("Write Asset");
        writeAsset.setSize(100, 18);
        Menu menuAsset = new Menu();
        menuAsset.add(addAssets);
        menuAsset.add(deleteAsset);
        menuAsset.add(new SeparatorMenuItem());
        menuAsset.add(readAsset);
        menuAsset.add(writeAsset);
        Menu menuNewAssetButton = new Menu();
        menuNewAssetButton.add(new Label("Modbus"));
        menuNewAssetButton.add(new Label("OPC-UA"));
        menuNewAssetButton.add(new Label("Others"));

        addAssets.setMenu(menuNewAssetButton);
        addAssets.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                addAssets.showMenu();

            }
        });
        assetsButton.setMenu(menuAsset);
        assetsButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                assetsButton.showMenu();

            }
        });
        toolBar.add(assetsButton);
        channelsButton = new ExportButton();
        channelsButton.setText(MSGS.channelButton());
        addChannel = new Button("Add Channel");
        addChannel.setSize(100, 18);
        deleteChannel = new Button("Delete Channel");
        deleteChannel.setSize(100, 18);
        readChannel = new Button("Read Channel");
        readChannel.setSize(100, 18);
        writeChannel = new Button("Write Channel");
        writeChannel.setSize(100, 18);
        Menu menuChannel = new Menu();
        menuChannel.add(addChannel);
        menuChannel.add(deleteChannel);
        menuChannel.add(new SeparatorMenuItem());
        menuChannel.add(readChannel);
        menuChannel.add(writeChannel);
        channelsButton.setMenu(menuChannel);
        channelsButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                channelsButton.showMenu();

            }
        });
        toolBar.add(channelsButton);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);
        initAssetTable();
        add(tableContainer);
    }

    /**
     * Shows and hides the table toolbar
     * 
     * @param show
     *            Whether show or not the toolbar
     */
    public void showToolbar(boolean show) {
        toolBar.setVisible(show);
    }
}
