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
package org.eclipse.kapua.app.console.client.data;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.button.Button;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
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


public class DeviceTabItem extends TabItem {
   
    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);
    
    private GwtSession currentSession;
    private DeviceTable deviceTable;
    private Button queryButton;
    private MetricsTable metricsTable;

    private ResultsTable resultsTable;
    
    public DeviceTabItem(GwtSession currentSession) {
        super(MSGS.deviceTabItemTitle(), null);
        this.currentSession = currentSession;
        this.setBorders(false);
        this.setLayout(new BorderLayout());
    }
    
   
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        this.setWidth("100%");
        BorderLayoutData messageLayout = new BorderLayoutData(LayoutRegion.NORTH, 0.06f);
        messageLayout.setMargins(new Margins(5));
        Text welcomeMessage = new Text();
        welcomeMessage.setText(MSGS.deviceTabItemMessage());
        add(welcomeMessage, messageLayout);
        
        LayoutContainer tables = new LayoutContainer(new BorderLayout());
        BorderLayoutData tablesLayout = new BorderLayoutData(LayoutRegion.CENTER);
        tablesLayout.setMinSize(250);
        add(tables, tablesLayout);
        
        BorderLayoutData deviceLayout = new BorderLayoutData(LayoutRegion.WEST, 0.5f);
        deviceTable = new DeviceTable(currentSession);
        deviceLayout.setMargins(new Margins(0, 5, 0, 0));
        deviceLayout.setSplit(true);
        deviceTable.addSelectionChangedListener(new SelectionChangedListener<GwtDatastoreDevice>() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent<GwtDatastoreDevice> selectedDevice) {
                metricsTable.refresh(selectedDevice.getSelectedItem());
            }
        });
        tables.add(deviceTable, deviceLayout);
        
        BorderLayoutData metricLayout = new BorderLayoutData(LayoutRegion.CENTER, 0.5f);
        metricsTable = new MetricsTable(currentSession, MetricsTable.Type.DEVICE);
        metricsTable.addSelectionListener(new SelectionChangedListener<GwtHeader>() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent<GwtHeader> se) {
                if(!se.getSelection().isEmpty()){
                    queryButton.enable();
                } else {
                    queryButton.disable();
                }
            }
        });
        metricLayout.setMargins(new Margins(0, 0, 0, 5));
        tables.add(metricsTable, metricLayout);

        BorderLayoutData queryButtonLayout = new BorderLayoutData(LayoutRegion.SOUTH, 0.1f);
        queryButtonLayout.setMargins(new Margins(5));
        queryButton = new Button(MSGS.deviceTabItemQueryButtonText(), new KapuaIcon(IconSet.SEARCH), new SelectionListener<ButtonEvent>() {
            
            @Override
            public void componentSelected(ButtonEvent ce) {
                GwtDatastoreDevice gwtDevice = deviceTable.getSelectedDevice();
                List<GwtHeader> metricsInfo = metricsTable.getSelectedMetrics();
                resultsTable.refresh(gwtDevice, metricsInfo);
            }
        });
        queryButton.disable();
        TableLayout queryButtonTL = new TableLayout();
        queryButtonTL.setCellPadding(0);
        LayoutContainer queryButtonContainer = new LayoutContainer(queryButtonTL);
        queryButtonContainer.add(queryButton, new TableData());
        tables.add(queryButtonContainer, queryButtonLayout);

        BorderLayoutData resultsLayout = new BorderLayoutData(LayoutRegion.SOUTH);
        resultsLayout.setSplit(true);
        resultsLayout.setMargins(new Margins(5, 0, 0, 0));

        
        TabPanel resultsTabPanel = new TabPanel();
        resultsTable = new ResultsTable(currentSession);
        TabItem resultsTableTabItem = new TabItem(MSGS.resultsTableTabItemTitle(), new KapuaIcon(IconSet.TABLE));
        resultsTableTabItem.setLayout(new FitLayout());
        resultsTableTabItem.add(resultsTable);
        resultsTabPanel.add(resultsTableTabItem);
//        ResultsChart resultsChart = new ResultsChart(currentSession);
//        TabItem resultsChartTabItem = new TabItem(MSGS.resultsChartTabItemTitle(), new KapuaIcon(IconSet.LINE_CHART));
//        resultsChartTabItem.setLayout(new FitLayout());
//        resultsChartTabItem.add(resultsChart);
//        resultsTabPanel.add(resultsChartTabItem);
        
        add(resultsTabPanel, resultsLayout);
    }
    
}
