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
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtTopic;
import org.eclipse.kapua.app.console.shared.model.data.GwtDataChannelInfoQuery;
import org.eclipse.kapua.app.console.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.shared.service.GwtDataServiceAsync;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TopicsTable extends LayoutContainer {

    private static final int topicINFO_PAGE_SIZE = 50;
    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);
    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);

    private BasePagingLoader<PagingLoadResult<GwtTopic>> loader;
    private GwtSession currentSession;
    private Grid<GwtTopic> topicInfoGrid;
    // private TreeGrid<GwtTopic> topicInfoGrid;
    private ContentPanel tableContainer;
    private PagingToolBar pagingToolBar;
    private List<SelectionChangedListener<GwtTopic>> listeners = new ArrayList<SelectionChangedListener<GwtTopic>>();

    public TopicsTable(GwtSession currentGwtSession) {
        this.currentSession = currentGwtSession;
    }

    public GwtTopic getSelectedTopic() {
        if (topicInfoGrid != null) {
            return (GwtTopic) topicInfoGrid.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    public void addSelectionChangedListener(SelectionChangedListener<GwtTopic> listener) {
        listeners.add(listener);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(true);

        inittopicInfoTable();
        add(tableContainer);
    }

    private void inittopicInfoTable() {
        inittopicInfoGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(MSGS.topicInfoTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(topicInfoGrid);
        tableContainer.setBottomComponent(pagingToolBar);
    }

    private void inittopicInfoGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("topicNameLimited", MSGS.topicInfoTableTopicHeader(), 150);
        // column.setRenderer(new TreeGridCellRenderer<ModelData>());
        configs.add(column);

        column = new ColumnConfig("timestamp", MSGS.topicInfoTableLastPostedHeader(), 150);
        configs.add(column);

        RpcProxy<PagingLoadResult<GwtTopic>> proxy = new RpcProxy<PagingLoadResult<GwtTopic>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtTopic>> callback) {
                GwtDataChannelInfoQuery query = new GwtDataChannelInfoQuery();
                String scopeId = currentSession.getSelectedAccount().getId();
                query.setScopeId(scopeId);
                dataService.findTopicsList((PagingLoadConfig) loadConfig, query, callback);
            }
        };

        loader = new BasePagingLoader<PagingLoadResult<GwtTopic>>(proxy);
        //loader.load();
        // TreeLoader<GwtTopic> treeLoader = new BaseTreeLoader<GwtTopic>(proxy);

        SwappableListStore<GwtTopic> store = new SwappableListStore<GwtTopic>(loader);

        // TreeStore<ModelData> store = new TreeStore<ModelData>();
        // topicInfoGrid = new TreeGrid<GwtTopic>(store, new ColumnModel(configs));
        topicInfoGrid = new Grid<GwtTopic>(store, new ColumnModel(configs));
        topicInfoGrid.setBorders(false);
        topicInfoGrid.setStateful(false);
        topicInfoGrid.setLoadMask(true);
        topicInfoGrid.setStripeRows(true);
        topicInfoGrid.getView().setAutoFill(true);
        topicInfoGrid.getView().setEmptyText(MSGS.topicInfoGridEmptyText());
        topicInfoGrid.disableTextSelection(false);
        pagingToolBar = new PagingToolBar(topicINFO_PAGE_SIZE);
        pagingToolBar.bind(loader);
        pagingToolBar.enable();
        pagingToolBar.refresh();

        GridSelectionModel<GwtTopic> selectionModel = new GridSelectionModel<GwtTopic>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        for(SelectionChangedListener<GwtTopic> listener : listeners){
            selectionModel.addSelectionChangedListener(listener);
         }
        topicInfoGrid.setSelectionModel(selectionModel);
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
