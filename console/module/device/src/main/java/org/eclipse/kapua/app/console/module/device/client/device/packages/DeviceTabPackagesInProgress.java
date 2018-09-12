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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.packages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageDownloadOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageOperation;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceTabPackagesInProgress extends TabItem {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);
    private final GwtDeviceManagementServiceAsync gwtDeviceManagementService = GWT.create(GwtDeviceManagementService.class);

    private boolean componentInitialized;
    private boolean contentDirty = true;

    private DeviceTabPackages parentTabPanel;
    private Grid<GwtPackageOperation> grid;
    private ListLoader<ListLoadResult<GwtPackageOperation>> storeLoader;

    public DeviceTabPackagesInProgress(DeviceTabPackages parentTabPanel) {
        super(MSGS.deviceInstallTabInProgress(), null);

        KapuaIcon icon = new KapuaIcon(IconSet.SPINNER);
        // icon.setColor(Color.GREEN);
        setIcon(icon);

        this.parentTabPanel = parentTabPanel;
    }

    public GwtPackageOperation getSelectedOperation() {
        return grid.getSelectionModel().getSelectedItem();
    }

    private GwtDevice getSelectedDevice() {
        return parentTabPanel.getSelectedDevice();
    }

    public void setDirty(boolean isDirty) {
        contentDirty = isDirty;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        //
        // Column Configuration
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        GridCellRenderer<GwtPackageOperation> renderer = new GridCellRenderer<GwtPackageOperation>() {

            @Override
            public Object render(GwtPackageOperation model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtPackageOperation> store, Grid<GwtPackageOperation> grid) {

                String operation;
                if (model instanceof GwtPackageDownloadOperation) {
                    operation = MSGS.deviceInstallTabInProgressTableOperationInstall();
                } else {
                    operation = MSGS.deviceInstallTabInProgressTableOperationUnknow();
                }

                return operation;
            }

        };

        ColumnConfig column = new ColumnConfig();
        column.setId("operation");
        column.setHeader(MSGS.deviceInstallTabInProgressTableOperation());
        column.setAlignment(HorizontalAlignment.CENTER);
        column.setWidth(60);
        column.setRenderer(renderer);
        configs.add(column);

        column.setId("id");
        column.setHeader(MSGS.deviceInstallTabInProgressTableOperationId());
        column.setAlignment(HorizontalAlignment.CENTER);
        column.setWidth(60);
        column.setRenderer(renderer);
        configs.add(column);

        renderer = new GridCellRenderer<GwtPackageOperation>() {

            @Override
            public Object render(GwtPackageOperation model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtPackageOperation> store, Grid<GwtPackageOperation> grid) {

                String sizeString;
                if (model instanceof GwtPackageDownloadOperation) {
                    Integer size = ((GwtPackageDownloadOperation) model).getSize();

                    if (size > 1024) {
                        size /= 1024;
                        if (size > 1024) {
                            sizeString = (size / 1024) + " MB";
                        } else {
                            sizeString = size.toString() + " KB";
                        }
                    } else {
                        sizeString = size.toString() + " B";
                    }
                } else {
                    sizeString = null;
                }

                return sizeString;
            }
        };

        column = new ColumnConfig();
        column.setId("size");
        column.setHeader(MSGS.deviceInstallTabInProgressTableSize());
        column.setWidth(200);
        column.setRenderer(renderer);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("progress");
        column.setHeader(MSGS.deviceInstallTabInProgressTableProgressPercentage());
        column.setWidth(80);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        ColumnModel columnModel = new ColumnModel(configs);

        RpcProxy<ListLoadResult<GwtPackageOperation>> proxy = new RpcProxy<ListLoadResult<GwtPackageOperation>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtPackageOperation>> callback) {
                gwtDeviceManagementService.getDownloadOperations(getSelectedDevice().getScopeId(),
                        getSelectedDevice().getId(),
                        callback);
            }
        };

        storeLoader = new BaseListLoader<ListLoadResult<GwtPackageOperation>>(proxy);
        storeLoader.addLoadListener(new InProgressDataLoadListener());

        ListStore<GwtPackageOperation> store = new ListStore<GwtPackageOperation>(storeLoader);

        grid = new Grid<GwtPackageOperation>(store, columnModel);
        grid.setBorders(false);
        grid.setStateful(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.disableTextSelection(false);
        grid.setAutoExpandColumn("statusMessage");
        grid.getView().setAutoFill(true);
        grid.getView().setEmptyText(MSGS.deviceInstallTabInProgressTableEmpty());

        ContentPanel rootContentPanel = new ContentPanel();
        rootContentPanel.setLayout(new FitLayout());
        rootContentPanel.setBorders(false);
        rootContentPanel.setBodyBorder(false);
        rootContentPanel.setHeaderVisible(false);
        rootContentPanel.add(grid);

        add(rootContentPanel);

        componentInitialized = true;
    }

    public void refresh() {
        if (contentDirty && componentInitialized) {

            if (getSelectedDevice() == null) {
                grid.getView().setEmptyText(MSGS.deviceNoDeviceSelectedOrOffline());
            } else {
                storeLoader.load();
            }

            contentDirty = false;
        }
    }

    private class InProgressDataLoadListener extends KapuaLoadListener {

        @Override
        public void loaderLoadException(LoadEvent le) {
            grid.unmask();

            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

        }
    }
}
