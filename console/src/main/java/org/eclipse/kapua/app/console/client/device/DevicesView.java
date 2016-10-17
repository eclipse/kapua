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

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.Resources;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DevicesView extends LayoutContainer {

    private final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private GwtSession m_currentSession;

    private TabPanel m_tabsPanel;
    private TabItem m_tabTable;
    private TabItem m_tabMap;
    private DeviceFilterPanel m_deviceFilterPanel;
    private DevicesTable m_deviceTable;
    private DevicesMap m_deviceMap;
    private DeviceTabs m_deviceTabs;

    public DevicesView(GwtSession currentSession) {
        m_currentSession = currentSession;
    }

    public void setDevice(GwtDevice device) {
        m_deviceTabs.setDevice(device);
    }

    public DeviceTabs getDeviceTabs() {
        return m_deviceTabs;
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

        m_deviceFilterPanel = new DeviceFilterPanel(m_currentSession);

        ContentPanel panel = new ContentPanel();
        panel.setLayout(new FitLayout());
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeading(MSGS.deviceFilteringPanelHeading());
        panel.add(m_deviceFilterPanel);
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
        m_deviceTable = new DevicesTable(this, m_currentSession, panel);
        m_deviceFilterPanel.setDeviceTable(m_deviceTable);
        m_deviceMap = new DevicesMap(this, m_currentSession);

        m_tabsPanel = new TabPanel();
        m_tabsPanel.setPlain(false);
        m_tabsPanel.setBorders(false);
        m_tabsPanel.setBodyBorder(true);

        m_tabTable = new TabItem(MSGS.tabTable());
        m_tabTable.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.table()));
        m_tabTable.setBorders(false);
        m_tabTable.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_deviceTable.refresh(new GwtDeviceQueryPredicates());
            }
        });
        m_tabTable.setLayout(new FitLayout());
        m_tabTable.add(m_deviceTable);
        m_tabsPanel.add(m_tabTable);

        m_tabMap = new TabItem(MSGS.tabMap());
        m_tabMap.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.deviceMap()));
        m_tabMap.setBorders(false);
        m_tabMap.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_deviceMap.refresh(new GwtDeviceQueryPredicates());
            }
        });
        m_tabMap.setLayout(new FitLayout());
        m_tabMap.add(m_deviceMap);

        m_tabsPanel.add(m_tabMap);

        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, .45F);
        northData.setMargins(new Margins(0, 0, 0, 0));
        northData.setSplit(true);
        northData.setMinSize(0);
        resultContainer.add(m_tabsPanel, northData);

        //
        // Center Panel: Profile and History Tabs
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, .55F);
        centerData.setMargins(new Margins(5, 0, 0, 0));
        centerData.setCollapsible(true);
        centerData.setHideCollapseTool(true);
        centerData.setSplit(true);

        m_deviceTabs = new DeviceTabs(m_deviceTable, m_deviceFilterPanel, m_currentSession);
        resultContainer.add(m_deviceTabs, centerData);

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
