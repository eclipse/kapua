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
package org.eclipse.kapua.app.console.module.device.client.device.configuration;

import org.eclipse.kapua.app.console.module.device.client.device.configuration.DeviceConfigComponents;
import org.eclipse.kapua.app.console.module.device.client.device.configuration.DeviceConfigSnapshots;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceTabConfiguration extends KapuaTabItem<GwtDevice> {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private TabPanel tabsPanel;
    private TabItem tabComponents;
    private TabItem tabSnapshots;

    private DeviceConfigComponents configComponents;
    private DeviceConfigSnapshots configSnapshots;

    public DeviceTabConfiguration(GwtSession currentSession) {
        super(MSGS.tabConfiguration(), new KapuaIcon(IconSet.WRENCH));
        configComponents = new DeviceConfigComponents(currentSession, this);
        configSnapshots = new DeviceConfigSnapshots(currentSession, this);
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);
        configComponents.setDevice(gwtDevice);
        configSnapshots.setDevice(gwtDevice);
        doRefresh();
    }


    @Override
    public void doRefresh() {

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

        tabComponents = new TabItem(MSGS.deviceConfigComponents(), new KapuaIcon(IconSet.PUZZLE_PIECE));
        tabComponents.setBorders(false);
        tabComponents.setLayout(new FitLayout());
        tabComponents.add(configComponents);
        tabComponents.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                configComponents.refresh();
            }
        });
        tabsPanel.add(tabComponents);

        tabSnapshots = new TabItem(MSGS.deviceConfigSnapshots(), new KapuaIcon(IconSet.ARCHIVE));
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
