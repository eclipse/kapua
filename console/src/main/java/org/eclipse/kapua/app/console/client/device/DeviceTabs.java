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
 *     Dario Maranta <dariomaranta@gmail.com>
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.client.device.management.packages.DeviceTabPackages;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDevice.GwtDeviceApplication;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceTabs extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private GwtSession currentSession;
    private DevicesTable devicesTable;
    private DeviceFilterPanel deviceFilterPanel;

    private TabPanel tabsPanel;
    private TabItem tabProfile;
    private TabItem tabHistory;
    private TabItem tabPackages;
    private TabItem tabBundles;
    private TabItem tabConfiguration;
    private TabItem tabAssets;
    private TabItem tabCommand;
    private TabItem tabLog;
    
    private DeviceTabProfile deviceProfileTab;
    private DeviceTabHistory deviceHistoryTab;
    private DeviceTabPackages devicePackagesTab;
    private DeviceTabBundles deviceBundlesTab;
    private DeviceTabConfiguration deviceConfigTab;
    private DeviceTabCommand deviceCommandTab;
    private DeviceTabAssets deviceAssetsTab;
    private DeviceTabLog deviceLogTab;

    public DeviceTabs(DevicesTable devicesTable, DeviceFilterPanel deviceFilterPanel, GwtSession currentSession) {
        this.devicesTable = devicesTable;
        this.deviceFilterPanel = deviceFilterPanel;
        this.currentSession = currentSession;

        deviceProfileTab = new DeviceTabProfile(this.devicesTable, this.deviceFilterPanel, this.currentSession);
        deviceHistoryTab = new DeviceTabHistory(this.currentSession);
        devicePackagesTab = new DeviceTabPackages(this.currentSession, this);
        deviceBundlesTab = new DeviceTabBundles(this.currentSession, this);
        deviceConfigTab = new DeviceTabConfiguration(this.currentSession);
        deviceAssetsTab = new DeviceTabAssets(this.currentSession);
        deviceLogTab = new DeviceTabLog();
        deviceCommandTab = new DeviceTabCommand(this.currentSession);
    }

    public void setDevice(GwtDevice selectedDevice) {
        // configure the tabs based on the available applications and user
        // permissions
        if (currentSession.hasDeviceReadPermission()) {
            tabHistory.enable();
        } else {
            tabHistory.disable();
            tabHistory.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (tabsPanel.getSelectedItem() == tabHistory) {
                tabsPanel.setSelection(tabProfile);
            }
        }

        boolean hasConfigApp = selectedDevice != null
                && selectedDevice.hasApplication(GwtDeviceApplication.APP_CONFIGURATION);
        if (hasConfigApp) {// && m_currentSession.hasDeviceManagePermission()) {
            tabConfiguration.enable();
        } else {
            tabConfiguration.disable();
            tabConfiguration.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (tabsPanel.getSelectedItem() == tabConfiguration) {
                tabsPanel.setSelection(tabProfile);
            }
        }

        boolean hasCmdApp = selectedDevice != null && selectedDevice.hasApplication(GwtDeviceApplication.APP_COMMAND);
        if (hasCmdApp) {// && m_currentSession.hasDeviceManagePermission()) {
            tabCommand.enable();
        } else {
            tabCommand.disable();
            tabCommand.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (tabsPanel.getSelectedItem() == tabCommand) {
                tabsPanel.setSelection(tabProfile);

            }
        }

        boolean hasPkgApp = selectedDevice != null && (selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V1)
                || selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V2));
        if (hasPkgApp && currentSession.hasDeviceManagePermission()) {
            tabPackages.enable();
        } else {
            tabPackages.disable();
            tabPackages.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (tabsPanel.getSelectedItem() == tabPackages) {
                tabsPanel.setSelection(tabProfile);
            }
        }

        boolean hasBundleApp = selectedDevice != null
                && (selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V1)
                        || selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V2));
        if (hasBundleApp && currentSession.hasDeviceManagePermission()) {
            tabBundles.enable();
        } else {
            tabBundles.disable();
            tabBundles.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (tabsPanel.getSelectedItem() == tabBundles) {
                tabsPanel.setSelection(tabProfile);
            }
        }

        tabLog.enable();
        boolean hasAssetApp = selectedDevice != null && (selectedDevice.hasApplication(GwtDeviceApplication.APP_ASSET_V1));
        if (hasAssetApp && currentSession.hasDeviceManagePermission()) {
            tabAssets.enable();
        } else {
            tabAssets.disable();
            tabAssets.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (tabsPanel.getSelectedItem() == tabAssets) {
                tabsPanel.setSelection(tabProfile);
            }
        }

        deviceProfileTab.setDevice(selectedDevice);
        deviceHistoryTab.setDevice(selectedDevice);
        devicePackagesTab.setDevice(selectedDevice);
        deviceBundlesTab.setDevice(selectedDevice);
        deviceConfigTab.setDevice(selectedDevice);
        deviceCommandTab.setDevice(selectedDevice);
        deviceAssetsTab.setDevice(selectedDevice);
        deviceLogTab.setDevice(selectedDevice);

        if (tabsPanel.getSelectedItem() == tabProfile) {
            deviceProfileTab.refresh();
        } else if (tabsPanel.getSelectedItem() == tabHistory) {
            deviceHistoryTab.refresh();
        } else if (tabsPanel.getSelectedItem() == tabConfiguration) {
            deviceConfigTab.refresh();
        } else if (tabsPanel.getSelectedItem() == tabCommand) {
            deviceCommandTab.refresh();
        } else if (tabsPanel.getSelectedItem() == tabPackages) {
            devicePackagesTab.refresh();
        } else if (tabsPanel.getSelectedItem() == tabBundles) {
            deviceBundlesTab.refresh();
        } else if (tabsPanel.getSelectedItem() == tabAssets) {
            deviceAssetsTab.refresh();
        } else if (tabsPanel.getSelectedItem() == tabLog) {
            deviceLogTab.refresh();
        }
    }

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setId("DeviceTabsContainer");
        setLayout(new FitLayout());

        tabsPanel = new TabPanel();
        tabsPanel.setPlain(true);
        tabsPanel.setBorders(false);
        tabsPanel.setBodyBorder(false);
        tabsPanel.setStyleAttribute("padding-top", "5px");

        tabProfile = new TabItem(MSGS.deviceTabDescription(), new KapuaIcon(IconSet.INFO));
        tabProfile.setBorders(true);
        tabProfile.setLayout(new FitLayout());
        tabProfile.add(deviceProfileTab);
        tabProfile.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                deviceProfileTab.refresh();
            }
        });
        tabsPanel.add(tabProfile);

        tabHistory = new TabItem(MSGS.tabHistory(), new KapuaIcon(IconSet.HISTORY));
        tabHistory.setBorders(true);
        tabHistory.setLayout(new FitLayout());
        tabHistory.add(deviceHistoryTab);
        tabHistory.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                deviceHistoryTab.refresh();
            }
        });
        tabsPanel.add(tabHistory);

        tabPackages = new TabItem(MSGS.tabPackages(), new KapuaIcon(IconSet.INBOX));
        tabPackages.setBorders(true);
        tabPackages.setLayout(new FitLayout());
        tabPackages.add(devicePackagesTab);
        tabPackages.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                devicePackagesTab.refresh();
            }
        });
        tabsPanel.add(tabPackages);

        tabBundles = new TabItem(MSGS.tabBundles(), new KapuaIcon(IconSet.CUBES));
        tabBundles.setBorders(true);
        tabBundles.setLayout(new FitLayout());
        tabBundles.add(deviceBundlesTab);
        tabBundles.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                deviceBundlesTab.refresh();
            }
        });
        tabsPanel.add(tabBundles);

        tabConfiguration = new TabItem(MSGS.tabConfiguration(), new KapuaIcon(IconSet.WRENCH));
        tabConfiguration.setBorders(true);
        tabConfiguration.setLayout(new FitLayout());
        tabConfiguration.add(deviceConfigTab);
        tabConfiguration.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                deviceConfigTab.refresh();
            }
        });
        tabsPanel.add(tabConfiguration);

        tabCommand = new TabItem(MSGS.tabCommand(), new KapuaIcon(IconSet.TERMINAL));
        tabCommand.setBorders(false);
        tabCommand.setLayout(new FitLayout());
        tabCommand.add(deviceCommandTab);
        tabCommand.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                deviceCommandTab.refresh();
            }
        });
        tabsPanel.add(tabCommand);

        tabLog = new TabItem(MSGS.taskLog(), new KapuaIcon(IconSet.TASKS));
        tabLog.setBorders(false);
        tabLog.setLayout(new FitLayout());
        tabLog.add(deviceLogTab);
        tabLog.addListener(Events.Select, new Listener<ComponentEvent>() {
        

            public void handleEvent(ComponentEvent be) {
                deviceLogTab.refresh();
            }
        });
        tabsPanel.add(tabLog);
        
        tabAssets = new TabItem(MSGS.asset(), new KapuaIcon(IconSet.AMAZON));
        tabAssets = new TabItem(MSGS.asset(), new KapuaIcon(IconSet.RETWEET));
        tabAssets.setBorders(false);
        tabAssets.setLayout(new FitLayout());
        tabAssets.add(deviceAssetsTab);
        tabAssets.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                deviceAssetsTab.refresh();
            }
        });
        tabsPanel.add(tabAssets);

        add(tabsPanel);
    }

}
