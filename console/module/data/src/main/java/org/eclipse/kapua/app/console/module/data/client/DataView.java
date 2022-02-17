/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.permission.DeviceSessionPermission;

public class DataView extends AbstractView {

    private TopicsTabItem channelTabItem;
    private DeviceTabItem deviceTabItem;
    private AssetTabItem assetTabItem;

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);

    DataView(GwtSession currentGwtSession) {
        currentSession = currentGwtSession;
    }

    public static String getName() {
        return MSGS.data();
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        TabPanel tabsPanel = new TabPanel();
        tabsPanel.setPlain(false);
        tabsPanel.setBorders(false);
        tabsPanel.setBodyBorder(true);

        channelTabItem = new TopicsTabItem(currentSession);
        tabsPanel.add(channelTabItem);
        if(currentSession.hasPermission(DeviceSessionPermission.read())) {
            deviceTabItem = new DeviceTabItem(currentSession);
            tabsPanel.add(deviceTabItem);
            assetTabItem = new AssetTabItem(currentSession);
            tabsPanel.add(assetTabItem);
        }

        add(tabsPanel);
    }

    @Override
    public void onUserChange() {
        channelTabItem.refreshTables();
        deviceTabItem.refreshTables();
        assetTabItem.refreshTables();
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------
    @Override
    public void onUnload() {
        super.onUnload();
    }
}
