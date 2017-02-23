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
package org.eclipse.kapua.app.console.client.device.management.packages;

import org.eclipse.kapua.app.console.client.device.DeviceTabs;
import org.eclipse.kapua.app.console.client.device.management.packages.button.PackageInstallButton;
import org.eclipse.kapua.app.console.client.device.management.packages.button.PackageUninstallButton;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.client.ui.dialog.InfoDialog.InfoDialogType;
import org.eclipse.kapua.app.console.shared.model.GwtDeploymentPackage;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceTabPackages extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final static String SERVLET_URL = "console/file/deploy";

    @SuppressWarnings("unused")
    private GwtSession m_currentSession;
    private DeviceTabs m_deviceTabs;

    private boolean componentInitialized = false;
    private GwtDevice selectedDevice;

    private ToolBar toolBar;
    private Button m_refreshButton;
    private Button m_installButton;
    private Button m_uninstallButton;

    private TabPanel tabsPanel;
    private DeviceTabPackagesInstalled installedPackageTab;
    private DeviceTabPackagesInProgress inProgressPackageTab;

    public DeviceTabPackages(GwtSession currentSession,
            DeviceTabs deviceTabs) {
        m_currentSession = currentSession;
        m_deviceTabs = deviceTabs;
    }

    public void setDevice(GwtDevice selectedDevice) {
        setDirty();
        this.selectedDevice = selectedDevice;

        if (componentInitialized) {
            tabsPanel.setSelection(installedPackageTab);
        }
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

        componentInitialized = true;
    }

    //
    // INITIALIZERS
    //

    private void initToolBar() {
        toolBar = new ToolBar();

        m_refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedDevice != null &&
                        selectedDevice.isOnline()) {
                    setDirty();
                    refresh();
                } else {
                    openDeviceOfflineAlertDialog();
                }
            }
        });

        m_installButton = new PackageInstallButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedDevice != null &&
                        selectedDevice.isOnline()) {
                    openInstallDialog();
                } else {
                    openDeviceOfflineAlertDialog();
                }
            }
        });

        m_uninstallButton = new PackageUninstallButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedDevice != null &&
                        selectedDevice.isOnline()) {
                    openUninstallDialog();
                } else {
                    openDeviceOfflineAlertDialog();
                }
            }
        });

        toolBar.add(m_refreshButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(m_installButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(m_uninstallButton);

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

        final PackageInstallDialog packageInstallDialog = new PackageInstallDialog(selectedDevice.getScopeId(), selectedDevice.getId());

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

                    m_uninstallButton.disable();
                    m_deviceTabs.setDevice(selectedDevice);
                }
            }
        });

        packageInstallDialog.show();
    }

    private void openUninstallDialog() {
        final GwtDeploymentPackage selectedDeploymentPackage = installedPackageTab.getSelectedDeploymentPackage();

        if (selectedDeploymentPackage != null) {
            toolBar.disable();

            final PackageUninstallDialog packageUninstallDialog = new PackageUninstallDialog(selectedDevice.getScopeId(), selectedDevice.getId(), selectedDeploymentPackage);

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

                        m_uninstallButton.disable();
                        m_deviceTabs.setDevice(selectedDevice);
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
    public void refresh() {
        //
        // Refresh the installed tab if selected
        if (tabsPanel.getSelectedItem().equals(installedPackageTab)) {
            installedPackageTab.refresh();
        } else {
            inProgressPackageTab.refresh();
        }

        //
        // Manage buttons
        if (selectedDevice != null && selectedDevice.isOnline()) {
            toolBar.enable();
            m_uninstallButton.disable();
        } else {
            toolBar.disable();
        }
    }

    //
    // ACCESSORS
    //

    public GwtDevice getSelectedDevice() {
        return selectedDevice;
    }

    public void setDirty() {
        if (componentInitialized) {
            installedPackageTab.setDirty(true);
            inProgressPackageTab.setDirty(true);
        }
    }

    public Button getUninstallButton() {
        return m_uninstallButton;
    }

}
