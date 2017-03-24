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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.client.resources.Resources;
import org.eclipse.kapua.app.console.client.ui.button.ExportButton;
import org.eclipse.kapua.app.console.client.ui.widget.DateRangeSelector;
import org.eclipse.kapua.app.console.client.ui.widget.DateRangeSelectorListener;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.shared.model.GwtAsset;
import org.eclipse.kapua.app.console.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.shared.model.GwtMessage;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtTopic;
import org.eclipse.kapua.app.console.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.shared.service.GwtDataServiceAsync;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ResultsTable extends LayoutContainer {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);
    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);
    private static final int RESULTSTABLE_PAGE_SIZE = 250;

    GwtSession currentSession;
    private ContentPanel tableContainer;
    private BasePagingLoader<PagingLoadResult<GwtMessage>> loader;
    private Grid<GwtMessage> resultsGrid;
    private PagingToolBar pagingToolBar;
    private ToolBar resultsToolBar;
    private ArrayList<ColumnConfig> columnConfigs;
    private ColumnConfig timestampColumn;
    private ColumnConfig assetColumn;
    private ColumnConfig topicColumn;
    private GwtTopic selectedTopic;
    private GwtAsset selectedAsset;
    private List<GwtHeader> selectedMetrics;
    private Date startDate;
    private Date endDate;
    private ExportButton exportButton;
    private DateRangeSelector dateRangeSelector;

    public ResultsTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(true);

        initResultsTable();
        add(tableContainer);
    }

    private void initResultsTable() {
        initResultsGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(false);
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(resultsGrid);
        tableContainer.setTopComponent(resultsToolBar);
        tableContainer.setBottomComponent(pagingToolBar);
    }

    private void initResultsGrid() {
        columnConfigs = new ArrayList<ColumnConfig>();

        timestampColumn = new ColumnConfig("timestamp", MSGS.resultsTableTimestampHeader(), 140);
        columnConfigs.add(timestampColumn);
        assetColumn = new ColumnConfig("asset", MSGS.resultsTableAssetHeader(), 90);
        topicColumn = new ColumnConfig("topic", MSGS.resultsTableTopicHeader(), 140);
        
        RpcProxy<PagingLoadResult<GwtMessage>> proxy = new RpcProxy<PagingLoadResult<GwtMessage>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtMessage>> callback) {
                if (selectedMetrics != null && !selectedMetrics.isEmpty()) {
                    if (selectedTopic != null) {
                        dataService.findMessagesByTopic((PagingLoadConfig) loadConfig, currentSession.getSelectedAccount().getId(), selectedTopic, selectedMetrics, startDate, endDate, callback);
                    } else if (selectedAsset != null) {
                        dataService.findMessagesByAsset((PagingLoadConfig) loadConfig, currentSession.getSelectedAccount().getId(), selectedAsset, selectedMetrics, startDate, endDate, callback);
                    }
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtMessage>(new ArrayList<GwtMessage>()));
                }
            }
        };

        loader = new BasePagingLoader<PagingLoadResult<GwtMessage>>(proxy);

        SwappableListStore<GwtMessage> store = new SwappableListStore<GwtMessage>(loader);
        resultsGrid = new Grid<GwtMessage>(store, new ColumnModel(columnConfigs));
        resultsGrid.setBorders(false);
        resultsGrid.setStateful(false);
        resultsGrid.setLoadMask(true);
        resultsGrid.setStripeRows(true);
        resultsGrid.getView().setAutoFill(true);
        resultsGrid.getView().setEmptyText(MSGS.resultsTableEmptyText());
        resultsGrid.disableTextSelection(false);

        resultsToolBar = new ToolBar();
        Menu menu = new Menu();
        menu.add(new MenuItem(MSGS.resultsTableExportToExcel(), AbstractImagePrototype.create(Resources.INSTANCE.exportExcel()),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {

                    }
                }));
        menu.add(new MenuItem(MSGS.resultsTableExportToCSV(), AbstractImagePrototype.create(Resources.INSTANCE.exportCSV()),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {

                    }
                }));

        exportButton = new ExportButton();
        exportButton.setMenu(menu);
        resultsToolBar.add(exportButton);
        resultsToolBar.add(new SeparatorToolItem());

        resultsToolBar.add(new FillToolItem());

        Label label = new Label(MSGS.dateRange());
        resultsToolBar.add(label);

        dateRangeSelector = new DateRangeSelector();
        dateRangeSelector.setListener(new DateRangeSelectorListener() {
            
            @Override
            public void onUpdate() {
                refresh(selectedMetrics);
            }
        });
        resultsToolBar.add(dateRangeSelector);

        pagingToolBar = new PagingToolBar(RESULTSTABLE_PAGE_SIZE);
        pagingToolBar.bind(loader);
        pagingToolBar.enable();
    }

    private void refresh(List<GwtHeader> headers) {
        columnConfigs.clear();
        columnConfigs.add(timestampColumn);
        resultsGrid.getColumnModel().getColumns().clear();
        resultsGrid.getColumnModel().getColumns().add(timestampColumn);        
        selectedMetrics = headers;
        if (headers != null && !headers.isEmpty()) {
            resultsGrid.getColumnModel().getColumns().add(assetColumn);
            resultsGrid.getColumnModel().getColumns().add(topicColumn);
            for (GwtHeader header : headers) {
                resultsGrid.getColumnModel().getColumns().add(new ColumnConfig(header.getName(), header.getName(), 100));
            }
            resultsGrid.getView().refresh(true);
            startDate = dateRangeSelector.getStartDate();
            endDate = dateRangeSelector.getEndDate();
        }
        loader.load();
    }

    public void refresh(GwtTopic topic, List<GwtHeader> headers) {
        this.selectedTopic = topic;
        refresh(headers);
    }
    
    public void refresh(GwtAsset asset, List<GwtHeader> headers) {
        this.selectedAsset = asset;
        refresh(headers);
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
