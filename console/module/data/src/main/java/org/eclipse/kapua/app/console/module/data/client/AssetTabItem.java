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

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;

import java.util.List;

public class AssetTabItem extends TabItem {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);

    private GwtSession currentSession;

    private DeviceTable deviceTable;

    private Button queryButton;
    private Button refreshButton;

    private ResultsTable resultsTable;
    private AssetTable assetTable;
    private MetricsTable metricsTable;

    public AssetTabItem(GwtSession currentSession) {
        super(MSGS.assetTabItemTitle(), null);
        this.currentSession = currentSession;
        this.setBorders(false);
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setWidth("100%");

        BorderLayoutData messageLayout = new BorderLayoutData(LayoutRegion.NORTH, 0.02f);
        messageLayout.setMargins(new Margins(5));
        Text welcomeMessage = new Text();
        welcomeMessage.setText(MSGS.assetTabItemMessage());
        add(welcomeMessage, messageLayout);

        LayoutContainer tables = new LayoutContainer(new BorderLayout());
        BorderLayoutData tablesLayout = new BorderLayoutData(LayoutRegion.CENTER);
        tablesLayout.setMargins(new Margins(0, 5, 0, 5));
        tablesLayout.setMinSize(250);
        add(tables, tablesLayout);

        BorderLayoutData refreshButtonLayout = new BorderLayoutData(LayoutRegion.NORTH, 0.08f);
        refreshButtonLayout.setMargins(new Margins(5));
        refreshButton = new Button(MSGS.refresh(), new KapuaIcon(IconSet.REFRESH), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                deviceTable.refresh();
                resultsTable.refresh();
            }
        });
        TableLayout refreshButtonTL = new TableLayout();
        refreshButtonTL.setCellPadding(0);
        LayoutContainer refreshButtonContainer = new LayoutContainer(refreshButtonTL);
        refreshButtonContainer.add(refreshButton, new TableData());
        tables.add(refreshButtonContainer, refreshButtonLayout);

        BorderLayoutData deviceLayout = new BorderLayoutData(LayoutRegion.WEST, 0.33f);
        deviceTable = new DeviceTable(currentSession);
        deviceLayout.setMargins(new Margins(0, 5, 0, 0));
        deviceLayout.setSplit(true);
        deviceTable.addSelectionChangedListener(new SelectionChangedListener<GwtDatastoreDevice>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtDatastoreDevice> se) {
                refreshButton.enable();
                if (se.getSelectedItem() != null) {
                    assetTable.refresh(se.getSelectedItem());
                } else {
                    assetTable.clearTable();
                }
            }
        });
        tables.add(deviceTable, deviceLayout);

        BorderLayoutData assetLayout = new BorderLayoutData(LayoutRegion.CENTER, 0.34f);
        assetLayout.setMargins(new Margins(0, 5, 0, 5));
        assetLayout.setSplit(true);
        assetTable = new AssetTable(currentSession);
        assetTable.addSelectionChangedListener(new SelectionChangedListener<GwtDatastoreAsset>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtDatastoreAsset> selectedAsset) {
                metricsTable.refresh(selectedAsset.getSelectedItem());
            }
        });
        tables.add(assetTable, assetLayout);

        BorderLayoutData channelLayout = new BorderLayoutData(LayoutRegion.EAST, 0.33f);
        channelLayout.setMargins(new Margins(0, 0, 0, 3));
        channelLayout.setSplit(true);
        metricsTable = new MetricsTable(currentSession, MetricsTable.Type.ASSET);
        metricsTable.setStyleAttribute("padding-left", "2px");
        metricsTable.addSelectionListener(new SelectionChangedListener<GwtHeader>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtHeader> se) {
                if (!se.getSelection().isEmpty()) {
                    queryButton.enable();
                } else {
                    queryButton.disable();
                }
            }
        });
        tables.add(metricsTable, channelLayout);

        BorderLayoutData queryButtonLayout = new BorderLayoutData(LayoutRegion.SOUTH, 0.1f);
        queryButtonLayout.setMargins(new Margins(5));
        queryButton = new Button(MSGS.assetTabItemQueryButtonText(), new KapuaIcon(IconSet.SEARCH), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                GwtDatastoreAsset gwtAsset = assetTable.getSelectedAsset();
                List<GwtHeader> metricsInfo = metricsTable.getSelectedMetrics();
                resultsTable.refresh(gwtAsset, metricsInfo);
            }
        });
        queryButton.disable();
        TableLayout queryButtonTL = new TableLayout();
        queryButtonTL.setCellPadding(0);
        LayoutContainer queryButtonContainer = new LayoutContainer(queryButtonTL);
        queryButtonContainer.add(queryButton, new TableData());
        tables.add(queryButtonContainer, queryButtonLayout);

        BorderLayoutData resultsLayout = new BorderLayoutData(LayoutRegion.SOUTH, 0.5f);
        resultsLayout.setSplit(true);

        TabPanel resultsTabPanel = new TabPanel();
        resultsTabPanel.setPlain(true);
        resultsTabPanel.setBorders(false);
        resultsTabPanel.setBodyBorder(false);

        resultsTable = new ResultsTable(currentSession);
        TabItem resultsTableTabItem = new TabItem(MSGS.resultsTableTabItemTitle(), new KapuaIcon(IconSet.TABLE));
        resultsTableTabItem.setLayout(new FitLayout());
        resultsTableTabItem.add(resultsTable);
        resultsTabPanel.add(resultsTableTabItem);

        add(resultsTabPanel, resultsLayout);
    }
}
