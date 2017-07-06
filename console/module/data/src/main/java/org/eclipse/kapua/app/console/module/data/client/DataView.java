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

import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

public class DataView extends LayoutContainer {

    private GwtSession currentSession;
    private TabPanel tabsPanel;

    public DataView(GwtSession currentGwtSession) {
        currentSession = currentGwtSession;
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
