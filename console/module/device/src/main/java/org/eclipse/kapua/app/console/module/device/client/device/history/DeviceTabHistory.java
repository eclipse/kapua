/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.history;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
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
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.ExportButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.DateRangeSelector;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.DateRangeSelectorListener;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaMenuItem;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceTabHistory extends KapuaTabItem<GwtDevice> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICES_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    private static final int DEVICE_PAGE_SIZE = 250;

    private GwtSession currentSession;

    private boolean initialized;

    private ToolBar toolBar;

    private Button refreshButton;
    private Button export;

    private DateRangeSelector dateRangeSelector;
    private Grid<GwtDeviceEvent> grid;
    private KapuaPagingToolBar pagingToolBar;
    private BasePagingLoader<PagingLoadResult<GwtDeviceEvent>> loader;

    protected boolean refreshProcess;

    public DeviceTabHistory(GwtSession currentSession) {
        super(currentSession, DEVICES_MSGS.tabHistory(), new KapuaIcon(IconSet.HISTORY));
        this.currentSession = currentSession;
        initialized = false;
        setEnabled(false);
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);
        setEnabled(gwtDevice != null);
    }

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);

        // init components
        initToolBar();
        initGrid();

        ContentPanel devicesHistoryPanel = new ContentPanel();
        devicesHistoryPanel.setBorders(false);
        devicesHistoryPanel.setBodyBorder(true);
        devicesHistoryPanel.setHeaderVisible(false);
        devicesHistoryPanel.setLayout(new FitLayout());
        devicesHistoryPanel.setScrollMode(Scroll.AUTO);
        devicesHistoryPanel.setTopComponent(toolBar);
        devicesHistoryPanel.add(grid);
        devicesHistoryPanel.setBottomComponent(pagingToolBar);

        add(devicesHistoryPanel);
        initialized = true;

        loader.load();
    }

    private void initToolBar() {
        toolBar = new ToolBar();
        toolBar.setBorders(true);

        //
        // Refresh Button
        refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshButton.setEnabled(false);
                    refreshProcess = true;

                    reload();

                    refreshProcess = false;
                    refreshButton.setEnabled(true);
                }
            }
        });
        refreshButton.setEnabled(true);
        toolBar.add(refreshButton);
        toolBar.add(new SeparatorToolItem());

        Menu menu = new Menu();
        menu.add(new KapuaMenuItem(MSGS.exportToExcel(), IconSet.FILE_EXCEL_O,
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("xls");
                    }
                }));
        menu.add(new KapuaMenuItem(MSGS.exportToCSV(), IconSet.FILE_TEXT_O,
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("csv");
                    }
                }));
        export = new ExportButton();
        export.setMenu(menu);

        toolBar.add(export);
        toolBar.add(new SeparatorToolItem());

        dateRangeSelector = new DateRangeSelector();
        dateRangeSelector.setListener(new DateRangeSelectorListener() {

            @Override
            public void onUpdate() {
                grid.getView().refresh(true);
                reload();
            }
        });

        toolBar.add(new FillToolItem());
        toolBar.add(new LabelToolItem(MSGS.dataDateRange()));
        toolBar.add(dateRangeSelector);
        toolBar.disable();
    }

    private void initGrid() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("receivedOnFormatted", DEVICES_MSGS.deviceEventTime(), 75);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("eventType", DEVICES_MSGS.deviceEventType(), 50);
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

        column = new ColumnConfig("actionType", DEVICES_MSGS.deviceEventActionType(), 50);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("responseCode", DEVICES_MSGS.deviceEventResponseCode(), 50);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("eventMessage", DEVICES_MSGS.deviceEventMessage(), 200);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.LEFT);
        column.setRenderer(eventMessageRenderer);
        columns.add(column);

        // loader and store
        RpcProxy<PagingLoadResult<GwtDeviceEvent>> proxy = new RpcProxy<PagingLoadResult<GwtDeviceEvent>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtDeviceEvent>> callback) {
                if (selectedEntity != null) {
                    PagingLoadConfig pagingConfig = (BasePagingLoadConfig) loadConfig;
                    ((BasePagingLoadConfig) pagingConfig).setLimit(DEVICE_PAGE_SIZE);
                    gwtDeviceService.findDeviceEvents(pagingConfig,
                            selectedEntity,
                            dateRangeSelector.getStartDate(),
                            dateRangeSelector.getEndDate(),
                            callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtDeviceEvent>(new ArrayList<GwtDeviceEvent>()));
                }
            }
        };
        loader = new BasePagingLoader<PagingLoadResult<GwtDeviceEvent>>(proxy);
        loader.setSortDir(SortDir.DESC);
        loader.setSortField("receivedOnFormatted");
        loader.setRemoteSort(true);
        loader.addLoadListener(new DataLoadListener());

        ListStore<GwtDeviceEvent> store = new ListStore<GwtDeviceEvent>(loader);

        grid = new Grid<GwtDeviceEvent>(store, new ColumnModel(columns));
        grid.setBorders(false);
        grid.setStateful(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.setAutoExpandColumn("eventMessage");
        grid.disableTextSelection(false);
        grid.getView().setAutoFill(true);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(DEVICES_MSGS.deviceHistoryTableNoHistory());

        pagingToolBar = new KapuaPagingToolBar(DEVICE_PAGE_SIZE);
        pagingToolBar.bind(loader);

        GridSelectionModel<GwtDeviceEvent> selectionModel = new GridSelectionModel<GwtDeviceEvent>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        grid.setSelectionModel(selectionModel);
    }

    // --------------------------------------------------------------------------------------
    //
    // Device Event List Management
    //
    // --------------------------------------------------------------------------------------

    @Override
    public void doRefresh() {
        if (initialized) {
            if (selectedEntity == null) {
                // clear the table
                grid.getStore().removeAll();
            } else {
                loader.load();
            }
        }
    }

    public void reload() {
        loader.load();
    }

    private void export(String format) {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append("exporter_device_event?");
        sbUrl.append("format=")
                .append(format)
                .append("&scopeId=")
                .append(URL.encodeQueryString(currentSession.getSelectedAccountId()))
                .append("&deviceId=")
                .append(URL.encodeQueryString(selectedEntity.getId()))
                .append("&startDate=")
                .append(dateRangeSelector.getStartDate().getTime())
                .append("&endDate=")
                .append(dateRangeSelector.getEndDate().getTime());
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

        @Override
        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

            toolBar.enable();
        }
    }
}
