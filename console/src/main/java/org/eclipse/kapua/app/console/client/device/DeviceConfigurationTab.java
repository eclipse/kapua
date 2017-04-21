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
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceConfigurationTab extends TabItem {

    private GwtSession currentSession;
    private GwtDevice selectedDevice;
    private boolean dirty;
    private boolean initialized;
    private AssetTable table;
    private ConfigurationPanelTable configTable;
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public DeviceConfigurationTab(GwtSession currentSession) {
        super(MSGS.configurationTabItemTitle(), null);
        this.setBorders(false);
        this.setLayout(new FitLayout());
        this.currentSession = currentSession;
        dirty = true;
        initialized = false;
    }

    public void setDevice(GwtDevice selectedDevice) {
        dirty = true;
        this.selectedDevice = selectedDevice;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        this.setWidth("100%");
        LayoutContainer tables = new LayoutContainer(new BorderLayout());
        add(tables);
        BorderLayoutData assetLayout = new BorderLayoutData(LayoutRegion.WEST, 0.3f);
        table = new AssetTable(currentSession);
        table.showToolbar(true);
        assetLayout.setMargins(new Margins(0, 5, 0, 0));
        assetLayout.setSplit(true);
        tables.add(table, assetLayout);
        LayoutContainer configurationPanelTableContainer = new LayoutContainer(new BorderLayout());
        BorderLayoutData configurationPanelTableBorder = new BorderLayoutData(LayoutRegion.CENTER, 0.7f);
        tables.add(configurationPanelTableContainer, configurationPanelTableBorder);
        BorderLayoutData detailsBorder = new BorderLayoutData(LayoutRegion.CENTER, 0.6f);
        configTable = new ConfigurationPanelTable(currentSession);
        detailsBorder.setMargins(new Margins(0, 5, 0, 0));
        detailsBorder.setSplit(true);
        configurationPanelTableContainer.add(configTable, detailsBorder);
    }

}
