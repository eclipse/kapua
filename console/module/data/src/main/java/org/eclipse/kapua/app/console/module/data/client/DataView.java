/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;

public class DataView extends AbstractView {

    private GwtSession currentSession;
    private TabPanel tabsPanel;

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);

    public DataView(GwtSession currentGwtSession) {
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

        tabsPanel = new TabPanel();
        tabsPanel.setPlain(false);
        tabsPanel.setBorders(false);
        tabsPanel.setBodyBorder(true);

        TopicsTabItem channelTabItem = new TopicsTabItem(currentSession);
        tabsPanel.add(channelTabItem);
        DeviceTabItem deviceTabItem = new DeviceTabItem(currentSession);
        tabsPanel.add(deviceTabItem);
        AssetTabItem assetTabItem = new AssetTabItem(currentSession);
        tabsPanel.add(assetTabItem);

        add(tabsPanel);
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
