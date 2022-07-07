/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
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
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.device.client.device.inventory.buttons.ContainerStartButton;
import org.eclipse.kapua.app.console.module.device.client.device.inventory.buttons.ContainerStopButton;
import org.eclipse.kapua.app.console.module.device.client.device.inventory.dialog.InventoryContainerStartStopDialog;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventoryContainer;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceTabInventoryTabContainer extends TabItem {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtDeviceInventoryManagementServiceAsync GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE = GWT.create(GwtDeviceInventoryManagementService.class);

    private boolean componentInitialized;
    private boolean dirty = true;

    private DeviceTabInventory parentTabPanel;
    private Grid<GwtInventoryContainer> grid;
    private ListLoader<ListLoadResult<GwtInventoryContainer>> storeLoader;

    Button startButton;
    Button stopButton;
    Button refreshButton;

    public DeviceTabInventoryTabContainer(DeviceTabInventory parentTabPanel) {
        super("Containers", new KapuaIcon(IconSet.TH));

        this.parentTabPanel = parentTabPanel;
    }

    private GwtDevice getSelectedDevice() {
        return parentTabPanel.getSelectedDevice();
    }

    public void setDirty(boolean isDirty) {
        dirty = isDirty;
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

        RpcProxy<ListLoadResult<GwtInventoryContainer>> proxy = new RpcProxy<ListLoadResult<GwtInventoryContainer>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtInventoryContainer>> callback) {
                GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE.findDeviceContainers(getSelectedDevice().getScopeId(),
                        getSelectedDevice().getId(),
                        callback);
            }
        };

        storeLoader = new BaseListLoader<ListLoadResult<GwtInventoryContainer>>(proxy);
        storeLoader.addLoadListener(new InventoryContainersLoadListener());

        ListStore<GwtInventoryContainer> store = new ListStore<GwtInventoryContainer>(storeLoader);

        grid = new Grid<GwtInventoryContainer>(store, columnModel);
        grid.setBorders(false);
        grid.setStateful(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.disableTextSelection(false);
        grid.setAutoExpandColumn("name");
        grid.getView().setAutoFill(true);
        grid.getView().setEmptyText("No containers found...");

        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtInventoryContainer>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<GwtInventoryContainer> selectionChangedEvent) {
                checkButtonEnablement();
            }
        });

        // Start Button
        startButton = new ContainerStartButton(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                InventoryContainerStartStopDialog startStopDialog = new InventoryContainerStartStopDialog(getSelectedDevice(), grid.getSelectionModel().getSelectedItem(), true);

                startStopDialog.addListener(Events.Hide, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent baseEvent) {
                        setDirty(true);
                        refresh();
                    }
                });

                startStopDialog.show();
            }
        });

        // Stop Button
        stopButton = new ContainerStopButton(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                InventoryContainerStartStopDialog startStopDialog = new InventoryContainerStartStopDialog(getSelectedDevice(), grid.getSelectionModel().getSelectedItem(), false);

                startStopDialog.addListener(Events.Hide, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent baseEvent) {
                        setDirty(true);
                        refresh();
                    }
                });

                startStopDialog.show();
            }
        });

        // Refresh Button
        refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

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
        toolBar.add(startButton);
        toolBar.add(stopButton);
        toolBar.add(refreshButton);

        checkButtonEnablement();

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

    private void checkButtonEnablement() {
        GwtInventoryContainer selectedContainer = grid.getSelectionModel().getSelectedItem();

        startButton.setEnabled(getSelectedDevice() != null && getSelectedDevice().isOnline() && selectedContainer != null);
        stopButton.setEnabled(getSelectedDevice() != null && getSelectedDevice().isOnline() && selectedContainer != null);
        refreshButton.setEnabled(getSelectedDevice() != null && getSelectedDevice().isOnline());
    }

    public void refresh() {
        if (dirty && componentInitialized) {

            if (getSelectedDevice() == null) {
                grid.getView().setEmptyText(DEVICE_MSGS.deviceNoDeviceSelectedOrOffline());
            } else {
                storeLoader.load();
            }

            dirty = false;
        }
    }

    public void openDeviceOfflineAlertDialog() {
        InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.INFO, DEVICE_MSGS.deviceOffline());
        errorDialog.show();
    }

    private class InventoryContainersLoadListener extends KapuaLoadListener {

        @Override
        public void loaderLoadException(LoadEvent le) {
            grid.unmask();

            Throwable loaderException = le.exception;

            if (loaderException != null) {

                if (loaderException instanceof GwtKapuaException &&
                        ((GwtKapuaException) loaderException).getCode().equals(GwtKapuaErrorCode.DEVICE_MANAGEMENT_RESPONSE_NOT_FOUND)) {
                    ConsoleInfo.display(MSGS.error(), "The 'containers' resource is not supported by this device!");
                } else {
                    FailureHandler.handle(le.exception);
                }
            }
        }
    }
}
