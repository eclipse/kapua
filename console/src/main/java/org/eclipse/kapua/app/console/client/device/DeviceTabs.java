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

    private GwtSession m_currentSession;
    private DevicesTable m_devicesTable;
    private DeviceFilterPanel m_deviceFilterPanel;

    private TabPanel m_tabsPanel;
    private TabItem m_tabProfile;
    private TabItem m_tabHistory;
    private TabItem m_tabPackages;
    private TabItem m_tabBundles;
    private TabItem m_tabConfiguration;
    private TabItem m_tabAssets;

    private TabItem m_tabCommand;

    private DeviceTabProfile m_deviceProfileTab;
    private DeviceTabHistory m_deviceHistoryTab;
    private DeviceTabPackages m_devicePackagesTab;
    private DeviceTabBundles m_deviceBundlesTab;
    private DeviceTabConfiguration m_deviceConfigTab;
    private DeviceTabCommand m_deviceCommandTab;
    private AssetTabItem m_deviceAssetsTab;

    public DeviceTabs(DevicesTable devicesTable, DeviceFilterPanel deviceFilterPanel, GwtSession currentSession) {
        m_devicesTable = devicesTable;
        m_deviceFilterPanel = deviceFilterPanel;
        m_currentSession = currentSession;

        m_deviceProfileTab = new DeviceTabProfile(m_devicesTable, m_deviceFilterPanel, m_currentSession);
        m_deviceHistoryTab = new DeviceTabHistory(m_currentSession);
        m_devicePackagesTab = new DeviceTabPackages(m_currentSession, this);
        m_deviceBundlesTab = new DeviceTabBundles(m_currentSession, this);
        m_deviceConfigTab = new DeviceTabConfiguration(m_currentSession);
        m_deviceAssetsTab = new AssetTabItem(m_currentSession);

        m_deviceCommandTab = new DeviceTabCommand(m_currentSession);
    }

    public void setDevice(GwtDevice selectedDevice) {
        // configure the tabs based on the available applications and user permissions
        if (m_currentSession.hasDeviceReadPermission()) {
            m_tabHistory.enable();
        } else {
            m_tabHistory.disable();
            m_tabHistory.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (m_tabsPanel.getSelectedItem() == m_tabHistory) {
                m_tabsPanel.setSelection(m_tabProfile);
            }
        }

        boolean hasConfigApp = selectedDevice != null && selectedDevice.hasApplication(GwtDeviceApplication.APP_CONFIGURATION);
        if (hasConfigApp) {// && m_currentSession.hasDeviceManagePermission()) {
            m_tabConfiguration.enable();
        } else {
            m_tabConfiguration.disable();
            m_tabConfiguration.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (m_tabsPanel.getSelectedItem() == m_tabConfiguration) {
                m_tabsPanel.setSelection(m_tabProfile);
            }
        }

        boolean hasCmdApp = selectedDevice != null && selectedDevice.hasApplication(GwtDeviceApplication.APP_COMMAND);
        if (hasCmdApp) {// && m_currentSession.hasDeviceManagePermission()) {
            m_tabCommand.enable();
        } else {
            m_tabCommand.disable();
            m_tabCommand.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (m_tabsPanel.getSelectedItem() == m_tabCommand) {
                m_tabsPanel.setSelection(m_tabProfile);

            }
        }

        boolean hasPkgApp = selectedDevice != null && (selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V1) ||
                selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V2));
        if (hasPkgApp && m_currentSession.hasDeviceManagePermission()) {
            m_tabPackages.enable();
        } else {
            m_tabPackages.disable();
            m_tabPackages.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (m_tabsPanel.getSelectedItem() == m_tabPackages) {
                m_tabsPanel.setSelection(m_tabProfile);
            }
        }

        boolean hasBundleApp = selectedDevice != null && (selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V1) ||
                selectedDevice.hasApplication(GwtDeviceApplication.APP_DEPLOY_V2));
        if (hasBundleApp && m_currentSession.hasDeviceManagePermission()) {
            m_tabBundles.enable();
        } else {
            m_tabBundles.disable();
            m_tabBundles.getHeader().setTitle(MSGS.youDontHavePermissionTo("view", "tab", "device:manage"));
            if (m_tabsPanel.getSelectedItem() == m_tabBundles) {
                m_tabsPanel.setSelection(m_tabProfile);
            }
        }

        m_deviceProfileTab.setDevice(selectedDevice);
        m_deviceHistoryTab.setDevice(selectedDevice);
        m_devicePackagesTab.setDevice(selectedDevice);
        m_deviceBundlesTab.setDevice(selectedDevice);
        m_deviceConfigTab.setDevice(selectedDevice);
        m_deviceCommandTab.setDevice(selectedDevice);

        if (m_tabsPanel.getSelectedItem() == m_tabProfile) {
            m_deviceProfileTab.refresh();
        } else if (m_tabsPanel.getSelectedItem() == m_tabHistory) {
            m_deviceHistoryTab.refresh();
        } else if (m_tabsPanel.getSelectedItem() == m_tabConfiguration) {
            m_deviceConfigTab.refresh();
        } else if (m_tabsPanel.getSelectedItem() == m_tabCommand) {
            m_deviceCommandTab.refresh();
        } else if (m_tabsPanel.getSelectedItem() == m_tabPackages) {
            m_devicePackagesTab.refresh();
        } else if (m_tabsPanel.getSelectedItem() == m_tabBundles) {
            m_deviceBundlesTab.refresh();
        }
    }

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setId("DeviceTabsContainer");
        setLayout(new FitLayout());

        m_tabsPanel = new TabPanel();
        m_tabsPanel.setPlain(true);
        m_tabsPanel.setBorders(false);
        m_tabsPanel.setBodyBorder(false);
        m_tabsPanel.setStyleAttribute("padding-top", "5px");

        m_tabProfile = new TabItem(MSGS.deviceTabDescription(), new KapuaIcon(IconSet.INFO));
        m_tabProfile.setBorders(true);
        m_tabProfile.setLayout(new FitLayout());
        m_tabProfile.add(m_deviceProfileTab);
        m_tabProfile.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_deviceProfileTab.refresh();
            }
        });
        m_tabsPanel.add(m_tabProfile);

        m_tabHistory = new TabItem(MSGS.tabHistory(), new KapuaIcon(IconSet.HISTORY));
        m_tabHistory.setBorders(true);
        m_tabHistory.setLayout(new FitLayout());
        m_tabHistory.add(m_deviceHistoryTab);
        m_tabHistory.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_deviceHistoryTab.refresh();
            }
        });
        m_tabsPanel.add(m_tabHistory);

        m_tabPackages = new TabItem(MSGS.tabPackages(), new KapuaIcon(IconSet.INBOX));
        m_tabPackages.setBorders(true);
        m_tabPackages.setLayout(new FitLayout());
        m_tabPackages.add(m_devicePackagesTab);
        m_tabPackages.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_devicePackagesTab.refresh();
            }
        });
        m_tabsPanel.add(m_tabPackages);

        m_tabBundles = new TabItem(MSGS.tabBundles(), new KapuaIcon(IconSet.CUBES));
        m_tabBundles.setBorders(true);
        m_tabBundles.setLayout(new FitLayout());
        m_tabBundles.add(m_deviceBundlesTab);
        m_tabBundles.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_deviceBundlesTab.refresh();
            }
        });
        m_tabsPanel.add(m_tabBundles);

        m_tabConfiguration = new TabItem(MSGS.tabConfiguration(), new KapuaIcon(IconSet.WRENCH));
        m_tabConfiguration.setBorders(true);
        m_tabConfiguration.setLayout(new FitLayout());
        m_tabConfiguration.add(m_deviceConfigTab);
        m_tabConfiguration.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_deviceConfigTab.refresh();
            }
        });
        m_tabsPanel.add(m_tabConfiguration);

        m_tabCommand = new TabItem(MSGS.tabCommand(), new KapuaIcon(IconSet.TERMINAL));
        m_tabCommand.setBorders(false);
        m_tabCommand.setLayout(new FitLayout());
        m_tabCommand.add(m_deviceCommandTab);
        m_tabCommand.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_deviceCommandTab.refresh();
            }
        });
        m_tabsPanel.add(m_tabCommand);
        
        m_tabAssets = new TabItem(MSGS.asset(), new KapuaIcon(IconSet.AMAZON));
        m_tabAssets.setBorders(false);
        m_tabAssets.setLayout(new FitLayout());
        m_tabAssets.add(m_deviceAssetsTab);
        m_tabAssets.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
          //      m_deviceAssetsTab.refresh();
            }
        });
        m_tabsPanel.add(m_tabAssets);

        add(m_tabsPanel);
    }

}
