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

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.commons.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.commons.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DevicesView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private GwtSession currentSession;

    private TabPanel tabsPanel;
    private TabItem tabTable;
    private TabItem tabMap;
    private DeviceFilterPanel deviceFilterPanel;
    private DevicesTable deviceTable;
    private DevicesMap deviceMap;
    private DeviceTabs deviceTabs;

    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);

    public DevicesView(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    public void setDevice(GwtDevice device) {
        this.deviceTabs.setDevice(device);
    }

    public DeviceTabs getDeviceTabs() {
        return deviceTabs;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        // FitLayout that expands to the whole screen
        setLayout(new FitLayout());
        setBorders(false);

        LayoutContainer mf = new LayoutContainer();
        mf.setBorders(false);
        mf.setLayout(new BorderLayout());

        // East Panel: Filtering menu
        BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 220);
        eastData.setMargins(new Margins(0, 0, 0, 0));
        eastData.setCollapsible(false);
        eastData.setSplit(false);

        deviceFilterPanel = new DeviceFilterPanel(currentSession);

        ContentPanel panel = new ContentPanel();
        panel.setLayout(new FitLayout());
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeading(MSGS.deviceFilteringPanelHeading());
        panel.add(deviceFilterPanel);
        mf.add(panel, eastData);

        // Center Main panel:
        BorderLayoutData centerMainPanel = new BorderLayoutData(LayoutRegion.CENTER);
        centerMainPanel.setMargins(new Margins(0, 5, 0, 0));
        centerMainPanel.setSplit(false);

        LayoutContainer resultContainer = new LayoutContainer(new BorderLayout());
        resultContainer.setBorders(false);

        mf.add(resultContainer, centerMainPanel);

        //
        // North Panel: Devices Table and Map Tabs
        deviceTable = new DevicesTable(this, currentSession, panel);
        deviceFilterPanel.setDeviceTable(deviceTable);

        tabsPanel = new TabPanel();
        tabsPanel.setPlain(false);
        tabsPanel.setBorders(false);
        tabsPanel.setBodyBorder(true);

        tabTable = new TabItem(MSGS.tabTable(), new KapuaIcon(IconSet.HDD_O));
        tabTable.setBorders(false);
        tabTable.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                deviceTable.refresh(new GwtDeviceQueryPredicates());
            }
        });
        tabTable.setLayout(new FitLayout());
        tabTable.add(deviceTable);
        tabsPanel.add(tabTable);

        GWT_DEVICE_SERVICE.isMapEnabled(new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    deviceMap = new DevicesMap(DevicesView.this, currentSession);

                    tabMap = new TabItem(MSGS.tabMap(), new KapuaIcon(IconSet.MAP_O));
                    tabMap.setBorders(false);
                    tabMap.addListener(Events.Select, new Listener<ComponentEvent>() {

                        public void handleEvent(ComponentEvent be) {
                            deviceMap.refresh(new GwtDeviceQueryPredicates());
                        }
                    });
                    tabMap.setLayout(new FitLayout());
                    tabMap.add(deviceMap);

                    tabsPanel.add(tabMap);
                }
            }

        });

        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, .45F);
        northData.setMargins(new Margins(0, 0, 0, 0));
        northData.setSplit(true);
        northData.setMinSize(0);
        resultContainer.add(tabsPanel, northData);

        //
        // Center Panel: Profile and History Tabs
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, .55F);
        centerData.setMargins(new Margins(5, 0, 0, 0));
        centerData.setCollapsible(true);
        centerData.setHideCollapseTool(true);
        centerData.setSplit(true);

        deviceTabs = new DeviceTabs(deviceTable, deviceFilterPanel, currentSession);
        resultContainer.add(deviceTabs, centerData);

        add(mf);
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------

    public void onUnload() {
        super.onUnload();
    }

}
