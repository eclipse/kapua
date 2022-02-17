/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.inventory;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventorySystemPackage;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceTabInventoryTabSystemPackages extends TabItem {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtDeviceInventoryManagementServiceAsync GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE = GWT.create(GwtDeviceInventoryManagementService.class);

    private boolean componentInitialized;
    private boolean contentDirty = true;

    private DeviceTabInventory parentTabPanel;
    private Grid<GwtInventorySystemPackage> grid;
    private ListLoader<ListLoadResult<GwtInventorySystemPackage>> storeLoader;

    public DeviceTabInventoryTabSystemPackages(DeviceTabInventory parentTabPanel) {
        super("System Packages", new KapuaIcon(IconSet.TH_LARGE));

        this.parentTabPanel = parentTabPanel;
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

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Name");
        column.setWidth(200);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("version");
        column.setHeader("Version");
        column.setWidth(80);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("type");
        column.setHeader("Type");
        column.setWidth(80);
        configs.add(column);

        ColumnModel columnModel = new ColumnModel(configs);

        RpcProxy<ListLoadResult<GwtInventorySystemPackage>> proxy = new RpcProxy<ListLoadResult<GwtInventorySystemPackage>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtInventorySystemPackage>> callback) {
                GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE.findDeviceSystemPackages(getSelectedDevice().getScopeId(),
                        getSelectedDevice().getId(),
                        callback);
            }
        };

        storeLoader = new BaseListLoader<ListLoadResult<GwtInventorySystemPackage>>(proxy);
        storeLoader.addLoadListener(new InventorySystemPackagesLoadListener());

        ListStore<GwtInventorySystemPackage> store = new ListStore<GwtInventorySystemPackage>(storeLoader);

        grid = new Grid<GwtInventorySystemPackage>(store, columnModel);
        grid.setBorders(false);
        grid.setStateful(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.disableTextSelection(false);
        grid.setAutoExpandColumn("name");
        grid.getView().setAutoFill(true);
        grid.getView().setEmptyText("No system packages found...");

        Button refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (getSelectedDevice() != null && getSelectedDevice().isOnline()) {
                    setDirty(true);
                    refresh();
                } else {
                    openDeviceOfflineAlertDialog();
                }
            }
        });

        ToolBar toolBar = new ToolBar();
        toolBar.setBorders(true);
        toolBar.add(refreshButton);
        toolBar.setEnabled(getSelectedDevice() != null);

        ContentPanel rootContentPanel = new ContentPanel();
        rootContentPanel.setLayout(new FitLayout());
        rootContentPanel.setBorders(false);
        rootContentPanel.setBodyBorder(false);
        rootContentPanel.setHeaderVisible(false);
        rootContentPanel.add(grid);
        rootContentPanel.setTopComponent(toolBar);

        add(rootContentPanel);

        componentInitialized = true;
    }

    public void openDeviceOfflineAlertDialog() {
        InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.INFO, MSGS.deviceOffline());
        errorDialog.show();
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

    private class InventorySystemPackagesLoadListener extends KapuaLoadListener {

        @Override
        public void loaderLoadException(LoadEvent le) {
            grid.unmask();

            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

        }
    }
}
