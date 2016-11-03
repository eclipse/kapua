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
package org.eclipse.kapua.app.console.client.device;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.Resources;
import org.eclipse.kapua.app.console.client.ui.button.ExportButton;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.ui.widget.DateRangeSelector;
import org.eclipse.kapua.app.console.client.ui.widget.DateRangeSelectorListener;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.client.util.UserAgentUtils;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DeviceTabHistory extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    private static final int DEVICE_PAGE_SIZE = 250;

    private GwtSession m_currentSession;

    private boolean m_dirty;
    private boolean m_initialized;
    private GwtDevice m_selectedDevice;

    private ToolBar m_toolBar;

    private Button m_refreshButton;
    private Button m_export;

    private DateRangeSelector m_dateRangeSelector;
    private Grid<GwtDeviceEvent> m_grid;
    private PagingToolBar m_pagingToolBar;
    private BasePagingLoader<PagingLoadResult<GwtDeviceEvent>> m_loader;

    protected boolean refreshProcess;

    public DeviceTabHistory(GwtSession currentSession) {
        m_currentSession = currentSession;
        m_dirty = false;
        m_initialized = false;
    }

    public void setDevice(GwtDevice selectedDevice) {
        m_dirty = true;
        m_selectedDevice = selectedDevice;
    }

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);

        // init components
        initToolBar();
        initGrid();

        ContentPanel devicesHistoryPanel = new ContentPanel();
        devicesHistoryPanel.setBorders(false);
        devicesHistoryPanel.setBodyBorder(false);
        devicesHistoryPanel.setHeaderVisible(false);
        devicesHistoryPanel.setLayout(new FitLayout());
        devicesHistoryPanel.setScrollMode(Scroll.AUTO);
        devicesHistoryPanel.setTopComponent(m_toolBar);
        devicesHistoryPanel.add(m_grid);
        devicesHistoryPanel.setBottomComponent(m_pagingToolBar);

        add(devicesHistoryPanel);
        m_initialized = true;
    }

    private void initToolBar() {
        m_toolBar = new ToolBar();

        //
        // Refresh Button
        m_refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    m_refreshButton.setEnabled(false);
                    refreshProcess = true;

                    reload();

                    refreshProcess = false;
                    m_refreshButton.setEnabled(true);
                }
            }
        });
        m_refreshButton.setEnabled(true);
        m_toolBar.add(m_refreshButton);
        m_toolBar.add(new SeparatorToolItem());

        Menu menu = new Menu();
        menu.add(new MenuItem(MSGS.exportToExcel(), AbstractImagePrototype.create(Resources.INSTANCE.exportExcel()),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("xls");
                    }
                }));
        menu.add(new MenuItem(MSGS.exportToCSV(), AbstractImagePrototype.create(Resources.INSTANCE.exportCSV()),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("csv");
                    }
                }));
        m_export = new ExportButton();
        m_export.setMenu(menu);

        m_toolBar.add(m_export);
        m_toolBar.add(new SeparatorToolItem());

        m_dateRangeSelector = new DateRangeSelector();
        m_dateRangeSelector.setListener(new DateRangeSelectorListener() {

            public void onUpdate() {
                m_dirty = true;
                refresh();
            }
        });

        m_toolBar.add(new FillToolItem());
        m_toolBar.add(new LabelToolItem(MSGS.dataDateRange()));
        m_toolBar.add(m_dateRangeSelector);
        m_toolBar.disable();
    }

    private void initGrid() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("receivedOnFormatted", MSGS.deviceEventTime(), 75);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("eventType", MSGS.deviceEventType(), 50);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        TreeGridCellRenderer<GwtDeviceEvent> eventMessageRenderer = new TreeGridCellRenderer<GwtDeviceEvent>() {

            @Override
            public Object render(GwtDeviceEvent model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtDeviceEvent> store, Grid<GwtDeviceEvent> grid) {
                StringBuilder message = new StringBuilder("");

                if (model.getEventMessage() != null) {
                    message.append("<label title='")
                            .append(model.getUnescapedEventMessage())
                            .append("'>")
                            .append(model.getUnescapedEventMessage())
                            .append("</label>");
                }
                return message.toString();
            }
        };

        column = new ColumnConfig("actionType", MSGS.deviceEventType(), 50);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("responseCode", MSGS.deviceEventType(), 50);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("eventMessage", MSGS.deviceEventMessage(), 200);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.LEFT);
        column.setRenderer(eventMessageRenderer);
        columns.add(column);

        // loader and store
        RpcProxy<PagingLoadResult<GwtDeviceEvent>> proxy = new RpcProxy<PagingLoadResult<GwtDeviceEvent>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtDeviceEvent>> callback) {
                if (m_selectedDevice != null) {
                    PagingLoadConfig pagingConfig = (BasePagingLoadConfig) loadConfig;
                    ((BasePagingLoadConfig) pagingConfig).setLimit(DEVICE_PAGE_SIZE);
                    gwtDeviceService.findDeviceEvents(pagingConfig,
                            m_selectedDevice,
                            m_dateRangeSelector.getStartDate(),
                            m_dateRangeSelector.getEndDate(),
                            callback);
                }
            }
        };
        m_loader = new BasePagingLoader<PagingLoadResult<GwtDeviceEvent>>(proxy);
        m_loader.setSortDir(SortDir.DESC);
        m_loader.setSortField("receivedOnFormatted");
        m_loader.setRemoteSort(true);
        m_loader.addLoadListener(new DataLoadListener());

        ListStore<GwtDeviceEvent> store = new ListStore<GwtDeviceEvent>(m_loader);

        m_grid = new Grid<GwtDeviceEvent>(store, new ColumnModel(columns));
        m_grid.setBorders(false);
        m_grid.setStateful(false);
        m_grid.setLoadMask(true);
        m_grid.setStripeRows(true);
        m_grid.setTrackMouseOver(false);
        m_grid.setAutoExpandColumn("eventMessage");
        m_grid.disableTextSelection(false);
        m_grid.getView().setAutoFill(true);
        m_grid.getView().setEmptyText(MSGS.deviceHistoryTableNoHistory());

        m_pagingToolBar = new PagingToolBar(DEVICE_PAGE_SIZE);
        m_pagingToolBar.bind(m_loader);

        GridSelectionModel<GwtDeviceEvent> selectionModel = new GridSelectionModel<GwtDeviceEvent>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        m_grid.setSelectionModel(selectionModel);
    }

    // --------------------------------------------------------------------------------------
    //
    // Device Event List Management
    //
    // --------------------------------------------------------------------------------------

    public void refresh() {
        if (m_dirty && m_initialized) {
            m_dirty = false;
            if (m_selectedDevice == null) {
                // clear the table
                m_grid.getStore().removeAll();
            } else {
                m_loader.load();
            }
        }
    }

    public void reload() {
        m_loader.load();
    }

    private void export(String format) {
        StringBuilder sbUrl = new StringBuilder();
        if (UserAgentUtils.isSafari() || UserAgentUtils.isChrome()) {
            sbUrl.append("console/exporter_device_event?");
        } else {
            sbUrl.append("exporter_device_event?");
        }

        sbUrl.append("format=")
                .append(format)
                .append("&account=")
                .append(URL.encodeQueryString(m_currentSession.getSelectedAccount().getName()))
                .append("&clientId=")
                .append(URL.encodeQueryString(m_selectedDevice.getClientId()))
                .append("&startDate=")
                .append(m_dateRangeSelector.getStartDate().getTime())
                .append("&endDate=")
                .append(m_dateRangeSelector.getEndDate().getTime());
        Window.open(sbUrl.toString(), "_blank", "location=no");
    }

    // --------------------------------------------------------------------------------------
    //
    // Data Load Listener
    //
    // --------------------------------------------------------------------------------------

    private class DataLoadListener extends KapuaLoadListener {

        public DataLoadListener() {
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

            m_toolBar.enable();
        }
    }
}
