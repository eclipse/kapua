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
package org.eclipse.kapua.app.console.client.device;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.device.management.packages.DeviceTabPackages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

public class DeviceView extends EntityView<GwtDevice> {

    private DeviceGrid deviceGrid;

    private DeviceFilterPanel deviceFilterPanel;
    // private DeviceTabs deviceTabs;

    private DeviceTabProfile deviceProfileTab;
    private DeviceTabHistory deviceHistoryTab;
    private DeviceTabPackages devicePackagesTab;
    private DeviceTabBundles deviceBundlesTab;
    private DeviceTabConfiguration deviceConfigTab;
    private DeviceTabAssets deviceAssetsTab;

    private DeviceTabCommand deviceCommandTab;

    public DeviceView(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public List<KapuaTabItem<GwtDevice>> getTabs(EntityView<GwtDevice> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtDevice>> tabs = new ArrayList<KapuaTabItem<GwtDevice>>();

        if (deviceProfileTab == null) {
            // deviceProfileTab = new DeviceTabProfile(this.devicesTable, getEntityFilterPanel(entityView, currentSession), this.currentSession);
            deviceProfileTab = new DeviceTabProfile();
            tabs.add(deviceProfileTab);
        }
        if (deviceHistoryTab == null) {
            deviceHistoryTab = new DeviceTabHistory(currentSession);
            tabs.add(deviceHistoryTab);
        }
        if (devicePackagesTab == null) {
            devicePackagesTab = new DeviceTabPackages(currentSession, this);
            tabs.add(devicePackagesTab);
        }
        if (deviceBundlesTab == null) {
            deviceBundlesTab = new DeviceTabBundles(currentSession, this);
            tabs.add(deviceBundlesTab);
        }
        if (deviceConfigTab == null) {
            deviceConfigTab = new DeviceTabConfiguration(currentSession);
            tabs.add(deviceConfigTab);
        }
        if (deviceAssetsTab == null) {
            deviceAssetsTab = new DeviceTabAssets(currentSession);
            tabs.add(deviceAssetsTab);
        }
        if (deviceCommandTab == null) {
            deviceCommandTab = new DeviceTabCommand(currentSession);
            tabs.add(deviceCommandTab);
        }

        return tabs;
    }

    /*
    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        // FitLayout that expands to the whole screen
        setLayout(new FitLayout());
        setBorders(false);

        // LayoutContainer mf = new LayoutContainer();
        // mf.setBorders(false);
        // mf.setLayout(new BorderLayout());
        //
        // // East Panel: Filtering menu
        // BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 220);
        // eastData.setMargins(new Margins(0, 0, 0, 0));
        // eastData.setCollapsible(false);
        // eastData.setSplit(false);
        //
        // deviceFilterPanel = new DeviceFilterPanel(currentSession);
        //
        // ContentPanel panel = new ContentPanel();
        // panel.setLayout(new FitLayout());
        // panel.setBorders(false);
        // panel.setBodyBorder(false);
        // panel.setHeading(MSGS.deviceFilteringPanelHeading());
        // panel.add(deviceFilterPanel);
        // mf.add(panel, eastData);

        // // Center Main panel:
        // BorderLayoutData centerMainPanel = new BorderLayoutData(LayoutRegion.CENTER);
        // centerMainPanel.setMargins(new Margins(0, 5, 0, 0));
        // centerMainPanel.setSplit(false);
        //
        // LayoutContainer resultContainer = new LayoutContainer(new BorderLayout());
        // resultContainer.setBorders(false);
        //
        // mf.add(resultContainer, centerMainPanel);

        //
        // North Panel: Devices Table and Map Tabs
        // ===> needed?
        // deviceTable = new DevicesTable(this, currentSession, panel);
        // deviceFilterPanel.setDeviceTable(deviceTable);
        //
        // tabsPanel = new TabPanel();
        // tabsPanel.setPlain(false);
        // tabsPanel.setBorders(false);
        // tabsPanel.setBodyBorder(true);
        //
        // tabTable = new TabItem(MSGS.tabTable(), new KapuaIcon(IconSet.HDD_O));
        // tabTable.setBorders(false);
        // tabTable.addListener(Events.Select, new Listener<ComponentEvent>() {
        //
        // public void handleEvent(ComponentEvent be) {
        // deviceTable.refresh(new GwtDeviceQueryPredicates());
        // }
        // });
        // tabTable.setLayout(new FitLayout());
        // tabTable.add(deviceTable);
        // tabsPanel.add(tabTable);
        //
        // GWT_DEVICE_SERVICE.isMapEnabled(new AsyncCallback<Boolean>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // FailureHandler.handle(caught);
        // }
        //
        // @Override
        // public void onSuccess(Boolean result) {
        // if (result) {
        // deviceMap = new DevicesMap(DevicesView.this, currentSession);
        //
        // tabMap = new TabItem(MSGS.tabMap(), new KapuaIcon(IconSet.MAP_O));
        // tabMap.setBorders(false);
        // tabMap.addListener(Events.Select, new Listener<ComponentEvent>() {
        //
        // public void handleEvent(ComponentEvent be) {
        // deviceMap.refresh(new GwtDeviceQueryPredicates());
        // }
        // });
        // tabMap.setLayout(new FitLayout());
        // tabMap.add(deviceMap);
        //
        // tabsPanel.add(tabMap);
        // }
        // }
        //
        // });
        //
        // BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, .45F);
        // northData.setMargins(new Margins(0, 0, 0, 0));
        // northData.setSplit(true);
        // northData.setMinSize(0);
        // resultContainer.add(tabsPanel, northData);
        //
        // //
        // // Center Panel: Profile and History Tabs
        // BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, .55F);
        // centerData.setMargins(new Margins(5, 0, 0, 0));
        // centerData.setCollapsible(true);
        // centerData.setHideCollapseTool(true);
        // centerData.setSplit(true);
        // <=== needed?

        // deviceTabs = new DeviceTabs(deviceTable, deviceFilterPanel, currentSession);
        // resultContainer.add(deviceTabs, centerData);

        // add(mf);
    }
    */

    @Override
    public EntityGrid<GwtDevice> getEntityGrid(EntityView<GwtDevice> entityView, GwtSession currentSession) {
        if (deviceGrid == null) {
            deviceGrid = new DeviceGrid(entityView, currentSession);
        }
        return deviceGrid;
    }

    @Override
    public EntityFilterPanel<GwtDevice> getEntityFilterPanel(EntityView<GwtDevice> entityView, GwtSession currentSession) {
        if (deviceFilterPanel == null) {
            deviceFilterPanel = new DeviceFilterPanel(entityView, currentSession);
        }
        return deviceFilterPanel;
    }

}
