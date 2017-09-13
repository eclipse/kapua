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

import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.device.client.device.DeviceView;
import org.eclipse.kapua.app.console.module.device.client.device.packages.button.PackageInstallButton;
import org.eclipse.kapua.app.console.module.device.client.device.packages.button.PackageUninstallButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog.InfoDialogType;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeploymentPackage;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceTabPackages extends KapuaTabItem<GwtDevice> {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private DeviceView deviceTabs;

    private boolean initialized;

    private ToolBar toolBar;
    private Button refreshButton;
    private Button installButton;
    private Button uninstallButton;

    private TabPanel tabsPanel;
    private DeviceTabPackagesInstalled installedPackageTab;
    private DeviceTabPackagesInProgress inProgressPackageTab;

    public DeviceTabPackages(GwtSession currentSession,
            DeviceView deviceTabs) {
        super(MSGS.tabPackages(), new KapuaIcon(IconSet.INBOX));
        this.deviceTabs = deviceTabs;
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);
        setDirty();

        if(initialized) {
            tabsPanel.setSelection(installedPackageTab);
        }

        doRefresh();
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);

        //
        // Init actions toolbar
        initToolBar();

        //
        // Init Packages tab
        initPackagesTabs();

        ContentPanel devicesConfigurationPanel = new ContentPanel();
        devicesConfigurationPanel.setBorders(false);
        devicesConfigurationPanel.setBodyBorder(false);
        devicesConfigurationPanel.setHeaderVisible(false);
        devicesConfigurationPanel.setLayout(new FitLayout());
        devicesConfigurationPanel.setScrollMode(Scroll.AUTO);
        devicesConfigurationPanel.setTopComponent(toolBar);
        devicesConfigurationPanel.add(tabsPanel);

        add(devicesConfigurationPanel);

        initialized = true;
    }

    //
    // INITIALIZERS
    //

    private void initToolBar() {
        toolBar = new ToolBar();

        refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedEntity != null &&
                        selectedEntity.isOnline()) {
                    setDirty();
                    refresh();
                } else {
                    openDeviceOfflineAlertDialog();
                }
            }
        });

        installButton = new PackageInstallButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedEntity != null &&
                        selectedEntity.isOnline()) {
                    openInstallDialog();
                } else {
                    openDeviceOfflineAlertDialog();
                }
            }
        });

        uninstallButton = new PackageUninstallButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedEntity != null &&
                        selectedEntity.isOnline()) {
                    openUninstallDialog();
                } else {
                    openDeviceOfflineAlertDialog();
                }
            }
        });

        toolBar.add(refreshButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(installButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(uninstallButton);

        toolBar.disable();
    }

    private void initPackagesTabs() {
        tabsPanel = new TabPanel();
        tabsPanel.setPlain(true);
        tabsPanel.setBorders(false);
        tabsPanel.setTabPosition(TabPosition.BOTTOM);

        //
        // Packages installed tab
        installedPackageTab = new DeviceTabPackagesInstalled(this);
        installedPackageTab.setBorders(false);
        installedPackageTab.setLayout(new FitLayout());

        installedPackageTab.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                refresh();
            }
        });
        tabsPanel.add(installedPackageTab);

        //
        // In progress packages install tab
        inProgressPackageTab = new DeviceTabPackagesInProgress(this);
        inProgressPackageTab.setBorders(false);
        inProgressPackageTab.setLayout(new FitLayout());

        inProgressPackageTab.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                refresh();
            }
        });
        tabsPanel.add(inProgressPackageTab);

        add(tabsPanel);
    }

    //
    // ACTIONS DIALOGS
    //
    private void openInstallDialog() {
        toolBar.disable();

        final PackageInstallDialog packageInstallDialog = new PackageInstallDialog(selectedEntity.getScopeId(), selectedEntity.getId());

        packageInstallDialog.addListener(Events.Hide, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                toolBar.enable();

                Boolean exitStatus = packageInstallDialog.getExitStatus();
                if (exitStatus == null) { // Operation Aborted
                    return;
                } else {

                    InfoDialogType exitDialogType;
                    String exitMessage = packageInstallDialog.getExitMessage();

                    if (exitStatus == true) { // Operation Success
                        exitDialogType = InfoDialogType.INFO;
                    } else { // Operaton Failed
                        exitDialogType = InfoDialogType.ERROR;
                    }

                    //
                    // Exit dialog
                    InfoDialog exitDialog = new InfoDialog(exitDialogType,
                            exitMessage);

                    exitDialog.show();

                    uninstallButton.disable();
                    deviceTabs.setSelectedEntity(selectedEntity);
                }
            }
        });

        packageInstallDialog.show();
    }

    private void openUninstallDialog() {
        final GwtDeploymentPackage selectedDeploymentPackage = installedPackageTab.getSelectedDeploymentPackage();

        if (selectedDeploymentPackage != null) {
            toolBar.disable();

            final PackageUninstallDialog packageUninstallDialog = new PackageUninstallDialog(selectedEntity.getScopeId(), selectedEntity.getId(), selectedDeploymentPackage);

            packageUninstallDialog.addListener(Events.Hide, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    toolBar.enable();

                    Boolean exitStatus = packageUninstallDialog.getExitStatus();
                    if (exitStatus == null) { // Operation Aborted
                        return;
                    } else {

                        InfoDialogType exitDialogType;
                        String exitMessage = packageUninstallDialog.getExitMessage();

                        if (exitStatus == true) { // Operation Success
                            exitDialogType = InfoDialogType.INFO;
                        } else { // Operaton Failed
                            exitDialogType = InfoDialogType.ERROR;
                        }

                        //
                        // Exit dialog
                        InfoDialog exitDialog = new InfoDialog(exitDialogType,
                                exitMessage);

                        exitDialog.show();

                        uninstallButton.disable();
                        deviceTabs.setSelectedEntity(selectedEntity);
                    }
                }
            });

            packageUninstallDialog.show();

        }
    }

    public void openDeviceOfflineAlertDialog() {
        InfoDialog errorDialog = new InfoDialog(InfoDialogType.INFO,
                MSGS.deviceOffline());
        errorDialog.show();
    }

    //
    // REFRESHER
    //
    @Override
    public void doRefresh() {
        if(initialized) {
        //
        // Refresh the installed tab if selected
        if (tabsPanel.getSelectedItem().equals(installedPackageTab)) {
            installedPackageTab.refresh();
        } else {
            inProgressPackageTab.refresh();
        }

        //
        // Manage buttons
            if (selectedEntity != null && selectedEntity.isOnline()) {
            toolBar.enable();
            uninstallButton.disable();
        } else {
            toolBar.disable();
        }
    }
    }

    //
    // ACCESSORS
    //

    public GwtDevice getSelectedDevice() {
        return selectedEntity;
    }

    public void setDirty() {
        if (initialized) {
            installedPackageTab.setDirty(true);
            inProgressPackageTab.setDirty(true);
        }
    }

    public Button getUninstallButton() {
        return uninstallButton;
    }

}
