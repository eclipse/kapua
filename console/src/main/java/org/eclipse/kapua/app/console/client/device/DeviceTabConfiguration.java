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
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceTabConfiguration extends LayoutContainer {

    private final ConsoleMessages msgs = GWT.create(ConsoleMessages.class);

    @SuppressWarnings("unused")
    private GwtSession m_currentSession;

    private TabPanel m_tabsPanel;
    private TabItem m_tabComponents;
    private TabItem m_tabSnapshots;

    private DeviceConfigComponents m_configComponents;
    private DeviceConfigSnapshots m_configSnapshots;

    public DeviceTabConfiguration(GwtSession currentSession) {
        m_currentSession = currentSession;
        m_configComponents = new DeviceConfigComponents(currentSession, this);
        m_configSnapshots = new DeviceConfigSnapshots(currentSession, this);
    }

    public void setDevice(GwtDevice selectedDevice) {
        m_configComponents.setDevice(selectedDevice);
        m_configSnapshots.setDevice(selectedDevice);
    }

    public void refresh() {

        if (m_tabsPanel == null) {
            return;
        }

        if (m_tabsPanel.getSelectedItem() == m_tabComponents) {
            m_configComponents.refresh();
        } else if (m_tabsPanel.getSelectedItem() == m_tabSnapshots) {
            m_configSnapshots.refresh();
        }
    }

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setId("DeviceTabsContainer");
        setLayout(new FitLayout());

        m_tabsPanel = new TabPanel();
        m_tabsPanel.setPlain(true);
        m_tabsPanel.setBorders(false);
        m_tabsPanel.setTabPosition(TabPosition.BOTTOM);

        m_tabComponents = new TabItem(msgs.deviceConfigComponents(), new KapuaIcon(IconSet.PUZZLE_PIECE));
        m_tabComponents.setBorders(false);
        m_tabComponents.setLayout(new FitLayout());
        m_tabComponents.add(m_configComponents);
        m_tabComponents.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_configComponents.refresh();
            }
        });
        m_tabsPanel.add(m_tabComponents);

        m_tabSnapshots = new TabItem(msgs.deviceConfigSnapshots(), new KapuaIcon(IconSet.ARCHIVE));
        m_tabSnapshots.setBorders(false);
        m_tabSnapshots.setLayout(new FitLayout());
        m_tabSnapshots.add(m_configSnapshots);
        m_tabSnapshots.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_configSnapshots.refresh();
            }
        });
        m_tabsPanel.add(m_tabSnapshots);

        add(m_tabsPanel);
    }
}
