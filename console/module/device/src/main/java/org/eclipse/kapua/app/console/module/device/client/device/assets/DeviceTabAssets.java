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
package org.eclipse.kapua.app.console.module.device.client.device.assets;

import org.eclipse.kapua.app.console.module.device.client.device.assets.DeviceAssetsValues;
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

public class DeviceTabAssets extends KapuaTabItem<GwtDevice> {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private TabPanel tabsPanel;
    private TabItem tabValues;

    private DeviceAssetsValues assetsValues;
    // private DeviceConfigSnapshots assetsConfiguration;

    public DeviceTabAssets(GwtSession currentSession) {
        super(MSGS.asset(), new KapuaIcon(IconSet.RETWEET));
        assetsValues = new DeviceAssetsValues(currentSession, this);
        // assetsConfiguration = new DeviceConfigSnapshots(currentSession, this);
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);
        assetsValues.setDevice(gwtDevice);
        doRefresh();
    }

    @Override
    public void doRefresh() {

        if (tabsPanel == null) {
            return;
        }

        if (tabsPanel.getSelectedItem() == tabValues) {
            assetsValues.refresh();
        }
        // else if (tabsPanel.getSelectedItem() == tabConfiguration) {
        // assetsConfiguration.refresh();
        // }
    }

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setId("DeviceTabsContainer");
        setLayout(new FitLayout());

        tabsPanel = new TabPanel();
        tabsPanel.setPlain(true);
        tabsPanel.setBorders(false);
        tabsPanel.setTabPosition(TabPosition.BOTTOM);

        tabValues = new TabItem(MSGS.assetValuesTab(), new KapuaIcon(IconSet.PUZZLE_PIECE));
        tabValues.setBorders(false);
        tabValues.setLayout(new FitLayout());
        tabValues.add(assetsValues);
        tabValues.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                assetsValues.refresh();
            }
        });
        tabsPanel.add(tabValues);

        // tabConfiguration = new TabItem(msgs.deviceConfigSnapshots(), new KapuaIcon(IconSet.ARCHIVE));
        // tabConfiguration.setBorders(false);
        // tabConfiguration.setLayout(new FitLayout());
        // tabConfiguration.add(assetsConfiguration);
        // tabConfiguration.addListener(Events.Select, new Listener<ComponentEvent>() {
        //
        // public void handleEvent(ComponentEvent be) {
        // assetsConfiguration.refresh();
        // }
        // });
        // tabsPanel.add(tabConfiguration);

        add(tabsPanel);
    }
}
