/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

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

    private TabPanel tabsPanel;
    private TabItem tabComponents;
    private TabItem tabSnapshots;

    private DeviceConfigComponents configComponents;
    private DeviceConfigSnapshots configSnapshots;

    public DeviceTabConfiguration(GwtSession currentSession) {
        configComponents = new DeviceConfigComponents(currentSession, this);
        configSnapshots = new DeviceConfigSnapshots(currentSession, this);
    }

    public void setDevice(GwtDevice selectedDevice) {
        configComponents.setDevice(selectedDevice);
        configSnapshots.setDevice(selectedDevice);
    }

    public void refresh() {

        if (tabsPanel == null) {
            return;
        }

        if (tabsPanel.getSelectedItem() == tabComponents) {
            configComponents.refresh();
        } else if (tabsPanel.getSelectedItem() == tabSnapshots) {
            configSnapshots.refresh();
        }
    }

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setId("DeviceTabsContainer");
        setLayout(new FitLayout());

        tabsPanel = new TabPanel();
        tabsPanel.setPlain(true);
        tabsPanel.setBorders(false);
        tabsPanel.setTabPosition(TabPosition.BOTTOM);

        tabComponents = new TabItem(msgs.deviceConfigComponents(), new KapuaIcon(IconSet.PUZZLE_PIECE));
        tabComponents.setBorders(false);
        tabComponents.setLayout(new FitLayout());
        tabComponents.add(configComponents);
        tabComponents.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                configComponents.refresh();
            }
        });
        tabsPanel.add(tabComponents);

        tabSnapshots = new TabItem(msgs.deviceConfigSnapshots(), new KapuaIcon(IconSet.ARCHIVE));
        tabSnapshots.setBorders(false);
        tabSnapshots.setLayout(new FitLayout());
        tabSnapshots.add(configSnapshots);
        tabSnapshots.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                configSnapshots.refresh();
            }
        });
        tabsPanel.add(tabSnapshots);

        add(tabsPanel);
    }
}
