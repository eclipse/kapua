/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.packages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.device.packages.dialog.DeviceManagementOperationLog;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry.GwtDeviceManagementOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry.GwtDeviceManagementOperationQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementOperationService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementOperationServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceTabPackagesHistory extends KapuaTabItem<GwtDevice> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICES_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtDeviceManagementOperationServiceAsync DEVICE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceManagementOperationService.class);

    private static final int PAGE_SIZE = 250;

    private boolean initialized;

    private Grid<GwtDeviceManagementOperation> grid;
    private KapuaPagingToolBar pagingToolBar;
    private BasePagingLoader<PagingLoadResult<GwtDeviceManagementOperation>> loader;
    private DeviceTabPackages deviceTabPackages;
    private boolean contentDirty = true;
    private boolean loadingInProgress;

    protected boolean refreshProcess;

    public DeviceTabPackagesHistory(GwtSession currentSession, DeviceTabPackages deviceTabPackages) {
        super(currentSession, DEVICES_MSGS.deviceInstallTabHistory(), new KapuaIcon(IconSet.CLOCK_O));

        initialized = false;
        this.deviceTabPackages = deviceTabPackages;
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);
        super.setDirty(true);
    }

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);

        // init components
        initGrid();

        ContentPanel devicesPackageHistoryPanel = new ContentPanel();
        devicesPackageHistoryPanel.setBorders(false);
        devicesPackageHistoryPanel.setBodyBorder(true);
        devicesPackageHistoryPanel.setHeaderVisible(false);
        devicesPackageHistoryPanel.setLayout(new FitLayout());
        devicesPackageHistoryPanel.add(grid);
        devicesPackageHistoryPanel.setBottomComponent(pagingToolBar);

        add(devicesPackageHistoryPanel);
        initialized = true;

        pagingToolBar.enable();
    }

    private void initGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("startedOnFormatted");
        column.setHeader(DEVICES_MSGS.deviceInstallTabHistoryTableStartedOn());
        column.setWidth(200);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("endedOnFormatted");
        column.setHeader(DEVICES_MSGS.deviceInstallTabHistoryTableEndedOn());
        column.setWidth(200);
        column.setAlignment(HorizontalAlignment.CENTER);
        column.setRenderer(
                new GridCellRenderer<GwtDeviceManagementOperation>() {
                    @Override
                    public Object render(GwtDeviceManagementOperation model, String property, ColumnData columnData, int row, int colum, ListStore listStore, Grid grid) {
                        if (model.getEndedOn() == null) {
                            return DEVICES_MSGS.deviceInstallTabHistoryTableEndedOnInProgress();
                        }

                        return model.get(property);
                    }
                });
        configs.add(column);

        column = new ColumnConfig();
        column.setId("status");
        column.setHeader(DEVICES_MSGS.deviceInstallTabHistoryTableStatus());
        column.setWidth(200);
        column.setAlignment(HorizontalAlignment.CENTER);
        column.setRenderer(
                new GridCellRenderer<GwtDeviceManagementOperation>() {

                    @Override
                    public Object render(GwtDeviceManagementOperation model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtDeviceManagementOperation> store, Grid<GwtDeviceManagementOperation> grid) {
                        switch (model.getStatusEnum()) {
                            case COMPLETED:
                                return DEVICES_MSGS.deviceInstallTabHistoryTableStatusCompleted();
                            case RUNNING:
                                return DEVICES_MSGS.deviceInstallTabHistoryTableStatusRunning();
                            case STALE:
                                return DEVICES_MSGS.deviceInstallTabHistoryTableStatusStale();
                            case FAILED:
                                return DEVICES_MSGS.deviceInstallTabHistoryTableStatusFailed();
                        }
                        return null;
                    }

                });
        configs.add(column);

        column = new ColumnConfig();
        column.setId("resource");
        column.setHeader(DEVICES_MSGS.deviceInstallTabHistoryTableResource());
        column.setWidth(200);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("inputProperty_packageName");
        column.setHeader(DEVICES_MSGS.deviceInstallTabHistoryTableName());
        column.setWidth(200);
        column.setSortable(false);
        column.setRenderer(
                new GridCellRenderer() {
                    @Override
                    public Object render(ModelData model, String property, ColumnData columnData, int row, int column, ListStore listStore, Grid grid) {

                        if (model.get("inputProperty_kapuapackagedownloadname") != null) {
                            return model.get("inputProperty_kapuapackagedownloadname");
                        }

                        if (model.get("inputProperty_kapuapackageuninstallname") != null) {
                            return model.get("inputProperty_kapuapackageuninstallname");
                        }

                        return null;
                    }
                });
        configs.add(column);

        column = new ColumnConfig();
        column.setId("inputProperty_packageVersion");
        column.setHeader(DEVICES_MSGS.deviceInstallTabHistoryTableVersion());
        column.setWidth(200);
        column.setSortable(false);
        column.setRenderer(
                new GridCellRenderer() {
                    @Override
                    public Object render(ModelData model, String property, ColumnData columnData, int row, int column, ListStore listStore, Grid grid) {

                        if (model.get("inputProperty_kapuapackagedownloadversion") != null) {
                            return model.get("inputProperty_kapuapackagedownloadversion");
                        }

                        if (model.get("inputProperty_kapuapackageuninstallversion") != null) {
                            return model.get("inputProperty_kapuapackageuninstallversion");
                        }

                        return null;
                    }
                });
        configs.add(column);

        column = new ColumnConfig();
        column.setId("inputProperty_kapuapackagedownloaduri");
        column.setSortable(false);
        column.setHeader(DEVICES_MSGS.deviceInstallTabHistoryTableURI());
        column.setWidth(200);
        configs.add(column);

        // loader and store
        RpcProxy<PagingLoadResult<GwtDeviceManagementOperation>> proxy = new RpcProxy<PagingLoadResult<GwtDeviceManagementOperation>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtDeviceManagementOperation>> callback) {
                if (selectedEntity != null) {
                    BasePagingLoadConfig pagingConfig = (BasePagingLoadConfig) loadConfig;
                    pagingConfig.setLimit(PAGE_SIZE);

                    GwtDeviceManagementOperationQuery query = new GwtDeviceManagementOperationQuery();
                    query.setScopeId(selectedEntity.getScopeId());
                    query.setDeviceId(selectedEntity.getId());
                    query.setAppId("DEPLOY");

                    DEVICE_MANAGEMENT_SERVICE.query(pagingConfig, query, callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtDeviceManagementOperation>(new ArrayList<GwtDeviceManagementOperation>()));
                }
            }
        };
        loader = new BasePagingLoader<PagingLoadResult<GwtDeviceManagementOperation>>(proxy);
        loader.addLoadListener(new HistoryLoadListener());
        loader.setSortDir(SortDir.DESC);
        loader.setSortField("startedOnFormatted");
        loader.setRemoteSort(true);

        ListStore<GwtDeviceManagementOperation> store = new ListStore<GwtDeviceManagementOperation>(loader);

        grid = new Grid<GwtDeviceManagementOperation>(store, new ColumnModel(configs));
        grid.setBorders(false);
        grid.setStateful(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.setAutoExpandColumn("package");
        grid.disableTextSelection(false);
        grid.getView().setAutoFill(true);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(DEVICES_MSGS.deviceInstallTabHistoryTableEmpty());

        pagingToolBar = new KapuaPagingToolBar(PAGE_SIZE);
        pagingToolBar.bind(loader);

        GridSelectionModel<GwtDeviceManagementOperation> selectionModel = new GridSelectionModel<GwtDeviceManagementOperation>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        grid.setSelectionModel(selectionModel);

        loader.load();
        initialized = true;
    }

    // --------------------------------------------------------------------------------------
    //
    // Device Operation List Management
    //
    // --------------------------------------------------------------------------------------

    public void showOperationLog() {
        GwtDeviceManagementOperation deviceManagementOperation = grid.getSelectionModel().getSelectedItem();
        if (deviceManagementOperation != null) {
            DeviceManagementOperationLog logDialog = new DeviceManagementOperationLog(deviceManagementOperation);
            logDialog.show();
        }
    }


    @Override
    public void doRefresh() {
        if (contentDirty && initialized) {
            if (selectedEntity == null) {
                // clear the table
                grid.getStore().removeAll();
            } else if (!loadingInProgress) {
                loader.load();
            }
            contentDirty = false;
        }
    }

    public void reload() {
        loader.load();
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    public void setDirty(boolean isDirty) {
        contentDirty = isDirty;
    }

    private class HistoryLoadListener extends LoadListener {

        @Override
        public void loaderLoadException(LoadEvent le) {
            loadingInProgress = false;
            deviceTabPackages.getRefreshButton().unmask();
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
        }

        @Override
        public void loaderBeforeLoad(LoadEvent le) {
            loadingInProgress = true;
            deviceTabPackages.getRefreshButton().mask();
        }

        @Override
        public void loaderLoad(LoadEvent le) {
            loadingInProgress = false;
            deviceTabPackages.getRefreshButton().unmask();
        }
    }
}
