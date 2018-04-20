/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.permission.DeviceManagementSessionPermission;

public class DeviceTabAssets extends KapuaTabItem<GwtDevice> {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private TabPanel tabsPanel;
    private TabItem tabValues;

    private DeviceAssetsValues assetsValues;

    public DeviceTabAssets(GwtSession currentSession) {
        super(currentSession, MSGS.assets(), new KapuaIcon(IconSet.RETWEET));
        assetsValues = new DeviceAssetsValues(currentSession, this);
        setEnabled(false);
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);

        setEnabled(gwtDevice != null &&
                gwtDevice.isOnline() &&
                currentSession.hasPermission(DeviceManagementSessionPermission.read()) &&
                gwtDevice.hasApplication(GwtDevice.GwtDeviceApplication.APP_ASSET_V1));

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
    }

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);
        setBorders(false);
        setId("DeviceTabsContainer");
        setLayout(new FitLayout());

        tabsPanel = new TabPanel();
        tabsPanel.setPlain(true);
        tabsPanel.setBorders(false);
        tabsPanel.setTabPosition(TabPosition.BOTTOM);

        tabValues = new TabItem(MSGS.assetValuesTab(), new KapuaIcon(IconSet.SIGNAL));
        tabValues.setBorders(false);
        tabValues.setLayout(new FitLayout());
        tabValues.add(assetsValues);
        tabValues.addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                assetsValues.refresh();
            }
        });
        tabsPanel.add(tabValues);

        add(tabsPanel);
        layout(true);
        Node node0 = tabsPanel.getElement();
        Node node1 = node0.getChild(1);
        Node node2 = node1.getChild(0);
        if (node2.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) node2;
            elem.setAttribute("style",
                    "border-top: 0px; border-bottom: 0px; border-color: #d0d0d0; background: #eaeaea;");
        }
    }
}
