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
package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class AssetTabItem extends TabItem {

    private GwtSession currentSession;
    private TabPanel tabsPanel;
    private DeviceValuesTab valuesTab;
    private DeviceConfigurationTab configurationsTab;
    private TabItem tabValue;
    private TabItem tabConfiguration;
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public AssetTabItem(GwtSession currentSession) {
        super(MSGS.assetTabItemTitle(), null);
        this.currentSession = currentSession;
        valuesTab = new DeviceValuesTab(currentSession);
        configurationsTab = new DeviceConfigurationTab(currentSession);
    }

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);
        setLayout(new FitLayout());
        tabsPanel = new TabPanel();
        tabsPanel.setPlain(true);
        tabsPanel.setBorders(false);
        tabsPanel.setBodyBorder(false);
        tabValue = new TabItem(MSGS.valueTabItem(), new KapuaIcon(IconSet.INFO));
        tabValue.setBorders(true);
        tabValue.setLayout(new FitLayout());
        tabValue.add(valuesTab);
        tabsPanel.add(tabValue);
        tabConfiguration = new TabItem(MSGS.configurationTabItem(), new KapuaIcon(IconSet.CODE));
        tabConfiguration.setBorders(true);
        tabConfiguration.setLayout(new FitLayout());
        tabConfiguration.add(configurationsTab);
        tabsPanel.add(tabConfiguration);
        tabsPanel.setTabPosition(TabPosition.BOTTOM);
        add(tabsPanel);
    }
}
