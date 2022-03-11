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

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventoryBundle;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventoryDeploymentPackage;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementServiceAsync;

import java.util.Arrays;
import java.util.List;

public class DeviceTabInventoryTabDeploymentPackages extends TabItem {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtDeviceInventoryManagementServiceAsync GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE = GWT.create(GwtDeviceInventoryManagementService.class);

    private boolean initialized;
    private boolean dirty = true;

    private DeviceTabInventory rootTabPanel;
    private TreeGrid<ModelData> treeGrid;
    private TreeStore<ModelData> treeStore = new TreeStore<ModelData>();

    public DeviceTabInventoryTabDeploymentPackages(DeviceTabInventory rootTabPanel) {
        super("Deployment Packages", new KapuaIcon(IconSet.INBOX));

        this.rootTabPanel = rootTabPanel;
    }

    private GwtDevice getSelectedDevice() {
        return rootTabPanel.getSelectedDevice();
    }

    public void setDirty(boolean isDirty) {
        dirty = isDirty;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        ColumnConfig name = new ColumnConfig();
        name.setId("name");
        name.setHeader("Name");
        name.setWidth(100);
        name.setRenderer(new TreeGridCellRenderer<ModelData>());

        ColumnConfig version = new ColumnConfig();
        version.setId("version");
        version.setHeader("Version");
        version.setWidth(150);

        ColumnModel cm = new ColumnModel(Arrays.asList(name, version));

        treeGrid = new TreeGrid<ModelData>(treeStore, cm);
        treeGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeGrid.setBorders(false);
        treeGrid.setLoadMask(true);
        treeGrid.setAutoExpandColumn("name");
        treeGrid.setTrackMouseOver(false);
        treeGrid.getAriaSupport().setLabelledBy(getHeader().getId() + "-label");
        treeGrid.getView().setAutoFill(true);
        treeGrid.getView().setForceFit(true);
        treeGrid.getView().setEmptyText(DEVICE_MSGS.deviceNoDeviceSelectedOrOffline());

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
        rootContentPanel.add(treeGrid);
        rootContentPanel.setTopComponent(toolBar);

        add(rootContentPanel);

        initialized = true;

        refresh();
    }

    public void openDeviceOfflineAlertDialog() {
        InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.INFO, DEVICE_MSGS.deviceOffline());
        errorDialog.show();
    }

    public void refresh() {

        if (initialized && dirty) {

            GwtDevice selectedDevice = getSelectedDevice();
            if (selectedDevice == null || !selectedDevice.isOnline()) {
                treeStore.removeAll();
                treeGrid.unmask();
                treeGrid.getView().setEmptyText(DEVICE_MSGS.deviceNoDeviceSelectedOrOffline());
            } else {
                treeGrid.mask(MSGS.loading());

                GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE.findDeviceDeploymentPackages(selectedDevice.getScopeId(), selectedDevice.getId(), new AsyncCallback<List<GwtInventoryDeploymentPackage>>() {

                    @Override
                    public void onSuccess(List<GwtInventoryDeploymentPackage> gwtInventoryDeploymentPackages) {
                        treeStore.removeAll();
                        if (gwtInventoryDeploymentPackages != null && !gwtInventoryDeploymentPackages.isEmpty()) {
                            for (GwtInventoryDeploymentPackage inventoryDeploymentPackage : gwtInventoryDeploymentPackages) {
                                treeStore.add(inventoryDeploymentPackage, false);

                                for (GwtInventoryBundle bundle : inventoryDeploymentPackage.getBundles()) {
                                    treeStore.add(inventoryDeploymentPackage, bundle, false);
                                }
                            }
                        } else {
                            treeGrid.getView().setEmptyText("No deployment packages found");
                            treeGrid.getView().refresh(false);
                        }

                        treeGrid.unmask();
                        rootTabPanel.getRefreshButton().enable();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof GwtKapuaException) {
                            FailureHandler.handle(caught);
                        } else {
                            ConsoleInfo.display(MSGS.popupError(), DEVICE_MSGS.deviceConnectionError());
                        }
                        treeGrid.unmask();
                    }
                });
            }

            dirty = false;
        }
    }

    @Override
    protected void onShow() {
        super.onShow();
        refresh();
    }

    public TreeGrid<ModelData> getTreeGrid() {
        return treeGrid;
    }
}
